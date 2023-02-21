import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

interface Process extends Runnable {}

@SuppressWarnings({"rawtypes", "unchecked"}) public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            /*Input1: Buffer size, specified by console*/
            Main.console("Tamanio del buffer");
            final int MIDDLE_BUFFER_CAP = Integer.parseInt(sc.nextLine());
            final int FINAL_BUFFER_CAP = Integer.MAX_VALUE;
            /*Input2: How many processes does the Main create by stage*/
            Main.console("Cuantos procesos por etapa?");
            final int PROCESSES_STAGE = Integer.parseInt(sc.nextLine());
            final int STAGE_NUMBER = 3;
            /*Input3. How many processes does the first stage produces, specified by console*/
            Main.console("Cantidad de mensajes producidas");
            final int PRODUCT_QUANTITY = Integer.parseInt(sc.nextLine());

            Process[] processes = new Process[STAGE_NUMBER * PROCESSES_STAGE + 1];
            Buffer[] buffers = IntStream.range(0, STAGE_NUMBER).mapToObj(i -> new Buffer(MIDDLE_BUFFER_CAP)).toArray(Buffer[]::new);
            buffers[STAGE_NUMBER - 1] = new Buffer(FINAL_BUFFER_CAP);

            Blue.setMsgLimit(PRODUCT_QUANTITY);
            Orange.setMsgLimit(PRODUCT_QUANTITY);
            Red.setMsgLimit(PRODUCT_QUANTITY);
            Red.setProcess(PROCESSES_STAGE);
            for (int stage = 0; stage < STAGE_NUMBER; stage++) {
                processes[stage * PROCESSES_STAGE] = new Orange(SecureId.nextTID(), stage,
                        stage == 0 ? null : buffers[stage - 1], buffers[stage]);
                for (int process = 1; process < PROCESSES_STAGE; process++)
                    processes[stage * PROCESSES_STAGE + process] = new Blue(SecureId.nextTID(), stage,
                            stage == 0 ? null : buffers[stage - 1], buffers[stage]);
            }
            processes[STAGE_NUMBER * PROCESSES_STAGE] = new Red(buffers[STAGE_NUMBER - 1]);
            Arrays.stream(processes).forEach(Main::start);
        }
    }

    public static void console(Object o) {System.out.println(o);}

    private static void start(Runnable p) {new Thread(p).start();}
}

class SecureId {
    private static int threadSed = 0;
    private static int productSeq = 0;

    static synchronized int nextTID() {return threadSed++;}

    static synchronized int nextPID() {return productSeq++;}
}