import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlightsMapper extends Mapper<LongWritable, Text, AnFWritableComparable, Text> {
    public static final String SEPARATOR = ",";
    public static final int INDICATOR = 1;
    public static final int DEST_AIRPORT_ID = 14;
    public static final int FLIGHT_DELAY = 17;

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] row = value.toString().split(SEPARATOR);

        if (key.get() > 0) {
            String airportCodeString = row[DEST_AIRPORT_ID];
            int airportCode = Integer.parseInt(airportCodeString);

            String flightDelay = row[FLIGHT_DELAY];

            if (flightDelay.length() != 0) {
                float delay = Float.parseFloat(flightDelay);
                if (delay > 0.0f) {
                    context.write(
                            new AnFWritableComparable(
                                    new IntWritable(airportCode),
                                    new IntWritable(INDICATOR)),
                            new Text(flightDelay)
                    );
                }
            }
        }
    }
}