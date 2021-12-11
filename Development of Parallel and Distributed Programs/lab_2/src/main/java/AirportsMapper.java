import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class AirportsMapper extends Mapper<LongWritable, Text, AnFWritableComparable, Text> {
    public static final String SEPARATOR = "\",\"";
    public static final int INDICATOR = 0;
    public static final int AIRPORT_ID

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] row = value.toString().split(SEPARATOR);

        if (key.get() > 0) {
            String destinationAirport = row[DEST_AIRPORT_ID];
            String flightDelay = row[FLIGHT_DELAY];
            boolean cancelled = Float.parseFloat(row[CANCELLED_STATUS]) == CANCELLED;
            if (!cancelled && !flightDelay.isEmpty()) {
                context.write(new AnFWritableComparable(Integer.parseInt(destinationAirport), INDICATOR), new Text(flightDelay));
            }
        }
    }
}