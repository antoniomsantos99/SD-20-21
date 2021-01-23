import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


/**
 * Classe com o servidor.
 */
public class Servidor{

    /**
     * Variaveis
     */
    private userManager master;

    public Servidor() {
        this.master= new userManager();
    }

    /**
     * Iniciar o servidor
     */
    public void startup(){
        try{
            ServerSocket clientSocket = new ServerSocket(12345);
            ServerSocket adminSocket = new ServerSocket(23456);
            System.out.println("Server up! À espera de utilizadores...\n");

            while(true){
                Thread listener = new Thread(new Listener(clientSocket, master));
                Thread adminListener = new Thread(new AdminListener(adminSocket, master));

                listener.start();
                adminListener.start();

                try {
                    listener.join();
                    adminListener.join();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }


    public static void main(String[] args){
        Servidor s = new Servidor();
        s.startup();
    }
}