import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class FlightsStatsApp {
    private static final String flightsFilePath = "scr/main/resources/flights.csv";
    private static final String airportsFilePath = "scr/main/resources/airports.csv";
    private static final String outputFileName = "output_lab_3";
    private static final String SEPARATOR = ",";

    public static void main(String[] args) throws Exception {
        SparkConf conf = new SparkConf().setAppName("lab3");
        JavaSparkContext sc = new JavaSparkContext(conf);

        Java
        JavaPairRDD<Tuple2<String, String>, FlightsDelay> flightsDelays;
        JavaPairRDD<Tuple2<String, String>, DelaysStats> delaysStats;
    }

    private static JavaRDD<String> readFromCSV(JavaSparkContext sc, final String path, final String firstLinePrefix) {
        JavaRDD<String> data = sc.textFile(path);
        return data.filter(line -> !line.startsWith(firstLinePrefix));
    }
}
