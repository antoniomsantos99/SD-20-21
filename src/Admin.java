import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Admin extends Cliente{

    /**
     * Construtor por parametros
     *
     * @param hostname Nome do host
     * @param porta    Numero do porto
     */
    public Admin(String hostname, int porta) {
        super(hostname, porta);
    }

    /**
     * Lê mapa pedido e escreve no terminal
     *
     */
    public void getMap() throws IOException {
        String read;
        while(!(read =this.input.readUTF()).equals("endDownload"))
            System.out.println(read);
        System.out.println("\n");
    }

    public static void main(String[] args) throws IOException {
        boolean loop = true;
        Admin admin = new Admin("127.0.0.1", 23456);
        System.out.println(admin.output);
        while (loop) {
            if(!admin.loggedIn) {
                switch (admin.m.run(new String[]{"Registar utilizador", "Login"})) {
                    case 1:
                        admin.output.writeUTF("registo");
                        admin.output.flush();
                        admin.signUser();
                        break;
                    case 2:
                        admin.output.writeUTF("login");
                        admin.output.flush();
                        if (admin.signUser()) {
                            admin.output.writeUTF("updatePos");
                            admin.output.flush();
                            admin.askPos();
                        }
                        break;
                }
            }

            else {
                admin.checkWarnings();
                switch (admin.m.run(new String[]{"Logout", "Atualizar posição", "Verificar o nº de utilizadores numa posição", "Notificar infeção ao servidor", "Receber notificação quando uma localização estiver vazia", "Download do mapa das visitas"})) {
                    case 1:
                        admin.output.writeUTF("logout");
                        admin.loggedIn = false;
                        admin.output.flush();
                        break;
                    case 2:
                        admin.output.writeUTF("updatePos");
                        admin.output.flush();
                        admin.askPos();
                        break;
                    case 3:
                        admin.output.writeUTF("checkPos");
                        admin.output.flush();
                        admin.askPos();
                        break;
                    case 4:
                        admin.output.writeUTF("infected");
                        admin.output.flush();
                        System.out.println("Obrigado pela cooperação!");
                        loop = false;
                        break;
                    case 5:
                        admin.output.writeUTF("notify");
                        admin.output.flush();
                        admin.askPos();
                        break;
                    case 6:
                        admin.output.writeUTF("download");
                        admin.output.writeInt(admin.m.run(new String[]{"Vista Detalhada","Vista Compacta (não recomendado para mapas de grandes dimensões)"}));
                        admin.output.flush();
                        admin.getMap();
                        break;
                }

            }


        }
    }
}