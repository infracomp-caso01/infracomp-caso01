import java.util.LinkedList;
import java.util.Queue;

@FunctionalInterface interface Synchronous {
    void synchronousBlock();
}

public class Buffer<Msg> {
    private final int MAX_SIZE;
    private final Queue<Msg> queueOrange = new LinkedList<>();
    private final Queue<Msg> queueBlue = new LinkedList<>();
    private int occupied = 0;

    Buffer(int cap) {MAX_SIZE = cap;}

    public Queue<Msg> getSyncOrange() {return queueOrange;}

    public Queue<Msg> getSyncBlue() {return queueBlue;}

    Msg getBlue() {
        --occupied;
        return queueBlue.poll();
    }

    Msg getOrange() {
        --occupied;
        return queueOrange.poll();
    }

    void putBlue(Msg msg) {
        ++occupied;
        queueBlue.add(msg);
    }

    void putOrange(Msg msg) {
        ++occupied;
        queueOrange.add(msg);
    }

    boolean isFullBlue() {return occupied == MAX_SIZE;}

    boolean isFullOrange() {return occupied == MAX_SIZE;}

    boolean isEmptyBlue() {return queueBlue.isEmpty();}

    boolean isEmptyOrange() {return queueOrange.isEmpty();}

    public void notifyBlue() {queueBlue.notify();}

    public void notifyAllBlue() {queueBlue.notifyAll();}

    public void waitBlue() throws InterruptedException {queueBlue.wait();}

    public void synchronizedBlue(Synchronous lambda) {
        synchronized (queueBlue) {
            lambda.synchronousBlock();
        }
    }

    public void synchronizedOrange(Synchronous lambdaOut, Synchronous lambdaIn) {
        lambdaOut.synchronousBlock();
        synchronized (queueOrange) {
            lambdaIn.synchronousBlock();
        }
    }
}