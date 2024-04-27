package test.java.semaphorelock;

import org.junit.Test;
import semaphorelock.SharedResourceSemaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class SemaphoreLockTest {

    @Test
    public void testSemaphore() throws InterruptedException {

        SharedResourceSemaphore sharedResource = new SharedResourceSemaphore(2);

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        int numThreads = 10;

        // Read threads
        for (int i = 0; i < numThreads; i++) {
            executorService.execute(() -> {
                try {
                    int value = sharedResource.read();
                    System.out.println("Read: " + value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // Write threads
        for (int i = 0; i < numThreads; i++) {
            final int newValue = i + 1; // Incrementing value for each thread
            executorService.execute(() -> {
                try {
                    sharedResource.write(newValue);
                    System.out.println("Write: " + newValue);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        // The final value should be the last written value
        assertEquals(numThreads, sharedResource.read());
    }
}
