package test.java.producerconsumer;

import ProducerConsumer.SharedResourceCS;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SharedResourceTest {


    @Test
    public void testProduceConsumeFail() throws InterruptedException {

        int size = 10;
        SharedResourceCS resource = new SharedResourceCS(size);

        int threadsCount = 10;
        Thread[] producers = new Thread[threadsCount];
        Thread[] consumers = new Thread[threadsCount];

        for(int i = 0; i<threadsCount; i++) {
            producers[i] = new Thread(() -> {

                try {
                    resource.produce(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            producers[i].start();
        }

        producers[9].stop();

        for(int i = 0; i<threadsCount; i++) {
            consumers[i] = new Thread(() -> {
                int consumedItem = 0;
                try {
                    consumedItem = resource.consume();
                    assertEquals(1, consumedItem);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            consumers[i].start();
        }



        for(Thread producer: producers) producer.join();
        for(Thread consumer: consumers) consumer.join();
    }
}
