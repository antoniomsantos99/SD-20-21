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
        Admin c = new Admin("127.0.0.1", 23456);
        System.out.println(c.output);
        while (loop) {
            if(!c.loggedIn) {
                switch (c.m.run(new String[]{"Registar utilizador", "Login"})) {
                    case 1:
                        c.output.writeUTF("registo");
                        c.output.flush();
                        c.signUser();
                        break;
                    case 2:
                        c.output.writeUTF("login");
                        c.output.flush();
                        if (c.signUser()) {
                            c.output.writeUTF("updatePos");
                            c.output.flush();
                            c.askPos();
                        }
                        break;
                }
            }

            else {
                c.checkWarnings();
                switch (c.m.run(new String[]{"Logout", "Atualizar posição", "Verificar o nº de utilizadores numa posição", "Notificar infeção ao servidor", "Receber notificação quando uma localização estiver vazia", "Download do mapa das visitas"})) {
                    case 1:
                        c.output.writeUTF("logout");
                        loop = false;
                        c.output.flush();
                        break;
                    case 2:
                        c.output.writeUTF("updatePos");
                        c.output.flush();
                        c.askPos();
                        break;
                    case 3:
                        c.output.writeUTF("checkPos");
                        c.output.flush();
                        c.askPos();
                        break;
                    case 4:
                        c.output.writeUTF("infected");
                        c.output.flush();
                        System.out.println("Obrigado pela cooperação!");
                        loop = false;
                        break;
                    case 5:
                        c.output.writeUTF("notify");
                        c.output.flush();
                        c.askPos();
                        break;
                    case 6:
                        c.output.writeUTF("download");
                        c.output.writeInt(c.m.run(new String[]{"Vista Detalhada","Vista Compacta (não recomendado para mapas de grandes dimensões)"}));
                        c.output.flush();
                        c.getMap();
                        break;
                }

            }


        }
    }
}