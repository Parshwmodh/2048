//import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        // Scanner scanner = new Scanner(System.in);
        // System.out.print("Wanna Play 2048 ? (Y or N):- ");
        // String choice = scanner.nextLine();
        // if(choice.equals("Y") || choice.equals("y")){
        Board matrix = new Board();
        Gamelogic.ran_pos(matrix);
        Gamelogic.display(matrix);
        do {
            Gamelogic.direction(matrix);
            Gamelogic.display(matrix);
        } while (true);
        }
    //}
}