import java.util.ArrayList;

public class BufferL {

    private int tamanio;
    private int ocupado = 0;
    private ArrayList buff;

    public BufferL(int tamanio) {
        this.tamanio = tamanio;
    }

    public synchronized void almacenar(String producto) {
        while (ocupado == tamanio) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        ocupado++;
        buff.add(producto);
        notify();
    }

    
}
