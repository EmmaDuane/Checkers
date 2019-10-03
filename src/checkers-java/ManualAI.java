/*
This module houses the ManualAI which is used when running the shell with the manual run options.

We are following the javadoc docstring format which is:
@param Tag Describes the input parameters of the function
@return Description of what the function returns
@throws Tag Describes the errors this function can raise
 */

import java.util.Scanner;
import java.util.Vector;

/**
 * This class describes the ManualAI.
 */
public class ManualAI extends AI {

    /**
     * Intializes manualAI
     * @param row no of rows in the board
     * @param col no of columns in the board
     * @param k no of rows to be filled with checker pieces at the start
     * @throws InvalidMoveError raises this objection if the move provided isn't valid on the current board
     */
    public ManualAI(int col, int row, int k) throws InvalidParameterError {
        super(col, row, k);

        this.board = new Board(col, row, k);
        this.board.initializeGame();
        this.player = 2;
    }

    /**
     * get_move function for manualAI called from the gameloop in the main module.
     * @param move A Move object describing the move.
     * @return a Move object describing the move manualAI wants to make. This move is basically console input.
     * @throws InvalidMoveError raises this objection if the move provided isn't valid on the current board
     */
    public Move GetMove(Move move) throws InvalidMoveError {
        if (!move.seq.isEmpty())
            board.makeMove(move, (player == 1) ? 2 : 1);
        else
            player = 1;
        Vector<Vector<Move>> moves = board.getAllPossibleMoves(player);
        for (int i = 0; i < moves.size(); ++i) {
            System.out.print(i + ": [");
            for (int j = 0; j < moves.get(i).size(); ++j) {
                System.out.print(((j != 0) ? ", " : "") + j + ": " + moves.get(i).get(j).toString());
            }
            System.out.println("]");
        }
        System.out.print("Waiting for input {int} {int}: ");
        Scanner scanner = new Scanner(System.in);
        int n = -1,
            m = -1;
        do {
            n = scanner.nextInt();
            m = scanner.nextInt();
            if ((n < 0 || n >= moves.size()) || (m < 0 || m >= moves.get(n).size()))
                System.out.print("Invalid move\nWaiting for input {int} {int}: ");
        } while ((n < 0 || n >= moves.size()) || (m < 0 || m >= moves.get(n).size()));
        Move resMove = moves.get(n).get(m);
        board.makeMove(resMove, player);
        return resMove;
    }
}
