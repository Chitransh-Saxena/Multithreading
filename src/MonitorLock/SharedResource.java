package MonitorLock;

public class SharedResource {
    boolean isItemPresent = false;

    // making synchronized, makes sure this method acquires monitor lock on the obj thro' which its called
    public synchronized void addItem() {
        isItemPresent = true;
        System.out.println("Producer thread calling notify method" + Thread.currentThread().getName());
        notifyAll();
    }

    // making synchronized, makes sure this method acquires monitor lock on the obj thro' which its called

    public synchronized void consumeItem() {

        System.out.println("Consumer thread calling method" + Thread.currentThread().getName());
        if(!isItemPresent) {
            // wait for producer to add the item
            try {
                System.out.println("Consumer thread searching for item and calling wait() method" + Thread.currentThread().getName());
                wait(); // releases all monitor lock
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        isItemPresent = false;
    }
}
