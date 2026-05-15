package org.lfan142.tictactoe;

public class TicTacGame {

    private final Player[][] board;
    private Player validPlayer = Player.X;

    private boolean gameOver = false; //TO DO

    private final int[] rowScore;
    private final int[] colScore;
    private int diagnalScore;
    private int antiDiagnalScore;
    private int moveCnt = 0;
    private int size = 0;
    private int total = 0;



    public TicTacGame(int size){
        validArgs(size);
        board = new Player[size][size];
        rowScore = new int[size];
        colScore = new int[size];
        total = size * size;
        this.size = size;

    }

    private void validArgs(int size) {
        if(size < 2){
            throw new IllegalArgumentException("The arg is illegal size" +size );
        }
    }


    public GameStatus play(Player player, int row, int col){
        validInput(player, row, col);
        move(player, row, col);
        GameStatus status = getGameStatus(player, row, col);

        if(status == GameStatus.IN_PROGRESS){
            switchValidPlayer(player);
        }
        return status;

    }

    private void validInput(Player player, int row, int col) {

        if(gameOver){
            throw new IllegalStateException("Game was over !");
        }
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if(player != validPlayer){
            throw new IllegalArgumentException("It is not your turn!");
        }

        if(row < 0 || row >= size){
            throw new IllegalArgumentException("Invalid row number :" + row);
        }
        if(col < 0 || col >= size){
            throw new IllegalArgumentException("Invalid row number :" + col);
        }
        if (board[row][col] != null) {
            throw new IllegalArgumentException("This cell is already occupied");
        }
    }

    private void switchValidPlayer(Player player) {
        if(player == Player.X){
            validPlayer = Player.O;
        }else{
            validPlayer = Player.X;
        }
    }


    private GameStatus getGameStatus(Player player, int row, int col) {
        if(Math.abs(rowScore[row]) == size
                || Math.abs(colScore[col]) == size
                ||Math.abs(antiDiagnalScore) == size
                ||Math.abs(diagnalScore) == size){
            gameOver= true;

            if(player == Player.O){
                return GameStatus.O_WINS;
            } else{
                return GameStatus.X_WINS;
            }
        }
        if(moveCnt == total){
            gameOver= true;
            return GameStatus.DRAW;
        }
        return GameStatus.IN_PROGRESS;
    }

    private void move(Player player, int row, int col){
        board[row][col] = player;
        moveCnt ++;

        int score = player.getScore();
        rowScore[row] += score;
        colScore[col] += score;

        if(row == col){
            diagnalScore += score;
        }
        if(row + col == size -1){
            antiDiagnalScore += score;
        }
    }


}
