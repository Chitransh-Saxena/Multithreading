package ThreadBasics;

public class Thread3 implements Runnable{

    @Override
    public void run() {
        System.out.println("Code is being executed by thread3:" + Thread.currentThread().getName());
    }
}


