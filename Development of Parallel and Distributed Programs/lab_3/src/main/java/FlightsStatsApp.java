import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class FlightsStatsApp {
    private static final String flightsFilePath = "scr/main/resources/flights.csv";
    private static final String airportsFilePath = "scr/main/resources/airports.csv";
    private static final String outputFileName = "output_lab_3";
    private static final String SEPARATOR = ",";

    public static void main(String[] args) throws Exception {
        SparkConf conf = new SparkConf().setAppName("lab3");
        JavaSparkContext sc = new JavaSparkContext(conf);
    }

    private static JavaRDD<String> readFromCSV(JavaSparkContext sc, final String path, final )
}
