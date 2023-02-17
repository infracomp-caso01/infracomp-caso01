public class Asignador {

    static int nprod = 0;

    public Asignador (){

    }

    public synchronized int asignarId() {
        nprod++;
        return nprod;
    }
    
}
