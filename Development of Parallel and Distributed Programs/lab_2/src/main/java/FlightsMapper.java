import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlightsMapper extends Mapper<LongWritable, Text, AnFWritableComparable, Text> {
    public static final String SEPARATOR = ",";
    

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

    }
}