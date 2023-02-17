class Blue implements Process {
    private final long securityId, timeMillis;
    private final Buffer<String> in, out;

    public Blue(Buffer<String> in, Buffer<String> out) {
        this.securityId = SecureUtility.nextID();
        this.timeMillis = SecureUtility.randomTime();
        this.in = in;
        this.out = out;
    }

    @Override public void run() {
        Main.log("Process.Blue Thread " + securityId + " has started");
        boolean sentinel = true;
        String msg = "";

        Main.log("B.Thread " + securityId + " in GET");
        while (sentinel) {
            synchronized (in) {
                while (in.isEmpty()) {
                    try {
                        in.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                msg = in.get();
                in.notify();
            }

            Main.log("B.Thread " + securityId + " transforming");
            if (msg.contains("FIN")) sentinel = false;
            else msg = msg + ";B.T" + securityId;

            try {
                Thread.sleep(timeMillis);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Main.log("B.Thread " + securityId + " in PUT");
            synchronized (out) {
                while (out.isFull()) {
                    try {
                        out.notify();
                        out.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                out.put(msg);
                out.notify();
            }
        }
        Main.log("B.Thread " + securityId + " in END");//Signal end
    }
}
