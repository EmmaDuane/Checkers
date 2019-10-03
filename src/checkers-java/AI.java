public abstract class AI {
    int col, row, p, player;
    Board board;
    public AI(int col, int row, int p) {
        this.col = col;
        this.row = row;
        this.p = p;
    }

    public abstract Move GetMove(Move opponentMove) throws InvalidMoveError;
}
