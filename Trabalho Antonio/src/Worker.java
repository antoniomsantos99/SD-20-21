import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Worker extends Thread implements Runnable {

    private Socket clSocket;
    private userManager master;
    private Utilizador utilizador;
    private DataInputStream in;
    private DataOutputStream out;


    /**
     * Construtor por parametros
     *
     * @param clsocket Socket
     * @param master   Master
     */
    public Worker(Socket clsocket, userManager master) {
        try {
            this.clSocket = clsocket;
            this.master = master;
            this.in = new DataInputStream(new BufferedInputStream(clsocket.getInputStream()));
            this.out = new DataOutputStream(new BufferedOutputStream(clsocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void run() {
        String res, pass, user = null;
        try {
            while ((res = in.readUTF()) != null) {
                switch(res){

                    case("registo"):
                        System.out.println("Teste");
                        if(this.master.registarUtilizador(new Utilizador().deserialize(this.in))){
                            this.utilizador = this.master.getUser(user);
                            out.writeUTF("Registo com sucesso!");
                            out.flush();
                        }
                        else {
                            out.writeUTF("Registo sem sucesso");
                            out.flush();
                        }
                        break;

                    case("login"):
                        user = in.readUTF();
                        pass = in.readUTF();

                        if(this.master.loginUtilizador(user,pass)) {
                            this.utilizador = this.master.getUser(user);
                            out.writeUTF("Login com sucesso!");
                            out.flush();
                        }
                        else{
                            if (this.master.getUser(user) == null) {
                                out.writeUTF("User não existe.");
                                out.flush();
                            }
                            else if (this.master.getUser(user).checkLock()){
                                out.writeUTF("User já em utilização.");
                                out.flush();}
                            else{
                                out.writeUTF("Password incorreta.");
                                out.flush();}
                        }
                        break;

                    case("logout"):
                        utilizador.unlockUser();
                        utilizador = null;
                        break;

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



