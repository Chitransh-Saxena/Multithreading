package MonitorLock;

public class Main {
    public static void main(String[] args) {
        SharedResource obj = new SharedResource();


        // both producer and consumer will be using the same SharedResource obj
        // Note: we have use Lmba below, we can do the traditional way of creating Producer class, implementing the Runnable Interface and overriding the run method
        Thread producer = new Thread(()-> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            obj.addItem();
        });


        Thread consumer = new Thread(() -> {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
           obj.consumeItem();
        });


        // To make sure consumer starts first, so we can understand that wait() is called, and monitor lock is released.
        producer.start();
        consumer.start();


    }
}

