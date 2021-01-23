import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class userManager {

    private HashMap<String, Utilizador> utilizadores; // Key: username | Value: Objeto do Utilizador
    private Integer DIM; // Dimensão do mapa
    private ArrayList<Utilizador>[][] mapa; //Localizacao dos users (talvez mudar)
    private ReentrantReadWriteLock mapLock; //Lock do hashmap dos utilizadores
    private Lock readLock, writeLock;

    public userManager() {
        this.utilizadores = new HashMap<String, Utilizador>();
        this.DIM = 10;
        this.mapa = new ArrayList[DIM][DIM];
        this.mapLock = new ReentrantReadWriteLock();
        this.readLock = mapLock.readLock();
        this.writeLock = mapLock.writeLock();

    }

    /**
     * Regista um utilizador
     * @param user Objeto do utilizador
     * @return Estado de conclusão
     */
    public boolean registarUtilizador(Utilizador user) {
        this.writeLock.lock();
        try {
            if (!this.utilizadores.containsKey(user.getUsername())) {
                this.utilizadores.put(user.getUsername(), user);
                return true;
            }
            return false;
        } finally {
            this.writeLock.unlock();
        }
    }

    /**
     * Login de um utilizador
     *
     * @param username Nome utilizador
     * @param password Password utilizador
     * @return True se login com sucesso, false caso contrario
     */
    public boolean loginUtilizador(String username, String password) {
        this.writeLock.lock();
        try {
            if (this.utilizadores.containsKey(username)
                    && this.utilizadores.get(username).getPassword().equals(password)
                    && !this.utilizadores.get(username).checkLock()) {

                this.utilizadores.get(username).lockUser();

                return true;
            }
            return false;
        } finally {
            this.writeLock.unlock();
        }
    }

    /**
     * Saca a informação de um utilizador
     * @param nome Nome do utilizador
     * @return Informação do utilizador
     */
    public Utilizador getUser(String nome) {
        this.readLock.lock();
        try {
            return utilizadores.get(nome);
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     * Saca a dimensão do mapa
     * @return Dimensão do mapa
     */
    public Integer getDIM() {
        this.readLock.lock();
        try {
            return DIM;
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     * Atualiza a posição do utilizador no mapa
     * @param user Objeto utilizador
     * @param x Coordenada X de onde se quer mover
     * @param y Coordenada Y de onde se quer mover
     * @return Estado de conclusão
     */
    public boolean updatePosition(Utilizador user, int x, int y) {
        this.writeLock.lock();
        try {
            if (mapa[x][y] == null) mapa[x][y] = new ArrayList<Utilizador>();
            if (user.getPosicao() != null) {
                this.mapa[user.getPosicao().getX()][user.getPosicao().getY()].remove(user);
            }
            user.setPosicao(new Coordinates(x, y));
            updateContacts(user.getUsername(), x, y);
            this.mapa[x][y].add(user);
            return true;
        } finally {
            this.writeLock.unlock();
        }
    }
    /**
     * Conta os utilizadores numa posição do mapa
     * @param x Coordenada X de onde se quer contar
     * @param y Coordenada Y de onde se quer contar
     * @return Numero de utilizadores na posição referida
     */
    public int checkPosition(int x, int y) {
        this.readLock.lock();
        try {
            if (mapa[x][y] == null) return 0;
            return mapa[x][y].size();
        }
        finally {
            this.readLock.unlock();
        }
    }

    /**
     * Avisa todos os utilizadores que contactaram com alguem infetado
     * @param username Nome do utilizador infetado
     */
    public void warnUsers(String username) {
        this.writeLock.lock();
        try {
            for (String user : this.getUser(username).getUtilizadoresEmContacto())
                this.utilizadores.get(user).incWarn();
        }
        finally {
            this.writeLock.unlock();
        }
    }

    /**
     * Atualiza contactos que um utilizador obteve segundo a sua posição
     * @param username Nome do utilizador
     * @param x Coordenada X onde o utilizador está
     * @param y Coordenada Y onde o utilizador está
     */
    public void updateContacts(String username, int x, int y) {
        this.writeLock.lock();
        try {
            for (Utilizador user : this.mapa[x][y]) {
                this.getUser(username).addToUtilizadoresEmContacto(user.getUsername());
                user.addToUtilizadoresEmContacto(username);
            }
        }
        finally {
            this.writeLock.unlock();
        }
    }

    /**
     * Dá logout do utilizador
     * @param user Objeto do utilizador
     */
    public void userLogout(Utilizador user) {
        this.writeLock.lock();
        try {
            this.mapa[user.getPosicao().getX()][user.getPosicao().getY()].remove(user);
            this.getUser(user.getUsername()).unlockUser();
        }
        finally {
            this.writeLock.unlock();
        }
    }
}

