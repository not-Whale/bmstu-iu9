import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class AirportsReducer extends Reducer<AnFWritableComparable, Text, Text, Text> {
    protected void reduce(AnFWritableComparable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        float minDelay = Float.MAX_VALUE;
        float maxDelay = Float.MIN_VALUE;
        float averageDelay = 0.0f;
        int numberOfFlights = 0;
        Iterator<Text> iterator = values.iterator();
        Text destinationAirport = new Text(String.valueOf(key.getDestAirport()));
        while (iterator.hasNext()) {
            String delayStr = iterator.next().toString();
            float delay = Float.parseFloat(delayStr);
            if (delay > maxDelay) {
                maxDelay = delay;
            }
            if (delay < minDelay) {
                minDelay = delay;
            }
            averageDelay += delay;
            numberOfFlights++;
        }

        if (numberOfFlights != 0) {
            averageDelay = averageDelay / numberOfFlights;
            context.write(new Text("DEST_AIRPORT_ID: " + destinationAirport ), new Text(minDelay + ", " + maxDelay + ", " + averageDelay));
        }
    }
}