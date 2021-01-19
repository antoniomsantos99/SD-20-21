import java.io.*;
import java.net.UnknownHostException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Cliente {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private String username;
    private Menu m;


    /**
     * Construtor por parametros
     *
     * @param hostname Nome do host
     * @param porta    Nmumero do porto
     */
    public Cliente(String hostname, int porta) {
        try {
            this.username = null;
            this.socket = new Socket(hostname, porta);
            this.input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            this.output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            this.m = new Menu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean signUser() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Username: ");
        String user = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();
        new Utilizador(user,pass).serialize(this.output);
        String status = this.input.readUTF();
        System.out.println(status);
        return status.equals("Login com sucesso!") || status.equals("Registo com sucesso!");
    }

    public void askPos() throws IOException {
        output.writeInt(m.run("Coordenada X: "));
        output.writeInt(m.run("Coordenada Y: "));
        output.flush();
        System.out.println(this.input.readUTF());
    }


    public static void main(String[] args) throws IOException {
        boolean loop = true;
        Cliente c = new Cliente("127.0.0.1", 12345);



        while (loop) {
            switch (c.m.run(new String[]{"Registar User", "Login User", "Logout User","Update position","Check position"})) {
                case 1:
                    c.output.writeUTF("registo");
                    c.output.flush();
                    c.signUser();
                    break;
                case 2:
                    c.output.writeUTF("login");
                    c.output.flush();
                    if(c.signUser()) {
                        c.output.writeUTF("updatePos");
                        c.output.flush();
                        c.askPos();
                    }
                    break;
                case 3:
                    c.output.writeUTF("logout");
                    loop = false;
                    c.output.flush();
                    break;
                case 4:
                    c.output.writeUTF("updatePos");
                    c.output.flush();
                    c.askPos();
                    break;
                case 5:
                    c.output.writeUTF("checkPos");
                    c.output.flush();
                    c.askPos();
                    break;
            }


        }
    }
}