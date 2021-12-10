public class FlightsStatsApp {
    public static void main(Stringp[] args) throws Exception {
        // проверка на правильность ввода

        Job job = Job.getInstance();
        job.setJarByClass(FlightsStatsApp.class);
        job.setJobName("Reduce side join App");

        MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, FlightsMapper.class);
        MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, A);
    }
}