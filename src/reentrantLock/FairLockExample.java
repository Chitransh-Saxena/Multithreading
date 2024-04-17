package reentrantLock;

import java.util.concurrent.locks.ReentrantLock;

public class FairLockExample {

    private final ReentrantLock lock;

    public FairLockExample(boolean isFairLock) {
        this.lock = new ReentrantLock(isFairLock);
    }


    public void printJob() {
        lock.lock();

        try {
            System.out.println("Thread " + Thread.currentThread().getName() + " acquired lock");
            Thread.sleep(3000); // Simulating some work
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
            System.out.println("Thread " + Thread.currentThread().getName() + " released lock");
        }
    }
}
