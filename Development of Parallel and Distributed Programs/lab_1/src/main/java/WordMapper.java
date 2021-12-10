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

        // перевод в нижний регистр
        inputString = inputString.toLowerCase();
        // удаляем все спецсимволы
        // сначала для прямой речи
        inputString = inputString.replaceAll("[—\\s]", " ");
        inputString = inputString.replaceAll("[-\\s]", " ");

        // далее все специальные символы, кроме ' для французский слов
        // и "картавости", а также дефисов внутри слов (например, "красно-синий")
        inputString = inputString.replaceAll("[\\W&&[^-'а-я]]", " ");
        // "схлопываем" пробелы
        inputString = inputString.replaceAll(" +", " ");
        // и удаляем их в начале и конце файла
        inputString = inputString.trim();

        // разделение на слова, запись в массив
        String[] words = inputString.split(" ");

        // запись слов в контекс с счетчиком 1
        for (String word : words) {
            context.write(new Text(word), new IntWritable(1));
        }
    }
}