import java.io.*;
import java.net.UnknownHostException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Cliente {
    protected Socket socket;
    protected DataInputStream input;
    protected DataOutputStream output;
    protected String username;
    protected Menu m;
    protected boolean loggedIn;


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
            this.loggedIn = false;
        } catch (IOException e) {
            System.out.println("Não foi possivel a conexão ao servidor. Por favor tente mais tarde!");
            System.exit(0);
        }
    }

    /**
     * Pede dados de autênticação e envia-os para o servidor
     *
     * @return True se autenticou com sucesso, false caso contrário
     */
    public boolean signUser() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Username: ");
        String user = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();
        new Utilizador(user,pass).serialize(this.output);
        String status = this.input.readUTF();
        System.out.println(status);
        if(status.equals("Login com sucesso!\n")) loggedIn = true;
        return status.equals("Login com sucesso!\n") || status.equals("Registo com sucesso!\n");
    }

    /**
     * Pede posição e envia para o servidor
     *
     */
    public void askPos() throws IOException {
        output.writeInt(m.run("Coordenada X: "));
        output.writeInt(m.run("Coordenada Y: "));
        output.flush();
        System.out.println(this.input.readUTF());
    }


    /**
     * Pede numero de pessoas infetadas que contactaram com um utilizador
     *
     */
    public void checkWarnings() throws IOException {
            output.writeUTF("check");
            output.flush();
            int num = input.readInt();
            if (num != 0)
                System.out.println(String.format("%d pessoas que estiveram na sua próximidade encontram-se infetadas. Por favor isole-se.\n", num));
    }

    /**
     * Lê mapa pedido e escreve no terminal
     *
     */
    public void getMap() throws IOException {
        String read;
        while(!(read =input.readUTF()).equals("endDownload"))
            System.out.println(read);
        System.out.println("\n");
    }

    public static void main(String[] args) throws IOException {
        boolean loop = true;
        Cliente cliente = new Cliente("127.0.0.1", 12345);

        while (loop) {
            if(!cliente.loggedIn) {
                switch (cliente.m.run(new String[]{"Registar utilizador", "Login"})) {
                    case 1:
                        cliente.output.writeUTF("registo");
                        cliente.output.flush();
                        cliente.signUser();
                        break;
                    case 2:
                        cliente.output.writeUTF("login");
                        cliente.output.flush();
                        if (cliente.signUser()) {
                            cliente.output.writeUTF("updatePos");
                            cliente.output.flush();
                            cliente.askPos();
                        }
                        break;
                }
            }

            else {
                cliente.checkWarnings();
                switch (cliente.m.run(new String[]{"Logout", "Atualizar posição", "Verificar o nº de utilizadores numa posição", "Notificar infeção ao servidor", "Receber notificação quando uma localização estiver vazia"})) {
                    case 1:
                        cliente.output.writeUTF("logout");
                        loop = false;
                        cliente.output.flush();
                        break;
                    case 2:
                        cliente.output.writeUTF("updatePos");
                        cliente.output.flush();
                        cliente.askPos();
                        break;
                    case 3:
                        cliente.output.writeUTF("checkPos");
                        cliente.output.flush();
                        cliente.askPos();
                        break;
                    case 4:
                        cliente.output.writeUTF("infected");
                        cliente.output.flush();
                        System.out.println("Obrigado pela cooperação!");
                        loop = false;
                        break;
                    case 5:
                        cliente.output.writeUTF("notify");
                        cliente.output.flush();
                        cliente.askPos();
                        break;
                }

                }


            }
        }
    }
