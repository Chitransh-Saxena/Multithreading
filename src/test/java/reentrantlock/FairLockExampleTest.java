package test.java.reentrantlock;

import org.junit.Test;
import reentrantLock.FairLockExample;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FairLockExampleTest {

    @Test
    public void testLockFairnessSuccess() throws InterruptedException {
        // Creating a fair lock instance
        FairLockExample example = new FairLockExample(true);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(2);
        AtomicBoolean firstThreadCompleted = new AtomicBoolean(false);

        // First thread should acquire the lock first
        Thread firstThread = new Thread(() -> {
            try {
                startLatch.await(); // Wait to start simultaneously
                example.printJob();
                firstThreadCompleted.set(true); // Mark first thread as complete
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                doneLatch.countDown();
            }
        }, "First");

        // Second thread should wait for the first thread to release the lock
        Thread secondThread = new Thread(() -> {
            try {
                startLatch.await();
                example.printJob();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                assertTrue("First thread should have completed.", firstThreadCompleted.get());
                doneLatch.countDown();
            }
        }, "Second");

        firstThread.start();
        secondThread.start();
        startLatch.countDown(); // Start both threads
        doneLatch.await(); // Wait for both threads to finish
    }

}
