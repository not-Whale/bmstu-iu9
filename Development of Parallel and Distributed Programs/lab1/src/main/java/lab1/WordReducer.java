public class WordReducer extends Reducer<Text, IntWritable, Text, LongWritable> {
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
       // <считаем количество записей в итераторе и генерируем запись в контекст
       // В контекст пишется пара — Text и LongWritable>
    }
}