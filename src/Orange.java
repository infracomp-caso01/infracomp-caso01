import java.util.Random;

/** 🟠 Thread */
@SuppressWarnings("BusyWait") class Orange implements Process {
    private static int msgLimit;
    private final int id, stage;
    private final long timeMillis = new Random().nextInt(451) + 50;
    private final Buffer<String> in, out;

    Orange(final int id, final int stage, final Buffer<String> in, final Buffer<String> out) {
        this.id = id;
        this.stage = stage;
        this.in = in;
        this.out = out;
    }

    public static void setMsgLimit(int msgLimit) {Orange.msgLimit = msgLimit;}

    @Override public void run() {
        //Main.log("🟠.Thread " + id + stage + " START");
        if (stage == 0) runZero();
        else runNonZero();
    }

    private void runNonZero() {
        var context = new Object() {
            String msg;
            int counter = 0;
        };
        while (context.counter < msgLimit) {
            in.synchronizedOrange(() -> {
                while (in.isEmptyOrange()) Thread.yield();
            }, () -> {
                //Main.log("🟠.Thread " + id + stage + " in GET");
                context.msg = in.getOrange();
            });

            //Main.log("🟠.Thread " + id + stage + " in TRANSFORM");
            context.msg += "╍🟠.ThreadID" + id + "-Stage" + stage + "(" + context.counter + ")";
            try {
                //noinspection BusyWait
                Thread.sleep(timeMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            out.synchronizedOrange(() -> {
                while (out.isFullOrange()) Thread.yield();
            }, () -> {
                //Main.log("🟠.Thread " + id + stage + " in PUT");
                out.putOrange(context.msg);
                ++context.counter;
            });
        }
        //Main.log("🟠.Thread " + id + stage + " END");
    }

    private void runZero() {
        var context = new Object() {
            int counter = 0;
        };
        while (context.counter < msgLimit) {
            out.synchronizedOrange(() -> {
                while (out.isFullOrange()) Thread.yield();
            }, () -> {
                //Main.log("🟠.Thread " + id + stage + " in PRODUCE");
                out.putOrange(
                        "🟠.Thread[" + SecureId.nextPID() + "]ID" + id + "-Stage" + stage + "(" + context.counter + ")");
                ++context.counter;
            });
        }
        //Main.log("🟠.Thread " + id + stage + " END");
    }
}
