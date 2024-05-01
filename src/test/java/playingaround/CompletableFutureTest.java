package test.java.playingaround;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureTest {

    // Look at Obsidian Multithreading Notes for all the method and their explanations.

    // Test case for CompletableFuture.supplyAsync
    @Test
    public void testSupplyAsync() throws ExecutionException, InterruptedException {

        // () -> 10 is the lambda expression to produce a result of 10
        // supplyAsync() takes in a Supplier<U> function. Look at library
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 10);
        Assert.assertEquals(10, future.get().intValue());
    }

    @Test
    public void testThenApply() throws ExecutionException, InterruptedException {

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 10);
        CompletableFuture<String> transformation = future.thenApply(aboveRes -> "Result : " + aboveRes);

        Assert.assertEquals("Result : 10", transformation.get());
    }


    // Instead of forming result with different data types, an instance of CompletableFuture is returned
    // The coming result of that is added to existing future's result
    @Test
    public void testThenCompose() throws ExecutionException, InterruptedException {

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 10);
        CompletableFuture<String> transformation = future.thenCompose(aboveRes -> CompletableFuture.completedFuture("Result : " + aboveRes));

        Assert.assertEquals("Result : 10", transformation.get());
    }

    // Test case for CompletableFuture.thenCombine
    // Combine result of both completable futures when they complete
    @Test
    public void testThenCombine() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> 10);
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> 20);

        // Combine result using lambda (Integer::sum)
        CompletableFuture<Integer> combined = future1.thenCombine(future2, Integer::sum);
        Assert.assertEquals(30, combined.get().intValue());
    }

    @Test
    public void testThenAccept() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 10);
        CompletableFuture<Void> consumed = future.thenAccept(i -> System.out.println("Result: " + i));
        consumed.get(); // Ensure the consumer completes before asserting
    }

    // Test case for CompletableFuture.exceptionally - It handles every exception in completable future
    @Test
    public void testExceptionally() throws ExecutionException, InterruptedException {

        CompletableFuture<Object> future = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("Error");
        }).exceptionally(e -> {
            System.out.println("Handled exception: " + e.getMessage());
            return 0;
        });
        Assert.assertEquals(0, future.get());
    }

    @Test
    public void testHandle() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 10);
        CompletableFuture<String> handled = future.handle((result, throwable) -> {
            if (throwable != null) {
                return "Handled error: " + throwable.getMessage();
            } else {
                return "Result: " + result;
            }
        });
        Assert.assertEquals("Result: 10", handled.get());
    }

    @Test
    public void testThenApplyAsync() throws ExecutionException, InterruptedException {

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> nextFuture = future.thenApplyAsync((futureRes) -> futureRes + " World");

        Assert.assertEquals("Hello World", nextFuture.get());
    }


    // We manually complete the future with a certain value
    @Test
    public void testComplete() throws ExecutionException, InterruptedException {

        CompletableFuture<Integer> completableFuture = new CompletableFuture();
        completableFuture.complete(10);

        Assert.assertEquals(10, completableFuture.get().intValue());
    }


}
