import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class AirportsMapper extends Mapper<LongWritable, Text, AnFWritableComparable, Text> {
    private static final String SEPARATOR = ",";
    private static final String QUOTE = "\"";
    private static final String SPACE = " ";
    private static final int INDICATOR = 0;
    private static final int AIRPORT_CODE = 0;
    private static final int AIRPORT_DESCRIPTION = 1;

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] row = value.toString().split(SEPARATOR);

        if (key.get() > 0) {
            String airportCodeString = deleteQuotesAndTrim(row[AIRPORT_CODE]);
            String airportDescription = deleteQuotesAndTrim(row[AIRPORT_DESCRIPTION]);
            int airportCode = Integer.parseInt(airportCodeString);
            context.write(
                    new AnFWritableComparable(
                            new IntWritable(airportCode),
                            new IntWritable(INDICATOR)
                    ),
                    new Text(airportDescription)
            );
        }
    }

    private String deleteQuotesAndTrim(String input_string) {
        return input_string.replaceAll(QUOTE, SPACE).trim();
    }
}