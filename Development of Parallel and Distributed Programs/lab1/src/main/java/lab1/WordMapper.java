package lab1;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // получаем строку из value
        String inputString = value.toString();

        // разбиваем строку на слова
        String[] words = inputString.split(" ");

        // записываем в контекст каждое слово с счетчиком 1
        for (String word : words) {
            context.write(new Text(word), new IntWritable(1));
        }
    }
}