public class FlightsStatsApp {
    public static void main(Stringp[] args) throws Exception {
        // проверка на правильность ввода
        // формат ввода 

        Job job = Job.getInstance();
        job.setJarByClass(FlightsStatsApp.class);
        job.setJobName("Reduce side join App");

        // задание путей файлов ввода и вывода
        MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, FlightsMapper.class);
        MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, AirportsMapper.class);
        FileOutputFormat.addInputPath(job, new Path(args[2]));
    }
}