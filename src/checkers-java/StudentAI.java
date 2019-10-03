import java.util.Random;
import java.util.Vector;

// The following part should be completed by students.
// Students can modify anything except the class name and exisiting functions and varibles.

public class StudentAI extends AI {
    public StudentAI(int col, int row, int k) throws InvalidParameterError {
        super(col, row, k);

        this.board = new Board(col, row, k);
        this.board.initializeGame();
        this.player = 2;
    }

    public Move GetMove(Move move) throws InvalidMoveError {
        if (!move.seq.isEmpty())
            board.makeMove(move, (player == 1) ? 2 : 1);
        else
            player = 1;
        Vector<Vector<Move>> moves = board.getAllPossibleMoves(player);
        Random randGen = new Random();
        int index = randGen.nextInt(moves.size());
        int innerIndex = randGen.nextInt(moves.get(index).size());
        Move resMove = moves.get(index).get(innerIndex);
        board.makeMove(resMove, player);
        return resMove;
    }
}
