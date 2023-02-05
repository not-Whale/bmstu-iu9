import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.util.Map;

public class FlightsStatsApp {
    private static final String FLIGHTS_FILE_PATH = "flights.csv";
    private static final String AIRPORTS_FILE_PATH = "airports.csv";
    private static final String OUTPUT_FILE_PATH = "output_lab_3";
    private static final String AIRPORT_CODE_PREFIX = "C";
    private static final String SEPARATOR = ",";
    private static final String QUOTE = "\"";
    private static final String EMPTY = "";

    public static void main(String[] args) throws Exception {
        SparkConf conf = new SparkConf().setAppName("lab3");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaPairRDD<Tuple2<String, String>, FlightsDelay> flightsDelays;
        JavaRDD<String> flightsDataCSV = readFromCSV(sc, FLIGHTS_FILE_PATH, QUOTE);
        flightsDelays = flightsDataCSV.mapToPair(
                flight -> {
                    String[] flightData = flight.split(SEPARATOR);
                    return new Tuple2<>(
                            FlightsDelay.getAirportsPair(flightData),
                            new FlightsDelay(flightData)
                    );
                }
        );

        JavaPairRDD<Tuple2<String, String>, DelaysStats> delaysStats;
        delaysStats = flightsDelays.combineByKey(
                DelaysStats::new,
                DelaysStats::addDelay,
                DelaysStats::add
        );

        JavaPairRDD<String, String> airportsDescriptions;
        JavaRDD<String> airportsDataCSV = readFromCSV(sc, AIRPORTS_FILE_PATH, AIRPORT_CODE_PREFIX);
        airportsDescriptions = airportsDataCSV.mapToPair(
                airport -> {
                    String[] airportsData = airport.split(SEPARATOR, 2);
                    return new Tuple2<>(
                            removeQuotes(airportsData[0]),
                            removeQuotes(airportsData[1])
                    );
                }
        );

        final Broadcast<Map<String, String>> airportsBroadcasted = sc.broadcast(airportsDescriptions.collectAsMap());

        JavaRDD<AirportsFlightsStats> parsedAirportsFlightsData = delaysStats.map(
                delaysWithAirports -> new AirportsFlightsStats(
                        delaysWithAirports._1(),
                        delaysWithAirports._2(),
                        airportsBroadcasted.value()
                )
        );

        parsedAirportsFlightsData.saveAsTextFile(OUTPUT_FILE_PATH);
    }

    private static String removeQuotes(String data) {
        return data.replaceAll(QUOTE, EMPTY);
    }

    private static JavaRDD<String> readFromCSV(JavaSparkContext sc, final String path, final String firstLinePrefix) {
        JavaRDD<String> data = sc.textFile(path);
        return data.filter(line -> !line.startsWith(firstLinePrefix));
    }
}