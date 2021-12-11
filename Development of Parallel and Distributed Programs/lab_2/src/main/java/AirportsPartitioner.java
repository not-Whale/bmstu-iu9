import org.apache.hadoop.mapreduce.Partitioner;

import javax.xml.soap.Text;

public class AirportsPartitioner extends Partitioner<AnFWritableComparable, Text> {
    @Override
    public int getPartition(AnFWritableComparable anFWritableComparable, Text text, int i) {
        return anFWritableComparable.getDestAirport()
    }
}