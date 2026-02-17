//import java.util.HashMap;

public class Board extends Score{
    private int[][] mat_pos = new int[5][5];

    public int[][] getMat_pos() {
        return mat_pos;
    }

    public void setMat_pos(int[][] mat_pos) {
        this.mat_pos = mat_pos;
    }

}

class Score{
    private int score, Moves, HighScore = 0;
    
    public int getScore() {
        return score;
    }

    public void setScore(int Score) {
        if(Score >= 0)
            this.score = Score;
    }

    public int getMoves() {
        return Moves;
    }

    public void setMoves(int Moves) {
        this.Moves = Moves;
    }
    
    public int getHighScore(){
        return HighScore;
    }
    
    public void setHighScore(int HighScore){
        this.HighScore = HighScore;
    }
    
    public void setHighScore(){
        this.HighScore = score;
    }
}

class point{
    int x, y;
    public point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

// class point{
//     int x, y;
//     HashMap<int, int> map = new HashMap<>();
//     public point(int x, int y){
//         map.put(x, y);
//     }
    
//     public point(int x){
//         map.get(x);
//     }
// }