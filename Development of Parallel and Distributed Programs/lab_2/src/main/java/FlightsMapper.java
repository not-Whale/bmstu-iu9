import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlightsMapper extends Mapper<LongWritable, Text, AnFWritableComparable, Text> {
    public static final String SEPARATOR = ",";
    public static final int INDICATOR = 1;
    public static final float CANCELLED = 1.0f;
    public static final int CANCELLED_STATUS = 19;
    public static final int DEST_AIRPORT_ID = 14;
    public static final int FLIGHT_DELAY = 18;

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] row = value.toString().split(SEPARATOR);

        if (key.get() > 0) {
            String destinationAirport = row[DEST_AIRPORT_ID];
            String flightDelay = row[FLIGHT_DELAY];
            boolean cancelled = Float.parseFloat(row[CANCELLED_STATUS]) == CANCELLED;
            if (!cancelled && !flightDelay.isEmpty()) {
                float delay = Float.parseFloat(flightDelay);
                context.write(new AnFWritableComparable(Integer.parseInt(destinationAirport)), );
            }
        }
    }
}