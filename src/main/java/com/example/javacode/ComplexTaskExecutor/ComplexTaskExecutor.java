package com.example.javacode.ComplexTaskExecutor;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ComplexTaskExecutor {
    private final int numOfThreads;


    public ComplexTaskExecutor(int numOfThreads) {
        this.numOfThreads = numOfThreads;
    }


    public void executeTasks(int numberOfTasks) {
        ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(numOfThreads + 1, new Runnable() {
            public void run() {
                System.out.println("Barrier is done");
            }
        });
        for (int i = 0; i < numberOfTasks; i++) {
            final int finalI = i;
            executorService.execute(() -> {
                ComplexTask complexTask = new ComplexTask(finalI);
                complexTask.run();
                try {
                    cyclicBarrier.await();
                } catch (BrokenBarrierException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        try {
            cyclicBarrier.await();
        } catch (BrokenBarrierException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        executorService.shutdown();
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
