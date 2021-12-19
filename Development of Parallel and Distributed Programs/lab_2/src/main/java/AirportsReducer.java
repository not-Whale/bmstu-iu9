import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class AirportsReducer extends Reducer<AnFWritableComparable, Text, Text, Text> {
    protected void reduce(AnFWritableComparable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        float minDelay = Float.MAX_VALUE;
        float maxDelay = Float.MIN_VALUE;
        float sumDelay = 0;
        float averageDelay;
        int numberOfFlights = 0;
        final Text airportDescription;

        Iterator<Text> iterator = values.iterator();
        airportDescription = new Text(iterator.next().toString());

        while (iterator.hasNext()) {
            String delayStr = iterator.next().toString();
            float delay = Float.parseFloat(delayStr);
            if (delay > maxDelay) {
                maxDelay = delay;
            }
            if (delay < minDelay) {
                minDelay = delay;
            }
            sumDelay += delay;
            numberOfFlights++;
        }

        if (numberOfFlights != 0) {
            averageDelay = sumDelay / numberOfFlights;
            context.write(
                    new Text(
                            "airport: "
                                    + airportDescription),
                    new Text(
                            "min: "
                                    + minDelay
                                    + ", max: "
                                    + maxDelay
                                    + ", average: "
                                    + averageDelay)
            );
        }
    }
}