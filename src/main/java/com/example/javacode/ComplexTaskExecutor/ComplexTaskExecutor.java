package com.example.javacode.ComplexTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ComplexTaskExecutor {
    private final int numOfThreads;

    public ComplexTaskExecutor(int numOfThreads) {
        this.numOfThreads = numOfThreads;
    }

    public void executeTasks(int numberOfTasks) {
        ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);
        AtomicInteger resultSum = new AtomicInteger(0);
        CyclicBarrier barrier = new CyclicBarrier(numberOfTasks, () -> {
            System.out.println("All tasks completed,result: " + resultSum.get());
        });

        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < numberOfTasks; i++) {
            final int taskId = i;
            Runnable task = () -> {
                try {
                    ComplexTask complexTask = new ComplexTask(taskId);
                    int taskResult = complexTask.call();
                    resultSum.addAndGet(taskResult);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        barrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            };
            Future<?> future = executorService.submit(task);
            futures.add(future);
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.err.println("Ожидание завершения прервано: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        ComplexTaskExecutor taskExecutor = new ComplexTaskExecutor(5); // Количество задач для выполнения

        Runnable testRunnable = () -> {
            System.out.println(Thread.currentThread().getName() + " started the test.");

            // Выполнение задач
            taskExecutor.executeTasks(5);

            System.out.println(Thread.currentThread().getName() + " completed the test.");
        };

        Thread thread1 = new Thread(testRunnable, "TestThread-1");
        Thread thread2 = new Thread(testRunnable, "TestThread-2");

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
