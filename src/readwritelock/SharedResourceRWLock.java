package readwritelock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SharedResourceRWLock {

    private int data = 0;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public int readData() {

        lock.readLock().lock();
        try {
            System.out.println("Read Lock acquired by - " + Thread.currentThread().getName());
            return this.data;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void writeData(int newData) {

        lock.writeLock().lock();

        try {
            System.out.println("Write Lock acquired by - " + Thread.currentThread().getName());
            this.data = newData;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
