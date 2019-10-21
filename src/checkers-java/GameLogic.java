import java.util.Scanner;
import java.util.Vector;

/*
This module has the GameLogic Class which handles the different game modes (manual, tournament) and run it

We are following the javadoc docstring format which is:
@param tag describes the input parameters of the function
@return tag describes what the function returns
@throws tag describes the errors this function can raise
 */

/**
 * This class describes Game Logic
 */
public class GameLogic {
    private int col, row, p, order;
    private String mode;
    private Vector<AI> aiList;

    /**
     * Initialize a GameLogic object
     * @param col number of columns in the board
     * @param row number of rows in the board
     * @param p number of rows to be filled with checker pieces at the start
     * @param mode different modes that the game will be run on ("m": manual, "t": tournament)
     * @param order determines which AI goes first in the game
     */
    public GameLogic(int col, int row, int p, String mode, int order) {
        this.col = col;
        this.row = row;
        this.p = p;
        this.mode = mode;
        this.order = order;
        this.aiList = new Vector<AI>();
    }

    /**
     * Runs manual mode
     * @throws InvalidParameterError raises this exception if there is a problem with the provided variables
     */
    private void Manual() throws InvalidParameterError {
        int player = 1, winPlayer = 0;
        Move move = new Move(new Vector<Position>());
        Board board = new Board(col, row, p);
        board.initializeGame();
        board.showBoard();
        while (true) {
            try {
                move = aiList.get(player - 1).GetMove(move);
                board.makeMove(move, player);
            } catch (InvalidMoveError e) {
                winPlayer = (player == 1) ? 2 : 1;
                break;
            }
            winPlayer = board.isWin(player);
            board.showBoard();
            if (winPlayer != 0)
                break;
            player = (player == 1) ? 2 : 1;
        }
        System.out.println((winPlayer == -1) ? "Tie" : "Player " + winPlayer + " wins");
    }

    /**
     * Runs tournament mode
     * @throws InvalidParameterError raises this exception if there is a problem with the provided variables
     * @throws InvalidMoveError raises this objection if the move provided isn't valid on the current board
     */
    private void TournamentInterface() throws InvalidParameterError, InvalidMoveError {
        Scanner scanner = new Scanner(System.in);
        StudentAI ai = new StudentAI(col, row, p);
        while (true) {
            String input = scanner.nextLine();
            Move result = ai.GetMove(new Move(input));
            System.out.println(result.toString());
        }
    }

    /**
     * Runs either manual mode or tournament mode depending on the configuration
     * @throws InvalidParameterError raises this exception if there is a problem with the provided variables
     * @throws InvalidMoveError raises this objection if the move provided isn't valid on the current board
     */
    public void Run() throws InvalidParameterError, InvalidMoveError {
        if ("m".equals(this.mode) || "manual".equals(this.mode)) {
            if (this.order == 1) {
                aiList.addElement(new ManualAI(col, row, p));
                aiList.addElement(new StudentAI(col, row, p));
            }
            else {
                aiList.addElement(new StudentAI(col, row, p));
                aiList.addElement(new ManualAI(col, row, p));
            }

            Manual();
        }
        else if ("t".equals(this.mode))
            TournamentInterface();
    }
}
