import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class AirportsGroupingComparator extends WritableComparator {

    public AirportsGroupingComparator() {
        super(AnFWritableComparable.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        AnFWritableComparable key1 = (AnFWritableComparable) a;
        AnFWritableComparable key2 = (AnFWritableComparable) b;
        return Integer.compare(key1.getDestAirport(), key2.getDestAirport());
    }
}