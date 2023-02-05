import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class AirportsPartitioner extends Partitioner<AnFWritableComparable, Text> {
    @Override
    public int getPartition(AnFWritableComparable anFWritableComparable, Text text, int i) {
        return Math.abs(anFWritableComparable.getDestAirport().hashCode()) % i;
    }
}