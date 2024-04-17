package ThreadBasics;

public class Thread2 implements Runnable{


    @Override
    public void run() {

        System.out.println("Thread 2 is running");
        for(int i = 0; i<10; i++) {
            System.out.println(Thread.currentThread() + " printing: " + i);
        }
        System.out.println("Thread 2 is exiting");
    }
}
