class Orange implements Process {
    private final long securityId, timeMillis;
    private final Buffer<String> in, out;

    public Orange(Buffer<String> in, Buffer<String> out) {
        this.securityId = SecureUtility.nextID();
        this.timeMillis = SecureUtility.randomTime();
        this.in = in;
        this.out = out;
    }

    @Override public void run() {
        Main.log("Process.Orange Thread " + securityId + " has started");
        boolean sentinel = true;
        String msg = "";

        Main.log("O.Thread " + securityId + " in GET");
        while (sentinel) {
            synchronized (in) {
                while (in.isEmpty()) try {
                    Thread.yield();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                msg = in.get();
                in.notify();
            }

            Main.log("O.Thread " + securityId + " transforming");
            if (msg.contains("FIN")) sentinel = false;
            else msg = msg + "O.T" + securityId;

            try {
                Thread.sleep(timeMillis);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Main.log("O.Thread " + securityId + " in PUT");
            synchronized (out) {
                while (out.isFull()) try {
                    out.notify();
                    Thread.yield();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                out.put(msg);
                out.notify();
            }
        }
        Main.log("O.Thread " + securityId + " in END");//Signal end
    }
}
