import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class AdminWorker extends Thread implements Runnable {

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
    public AdminWorker(Socket clsocket, userManager master) {
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
                                out.writeUTF("Registo com sucesso!\n");
                                out.flush();
                            }
                            else {
                                out.writeUTF("Registo sem sucesso\n");
                                out.flush();
                            }
                            break;

                        case("login"):
                            user = in.readUTF();
                            pass = in.readUTF();

                            if(this.master.loginUtilizador(user,pass)) {
                                if(this.utilizador != null) this.utilizador.unlockUser();
                                this.utilizador = this.master.getUser(user);
                                out.writeUTF("Login com sucesso!\n");
                                out.flush();
                            }
                            else{
                                if (this.master.getUser(user) == null) {
                                    out.writeUTF("Utilizador não existe.\n");
                                    out.flush();
                                }
                                else if (this.master.getUser(user).checkLock()){
                                    out.writeUTF("Utilizador já deu login.\n");
                                    out.flush();}
                                else{
                                    out.writeUTF("Password incorreta.\n");
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
                                out.writeUTF("Posição atualizada!\n");
                                out.flush();
                            }
                            else{
                                out.writeUTF("Posição inválida!\n");
                                out.flush();
                            }

                            break;

                        case("checkPos"):
                            x = in.readInt();
                            y = in.readInt();
                            if(0<=x && x<master.getDIM() && 0<=y && y<master.getDIM()){
                                out.writeUTF(String.format("De momento estão %d pessoas na posição (%d,%d)\n",master.checkPosition(x,y),x,y));
                                out.flush();
                            }
                            else{
                                out.writeUTF("Posição inválida!\n");
                                out.flush();
                            }

                            break;

                        case("notify"):
                            x = in.readInt();
                            y = in.readInt();
                            this.master.waitUntilEmpty(x,y);
                            out.writeUTF(String.format("Posição (%d, %d) vazia!\n", x, y));
                            out.flush();
                            break;

                        case("download"):
                            int[][][] mapa = this.master.downloadMap();
                            for(int i = 0;i<master.getDIM();i++)
                                for(int j = 0;j< master.getDIM();j++)
                                    out.writeUTF(String.format("Na posição (%d,%d) temos %d utilizadores e %d infetados\n",i,j,mapa[i][j][0],mapa[i][j][1]));
                            out.writeUTF("endDownload");
                            out.flush();
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


class AdminListener implements Runnable {
    private ServerSocket adSocket;
    private userManager master;

    public AdminListener(ServerSocket adSocket, userManager master) {
        this.adSocket = adSocket;
        this.master = master;
    }

    public void run () {
        try {
            while (true) {
                Socket socket = adSocket.accept();
                System.out.println("Admin conetado!\n");

                Thread worker = new Thread(new Worker(socket, master));
                worker.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}