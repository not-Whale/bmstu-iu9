import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String inputString = value.toString();

        inputString = inputString.toLowerCase();
        inputString = inputString.replaceAll("[—\\s]", " ");
        inputString = inputString.replaceAll("[-\\s]", " ");
        inputString = inputString.replaceAll("[\\W&&[^-'а-я]]", " ");
        inputString = inputString.replaceAll(" +", " ");
        inputString = inputString.trim();

        String[] words = inputString.split(" ");

        for (String word : words) {
            context.write(new Text(word), new IntWritable(1));
        }
    }
}