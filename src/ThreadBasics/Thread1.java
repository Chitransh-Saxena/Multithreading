package ThreadBasics;

public class Thread1 extends Thread {

    public Thread1(String threadName) {
        super(threadName);
    }
    @Override
    public void run() {
        System.out.println("Thread 1 is starting");
        for(int i = 0; i<10; i++) {
            System.out.println(Thread.currentThread() + " printing: " + i);
        }

        System.out.println("Thread 1 is exiting");
    }
}
