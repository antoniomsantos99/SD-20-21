import java.util.Scanner;

public class Menu {

    public static void main(String[] args) {
        //utilizado apenas para testes
        System.out.println("I'm Alive");
    }

    /**
     * Possível update: adicionar opção de cancelar operação
     * @param question questão para responder sim ou não
     * @return retorna sim ou não
     */
    public int run(String question){
        try{
            System.out.println(question);
            Scanner sc = new Scanner(System.in);
            int choice = sc.nextInt();
            return choice;
        }catch(Exception e){
            System.out.println("Error: Invalid Input.");
            return run(question);
        }
    }

    /**
     * Possível update: adicionar opção de cancelar operação
     * @param options opções de escolha
     * @return valor escolhido pelo utilizador
     */
    public int run(String[] options){
        try{
            System.out.println("Please Select an option:");
            for(int i = 0 ; i< options.length ; i++){
                System.out.println(String.format("[%d] - %s", i+1, options[i]));
            }
            Scanner sc = new Scanner(System.in);
            int choice = sc.nextInt();
            if(choice-1 < 0 || choice-1 > options.length) throw new IllegalArgumentException();
            return choice;
        }catch(Exception e){
            System.out.println("Error: Invalid Input.");
            return run(options);
        }
    }

    /**
     * Possível update: adicionar opção de cancelar operação
     * @param question questão a perguntar antes da escolha
     * @param options opções de escolha
     * @return valor escolhido pelo utilizador
     */
    public int run(String question, String[] options){
        try{
            System.out.println(question);
            for(int i = 0 ; i< options.length ; i++){
                System.out.println(String.format("[%d] - %s", i, options[i]));
            }
            Scanner sc = new Scanner(System.in);
            int choice = sc.nextInt();
            if(choice < 0 || choice > options.length) throw new IllegalArgumentException();
            return choice;
        }catch(Exception e){
            System.out.println("Error: Invalid Input.");
            return run(question, options);
        }
    }
}