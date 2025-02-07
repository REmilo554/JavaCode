package com.example.javacode.BlockingQueue;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.out;
import static java.lang.Thread.currentThread;

public class BlockingQueue<T> {

    private final Queue<T> queue;
    private final int capacity;

    public BlockingQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        this.queue = new ArrayDeque<>(capacity);
        this.capacity = capacity;
    }

    public synchronized void enqueue(T message) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();
        }
        queue.add(message);
        notifyAll();
    }

    public synchronized T dequeue() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        T result = queue.poll();
        notifyAll();
        return result;
    }

    public synchronized int size() {
        return queue.size();
    }


    public static void main(String[] args) {
        BlockingQueue<Integer> queue1 = new BlockingQueue<>(10);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Thread producer = new Thread(() -> {
            try {
                while (!currentThread().isInterrupted()) {
                    out.println("Creating new message: " + atomicInteger.get());
                        queue1.enqueue(atomicInteger.get());
                        Thread.sleep(100);
                        if(queue1.size() <= 10){
                            atomicInteger.incrementAndGet();
                        }
                }
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
        });
        Thread consumer = new Thread(() -> {
            try {
                while (!currentThread().isInterrupted()) {
                    queue1.dequeue();
                    out.println("Consuming new message: " + atomicInteger.getAndDecrement());
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
        });
        producer.start();
        consumer.start();
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            currentThread().interrupt();
        }
        producer.interrupt();
        consumer.interrupt();
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            currentThread().interrupt();
        }
        out.println(queue1.size());
    }
}
