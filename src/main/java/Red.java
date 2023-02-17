@SuppressWarnings("StringConcatenationInLoop") class Red implements Process {
    private final long securityId;
    private final Buffer<String> in;
    private String finalMessage;

    Red(Buffer<String> b) {
        this.securityId = SecureUtility.nextID();
        this.in = b;
        this.finalMessage = "";
    }

    @Override public void run() {
        String msg;
        boolean sentinel = false;
        int counter = 0;
        Main.log("Process.Red Thread " + securityId + " has started");
        do {
            synchronized (in) {
                while (!in.isEmpty()) {
                    sentinel = true;
                    msg = in.get();
                    in.notify();
                    if (msg.equals("FIN")) counter++;
                    finalMessage += "<" + msg + "> \n";
                }
            }
            if (!sentinel) Thread.yield();
            sentinel = false;
        } while (counter < 2);
        Main.log("-===========-\n" + finalMessage + "\n-===========-");
    }
}
