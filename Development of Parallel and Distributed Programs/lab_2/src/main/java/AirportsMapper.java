import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class AirportsMapper extends Mapper<LongWritable, Text, AnFWritableComparable, Text> {
    public static final String SEPARATOR = "\",\"";
    public static final int INDICATOR = 0;
    public static final int AIRPORT_CODE = 0;
    public static final int AIRPORT_DESCRIPTION = 1;

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] row = value.toString().split(SEPARATOR);

        if (key.get() > 0) {
            String airportCodeStr = row[AIRPORT_CODE].replaceAll("\"", "");
            int airportCode = Integer.parseInt(airportCodeStr);
            String airportDescription = row[AIRPORT_DESCRIPTION];
            context.write(new AnFWritableComparable(airportCode, INDICATOR), new Text(airportDescription));
        }
    }
}