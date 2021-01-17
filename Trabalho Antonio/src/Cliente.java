import java.io.*;
import java.net.UnknownHostException;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private String username;


    /**
     * Construtor por parametros
     *
     * @param hostname Nome do host
     * @param porta    Nmumero do porto
     */
    public Cliente(String hostname, int porta) {
        try {
            this.socket = new Socket(hostname, porta);
            this.input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            this.output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void signUser() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Username: ");
        String user = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();
        new Utilizador(user,pass).serialize(this.output);
        System.out.println(input.readUTF());

    }


    public static void main(String[] args) throws IOException {
        boolean loop = true;
        Cliente c = new Cliente("127.0.0.1", 12345);
        Menu m = new Menu();

        while (loop) {
            switch (m.run(new String[]{"Registar User", "Login User", "Logout User"})) {
                case 1:
                    c.output.writeUTF("registo");
                    c.signUser();
                    break;
                case 2:
                    c.output.writeUTF("login");
                    c.signUser();
                    break;
                case 3:
                    c.output.writeUTF("logout");
                    loop = false;
                    break;
            }


        }
    }
}