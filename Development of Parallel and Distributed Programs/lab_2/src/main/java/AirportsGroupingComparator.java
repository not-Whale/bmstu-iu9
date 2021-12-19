import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class AirportsGroupingComparator extends WritableComparator {

    public AirportsGroupingComparator() { super(AnFWritableComparable.class, true); }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        AnFWritableComparable key1 = (AnFWritableComparable) a;
        AnFWritableComparable key2 = (AnFWritableComparable) b;
        return key1.getDestAirport().compareTo(key2.getDestAirport());
    }
}