package lab1;

public class WordMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // <получаем строку из value, удаляем все спецсимволы, переводим в lowercase, разбиваем на слова и каждое
        // слово пишем в контекст с счетчиком 1
        // в контекст пишется пара — Text и IntWritable >

        // получаем строку из value
        String inputString = value.toString();


    }
}