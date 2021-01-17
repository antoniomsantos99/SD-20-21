import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class Utilizador {

    /* Variaveis */
    private String username;
    private String password;
    private Boolean estadoInfecao;
    private ReentrantLock lockUser;

    /**
     * Contrutor sem parametros
     * @param username Nome utilizador
     * @param password Password utilizador
     */
    public Utilizador() {
        this.username = null;
        this.password = null;
        this.estadoInfecao = false;
        this.lockUser = new ReentrantLock();
    }


    /**
     * Contrutor por parametros
     * @param username Nome utilizador
     * @param password Password utilizador
     */
    public Utilizador(String username, String password) {
        this.username = username;
        this.password = password;
        this.estadoInfecao = false;
        this.lockUser = new ReentrantLock();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEstadoInfecao() {
        return estadoInfecao;
    }

    public void setEstadoInfecao(Boolean estadoInfecao) {
        this.estadoInfecao = estadoInfecao;
    }

    public void unlockUser() {
        this.lockUser.unlock();
    }

    public void lockUser() {
        this.lockUser.lock();
    }

    public boolean checkLock() {
        return this.lockUser.isLocked();
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(this.username);
        out.writeUTF(this.password);

        out.flush();
    }

    public static Utilizador deserialize(DataInputStream in) throws IOException {
        String username = in.readUTF();
        String password = in.readUTF();
        System.out.println(username);
        return new Utilizador(username, password);
    }
}

