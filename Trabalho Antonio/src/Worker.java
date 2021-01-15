import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Worker extends Thread implements Runnable {

    private Socket clSocket;
    private userManager master;
    private Utilizador utilizador;
    private BufferedReader in;
    private BufferedWriter out;


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
            this.in = new BufferedReader(new InputStreamReader(clSocket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(clSocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Escreve uma mensagem no socket
     *
     * @param msg Mensagem
     */
    public void writeSocket(String msg) {
        try {
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        String res, pass, user = null;
        try {
            while ((res = in.readLine()) != null) {
                switch(res){

                    case("registo"):
                        user = in.readLine();
                        pass = in.readLine();
                        if(this.master.registarUtilizador(user,pass)){
                            this.master.loginUtilizador(user,pass);
                            this.utilizador = this.master.getUser(user);
                            writeSocket("Registo com sucesso!");
                        }
                        else {
                            writeSocket("Registo sem sucesso");
                        }

                    case("login"):
                        user = in.readLine();
                        pass = in.readLine();

                        if(this.master.loginUtilizador(user,pass))
                            writeSocket("Login com sucesso");
                        else{
                            if (this.master.getUser(user) == null)
                                writeSocket("User não existe.");
                            else if (this.master.getUser(user).checkLock())
                                writeSocket("User já em utilização.");
                            else
                                writeSocket("Password incorreta.");
                        }


                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



