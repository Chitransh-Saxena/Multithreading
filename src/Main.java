import ThreadBasics.Thread1;
import ThreadBasics.Thread2;
import ThreadBasics.Thread3;

public class Main {
    public static void main(String[] args) {
        System.out.println("Main thread is starting");

        Thread thread1 = new Thread1("Thread1"); // A child thread is created
        thread1.start();    // This will implement the run method which we created in Thread1 class


        Thread thread2 = new Thread(new Thread2(), "Thread2");
        thread2.start();

        // It is not necessary that below print statement will execute at the end
        // It is at the mercy of JVM, it could happen while thread1 is still executing
        System.out.println("Main thread is exiting");
        System.out.println(printMessage("text"));

        // Understanding how a thread is being created and started, when implementing Runnable Interface
        System.out.println("Thread3 starting main method");
        Thread3 thread3 = new Thread3();
        Thread thread3_thread = new Thread(thread3);
        thread3_thread.start();

        System.out.println("Thread3 exiting");
    }

  public static boolean printMessage(String text) {

    System.out.println("Texting" + text);
      return false;
  }
}
