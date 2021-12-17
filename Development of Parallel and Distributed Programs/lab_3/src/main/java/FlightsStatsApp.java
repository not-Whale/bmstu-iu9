import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class FlightsStatsApp {
    private static final String FLIGHTS_FILE_PATH = "scr/main/resources/flights.csv";
    private static final String AIRPORTS_FILE_PATH = "scr/main/resources/airports.csv";
    private static final String OUTPUT_FILE_PATH = "output_lab_3";
    private static final String SEPARATOR = ",";

    public static void main(String[] args) throws Exception {
        SparkConf conf = new SparkConf().setAppName("lab3");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaPairRDD<Tuple2<String, String>, FlightsDelay> flightsDelays;
        JavaRDD<String> flightsDataCSV = readFromCSV(sc, FLIGHTS_FILE_PATH, "\"");
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
        JavaPairRDD<String, String> airportsDescriptions;
    }

    private static JavaRDD<String> readFromCSV(JavaSparkContext sc, final String path, final String firstLinePrefix) {
        JavaRDD<String> data = sc.textFile(path);
        return data.filter(line -> !line.startsWith(firstLinePrefix));
    }
}