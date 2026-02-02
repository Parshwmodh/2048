import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        int[][] mat_pos = new int[5][5];
        Scanner scanner = new Scanner(System.in);
        System.out.print("Wanna Play 2048 ? (Y or N):- ");
        String choice = scanner.nextLine();
        if(choice.equals("Y") || choice.equals("y")){
            do {
            Gamelogic.ran_pos(mat_pos);
            Gamelogic.display(mat_pos);
            Gamelogic.direction(mat_pos);
            } while (true);
        }
    }
}