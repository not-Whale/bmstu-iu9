import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class AnFWritableComparable implements WritableComparable<AnFWritableComparable> {
    private final IntWritable destAirport;
    private final IntWritable indicator;

    public AnFWritableComparable(IntWritable destinationAirport, IntWritable indicator) {
        this.destAirport = destinationAirport;
        this.indicator = indicator;
    }

    public AnFWritableComparable() {
        this.destAirport = new IntWritable(0);
        this.indicator = new IntWritable(0);
    }

    public IntWritable getDestAirport() {
        return this.destAirport;
    }

    public IntWritable getIndicator() {
        return this.indicator;
    }

    @Override
    public int compareTo(AnFWritableComparable o) {
        int airportCodesIsEqual = this.getDestAirport().compareTo(o.getDestAirport());
        int indicatorsIsEqual = this.getIndicator().compareTo(o.getIndicator());
        return airportCodesIsEqual == 0 ? indicatorsIsEqual : airportCodesIsEqual;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        destAirport.write(dataOutput);
        indicator.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        destAirport.readFields(dataInput);
        indicator.readFields(dataInput);
    }
}