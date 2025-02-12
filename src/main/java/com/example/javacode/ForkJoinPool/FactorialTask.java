package com.example.javacode.ForkJoinPool;

import java.math.BigInteger;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class FactorialTask extends RecursiveTask<BigInteger> {
    private final long factorial;

    public FactorialTask(long factorial) {
        this.factorial = factorial;
    }

    @Override
    protected BigInteger compute() {
        if(factorial == 1 || factorial == 0) {
            return BigInteger.ONE;
        }
        if(factorial == 2){
            return BigInteger.TWO;
        }
        FactorialTask task = new FactorialTask(factorial - 1);
        task.fork();
        return task.join().multiply(BigInteger.valueOf(factorial));
    }

    public static void main(String[] args) {
        long n = 10; // Вычисление факториала для числа 10

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        FactorialTask factorialTask = new FactorialTask(n);

        BigInteger result = forkJoinPool.invoke(factorialTask);

        System.out.println("Факториал " + n + "! = " + result);
    }
}
