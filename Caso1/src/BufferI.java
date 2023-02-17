import java.util.ArrayList;

public class BufferI {

    private ArrayList buff;
    

    public BufferI() {
        buff = new ArrayList();
    }

    public synchronized void almacenar(String producto) {
        
        
        buff.add(producto);
        notify();
    }

    
}
