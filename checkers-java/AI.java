public abstract class AI {
    int col, row, k, player;
    Board board;
    public AI(int col, int row, int k) {
        this.col = col;
        this.row = row;
        this.k = k;
        this.board = new Board(col, row, k);
        this.board.initializeGame();
        this.player = 2;
    }

    public abstract Move GetMove(Move opponentMove);
}
