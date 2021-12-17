import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class AirportsMapper extends Mapper<LongWritable, Text, AnFWritableComparable, Text> {
    private static final String SEPARATOR = ",";
    private static final int INDICATOR = 0;
    private static final int AIRPORT_CODE = 0;
    private static final int AIRPORT_DESCRIPTION = 1;

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] row = value.toString().split(SEPARATOR);

        if (key.get() > 0) {
            String airportCodeString = row[AIRPORT_CODE]
                    .replaceAll("\"", "")
                    .trim();
            String airportDescription = row[AIRPORT_DESCRIPTION]
                    .replaceAll("\"", "")
                    .trim();
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
}