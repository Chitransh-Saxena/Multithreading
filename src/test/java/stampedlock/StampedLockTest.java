package test.java.stampedlock;

import org.junit.Assert;
import org.junit.Test;
import stampedlock.SharedResourceStamped;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StampedLockTest {

    @Test
    public void testOptimisticRead() {

        SharedResourceStamped resource = new SharedResourceStamped();
        resource.writeData(7);

        int numThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for(int i = 0; i<numThreads; i++) {
            executorService.execute(() -> {
                int val = 0;
                try {
                    val = resource.readOptimistic();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Assert.assertEquals(7, val);
            });
        }

        executorService.shutdown();

    }

    @Test
    public void testOptimisticReadWithWrite() throws InterruptedException {

        SharedResourceStamped resource = new SharedResourceStamped();
        resource.writeData(7);

        int numThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for(int i = 0; i<numThreads; i++) {
            executorService.execute(() -> {
                try {
                    int val = resource.readOptimistic();
                    Assert.assertEquals(7, val);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        // Introduce concurrent write
        new Thread(() -> {
            resource.writeData(10);
            try {
                Assert.assertEquals(10, resource.readOptimistic());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        executorService.shutdown();

    }
}
