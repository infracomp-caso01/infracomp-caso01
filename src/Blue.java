import java.util.Random;

/** 🔵 Thread */
class Blue implements Process {
    private static int msgLimit;
    private final int id, stage;
    private final long timeMillis = new Random().nextInt(451) + 50;
    private final Buffer<String> in, out;

    public Blue(final int id, final int stage, final Buffer<String> in, final Buffer<String> out) {
        this.id = id;
        this.stage = stage;
        this.in = in;
        this.out = out;
    }

    public static void setMsgLimit(int msgLimit) {Blue.msgLimit = msgLimit;}

    @Override public void run() {
        //Main.log("🔵.Thread " + id + stage + " START");
        if (stage == 0) runZero();
        else runNonZero();
    }

    private void runNonZero() {
        var context = new Object() {
            String msg;
            int counter = 0;
        };
        while (context.counter < msgLimit) {
            in.synchronizedBlue(() -> {
                while (in.isEmptyBlue()) try {
                    in.waitBlue();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Main.log("🔵.Thread " + id + stage + " in GET");
                context.msg = in.getBlue();
                in.notifyBlue();
            });

            //Main.log("🔵.Thread " + id + stage + " in TRANSFORM");
            context.msg += "╍🔵.Thread" + id + "-" + stage + "(" + context.counter + ")";
            try {
                //noinspection BusyWait
                Thread.sleep(timeMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            out.synchronizedBlue(() -> {
                while (out.isFullBlue()) try {
                    out.notifyBlue();
                    out.waitBlue();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Main.log("🔵.Thread " + id + stage + " in PUT");
                out.putBlue(context.msg);
                ++context.counter;
                out.notifyAllBlue();
            });
        }
        //Main.log("🔵.Thread " + id + stage + " END");
    }

    private void runZero() {
        var context = new Object() {
            int counter = 0;
        };
        while (context.counter < msgLimit) out.synchronizedBlue(() -> {
            while (out.isFullBlue()) {
                try {
                    out.waitBlue();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //Main.log("🔵.Thread " + id + stage + " in PRODUCE");
            out.putBlue("🔵.Thread[" + SecureId.nextPID() + "]ID" + id + "-Stage" + stage + "(" + context.counter + ")");
            ++context.counter;
            out.notifyBlue();
        });
        //Main.log("🔵.Thread " + id + stage + " END");
    }
}
