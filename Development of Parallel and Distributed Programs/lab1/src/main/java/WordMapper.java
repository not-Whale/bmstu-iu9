import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private final String SPACE_SEPARATOR = " ";

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String inputString = value.toString();
        String[] words = replaceAndTrim(inputString).split(SPACE_SEPARATOR);

        for (String word : words) {
            context.write(new Text(word), new IntWritable(1));
        }
    }

    private String replaceAndTrim(String inputString) {
        return inputString
                .toLowerCase()
                .replaceAll("[—\\s]", SPACE_SEPARATOR)
                .replaceAll("[-\\s]", SPACE_SEPARATOR)
                .replaceAll("[\\W&&[^-'а-я]]", SPACE_SEPARATOR)
                .replaceAll(" +", SPACE_SEPARATOR)
                .trim();
    }
}