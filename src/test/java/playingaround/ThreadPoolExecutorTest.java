package test.java.playingaround;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.*;

public class ThreadPoolExecutorTest {



    // Test case for fixed size thread pool with LinkedBlockingQueue
    @Test
    public void testFixedThreadPoolWithLinkedBlockingQueue() {

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        Assert.assertTrue(threadPoolExecutor.getQueue() instanceof LinkedBlockingQueue<Runnable>);
    }


    @Test
    public void testFixedThreadPoolWithSynchronousQueue() {

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        Assert.assertTrue(threadPoolExecutor.getQueue() instanceof SynchronousQueue);
    }

    @Test
    public void testArrayBlockingQueue() throws InterruptedException {
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(2);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, queue);

        CountDownLatch latch = new CountDownLatch(3); // Initialize with the number of tasks

        // Submit three tasks
        threadPoolExecutor.submit(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("Task 1 completed");
                latch.countDown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        threadPoolExecutor.submit(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("Task 2 completed");
                latch.countDown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        threadPoolExecutor.submit(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("Task 3 completed");
                latch.countDown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // Queue size will be 1, because two tasks are handled by 2 threads, and 1 task waits in queue.
        Assert.assertEquals(1, queue.size());

        // Await the countdown on the latch
        latch.await();

        // Shutdown the executor
        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(1, TimeUnit.SECONDS);

        // Assert that the queue is empty after shutdown
        Assert.assertTrue(queue.isEmpty());
    }


    @Test(expected = RejectedExecutionException.class)      // Throws this error because too many tasks are there for too little threads and queue size.
    public void testArrayBlockingQueueFullCapacity() throws InterruptedException {
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(2);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, queue);

//        CountDownLatch latch = new CountDownLatch(3); // Initialize with the number of tasks

        for(int i = 0; i<10; i++) {
            int finalI = i;
            threadPoolExecutor.submit(() -> {
                try {
                    Thread.sleep(3000);
                    System.out.println("Task " + finalI + " completed");
                    // latch.countDown();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        // Shutdown the executor
        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(1, TimeUnit.SECONDS);

        // Assert that the queue is empty after shutdown
        Assert.assertTrue(queue.isEmpty());
    }

    @Test      // Throws this error because too many tasks are there for too little threads and queue size.
    public void testArrayBlockingQueueMaxThreads() throws InterruptedException {
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(5);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 5, 0L, TimeUnit.MILLISECONDS, queue);


        for(int i = 0; i<10; i++) {
            int finalI = i;
            threadPoolExecutor.submit(() -> {
                try {
                    Thread.sleep(1000);
                    System.out.println("Task " + finalI + " completed");
                    // latch.countDown();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        // Shutdown the executor
        threadPoolExecutor.awaitTermination(5000, TimeUnit.MILLISECONDS);
        threadPoolExecutor.shutdown();

        // Assert that the queue is empty after shutdown
        Assert.assertTrue(queue.isEmpty());
    }

    // If no threads are available, since there is no internals storage, task will be rejected
    // Synchronous Queue does immediate hand-off of tasks between threads, in below case, since sleep is added, no threads would be available
    // And thus, the failure
    @Test(expected = RejectedExecutionException.class)
    public void testSynchronousQueue() throws InterruptedException {
        SynchronousQueue<Runnable> queue = new SynchronousQueue<>();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, queue);

        CountDownLatch latch = new CountDownLatch(3); // Initialize with the number of tasks

        // Submit three tasks
        threadPoolExecutor.submit(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("Task 1 completed");
                latch.countDown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        threadPoolExecutor.submit(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("Task 2 completed");
                latch.countDown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        threadPoolExecutor.submit(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("Task 3 completed");
                latch.countDown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });


        // Await the countdown on the latch
        latch.await();

        // Shutdown the executor
        threadPoolExecutor.awaitTermination(5, TimeUnit.SECONDS);
        threadPoolExecutor.shutdown();

        // Assert that the queue is empty after shutdown
        Assert.assertTrue(queue.isEmpty());
    }

    // Max threads should be enough to accommodate a new task. If not available, then RejectedExecutionException will occur
    // maximumPoolSize is increased in this case
    @Test
    public void testSynchronousQueueSuccess() throws InterruptedException {
        SynchronousQueue<Runnable> queue = new SynchronousQueue<>();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 3, 0L, TimeUnit.MILLISECONDS, queue);


        // Submit three tasks
        threadPoolExecutor.submit(() -> {
            System.out.println("Task 1 completed");
        });

        threadPoolExecutor.submit(() -> {
            System.out.println("Task 2 completed");
        });

        threadPoolExecutor.submit(() -> {
            System.out.println("Task 3 completed");
        });


        // Shutdown the executor
        threadPoolExecutor.awaitTermination(5, TimeUnit.SECONDS);
        threadPoolExecutor.shutdown();

        // Assert that the queue is empty after shutdown
        Assert.assertTrue(queue.isEmpty());
    }


    // Test case for failed execution of task
    @Test(expected = NullPointerException.class)
    public void testFailedTaskExecution() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

        // Submit a task with null runnable, which will result in a NullPointerException
        executor.execute(null);

    }

}
