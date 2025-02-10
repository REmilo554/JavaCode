package com.example.javacode.ConcurrentBank;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentBank {
    BigDecimal totalBalance;

    final Lock lock = new ReentrantLock();

    public ConcurrentBank() {
        this.totalBalance = BigDecimal.ZERO;
    }

    public BankAccount createAccount(BigDecimal balance) {
        lock.lock();
        try {
            totalBalance = totalBalance.add(balance);
            return new BankAccount(balance);
        } finally {
            lock.unlock();
        }
    }

    public void transfer(BankAccount from, BankAccount to, BigDecimal amount) {
        if (from.getUUID().compareTo(to.getUUID()) < 0) {
            synchronized (from) {
                synchronized (to) {
                    startTransfer(from, to, amount);
                }
            }
        } else {
            synchronized (to) {
                synchronized (from) {
                    startTransfer(from, to, amount);
                }
            }
        }
    }

    public void startTransfer(BankAccount from, BankAccount to, BigDecimal amount) {
        synchronized (from) {
            synchronized (to) {
                if (from.getBalance().compareTo(amount) >= 0) {
                    from.withdraw(amount);
                    to.deposit(amount);
                } else {
                    throw new IllegalArgumentException("Insufficient funds on balance for transfer");
                }
            }
        }
    }

    public BigDecimal getTotalBalance() {
        lock.lock();
        try {
            return totalBalance;
        } finally {
            lock.unlock();
        }
    }


    public static void main(String[] args) {
        ConcurrentBank bank = new ConcurrentBank();

        BankAccount account = bank.createAccount(new BigDecimal(10000));
        BankAccount account1 = bank.createAccount(new BigDecimal(20000));

        Thread t1 = new Thread(() -> bank.transfer(account, account1, new BigDecimal(1500)));
        Thread t2 = new Thread(() -> bank.transfer(account, account1, new BigDecimal(2000)));
        Thread t3 = new Thread(() -> bank.transfer(account1, account, new BigDecimal(3000)));
        Thread t4 = new Thread(() -> bank.transfer(account1, account, new BigDecimal(4000)));
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Total balance: " + bank.getTotalBalance());
        System.out.println("First account balance: " + account.getBalance());
        System.out.println("Second account balance: " + account1.getBalance());
    }
}
