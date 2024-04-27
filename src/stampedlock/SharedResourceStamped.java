package stampedlock;

import java.util.concurrent.locks.StampedLock;

public class SharedResourceStamped {

    int data = 0;
    StampedLock stampedLock = new StampedLock();

    public void writeData(int newData) {

        long stamp = stampedLock.writeLock();
        try {
            this.data = newData;
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    public int readOptimistic() throws InterruptedException {
        long stamp = stampedLock.tryOptimisticRead();
        int val = this.data;

        Thread.sleep(2000);
        if(!stampedLock.validate(stamp)) {

            // Lock was not valid, upgrade to read-lock
            System.out.println("Looks like someone wrote while this code wanted to read");
            stamp = stampedLock.readLock();
            try {
                val = this.data;
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }

        return val;
    }

    public int readPessimistic() {

        long stamp = stampedLock.readLock();
        try {
            return this.data;
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }
}
