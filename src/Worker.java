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
        int x,y;

        boolean loop = true;
        try {
            while (loop)
                while((res = in.readUTF()) != null) {
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

                    case("check"):
                        if(user != null) {
                            x = this.master.getUser(user).getInfecao();
                            System.out.println(x);
                            out.writeInt(x);
                            out.flush();
                            if (x != 0)
                                this.master.getUser(user).resetInfecao();
                        }
                        break;

                    case("updatePos"):
                        x = in.readInt();
                        y = in.readInt();
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

                    case("checkPos"):
                        x = in.readInt();
                        y = in.readInt();
                        if(0<=x && x<master.getDIM() && 0<=y && y<master.getDIM()){
                            out.writeUTF(String.format("De momento estão %d pessoas na posição (%d,%d)",master.checkPosition(x,y),x,y));
                            out.flush();
                        }
                        else{
                            out.writeUTF("Posição Invalida!");
                            out.flush();
                        }

                        break;

                    case("infected"):
                        if (utilizador != null) {
                            this.master.getUser(utilizador.getUsername()).setEstadoInfecao(true);
                            this.master.warnUsers(utilizador.getUsername());
                        }


                    case("logout"):
                        if(utilizador != null) {
                            this.master.userLogout(utilizador);
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



