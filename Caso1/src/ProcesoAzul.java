//Pasiva

public class ProcesoAzul extends Proceso{

    private Asignador asignador;
    private BufferL bufferPost;
    private int cantidadProd;

    @Override
    public void run() {
        for (int i = 0; i < cantidadProd; i++) {
            int id = asignador.asignarId();
            String mensaje =  Integer.toString(id) + "E1Azul";
            intentarEntrar(mensaje);
        }


    }

    public void intentarEntrar(String mensaje){
        if (bufferPost.estaLleno()) {
            try {
                bufferPost.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        bufferPost.agregar(mensaje);
        bufferPost.notifyAll();
    }

    public ProcesoAzul(Asignador asignador, BufferL bufferPost, int cantidadProd) {
        this.asignador = asignador;
        this.bufferPost = bufferPost;
        this.cantidadProd = cantidadProd;
        
    }

    public void crearProceso(){
        for (int i = 0; i < cantidadProd; i++) {
            String id = asignador.asignarId();
            
        }
    }


}
