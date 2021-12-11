import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class AnFWritableComparable implements WritableComparable<AnFWritableComparable> {
    private int destAirport;
    private int indicator;

    public AnFWritableComparable(int destinationAirport, int indicator) {
        super();
        this.destAirport = destinationAirport;
        this.indicator = indicator;
    }

    @Override
    public int compareTo(AnFWritableComparable o) {
        int airport
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {

    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {

    }
}