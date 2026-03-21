import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Gamelogic{
    
    void restartGame(Board matrix){
        try(FileWriter writer = new FileWriter("C:\\Users\\Lenovo\\Desktop\\JAVA\\JavaFx\\HelloFx\\src\\savedGame.txt")){
            Random random = new Random();
            int ran1 = random.nextInt(5);
            int ran2 = random.nextInt(5);
            int ran3 = random.nextInt(5);
            int ran4 = random.nextInt(5);
            for(int i=0;i<5;i++){
                for(int j=0;j<5;j++){
                    if(ran1 == i && ran2 == j || ran3 == i && ran4 == j){
                        writer.write(2 + " ");
                    }else
                        writer.write(0 + " ");
                }
            }
            writer.write(String.valueOf(0 + " "));
            writer.write(String.valueOf(0 + " "));
            writer.write(String.valueOf(matrix.getHighScore()));
            saveGame(matrix);
        } catch (IOException e) {
           System.out.println("\n\nError occured while Saving the Game!\n\n");
        }
    }
    
    void saveGame(Board matrix, int x){
        int[][] tmat = matrix.getMat_pos();
        try(FileWriter writer = new FileWriter("C:\\Users\\Lenovo\\Desktop\\JAVA\\JavaFx\\HelloFx\\src\\savedGame.txt")){
            for(int i=0;i<x;i++){
                for(int j=0;j<x;j++){
                    writer.write(tmat[i][j] + " ");
                }
            }
            writer.write(String.valueOf(matrix.getScore()) + " ");
            writer.write(String.valueOf(matrix.getMoves()) + " ");
            writer.write(String.valueOf(matrix.getHighScore()));
        } catch (IOException e) {
           System.out.println("\n\nError occured while Saving the Game!\n\n");
        }
    }

    void saveGame(Board matrix){
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

    public void loadGame(Board matrix){
        int[][] tmat = new int[5][5];
        try {
            File myFile = new File("C:\\Users\\Lenovo\\Desktop\\JAVA\\JavaFx\\HelloFx\\src\\savedGame.txt");
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

    private void exit(Board matrix){
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

    int isGameOver(Board matrix){
        loadGame(matrix);
        int[][] tmat_pos = matrix.getMat_pos();
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                if(tmat_pos[i][j] == 2048){
                    System.out.println("it is");
                    return 1;
                }
            }
        }
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                if(tmat_pos[i][j] == 0){
                    return 0;
                }
                if((j+1)<5){
                    if(tmat_pos[i][j] == tmat_pos[i][j+1]){
                        return 2;
                    }else if(tmat_pos[j][i] == tmat_pos[j+1][i]){
                        return 2;
                    }
                }
            }
        }return 3;
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

public static List<int[]> mergedPositions = new ArrayList<>();

// private static void merger(int[] t_arr, int lineIndex, List<int[]> mergedPositions, boolean isVertical){
//     for(int i = 0; i < 4; i++){
//         if(t_arr[i] != 0 && t_arr[i] == t_arr[i+1]){
//             t_arr[i] *= 2;
//             Score.score += t_arr[i];
//             Score.isMoves = true;
//             t_arr[i+1] = 0;
            
//             // save real 2D position
//             if(isVertical)
//                 mergedPositions.add(new int[]{i, lineIndex}); // row=i, col=lineIndex
//             else
//                 mergedPositions.add(new int[]{lineIndex, i}); // row=lineIndex, col=i
//         }
//     }
// }

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

    int swipeUp(int[][] tmat_pos, Board matrix){
    int[] t_arr = new int[5];
    int XY = -1;
    
    for(int j = 0; j < 5; j++){
        
        // save old column BEFORE operations
        int[] oldCol = new int[5];
        for(int i = 0; i < 5; i++){
            t_arr[i] = tmat_pos[i][j];
            oldCol[i] = t_arr[i]; // snapshot!
        }
        
        zeroRemover(t_arr);
        merger(t_arr);
        zeroRemover(t_arr);
        
        // compare old vs new to find merges
        for(int i = 0; i < 5; i++){
            if(oldCol[i] != 0 && t_arr[i] == oldCol[i] * 2){
                mergedPositions.add(new int[]{i, j});
            }
        }
        
        // write back to board
        for(int i = 0; i < 5; i++){
            tmat_pos[i][j] = t_arr[i];
        }
    }
    
    if(Score.isMoves){
        Score.Moves++;
        XY = ran_pos(matrix);
    }
    Score.isMoves = false;
    matrix.setScore(Score.score);
    matrix.setMoves(Score.Moves);
    return XY;
}

    int swipeDown(int[][] tmat_pos, Board matrix){
        int[] t_arr = new int[5];
        int XY = -1;
        for(int j=0;j<5;j++){
        int[] oldCol = new int[5];
            for(int i=0;i<5;i++){
                t_arr[4-i] = tmat_pos[i][j];
            }
            // snapshot AFTER filling t_arr
            for(int i=0;i<5;i++){
                oldCol[i] = t_arr[i];
}
            zeroRemover(t_arr);
            // merger(t_arr, j, mergedPositions, true);
            merger(t_arr);
            zeroRemover(t_arr);
            for(int i = 0; i < 5; i++){
                if(oldCol[i] != 0 && t_arr[i] == oldCol[i] * 2){
                    mergedPositions.add(new int[]{i, j}); // real board position!
                }
            }
            for(int i=0;i<5;i++){
                tmat_pos[i][j] = t_arr[4-i];
            }
        }if(Score.isMoves){
            Score.Moves++;
            XY = ran_pos(matrix);
        }Score.isMoves = false;
        matrix.setScore(Score.score);
        matrix.setMoves(Score.Moves);
        return XY;
    }

    int swipeLeft(int[][] tmat_pos, Board matrix){
        int[] t_arr = new int[5];
        int XY = -1;
        for(int i=0;i<5;i++){
            int[] oldCol = new int[5];
            System.arraycopy(tmat_pos[i], 0, t_arr, 0, 5);
            System.arraycopy(t_arr, 0, oldCol, 0, 5);
            zeroRemover(t_arr);
            // merger(t_arr, i, mergedPositions, false);
            merger(t_arr);
            zeroRemover(t_arr);
            for(int j = 0; j < 5; j++){
                if(oldCol[j] != 0 && t_arr[j] == oldCol[j] * 2){
                    mergedPositions.add(new int[]{i, j}); // real board position!
                }
            }
            System.arraycopy(t_arr, 0, tmat_pos[i], 0, 5);
        }if(Score.isMoves){
            Score.Moves++;
            XY = ran_pos(matrix);
        }Score.isMoves = false;
        matrix.setScore(Score.score);
        matrix.setMoves(Score.Moves);
        return XY;
        
    }

    int swipeRight(int[][] tmat_pos, Board matrix){
        int[] t_arr = new int[5];
        int XY = -1;
        for(int i=0;i<5;i++){
            int[] oldCol = new int[5];
            for(int j=0;j<5;j++){
                t_arr[4-j] = tmat_pos[i][j];
            }
            // snapshot after filling
            for(int j=0;j<5;j++){
                oldCol[j] = t_arr[j];
            }
            zeroRemover(t_arr);
            // merger(t_arr, i, mergedPositions, false);
            merger(t_arr);
            zeroRemover(t_arr);
            for(int j = 0; j < 5; j++){
                if(oldCol[j] != 0 && t_arr[j] == oldCol[j] * 2){
                    mergedPositions.add(new int[]{i, j}); // real board position!
                }
            }
            for(int j=0;j<5;j++){
                tmat_pos[i][j] = t_arr[4-j];
            }
        }if(Score.isMoves){
            Score.Moves++;
            XY = ran_pos(matrix);
        }Score.isMoves = false;
        matrix.setScore(Score.score);
        matrix.setMoves(Score.Moves);
        return XY;
    }

    public void direction(Board matrix, int x){
        if(isGameOver(matrix) == 1){
            System.out.print("\n\n\t CONGRATS YOU REACHED 2048!!! \n\n");
            System.exit(0);
        }else if(isGameOver(matrix) == 3){
            System.out.print("\n\n\tYOU HAVE NO MORE MOVES LEFT, SORRY!!!\n\n");
            System.exit(0);
        }

        int direc = x;
        int[][] tmat_pos = matrix.getMat_pos();
        if(direc > 0 && direc < 5){
            switch(direc) {
                case 1 -> swipeUp(tmat_pos, matrix);
                case 2 -> swipeDown(tmat_pos, matrix);
                case 3 -> swipeLeft(tmat_pos, matrix);
                case 4 -> swipeRight(tmat_pos, matrix);
                //case "EXIT" -> exit(matrix);
                default -> {
                    System.out.println("You have to Enter Valid Move (w,a,s,d)!");
                }
            }
        }
        matrix.setMat_pos(tmat_pos);
    }
    
    public void direction(Board matrix){
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
                //case "W" -> swipeUp(tmat_pos);
                //case "S" -> swipeDown(tmat_pos);
                //case "A" -> swipeLeft(tmat_pos);
                //case "D" -> swipeRight(tmat_pos);
                //case "EXIT" -> exit(matrix);
                default -> {
                    System.out.println("You have to Enter Valid Move (w,a,s,d)!");
                }
            }
        }
        matrix.setMat_pos(tmat_pos);
    }

    public void display(Board matrix) {
        loadGame(matrix);
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
    
    public void ranPos(Board matrix){
        ran_pos(matrix);
        saveGame(matrix, 5);
    }
    
    public static int ran_pos(Board matrix){
        int[][] tmat_pos = matrix.getMat_pos();
        HashMap<Integer, Integer> map = new HashMap<>();
        map.clear();
        int count = 0;
        int XY = 0;
        for(int i = 0; i < 5; i++){
            for (int j = 0; j < 5; j++) {
                if(tmat_pos[i][j] == 0){
                    System.out.print("Success");
                    
                    int xy = (i*5)+j;
                    map.put(count, xy);
                    count++;
                }
            }
        }
        if(count>0){
            System.out.print("Running");
            Random random = new Random();
            int ran_pos = random.nextInt(count);
            XY = map.get(ran_pos);
            int x = XY/5;
            int y = XY%5;
            tmat_pos[x][y] = 2;
        }
        matrix.setMat_pos(tmat_pos);
        return XY;
    }
    
    // public static void ran_pos(Board matrix) {
    //     int[][] tmat_pos = matrix.getMat_pos();
    //     point[] emp_spot = new point[25];
    //     int count = 0;

    //     for (int i = 0; i < 5; i++) {
    //         for (int j = 0; j < 5; j++) {
    //             if(tmat_pos[i][j] == 0){
    //                 emp_spot[count] = new point(i, j);
    //                 count++;
    //             }
    //         }
    //     }
    //     if(count>0){
    //         Random random = new Random();
    //         int ran_pos = random.nextInt(count);
    //         point chosen = emp_spot[ran_pos];
    //         tmat_pos[chosen.x][chosen.y] = 2;
    //     }
    //     matrix.setMat_pos(tmat_pos);
    // }
}