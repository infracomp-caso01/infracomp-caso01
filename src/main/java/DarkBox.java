import java.util.Stack;

class DarkBox implements Process {
    private final Stack<String> in;
    private final Buffer<String> out;

    DarkBox(Stack<String> in, Buffer<String> out) {
        this.in = in;
        this.out = out;
    }

    @Override public void run() {
        boolean sentinel = false;
        Main.log("DARBOX start");
        while (!in.isEmpty()) {
            synchronized (out) {
                if (!out.isFull()) {
                    sentinel = true;
                    out.put(in.pop());
                    out.notifyAll();
                }
            }
            if (!sentinel) Thread.yield();
            sentinel = false;
        }
        Main.log("DARKBOX put all messages, then END");
        for (int i = 0; i < 3; i++) {
            synchronized (out) {
                if (out.isFull()) --i;
                else {
                    out.put("FIN");
                    out.notifyAll();
                    sentinel = true;
                }
            }
            if (!sentinel) Thread.yield();
        }
    }
}
