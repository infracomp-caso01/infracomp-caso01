/** 🔴 Thread */
class Red implements Process {
    private static int msgLimit, process;
    private final Buffer<String> in;

    Red(final Buffer<String> in) {
        this.in = in;
    }

    public static void setMsgLimit(int msgLimit) {Red.msgLimit = msgLimit;}

    public static void setProcess(int process) {Red.process = process;}

    @Override public void run() {
        //Main.log("🔴.Thread " + id + " START");
        String[] msg = new String[msgLimit * process];
        boolean sentinelO = true, sentinelB = true;
        int counterO = 0, counterB = 0;
        //Main.log("🔴.Thread " + id + " GET 🟠");
        while (sentinelO && process > 0) synchronized (in.getSyncOrange()) {
            while (!in.isEmptyOrange()) {
                String temp = "╠" + in.getOrange() + "\n";
                msg[between(temp)] = temp;
                sentinelO = ++counterO != msgLimit;
            }
        }
        while (sentinelB && process > 1) synchronized (in.getSyncBlue()) {
            while (!in.isEmptyBlue()) {
                String temp = "╠" + in.getBlue() + "\n";
                msg[between(temp)] = temp;
                in.notifyBlue();
                sentinelB = ++counterB != msgLimit * (process - 1);//
            }
        }
        //Main.log("🔴.Thread " + id + " GET 🔵");
        Main.console("🔴\n╔══════════╕\n" + String.join("", msg) + "╚══════════╛");
    }

    private int between(String s) {
        return Integer.parseInt(s.substring(s.indexOf("[") + 1, s.indexOf("]")));
    }
}
