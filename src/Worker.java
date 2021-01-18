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
        Coordinates pos = new Coordinates(0,0);

        boolean loop = true;
        try {
            while (loop && (res = in.readUTF()) != null) {
                switch(res){

                    case("registo"):
                        if(this.master.registarUtilizador(new Utilizador().deserialize(this.in))){
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
                            if(this.utilizador != null) this.utilizador.unlockUser();
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

                    case("updatePos"):
                        int x = in.readInt();
                        int y = in.readInt();
                        if(0<=x && x<master.getDIM() && 0<=y && y<master.getDIM()){
                            master.updatePosition(utilizador,x,y);
                            out.writeUTF("Posição atualizada!");
                            out.flush();
                        }
                        else{
                            out.writeUTF("Posição Invalida!");
                            out.flush();
                        }

                        break;


                    case("logout"):
                        if(utilizador != null) {
                            this.master.getUser(utilizador.getUsername()).unlockUser();
                            utilizador = null;
                        }
                        clSocket.close();
                        loop = false;
                        break;

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



