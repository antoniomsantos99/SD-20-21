import java.util.concurrent.locks.ReentrantLock;

public class Utilizador {

    /* Variaveis */
    private String username;
    private String password;
    private Boolean estadoInfecao;
    private ReentrantLock lockUser;

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
}
