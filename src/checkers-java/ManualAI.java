import java.util.ArrayList;
import java.util.Scanner;

public class ManualAI extends AI {
    public ManualAI(int col, int row, int k) {
        super(col, row, k);
    }

    @Override
    public Move GetMove(Move move) {
        if (!move.seq.isEmpty())
            board.makeMove(move, (player == 1) ? 2 : 1);
        else
            player = 1;
        ArrayList<ArrayList<Move>> moves = board.getAllPossibleMoves(player);
        for (int i = 0; i < moves.size(); ++i) {
            System.out.print(i + ": [");
            for (int j = 0; j < moves.get(i).size(); ++j) {
                System.out.print(j + ": " + moves.get(i).get(j).toString() + ",");
            }
            System.out.println("]");
        }
        System.out.print("Waiting for input {int} {int}: ");
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(),
            m = scanner.nextInt();
        Move resMove = moves.get(n).get(m);
        board.makeMove(resMove, player);
        return resMove;
    }
}
