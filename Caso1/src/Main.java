import java.util.Scanner;
public class Main {

    Asignador asignador = new Asignador();
    //E la tamaño del buffer se ingresa por parametro
    BufferL buffer1 = new BufferL(10);
    BufferL buffer2 = new BufferL(10);
    BufferI buffer3 = new BufferI();


    public static void main(int nprod) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Ingrese el numero de procesos a crear: ");
        int nproductos = sc.nextInt();

        System.out.println("Ingrese el tamaño del buffer: ");
        int tamBuffer = sc.nextInt();
        
        System.out.println("Ingrese el número de productos a crear por tipo de proceso: ");
        int nprocesos = sc.nextInt();

        Asignador asignador = new Asignador();
        BufferL buffer1 = new BufferL(tamBuffer);
        BufferL buffer2 = new BufferL(tamBuffer);
        BufferI buffer3 = new BufferI();

        new ProcesoNaranja(asignador, buffer1, nproductos).start();

        for (int i = 0; i < nprocesos-1; i++) {
            new ProcesoAzul(asignador, buffer1, nproductos).start();
        }

        

        
    }
}