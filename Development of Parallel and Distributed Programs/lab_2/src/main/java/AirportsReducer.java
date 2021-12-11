import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class AirportsReducer extends Reducer<AnFWritableComparable, Text, Text, Text> {
    protected void reduce(AnFWritableComparable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        
    }
}