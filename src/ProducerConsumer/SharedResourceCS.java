package ProducerConsumer;

import java.util.LinkedList;
import java.util.Queue;

public class SharedResourceCS {

    private final Queue<Integer> queue = new LinkedList<>();
    private final int size;


    public SharedResourceCS(int size) {
        this.size = size;
    }

    public synchronized void produce(int item) throws InterruptedException {

        while(queue.size() == size) wait();

        queue.add(item);
        notify();
    }


    public synchronized int consume() throws InterruptedException {

        while(queue.isEmpty())    wait();

        int item = queue.poll();
        System.out.println("Item consumed - " + item);
        notify();

        return item;
    }
}
