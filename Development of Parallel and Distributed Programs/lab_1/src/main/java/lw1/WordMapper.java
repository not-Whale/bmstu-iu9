package lw1;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // <получаем строку из value, удаляем все спецсимволы, переводим в lowercase, разбиваем
        // на слова и каждое слово пишем в контекст с счетчиком 1
        // в контекст пишется пара — Text и IntWritable>

        String inputString = value.toString();

        inputString = inputString.replaceAll("[—\\s]", " ");
        inputString = inputString.replaceAll("[-\\s]", " ");
        inputString = inputString.replaceAll(".", "");
        inputString = inputString.replaceAll("[\\W&&[^-']]", " ");
        inputString = inputString.replaceAll("[ \\+]", " ");

        String[] words = inputString.split("\\s");

        for (String word : words) {
            context.write(new Text(word), new IntWritable(1));
        }
    }
}