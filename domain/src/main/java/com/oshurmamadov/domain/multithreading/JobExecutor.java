package com.oshurmamadov.domain.multithreading;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
    /**
    * Background job executor
    *
    * Based on Fernando Cejas' ThreadExecutor as part of his clean architecture service
    *
    * github.com/android10/Android-CleanArchitecture/blob/master/data/src/main/java/com/fernandocejas/android10/sample/data/executor/JobExecutor.java
    */
public class JobExecutor implements Executor {

    private static final int INITIAL_POOL_SIZE = 3;
    private static final int MAX_POOL_SIZE = 5;

    // Amount of time an idle thread waits before terminating
    private static final long KEEP_ALIVE_TIME = 10;

    // The time unit of seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private final BlockingQueue<Runnable> workQueue;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final ThreadFactory threadFactory;

    /**
     * Create IO job executor.
     */
    public JobExecutor() {
        workQueue = new LinkedBlockingQueue<>();
        threadFactory = new JobThreadFactory();

        threadPoolExecutor = new ThreadPoolExecutor(
            INITIAL_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            KEEP_ALIVE_TIME_UNIT,
            workQueue,
            threadFactory
        );
    }

    @Override
    public void execute(@NotNull Runnable command) {
        threadPoolExecutor.execute(command);
    }

    private static class JobThreadFactory implements ThreadFactory {

        private static final String THREAD_NAME = "SYD_RX_";

        private int counter = 0;

        @Override
        public Thread newThread(@NotNull Runnable r) {
            return new Thread(r, THREAD_NAME + counter);
        }
    }
}
