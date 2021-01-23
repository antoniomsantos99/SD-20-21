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
            ServerSocket sSocket = new ServerSocket(12345);
            System.out.println("Server up! À espera de utilizadores...");
            while(true){
                Socket socket = sSocket.accept();
                System.out.println("Utilizador conetado!");
                Thread t=new Thread(new Worker(socket,master));
                t.start();
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
