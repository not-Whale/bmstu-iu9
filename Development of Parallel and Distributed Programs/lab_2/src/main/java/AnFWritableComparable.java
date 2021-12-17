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

    public AnFWritableComparable() {}

    public int getDestAirport() {
        return this.destAirport;
    }

    @Override
    public int compareTo(AnFWritableComparable o) {
        int airportCodesIsEqual = Integer.compare(this.destAirport, o.destAirport);
        int indicatorsIsEqual = Integer.compare(this.indicator, o.indicator);
        return airportCodesIsEqual == 0 ? indicatorsIsEqual : airportCodesIsEqual;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.destAirport);
        dataOutput.writeInt(this.indicator);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.destAirport = dataInput.readInt();
        this.indicator = dataInput.readInt();
    }
}