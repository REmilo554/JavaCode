package com.example.javacode.ConcurrentBank;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private final UUID uuid = UUID.randomUUID();
    private BigDecimal balance;
    private final Lock lock = new ReentrantLock();


    public BankAccount(BigDecimal balance) {
        this.balance = balance;
    }

    public void deposit(BigDecimal amount) {
        lock.lock();
        try {
            balance = balance.add(amount);
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(BigDecimal amount) {
        lock.lock();
        try {
            balance = balance.subtract(amount);
        } finally {
            lock.unlock();
        }
    }

    public BigDecimal getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public UUID getUUID() {
        return uuid;
    }
}
