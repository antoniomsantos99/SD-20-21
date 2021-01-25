import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.locks.ReentrantLock;


public class Utilizador {

    /* Variaveis */
    private String username;
    private String password;
    private Boolean estadoInfecao;
    private Coordinates posicao;
    private ReentrantLock loginLock;
    private HashSet<String> utilizadoresEmContacto;
    private Integer infecao;

    /**
     * Contrutor sem parametros
     */
    public Utilizador() {
        this.username = null;
        this.password = null;
        this.posicao = null;
        this.estadoInfecao = false;
        this.loginLock = new ReentrantLock();
        this.infecao = 0;
        this.utilizadoresEmContacto = new HashSet<>();
    }


    /**
     * Contrutor por parametros
     * @param username Nome utilizador
     * @param password Password utilizador
     */
    public Utilizador(String username, String password) {
        this.username = username;
        this.password = password;
        this.posicao = null;
        this.estadoInfecao = false;
        this.loginLock = new ReentrantLock();
        this.infecao = 0;
        this.utilizadoresEmContacto = new HashSet<>();
    }
    /**
     * Saca username do utilizador
     * @return Username de utilizador
     */
    public String getUsername() {
        return username;
    }

    /**
     * Saca a password do utilizador
     * @return Password de utilizador
     */
    public String getPassword() {
        return password;
    }

    /**
     * Saca o estado de infeção do utilizador
     * @return True se o utilizador estiver infetado False caso contrário
     */
    public Boolean getEstadoInfecao() { return estadoInfecao; }

    /**
     * Modifica o estado de infeção do utilizador
     * @return True se o utilizador estiver infetado False caso contrário
     */
    public void setEstadoInfecao(Boolean estadoInfecao) {
        this.estadoInfecao = estadoInfecao;
    }

    public void unlockUser() {
        this.loginLock.unlock();
    }

    public void lockUser() {
        this.loginLock.lock();
    }

    public boolean checkLock() {
        return this.loginLock.isLocked();
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(this.username);
        out.writeUTF(this.password);
        out.flush();
    }

    public static Utilizador deserialize(DataInputStream in) throws IOException {
        String username = in.readUTF();
        String password = in.readUTF();
        return new Utilizador(username, password);
    }

    public Coordinates getPosicao() {
        return posicao;
    }

    public void setPosicao(Coordinates posicao) {
        this.posicao = posicao;
    }
    public void setPosicao(int x,int y) {
        this.posicao.setCoords(x,y);
    }

    public void incWarn(){
        this.infecao++;
    }

    public HashSet<String> getUtilizadoresEmContacto() {
        return utilizadoresEmContacto;
    }

    public boolean addToUtilizadoresEmContacto(String username){
        this.utilizadoresEmContacto.add(username);
        return true;
    }

    public Integer getInfecao() {
        return infecao;
    }

    public void resetInfecao() {
        this.infecao = 0;
    }
}

