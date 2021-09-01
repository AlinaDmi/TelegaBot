import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulerExample {

    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        for (int i = 1; i <= 10; i++) {
            pool.schedule(SchedulerExample::task, i, TimeUnit.SECONDS);
        }
        pool.shutdown();
        pool.awaitTermination(15, TimeUnit.SECONDS);
    }

    static void task() {
        final LocalTime now = LocalTime.now();
        if (now.getSecond() % 10 == 0) {
            throw new IllegalStateException();
        }
        System.out.format("%s: %s%n", now, Thread.currentThread().getName());
    }
}