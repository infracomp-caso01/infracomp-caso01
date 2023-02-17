public class Producto {
    private static String id;
    private static Buffer buffer;
    
    public Producto (String id, Buffer buffer){
        this.id = id;
        this.buffer = buffer;
    }

    public String getId(){
        return id;
    }

    public Buffer getBuffer(){
        return buffer;
    }
    
}
