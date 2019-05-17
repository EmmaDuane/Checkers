import java.util.HashMap;
import java.util.Vector;
import java.lang.Math; 

public class Board {
	static final HashMap<String, String> opponent = new HashMap<String,String> () {{
    	put("W","B");
    	put("B","W");
    	}};
    int col = 0;
    int row = 0;
    int k = 0;
	Vector<Vector<Checker> > board;
    int blackCount = 0;
    int whiteCount = 0;
    
    public Board(int col, int row,int k) {
    	this.col = col;
        this.row = row;
        this.k = k;
        this.blackCount = 0;
        this.whiteCount = 0;
        this.board = new Vector<Vector<Checker> >(row);
        for (int i = 0;i < row; ++i)
        {
            for (int j = 0;j < col;++j)
            {
                this.board.get(i).add((new Checker(".",i,j)));
            }
        }
    }
    
    public Board(Board b) {
    	this.col = b.col;
    	this.row = b.row;
    	this.k = b.k;
    	this.blackCount = b.blackCount;
    	this.whiteCount = b.whiteCount;
    	this.board = new Vector<Vector<Checker> >(row);
    	for (int i = 0;i < row; ++i)
        {
            for (int j = 0;j < col;++j)
            {
                this.board.get(i).add((new Checker(b.board.get(i).get(j).color,i,j)));
            }
        }
    }
    
    
    public void showBoard()
    {
    	System.out.print("  ");
        for (int j = 0; j < this.col; ++j)
        {
        	System.out.print("  "+ Integer.toString(j));
        }
        System.out.println();
        for (int i = 0; i < this.row; ++i)
        {
        	System.out.print(Integer.toString(i) + "|");
            for (int j = 0; j < this.col; ++j)
            {
            	System.out.print("  " + this.board.get(i).get(j).toString());
            }
            System.out.println();
        }
        for (int j = 0; j < col; ++j)
        {
            System.out.print("----");
        }
        System.out.println();
    }

    public void checkInitialVariable() throws InvalidParameterError {
        // Recently changed: return false is changed to raising exceptions
        // Q > 0
        if (row - 2 * k <= 0)
        {
        	System.err.println("Q <= 0");
            throw new InvalidParameterError();
        }
        //M = 2P + Q
        else if (row != 2 * k + (row - 2 * k))
        {
        	System.err.println("M != 2P + Q");
            throw new InvalidParameterError();
        }
        // N*P is even
        else if (col * k % 2 != 0)
        {
        	System.err.println("N*P is odd");
            throw new InvalidParameterError();
        }
    }

    public int isWin() {
        boolean W = true;
        boolean B = true;
        for (int i = 0;i< this.row;i++)
            for (int j = 0;j< this.col;j++)
            {
                if (this.board.get(i).get(j).color == "W")
                    W = false;
                else if (this.board.get(i).get(j).color == "B")
                    B = false;
                if (! W && ! B)
                    return 0;
            }

        if (W)
            return 1;
        else if (B)
            return 2;
        else
            return 0;
    }
    
    public void initializeGame() throws InvalidParameterError{
        this.checkInitialVariable();
        for (int i = 0; i < this.k; ++i)
            for (int j = 0; j < this.col; ++j)
            {
                if (i % 2 != 0)
                {
                    if (j%2 == 0)
                    {
                    	this.board.get(i).get(j).changeColor_helper("B");
                        this.blackCount += 1;
                    	this.board.get(this.row - this.k + i).get(j).changeColor_helper("W");
                        this.whiteCount += 1;
                    }
                }
                else if (i%2 == 0)
                {

                    if (j%2 !=0)
                    {
                    	this.board.get(i).get(j).changeColor_helper("B");
                        this.blackCount += 1;
                    	this.board.get(this.row - this.k + i).get(j).changeColor_helper("W");
                        this.whiteCount += 1;
                    }
                }
            }
    }

    public boolean isInBoard(int pos_x, int pos_y)
    {
        return (pos_x >= 0 && pos_x < this.row && pos_y >= 0 && pos_y < this.col);
    }

