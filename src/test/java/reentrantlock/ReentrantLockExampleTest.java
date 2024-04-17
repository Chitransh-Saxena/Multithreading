package test.java.reentrantlock;

import org.junit.Test;
import reentrantLock.ReentrantLockExample;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class ReentrantLockExampleTest {


    @Test
    public void testSingleThreadLocking() throws InterruptedException {
        ReentrantLockExample example = new ReentrantLockExample();
        CountDownLatch latch = new CountDownLatch(1);

        Thread thread = new Thread(() -> {
            try {
                example.process();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            latch.countDown();
        });

        thread.start();
        latch.await(1, TimeUnit.SECONDS);
    }
}
