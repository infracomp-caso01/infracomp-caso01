import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.IntStream;

public class Main {

    private final Properties prop = new Properties();
    private final int MESSAGE_NUM;
    private final int PROCESS_NUM;
    private final int BUFFER_CAP;
    private final int BUFFER_NUM;
    private final int STAGE_NUM;

    private Main() {
        try (InputStream input = Main.class.getResourceAsStream("args.properties")) {
            prop.load(input);
            MESSAGE_NUM = getProperty("ccu.message_number");
            PROCESS_NUM = getProperty("ccu.process_number");
            BUFFER_CAP = getProperty("ccu.buffer_cap");
            BUFFER_NUM = getProperty("ccu.buffer_quantity");
            STAGE_NUM = getProperty("ccu.stage_quantity");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Main m = new Main();
        m.exec();
    }

    private static void start(Process r) {new Thread(r).start();}

    public static void log(Object o) {
        System.out.println(o);
    }

    public static void high(Object o) {System.err.println(o);}

    private void exec() {

        Stack<String> stack = new Stack<>();
        List<Process> processList = new ArrayList<>();
        IntStream.range(0, MESSAGE_NUM).mapToObj(i -> "M" + i).forEach(stack::add);

        var buffers = new Buffer[BUFFER_NUM + 1];
        IntStream.range(0, BUFFER_NUM).forEach(i -> buffers[i] = new Buffer<>(BUFFER_CAP));
        buffers[3] = new Buffer<>(Integer.MAX_VALUE);

        processList.add(new Red(buffers[3])); //end
        processList.add(new DarkBox(stack, buffers[0])); //start

        long orangeID = SecureUtility.nextORANGE(PROCESS_NUM);
        for (int i = 0; i < STAGE_NUM; i++) {
            Process[] processes = new Process[PROCESS_NUM];
            for (int j = 0; j < PROCESS_NUM - 1; j++)
                //processes[j] = j == orangeID ? new Orange(buffers[i], buffers[i + 1]) : new Blue(buffers[i], buffers[i + 1]);
                processes[j] = new Orange(buffers[i], buffers[i + 1]);
            processList.addAll(Arrays.asList(processes));
            orangeID = SecureUtility.nextORANGE(PROCESS_NUM);
        }

        processList.forEach(Main::start);
    }

    private int getProperty(String property) {
        return Integer.parseInt(prop.getProperty(property));
    }

}

class SecureUtility {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static int sequence = 0;

    static int nextID() {return sequence++;}

    static int nextORANGE(final int bound) {return RANDOM.nextInt(0, bound);}

    static void print(final Object o) {System.out.println(o);}

    static long randomTime() {return new SecureRandom().nextInt(50, 501);}
}
