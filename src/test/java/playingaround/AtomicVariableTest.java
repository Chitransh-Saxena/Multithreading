package test.java.playingaround;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicVariableTest {


    @Test
    public void testAtomic() throws InterruptedException {

        int val = 0;
        AtomicInteger value = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(10);

        for(int i = 0; i < 10; i++) {
            executor.submit(() -> {

                for(int j = 0; j < 100; j++) {
                    int x = value.get();
                    value.set(x+1);
                }
            });
        }

        executor.awaitTermination(1000, TimeUnit.MILLISECONDS);
        Assert.assertEquals(1000, value.get());
    }


    @Test
    public void testAtomicCompareAndSet() {

        AtomicInteger atomicInt = new AtomicInteger(0);
        boolean result = atomicInt.compareAndSet(0, 2);

        Assert.assertTrue(result);
        Assert.assertEquals(2, atomicInt.get());

        result = atomicInt.compareAndSet(1, 3); // Try to set when the value is not equal to expected
        Assert.assertFalse(result);
        Assert.assertEquals(2, atomicInt.get()); // Value should remain unchanged
    }
}
