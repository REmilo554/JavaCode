package com.example.javacode.ComplexTaskExecutor;

public class ComplexTask implements Runnable {
    private final int taskId;

    public int getTaskId() {
        return taskId;
    }

    public ComplexTask(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public void run() {
        try {
            System.out.println("Task:" + getTaskId() + " in " + Thread.currentThread().getName() + " started");
            Thread.sleep(100);
            execute();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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
