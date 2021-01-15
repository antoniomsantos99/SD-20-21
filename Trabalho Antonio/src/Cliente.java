import java.io.*;
import java.net.UnknownHostException;
import java.net.Socket;

public class Cliente {
    private Socket socket;
    private BufferedReader input;
    private BufferedWriter output;
    private BufferedReader systemInput;
    private String username;


    /**
     * Construtor por parametros
     * @param hostname Nome do host
     * @param porta Nmumero do porto
     */
    public Cliente(String hostname, int porta) {
        try {
            this.socket = new Socket(hostname, porta);
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.systemInput= new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Escreve uma mensagem no socket
     * @param msg String para enviar
     */
    public void writeSocket(String msg) {
        try {
            this.output.write(msg);
            this.output.newLine();
            this.output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void signUser() throws IOException {
        System.out.print("Username: ");
        writeSocket(this.systemInput.readLine());
        System.out.print("Password: ");
        writeSocket(this.systemInput.readLine());
        System.out.println(this.input.readLine());

    }


public static void main(String[] args) throws IOException {
        Cliente c = new Cliente("127.0.0.1", 12345);
        Menu m = new Menu();

    switch(m.run(new String[]{"Registar User", "Login User"})){
        case 1:
            c.writeSocket("registo");
            c.signUser();
        case 2:
            c.writeSocket("login");
            c.signUser();
    }


        }
}