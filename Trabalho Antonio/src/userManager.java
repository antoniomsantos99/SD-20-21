import java.util.ArrayList;
import java.util.HashMap;

public class userManager {

    private HashMap<String,Utilizador> utilizadores; // Key: username | Value: Objeto do Utilizador
    private Integer DIM; // Dimensão do mapa
    private ArrayList<Utilizador>[][] mapa; //Localizacao dos users (talvez mudar)

    public userManager() {
        this.utilizadores = new HashMap<String, Utilizador>();
        this.DIM = 100;
        this.mapa = new ArrayList[DIM][DIM];

    }

    /**
     * Regista um utilizador
     * @param username Nome do utilizador
     * @param password Password do utilizador
     * @return Estado de conclusão
     */
    public synchronized boolean registarUtilizador(String username,String password){
        if (!this.utilizadores.containsKey(username)){
            Utilizador user = new Utilizador(username,password);
            this.utilizadores.put(username,user);
            return true;
        }
        return false;
    }

    /**
     * Login de um utilizador
     * @param username Nome utilizador
     * @param password Password utilizador
     * @return True se login com sucesso, false caso contrario
     */
    public synchronized boolean loginUtilizador(String username, String password){
        if (this.utilizadores.containsKey(username)
                && this.utilizadores.get(username).getPassword().equals(password)
                && !this.utilizadores.get(username).checkLock()){

            this.utilizadores.get(username).lockUser();

            return true;
        }
        return false;
    }

    /**
     * Buscar a infromação de um utilizador
     * @param nome Nome do utilizador
     * @return Informação do utilizador
     */
    public synchronized Utilizador getUser(String nome) {
        return utilizadores.get(nome);
    }

}

