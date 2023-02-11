import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
public class Proceso implements Runnable {
    private int id;
    private void esperar() throws InterruptedException {
        int randomNum = ThreadLocalRandom.current().nextInt(50, 500 + 1);
        this.wait(randomNum);
    }
    private String modificarMensaje(String mensaje) {
        return mensaje;
    }
    public void run() {

    }

}
