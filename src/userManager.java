import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class userManager {

    private HashMap<String,Utilizador> utilizadores; // Key: username | Value: Objeto do Utilizador
    private Integer DIM; // Dimensão do mapa
    private ArrayList<Utilizador>[][] mapa; //Localizacao dos users (talvez mudar)
    private ReentrantLock userLock; //Lock do hashmap dos utilizadores

    public userManager() {
        this.utilizadores = new HashMap<String, Utilizador>();
        this.DIM = 10;
        this.mapa = new ArrayList[DIM][DIM];
        this.userLock = new ReentrantLock();

    }

    /**
     * Regista um utilizador
     * @param user Objeto do utilizador
     * @return Estado de conclusão
     */
    public boolean registarUtilizador(Utilizador user){
        this.userLock.lock();
        try {
            if (!this.utilizadores.containsKey(user.getUsername())) {
                this.utilizadores.put(user.getUsername(), user);
                return true;
            }
            return false;
        }
        finally {
            this.userLock.unlock();
        }
    }

    /**
     * Login de um utilizador
     * @param username Nome utilizador
     * @param password Password utilizador
     * @return True se login com sucesso, false caso contrario
     */
    public boolean loginUtilizador(String username, String password) {
        this.userLock.lock();
        try {
            if (this.utilizadores.containsKey(username)
                    && this.utilizadores.get(username).getPassword().equals(password)
                    && !this.utilizadores.get(username).checkLock()) {

                this.utilizadores.get(username).lockUser();

                return true;
            }
            return false;
        } finally {
            this.userLock.unlock();
        }
    }

    /**
     * Buscar a infromação de um utilizador
     * @param nome Nome do utilizador
     * @return Informação do utilizador
     */
    public Utilizador getUser(String nome) {
        return utilizadores.get(nome);
    }

    public Integer getDIM() {
        return DIM;
    }

    public boolean updatePosition(Utilizador user,int x,int y){
        if(mapa[x][y] == null) mapa[x][y] = new ArrayList<Utilizador>();
        if(user.getPosicao() != null){
            this.mapa[user.getPosicao().getX()][user.getPosicao().getY()].remove(user);
        }
        user.setPosicao(new Coordinates(x,y));
        this.mapa[x][y].add(user);
        return true;
    }

    public int checkPosition(int x, int y){
        if(mapa[x][y] == null) return 0;
        return mapa[x][y].size();
    }
}

