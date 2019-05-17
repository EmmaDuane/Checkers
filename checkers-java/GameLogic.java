import java.util.ArrayList;
import java.util.Scanner;

public class GameLogic {
    private int col, row, k, order;
    private String mode;
    private ArrayList<AI> aiList;

    public GameLogic(int col, int row, int k, String mode, int order) {
        this.col = col;
        this.row = row;
        this.k = k;
        this.mode = mode;
        this.order = order;
        this.aiList = new ArrayList<AI>();
    }

    public void Manual() {
        int player = 1, winPlayer = 0;
        Move move = new Move(-1, -1);
        Board board = new Board(col, row, k);
        board.initializeGame();
        while (true) {
            move = aiList.get(player - 1).getMove(move);
            try {
                board = board.makeMove(move, player);
            } catch (InvalidMoveError e) {
                winPlayer = (player == 1) ? 2 : 1;
                break;
            }
            winPlayer = board.isWin();
            board.showBoard();
            if (winPlayer != 0)
                break;
            player = (player == 1) ? 2 : 1;
        }
        System.out.println((winPlayer == -1) ? "Tie" : "Player " + winPlayer + " wins");
    }

    public void TournamentInterface() {
        Scanner scanner = new Scanner(System.in);
        StudentAI ai = new StudentAI(col, row, k);
        while (true) {
            int col = scanner.nextInt(),
                row = scanner.nextInt();
            Move result = ai.getMove(new Move(col, row));
            System.out.println(result.col + ' ' + result.row + ' ');
        }
    }

    public void Run() {
        if ("m".equals(this.mode) || "manual".equals(this.mode)) {
            if (this.order == 1) {
                aiList.add(new ManualAI(col, row, k));
                aiList.add(new StudentAI(col, row, k));
            }
            else {
                aiList.add(new StudentAI(col, row, k));
                aiList.add(new ManualAI(col, row, k));
            }

            Manual();
        }
        else if ("t".equals(this.mode))
            TournamentInterface();
    }
}