    public void makeMove(Move move, int player) throws InvalidMoveError
    {
        //DO NOT TOUCH ANYTHING IN THIS FUNCTION
        //THE CODE LOGIC IS FROM PYTHON VERSION

        String turn = "";

        if (player == 1)
            turn = "B";
        else if (player == 2)
            turn = "W";
        else
            throw new InvalidMoveError();
        Vector<Position> move_list = move.seq;
        Vector<Vector<Position>> move_to_check = new Vector<Vector<Position>> ();
        Position ultimate_start = move_list.elementAt(0);
        Position ultimate_end = move_list.elementAt(move_list.size()-1);
        Vector<Position> past_positions = new Vector<Position> () {{
        	addElement(ultimate_start);
        }};
        Vector<Position> capture_positions = new Vector<Position> ();
        for (int i = 0;i<move_list.size()-1;++i)
        	{
            final Integer inneri = new Integer(i);
        	move_to_check.addElement(new Vector<Position> () {{
            	addElement(move_list.elementAt(inneri));
            	addElement(move_list.elementAt(inneri + 1));
            }}
           );}
        boolean if_capture = false;

        for (int t = 0; t<move_to_check.size();++t){
            Position start = move_to_check.get(t).get(0);
            Position target= move_to_check.get(t).get(1);
            if (this.isValidMove(start.getX(),start.getY(),target.getX(),target.getY(),turn) || (if_capture  && Math.abs(start.getX()-target.getX()) == 1)){
                this.board.get(start.getX()).get(start.getY()).color = ".";
                this.board.get(target.getX()).get(target.getY()).color = turn;
                this.board.get(target.getX()).get(target.getY()).isKing = this.board.get(start.getX()).get(start.getY()).isKing;
                this.board.get(start.getX()).get(start.getY()).becomeMan();
                past_positions.addElement(target);
                if (Math.abs(start.getX()-target.getX()) == 2)
                {
                    if_capture = true;
                    Position capture_position = new Position((start.getX() + (int)(target.getX()-start.getX())/2), (start.getY() + (int)(target.getY()-start.getY())/2));

                    capture_positions.addElement(capture_position);

                    this.board.get(capture_position.getX()).get(capture_position.getY()).changeColor_helper(".");
                }
                if (turn == "B"  && target.getX() == this.row - 1)
                    this.board.get(target.getX()).get(target.getY()).becomeKing();
                else if (turn == "W"  && target.getX() == 0)
                    this.board.get(target.getX()).get(target.getY()).becomeKing();
            }
            else {
                throw new InvalidMoveError();
            }
        }
    }

    public Vector<Vector<Move>> getAllPossibleMoves(String color) {
        Vector<Vector<Move>> result = new Vector<Vector<Move>> ();

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                Checker checker = board.get(i).get(j);
                if (checker.color == color) {
                    Vector<Move> moves;
                    moves = checker.getPossibleMoves(this);
                    if (!moves.isEmpty())
                        result.addElement(moves);
                }

            }
        }
        return result;
    }

    public Vector<Vector<Move>> getAllPossibleMoves(int player) {
        Vector<Vector<Move>> result = new Vector<Vector<Move>> ();
        String color = player == 1?"B" : "W";
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                Checker checker = board.get(i).get(j);
                if (checker.color == color) {
                    Vector<Move> moves;
                    moves = checker.getPossibleMoves(this);
                    if (!moves.isEmpty())
                        result.addElement(moves);
                }

            }
        }
        return result;
    }

    
    public boolean isValidMove(int chess_row, int chess_col, int target_row, int target_col, String turn)
    {
        if (target_row < 0||target_row >= this.row||target_col < 0||target_col >= this.col)
        	return false;
        if  (! (this.board.get(target_row).get(target_col).color == "."))
        	return false;
	    if  (! (this.board.get(chess_row).get(chess_col).color == turn))
	        return false;
	    int diff_col = target_col - chess_col;
	    int diff_row = target_row - chess_row;
	    if (Math.abs(diff_col) != Math.abs(diff_row))
	        return false;
	    Checker chess_being_moved = this.board.get(chess_row).get(chess_col);
	    if (diff_row == 1 && diff_col == 1)
	        return chess_being_moved.isKing||chess_being_moved.color == "B";
	    if (diff_row == 1 && diff_col == -1)
	        return chess_being_moved.isKing||chess_being_moved.color == "B";
	    if (diff_row == -1 && diff_col == 1  )
	        return chess_being_moved.isKing||chess_being_moved.color == "W";
	    if (diff_row == -1 && diff_col == -1  )
	        return chess_being_moved.isKing||chess_being_moved.color == "W";
	    if (diff_row == 2 && diff_col == 2 )
	        return (chess_being_moved.isKing||chess_being_moved.color == "B") && this.board.get(chess_row + 1).get(chess_col + 1).color != turn && this.board.get(chess_row + 1).get(chess_col + 1).color != ".";
	    if (diff_row == 2 && diff_col == -2 )
	        return (chess_being_moved.isKing||chess_being_moved.color == "B") && this.board.get(chess_row + 1).get(chess_col - 1).color != turn && this.board.get(chess_row + 1).get(chess_col - 1).color != ".";
	    if (diff_row == -2 && diff_col == 2 )
	        return (chess_being_moved.isKing||chess_being_moved.color == "W") && this.board.get(chess_row - 1).get(chess_col + 1).color != turn && this.board.get(chess_row - 1).get(chess_col + 1).color != ".";
	    if (diff_row == -2 && diff_col == -2 )
	        return (chess_being_moved.isKing||chess_being_moved.color == "W") && this.board.get(chess_row - 1).get(chess_col - 1).color != turn && this.board.get(chess_row - 1).get(chess_col - 1).color != ".";
	    return false;
    }


}
