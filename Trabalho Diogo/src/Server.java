import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


class ServerWorker implements Runnable {
    private Socket socket;

    public ServerWorker (Socket s) {
        this.socket = s;
    }

    @Override
    public void run() {
        try {
            DataInputStream in = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
            boolean isOpen = true;

            // autenticar

            while (isOpen) {
                // server trabalha
                User newUser = User.deserialize(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class Server {
    public static void main (String[] args) {
        try {
            ServerSocket ss = new ServerSocket(123);

            while (true) {
                Socket s = ss.accept();

                Thread serverWorker = new Thread(new ServerWorker(s));
                serverWorker.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
