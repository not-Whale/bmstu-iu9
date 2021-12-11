import org.apache.hadoop.io.Text;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class FlightsStatsApp {
    public static void main(String[] args) throws Exception {
        // проверка на правильность ввода
        // формат: FlightsStatsApp <путь к списку рейсов> <путь к списку аэропортов> <путь вывода>
        if (args.length != 3) {
            System.err.println("Usage: FlightsStatsApp <flights list path> <airports list path> <output path>");
            System.exit(-1);
        }

        Job job = Job.getInstance();
        job.setJarByClass(FlightsStatsApp.class);
        job.setJobName("Reduce side join App");

        // задание путей файлов ввода и вывода
        MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, FlightsMapper.class);
        MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, AirportsMapper.class);
        FileOutputFormat.setOutputPath(job, new Path(args[2]));

        job.setMapOutputKeyClass(AnFWritableComparable.class);
        job.setMapOutputValueClass(Text.class);
        job.setPartitionerClass(AirportsPartitioner.class);
        job.setGroupingComparatorClass(AirportsGroupingComparator.class);
        job.setReducerClass(AirportsReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setNumReduceTasks(2);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}