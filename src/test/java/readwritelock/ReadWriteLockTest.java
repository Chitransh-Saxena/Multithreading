package test.java.readwritelock;

import org.junit.Assert;
import org.junit.Test;
import readwritelock.SharedResourceRWLock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReadWriteLockTest {

    @Test
    public void testReadWriteLock() {

        SharedResourceRWLock resource = new SharedResourceRWLock();
        resource.writeData(5);

        Assert.assertEquals(5, resource.readData());
    }

    @Test
    public void testMultithreadingRead() {

        SharedResourceRWLock resource = new SharedResourceRWLock();
        Thread[] threads = new Thread[5];

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.execute(() -> {
            resource.writeData(10);
        });


        for(int i = 0; i<threads.length; i++) {

            executorService.execute(() -> {
                int res = resource.readData();
                Assert.assertEquals(10, res);
            });
        }

        executorService.shutdown();
    }
}
