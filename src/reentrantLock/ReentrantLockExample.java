package reentrantLock;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExample {

    private final ReentrantLock lock = new ReentrantLock();

    public void process() throws InterruptedException {

        this.lock.lock(); // Block until the lock is acquired

        try {

            // Only one thread can execute this section at a time
            Thread.sleep(2000);
            System.out.println("Lock held by thread: " + Thread.currentThread().getName());
        } finally {
            this.lock.unlock(); // ensuring the lock is released
        }

    }
}
