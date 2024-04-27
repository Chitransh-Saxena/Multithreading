package semaphorelock;

import java.util.concurrent.Semaphore;

public class SharedResourceSemaphore {

    int data = 0;
    int permits;
    Semaphore lock;

    public SharedResourceSemaphore(int permits) {
        this.permits = permits;
        this.lock = new Semaphore(permits);
    }


    public void write(int newData) throws InterruptedException {

        lock.acquire();
        this.data = newData;
        lock.release();
    }

    public int read() throws InterruptedException {
        lock.acquire();
        int val = this.data;

        lock.release();
        return val;
    }
}
