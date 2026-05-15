package org.lfan142.tictactoe;

public enum Player {

    O(1),
    X(-1);

    private int score = 0;

    Player(int score){
        this.score = score;
    }

    public int getScore(){
        return score;
    }
}
