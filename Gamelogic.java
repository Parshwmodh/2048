import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Gamelogic{

    private static void saveGame(Board matrix){
        int[][] tmat = matrix.getMat_pos();
        try(FileWriter writer = new FileWriter("savedGame.txt")){
            for(int i=0;i<5;i++){
                for(int j=0;j<5;j++){
                    writer.write(tmat[i][j] + " ");
                }
            }
            System.out.println("Game State Saved...");
            writer.write(String.valueOf(matrix.getScore()) + " ");
            System.out.println("Current Score Saved...");
            writer.write(String.valueOf(matrix.getMoves()) + " ");
            System.out.println("Current Moves Saved...");
            writer.write(String.valueOf(matrix.getHighScore()));
            System.out.println("\nGame Successfully Saved!\n");
        } catch (IOException e) {
           System.out.println("\n\nError occured while Saving the Game!\n\n");
        }
    }
    
    public static void loadGame(Board matrix){
        int[][] tmat = new int[5][5];
        try {
            File myFile = new File("savedGame.txt");
            try (Scanner reader = new Scanner(myFile)) {
                for(int i=0;i<5;i++){
                    for(int j=0;j<5;j++){
                        if(reader.hasNextInt()){
                            tmat[i][j] = reader.nextInt();
                        }
                    }
                }
                matrix.setScore(reader.nextInt());
                Score.score = matrix.getScore();
                matrix.setMoves(reader.nextInt());
                Score.Moves = matrix.getMoves();
                matrix.setHighScore(reader.nextInt());
                matrix.setMat_pos(tmat);
            }

        } catch (Exception e) {
            System.out.println("\nSomething went wrong While Loading the Data or There is no Data!\n");
        }
    }

    private static void exit(Board matrix){
        System.out.println("Saving Game Data...\n");
        if(matrix.getHighScore() <= matrix.getScore()){
            matrix.setHighScore();
            System.out.println("HighScore Updated...");
        }
        saveGame(matrix);
        System.exit(0);
    }

    private  class Score{
        public static int score = 0;
        public static boolean  isMoves = false;
        public static int Moves = 0;
    }

    private static int isGameOver(Board matrix){
        int[][] tmat_pos = matrix.getMat_pos();
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                if(tmat_pos[i][j] == 2048){
                    return 1;
                }
                if(tmat_pos[i][j] == 0){
                    return 0;
                }
                if(tmat_pos[i][j] == tmat_pos[i][j+1]){
                    return 2;
                }else if(tmat_pos[j][i] == tmat_pos[j+1][i]){
                    return 2;
                }
            }
        }
        return 3;
    }

    private static void zeroRemover(int[] t_arr){
        for(int i=0;i<4;i++){
            if(t_arr[i] == 0){
                for(int j=i+1;j<5;j++){
                    if(t_arr[j]!=0){
                        int temp = t_arr[j];
                        t_arr[j] = t_arr[i];
                        t_arr[i] = temp;
                        Score.isMoves = true;
                        break;
                    }
                }
            }
        }
    }

    private static void merger(int[] t_arr){
        for(int i=0;i<4;i++){
            if(t_arr[i] != 0 && t_arr[i] == t_arr[i+1]){
                t_arr[i] *= 2;
                Score.score += t_arr[i];
                Score.isMoves = true;
                t_arr[i+1] = 0;
            }
        }
    }

    private static void swipeUp(int[][] tmat_pos){
        int[] t_arr = new int[5];

        for(int j=0;j<5;j++){
            for(int i=0;i<5;i++){
                t_arr[i] = tmat_pos[i][j];
            }
            zeroRemover(t_arr);
            merger(t_arr);
            zeroRemover(t_arr);

            for(int i=0;i<5;i++){
                tmat_pos[i][j] = t_arr[i];
            }
        }
        if(Score.isMoves){
            Score.Moves++;
        }Score.isMoves = false;
    }

    private static void swipeDown(int[][] tmat_pos){
        int[] t_arr = new int[5];

        for(int j=0;j<5;j++){
            for(int i=0;i<5;i++){
                t_arr[4-i] = tmat_pos[i][j];
            } 
            zeroRemover(t_arr);
            merger(t_arr);
            zeroRemover(t_arr);

            for(int i=0;i<5;i++){
                tmat_pos[i][j] = t_arr[4-i];
            }
        }if(Score.isMoves){
            Score.Moves++;
        }Score.isMoves = false;
    }

    private static void swipeLeft(int[][] tmat_pos){
        int[] t_arr = new int[5];

        for(int i=0;i<5;i++){
            System.arraycopy(tmat_pos[i], 0, t_arr, 0, 5);
            zeroRemover(t_arr);
            merger(t_arr);
            zeroRemover(t_arr);

            System.arraycopy(t_arr, 0, tmat_pos[i], 0, 5);
        }if(Score.isMoves){
            Score.Moves++;
        }Score.isMoves = false;
    }

    private static void swipeRight(int[][] tmat_pos){
        int[] t_arr = new int[5];

        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                t_arr[4-j] = tmat_pos[i][j];
            }
            zeroRemover(t_arr);
            merger(t_arr);
            zeroRemover(t_arr);
            for(int j=0;j<5;j++){
                tmat_pos[i][j] = t_arr[4-j];
            }
        }if(Score.isMoves){
            Score.Moves++;
        }Score.isMoves = false;
    }

    public static void direction(Board matrix){
        if(isGameOver(matrix) == 1){
            System.out.print("\n\n\t CONGRATS YOU REACHED 2048!!! \n\n");
            System.exit(0);
        }else if(isGameOver(matrix) == 3){
            System.out.print("\n\n\tYOU HAVE NO MORE MOVES LEFT, SORRY!!!\n\n");
            System.exit(0);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Swipe in? : ");
        String direc = scanner.nextLine().toUpperCase();
        int[][] tmat_pos = matrix.getMat_pos();
        if(direc != null){
            switch(direc) {
                case "W" -> swipeUp(tmat_pos);
                case "S" -> swipeDown(tmat_pos);
                case "A" -> swipeLeft(tmat_pos);
                case "D" -> swipeRight(tmat_pos);
                case "EXIT" -> exit(matrix);
                default -> {
                    System.out.println("You have to Enter Valid Move (w,a,s,d)!");
                }
            }
        }
        matrix.setMat_pos(tmat_pos);
    }

    public static void display(Board matrix) {
        matrix.setScore(Score.score);
        if(matrix.getHighScore() < matrix.getScore()){
            matrix.setHighScore();
        }
        if(Score.Moves == (matrix.getMoves()+1)){
            matrix.setMoves(Score.Moves);
            ran_pos(matrix);
        }
        System.out.println("\tHighest Score: " + matrix.getHighScore() + "\n\n");
        int[][] tmat_pos = matrix.getMat_pos();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(tmat_pos[i][j] == 0){
                    System.out.print(" _ ");
                }else
                    System.out.print(" " + tmat_pos[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("\n");
        System.out.println("\n Score: "+matrix.getScore()+"  Moves: "+matrix.getMoves());
        System.out.println("\n");
    }

    public static void ran_pos(Board matrix) {
        int[][] tmat_pos = matrix.getMat_pos();
        point[] emp_spot = new point[25];
        int count = 0;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(tmat_pos[i][j] == 0){
                    emp_spot[count] = new point(i, j);
                    count++;
                }
            }
        }
        if(count>0){
            Random random = new Random();
            int ran_pos = random.nextInt(count);
            point chosen = emp_spot[ran_pos];
            tmat_pos[chosen.x][chosen.y] = 2;
        }
        matrix.setMat_pos(tmat_pos);
    }
}