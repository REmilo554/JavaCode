package com.example.javacode.ComplexTaskExecutor;

import java.util.concurrent.Callable;

public class ComplexTask implements Callable<Integer> {
    private final int taskId;

    public int getTaskId() {
        return taskId;
    }

    public ComplexTask(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public Integer call() {
        try {
            System.out.println("Task:" + getTaskId() + " in " + Thread.currentThread().getName() + " started");
            Thread.sleep(100);
            execute();
            return getTaskId();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return 0;
    }

    public void execute() {
        try {
            Thread.sleep(1000);
            System.out.println("Помощь в выполнении тяжелого задания");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
