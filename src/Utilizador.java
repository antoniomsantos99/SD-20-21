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
     */
    public void setEstadoInfecao(Boolean estadoInfecao) {
        this.estadoInfecao = estadoInfecao;
    }

    /**
     * Desbloqueia o utilizador
     */
    public void unlockUser() {
        this.loginLock.unlock();
    }


    /**
     * Bloqueia o utilizador
     */
    public void lockUser() {
        this.loginLock.lock();
    }

    /**
     * Retorna o estado do lock
     * @return True se o lock está bloqueado False caso contrário
     */
    public boolean checkLock() {
        return this.loginLock.isLocked();
    }

    /**
     *Envia os dados de autenticação para o socket
     */
    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(this.username);
        out.writeUTF(this.password);
        out.flush();
    }

    /**
     * De acordo com os dados fornecidos pelo serivdor cria um utilizador
     * @return Utilizador criado
     */
    public static Utilizador deserialize(DataInputStream in) throws IOException {
        String username = in.readUTF();
        String password = in.readUTF();
        return new Utilizador(username, password);
    }

    /**
     * Devolve a posição de um Utilizador
     * @return Posição do utilizador
     */
    public Coordinates getPosicao() {
        return posicao;
    }

    /**
     * Modifica a posição de um Utilizador
     * @param posicao Objeto coordenadas com a posição
     */
    public void setPosicao(Coordinates posicao) {
        this.posicao = posicao;
    }

    /**
     * Modifica a posição de um Utilizador
     * @param x Coordenada X
     * @param y Coordenada Y
     */
    public void setPosicao(int x,int y) {
        if(this.posicao != null) this.posicao.setCoords(x,y);
        else this.setPosicao(new Coordinates(x,y));
    }

    /**
     * Incrementa a variável de aviso de infeção
     */
    public void incWarn(){
        this.infecao++;
    }

    /**
     * Devolve a lista de utilizadores cujo um utilizador teve em contacto
     * @return Lista de utilizadores
     */
    public HashSet<String> getUtilizadoresEmContacto() {
        return utilizadoresEmContacto;
    }

    /**
     * Adiciona um username a uma  lista de utilizadores cujo um utilizador teve em contacto
     * @oaram Username do utilizador pretendido
     */
    public void addToUtilizadoresEmContacto(String username){
        this.utilizadoresEmContacto.add(username);
    }

    /**
     * Adiciona um username a uma  lista de utilizadores cujo um utilizador teve em contacto
     * @return Numero de pessoas infetadas que o utilizador esteve em contacto
     */
    public Integer getInfecao() {
        return infecao;
    }

    /**
     * Reseta o contador de utilizadores infetados cujo um utilizador esteve em contacto
     */
    public void resetInfecao() {
        this.infecao = 0;
    }
}

