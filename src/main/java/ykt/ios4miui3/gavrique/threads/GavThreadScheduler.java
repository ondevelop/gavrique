package ykt.ios4miui3.gavrique.threads;

import ykt.ios4miui3.gavrique.Core.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GavThreadScheduler {
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
    private static List<Task> tasks = new ArrayList<Task>() {{
        add(new Task(QueueManager::playFromQueue, 2));
        add(new Task(BotUpdates::check, 3));
    }};

    public static void start() {
        int initialDelay = 3;
        for (Task task : tasks) {
            scheduler.scheduleAtFixedRate(task, initialDelay++, task.getDelay(), TimeUnit.SECONDS);
        }
    }

    public static class Task implements Runnable {
        private final Runnable method;
        private final int delay;
        private boolean run = false;

        public int getDelay() {
            return delay;
        }

        public Task(Runnable method, int delay) {
            this.method = method;
            this.delay = delay;
        }

        @Override
        public void run() {
            if (!run) {
                try {
                    run = true;
                    this.method.run();
                } catch (Exception e) {
                    Logger.get().error("thread error", e);
                } finally {
                    run = false;
                }
            }
        }
    }

    public static void shutdown() {
        scheduler.shutdownNow();
        try {
            boolean isTerminated = scheduler.awaitTermination(20, TimeUnit.SECONDS);
            if (!isTerminated) {
                StringBuilder sb = new StringBuilder("scheduler");
                sb.append(" executor termination timeout expired(");
                sb.append(20);
                sb.append(" sec)");
                Logger.get().warn(sb.toString());
            }
        } catch (InterruptedException e) {
            Logger.get().error("scheduler executor termination failed", e);
        }
    }
}
