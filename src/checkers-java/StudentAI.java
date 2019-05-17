import java.util.ArrayList;
import java.util.Random;

public class StudentAI extends AI {
    public StudentAI(int col, int row, int k) {
        super(col, row, k);
    }

    public Move GetMove(Move move) {
        if (!move.seq.isEmpty())
            board.makeMove(move, (player == 1) ? 2 : 1);
        else
            player = 1;
        ArrayList<ArrayList<Move>> moves = board.GetAllPossibleMoves(player);
        Random randGen = new Random();
        int index = randGen.nextInt(moves.size());
        int innerIndex = randGen.nextInt(moves.get(index).size());
        Move resMove = moves.get(index).get(innerIndex);
        board.makeMove(move, player);
        return resMove;
    }
}
