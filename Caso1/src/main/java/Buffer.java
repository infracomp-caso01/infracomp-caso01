import java.util.LinkedList;
import java.util.Queue;

public class Buffer<Msg> {
    private final int MAX_SIZE;
    private Queue<Msg> queue = new LinkedList<>();

    public Buffer(int maxSize) {MAX_SIZE = maxSize;}

    Msg get() {return queue.remove();}

    void put(Msg msg) {queue.add(msg);}

    boolean isFull() {return queue.size() == MAX_SIZE;}

    boolean isEmpty() {return queue.isEmpty();}
}
