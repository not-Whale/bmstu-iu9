import javax.xml.soap.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class AirportsPartitioner extends Partitioner<AnFWritableComparable, Text> {
    @Override
    public int getPartition(AnFWritableComparable anFWritableComparable, Text text, int i) {
        return anFWritableComparable.getDestAirport() % i;
    }
}