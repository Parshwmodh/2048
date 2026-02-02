import java.util.Random;
import java.util.Scanner;

public class Gamelogic{

    public class Score{
        public static int score = 0;
        public static boolean  isMoves = false;
        public static int Moves = 0;
    }

    // public static isGameOver(int[][] mat_pos){
    //     int[][] d_mat_pos = new int[5][5];
    //     for
    // }

    public static void zeroRemover(int[] t_arr){
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

    public static void merger(int[] t_arr, int[][] mat_pos){

        for(int i=0;i<4;i++){
            if(t_arr[i] != 0 && t_arr[i] == t_arr[i+1]){
                t_arr[i] *= 2;
                Score.score += t_arr[i];
                t_arr[i+1] = 0;
            }
        }
    }

    public static void swipeUp(int[][] mat_pos){
        int[] t_arr = new int[5];

        for(int j=0;j<5;j++){
            for(int i=0;i<5;i++){
                t_arr[i] = mat_pos[i][j];
            }
            zeroRemover(t_arr);
            merger(t_arr, mat_pos);
            zeroRemover(t_arr);

            for(int i=0;i<5;i++){
                mat_pos[i][j] = t_arr[i];
            }
        }
        if(Score.isMoves){
            Score.Moves++;
        }Score.isMoves = false;
    }
    public static void swipeDown(int[][] mat_pos){
        int[] t_arr = new int[5];

        for(int j=0;j<5;j++){
            for(int i=0;i<5;i++){
                t_arr[4-i] = mat_pos[i][j];
            } 
            zeroRemover(t_arr);
            merger(t_arr, mat_pos);
            zeroRemover(t_arr);

            for(int i=0;i<5;i++){
                mat_pos[i][j] = t_arr[4-i];
            }
        }if(Score.isMoves){
            Score.Moves++;
        }Score.isMoves = false;
    }

    public static void swipeLeft(int[][] mat_pos){
        int[] t_arr = new int[5];

        for(int i=0;i<5;i++){
            System.arraycopy(mat_pos[i], 0, t_arr, 0, 5);
            zeroRemover(t_arr);
            merger(t_arr, mat_pos);
            zeroRemover(t_arr);

            System.arraycopy(t_arr, 0, mat_pos[i], 0, 5);
        }if(Score.isMoves){
            Score.Moves++;
        }Score.isMoves = false;
    }

    public static void swipeRight(int[][] mat_pos){
        int[] t_arr = new int[5];

        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                t_arr[4-j] = mat_pos[i][j];
            }
            zeroRemover(t_arr);
            merger(t_arr, mat_pos);
            zeroRemover(t_arr);
            for(int j=0;j<5;j++){
                mat_pos[i][j] = t_arr[4-j];
            }
        }if(Score.isMoves){
            Score.Moves++;
        }Score.isMoves = false;
    }

    public static void direction(int[][] mat_pos){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Swipe in? : ");
        String direc = scanner.nextLine().toUpperCase();
        if(direc != null){
            switch(direc) {
                case "W" -> swipeUp(mat_pos);
                case "S" -> swipeDown(mat_pos);
                case "A" -> swipeLeft(mat_pos);
                case "D" -> swipeRight(mat_pos);
                default -> {
                    throw new AssertionError();
                }
            }
        }
    }

    public static void display(int[][] mat_pos) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(mat_pos[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println("\n");
        System.out.println(" Score :: " + Score.score + "  Moves :: " + Score.Moves + "\n\n");
    
    }
    
    public static void ran_pos(int[][] mat_pos) {
        point[] emp_spot = new point[25];
        int count = 0;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(mat_pos[i][j] == 0){
                    emp_spot[count] = new point(i, j);
                    count++;
                }
            }
        }   
        if(count>0){
            Random random = new Random();
            int ran_pos = random.nextInt(count);
            point chosen = emp_spot[ran_pos];
            mat_pos[chosen.x][chosen.y] = 2;
        }
    }
}