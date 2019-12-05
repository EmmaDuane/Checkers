import java.util.HashMap;
import java.util.Vector;
import java.lang.Math;

/*
This module has the Board Class which is the class which handles the current board.

We are following the javadoc docstring format which is:
@param tag describes the input parameters of the function
@return tag describes what the function returns
@throws tag describes the errors this function can raise
 */

/**
 * This class describes Board
 */
public class Board {
	static final HashMap<String, String> opponent = new HashMap<String,String> () {{
    	put("W","B");
    	put("B","W");
    	}};
    int col = 0;
    int row = 0;
    int p = 0;
    int tieMax = 40;
    int tieCount = 0;
	Vector<Vector<Checker> > board;
    int blackCount = 0;
    int whiteCount = 0;

    // new
    class Saved_Move {
        Move made_move;
        Vector<Vector<Integer>> enemy_list;
        boolean become_king;
     }

     Vector<Saved_Move> saved_move_list = new Vector<Saved_Move>();
     // end

     /**
     * Initializes board:
     *      M = number of rows
     *      N = number of columns
     *      P = number of rows containing initial checker pieces
     * Adds the white checkers and black checkers to the board based on the board variables (M,N,P)
     * provided. N*P should be even to ensure that both players get the same number of checker pieces at the start
     *
     * @param row number of rows in the board
     * @param col number of columns in the board
     * @param p number of rows to be filled with checker pieces at the start
     */
    public Board(int col, int row,int p) {
    	this.col = col;
        this.row = row;
        this.p = p;
        this.blackCount = 0;
        this.whiteCount = 0;
        this.board = new Vector<Vector<Checker> >(row);
        for (int i = 0;i < row; ++i)
        {
            this.board.add(new Vector<Checker>());
            for (int j = 0;j < col;++j)
            {
                this.board.get(i).add((new Checker(".",i,j)));
            }
        }
    }

    /**
     * Copy constructor
     * @param b another board to copy
     */
    public Board(Board b) {
    	this.col = b.col;
    	this.row = b.row;
    	this.p = b.p;
    	this.blackCount = b.blackCount;
    	this.whiteCount = b.whiteCount;
    	this.board = new Vector<Vector<Checker> >(row);
    	for (int i = 0;i < row; ++i)
        {
            this.board.add(new Vector<Checker>());
            for (int j = 0;j < col;++j)
            {
                this.board.get(i).add((new Checker(b.board.get(i).get(j).color,i,j)));
                if (b.board.get(i).get(j).isKing)
                    this.board.get(i).get(j).becomeKing();
            }
        }
    }

    /**
     * prints board to console
     */
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

    /**
     * Checks the integrity of the initial board variables provided (M,N,P,Q)
     * @throws InvalidParameterError raises this exception if there is a problem with the provided variables
     */
    public void checkInitialVariable() throws InvalidParameterError {
        // Recently changed: return false is changed to raising exceptions
        // Q > 0
        if (row - 2 * p <= 0)
        {
        	System.err.println("Q <= 0");
            throw new InvalidParameterError();
        }
        //M = 2P + Q
        else if (row != 2 * p + (row - 2 * p))
        {
        	System.err.println("M != 2P + Q");
            throw new InvalidParameterError();
        }
        // N*P is even
        else if (col * p % 2 != 0)
        {
        	System.err.println("N*P is odd");
            throw new InvalidParameterError();
        }
    }

    /**
     * this function tracks if any player has won
     * @return the player who wins (-1 if tie, 0 if still going, 1 or 2 for Black and White)
     */
    public int isWin(int turn) {
        if (this.tieCount >= this.tieMax)
        {
            return -1;
        }
        boolean W = true;
        boolean B = true;
        boolean WHasMove = true;
        boolean BHasMove = true;
        if (this.getAllPossibleMoves(1).size() == 0) {

            if (turn != 1)
                BHasMove = false;
        } else if (this.getAllPossibleMoves(2).size() == 0) {
            if (turn != 2)
                WHasMove = false;
        }
        if (WHasMove && !BHasMove)
        {
            return 2;
        }
        else if  (!WHasMove && BHasMove)
        {
            return 1;
        }
        for (int row = 0; row < this.row; row++) {
            for (int col = 0; col < this.col; col++) {
                Checker checker = this.board.get(row).get(col);
                if (checker.color == "W")
                    W = false;
                else if (checker.color == "B")
                    B = false;
                if (!W && !B)
                    return 0;
            }
        }

        if (W)
            return 2;
        else if (B)
            return 1;
        else
            return 0;
    }


    /**
     * Initializes game. Adds the white checkers and black checkers to the board based on the board variables (M,N,P)
     * when the game starts
     * @throws InvalidParameterError raises this exception if there is a problem with the provided variables
     */
    public void initializeGame() throws InvalidParameterError{
        this.checkInitialVariable();
        for (int i = this.p; i >= 0; i--)
        {
            for (int j = (this.p - i - 1) % 2; j >= 0 && j < this.col; j += 2)
            {
                // put white pieces
                int i_white = this.row - this.p + i;
                this.board.get(i_white).get(j).changeColor_helper("W");
                // put black pieces
                if ((this.row % 2 + this.p % 2) % 2 == 1)  // row,p = even,odd or odd,even
                { 
                    if (i % 2 == 1)  // even row, shift to the left and attach a piece to the end when needed
                    {
                        if (j - 1 >= 0)
                            this.board.get(i).get(j-1).changeColor_helper("B");
                        if ((j == this.col - 2) && (this.col % 2 == 0))
                            this.board.get(i).get(this.col-1).changeColor_helper("B");
                    } else {  // odd row, shift to the right and attach a piece to the beginning when needed
                        if (j + 1 <= this.col - 1)
                            this.board.get(i).get(j+1).changeColor_helper("B");
                        if ((j == this.col - 1 || j == this.col - 2) && (this.p % 2 == 0))
                            this.board.get(i).get(0).changeColor_helper("B");
                    }
                } else {  // row,p = even,even
                    this.board.get(i).get(j).changeColor_helper("B");
                }
                this.whiteCount++;
                this.blackCount++;
            }
        }
    }

    /**
     * Checks if the coordinate provided is in board. Is an internal function
     * @param pos_x x coordinte of the object to check for
     * @param pos_y y coordinte of the object to check for
     * @return a bool to describe if object is in the board or not
     */
    public boolean isInBoard(int pos_x, int pos_y)
    {
        return (pos_x >= 0 && pos_x < this.row && pos_y >= 0 && pos_y < this.col);
    }

    /**
     * Makes Move on the board
     * @param move Move object provided by the StudentAI, Uses this parameter to make the move on the board
     * @param player this parameter tracks the current turn. either player 1 (white) or player 2(black)
     * @throws InvalidMoveError raises this objection if the move provided isn't valid on the current board
     */
    public void makeMove(Move move, int player) throws InvalidMoveError
    {
        //DO NOT TOUCH ANYTHING IN THIS FUNCTION
        //THE CODE LOGIC IS FROM PYTHON VERSION

        // create a new saved_move object
        Saved_Move temp_saved_move  = new Saved_Move();
        temp_saved_move.made_move = move;
        Vector<Vector<Integer>> saved_enemy_position = new Vector<Vector<Integer>>();
        // end

        String turn = "";

        if (player == 1)
            turn = "B";
        else if (player == 2)
            turn = "W";
        else
            throw new InvalidMoveError();
        final Vector<Position> move_list = move.seq;
        Vector<Vector<Position>> move_to_check = new Vector<Vector<Position>> ();
        final Position ultimate_start = move_list.elementAt(0);
        // new
        boolean is_start_check_king = this.board.get(ultimate_start.getX()).get(ultimate_start.getY()).isKing;
        // end
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
        this.tieCount += 1;
        for (int t = 0; t<move_to_check.size();++t)
        {
            Position start = move_to_check.get(t).get(0);
            Position target= move_to_check.get(t).get(1);
            if (this.isValidMove(start.getX(),start.getY(),target.getX(),target.getY(),turn) ||
                    (if_capture  && Math.abs(start.getX()-target.getX()) == 1))
            {
                this.board.get(start.getX()).get(start.getY()).color = ".";
                this.board.get(target.getX()).get(target.getY()).color = turn;
                this.board.get(target.getX()).get(target.getY()).isKing = this.board.get(start.getX()).get(start.getY()).isKing;
                this.board.get(start.getX()).get(start.getY()).becomeMan();
                past_positions.addElement(target);
                if (Math.abs(start.getX()-target.getX()) == 2)
                {
                    // new
                    Vector<Integer> temp_enemy_position = new Vector<Integer>();
                    //end

                    if_capture = true;
                    this.tieCount = 0;
                    Position capture_position = new Position((start.getX() + (int)(target.getX()-start.getX())/2), (start.getY() + (int)(target.getY()-start.getY())/2));

                    capture_positions.addElement(capture_position);
                    // new record capture position
                    temp_enemy_position.addElement(capture_position.x); //row
                    temp_enemy_position.addElement(capture_position.y); //col
                    temp_enemy_position.addElement(this.board.get(capture_position.getX()).get(capture_position.getY()).color == "B" ? 1 : 2);
                    temp_enemy_position.addElement(this.board.get(capture_position.getX()).get(capture_position.getY()).isKing ? 1 : 0);
                    saved_enemy_position.addElement(temp_enemy_position);
                    // end
                    this.board.get(capture_position.getX()).get(capture_position.getY()).changeColor_helper(".");
                    if(turn.equals("B"))
                        this.whiteCount --;
                    else
                        this.blackCount --;

                }
                if (turn == "B"  && target.getX() == this.row - 1) {
                    // new
                    if (!is_start_check_king){
                        temp_saved_move.become_king = true;
                    }
                    else{
                        temp_saved_move.become_king = false;
                    }  
                    // end
                    
                    this.board.get(target.getX()).get(target.getY()).becomeKing();
                    if (!is_start_check_king){
                        break;
                    }
                }
                else if (turn == "W"  && target.getX() == 0) {
                    // end
                    if (!is_start_check_king){
                        temp_saved_move.become_king = true;
                    }
                    else{
                        temp_saved_move.become_king = false;
                    }  
                    this.board.get(target.getX()).get(target.getY()).becomeKing();
                    if (!is_start_check_king){
                        break;
                    }
                }
                // new
                else
                    temp_saved_move.become_king = false;
                // end

            }
            else
                throw new InvalidMoveError();

        }
        // new
        temp_saved_move.enemy_list = saved_enemy_position;
        saved_move_list.addElement(temp_saved_move);
        // end
    }

    /**
     * this function returns the all possible moves of the player whose turn it is
     * @param color color of the player whose turn it is
     * @return a list of Move objects which describe possible moves
     */
    public Vector<Vector<Move>> getAllPossibleMoves(String color) {
        Vector<Vector<Move>> result = new Vector<Vector<Move>> ();
        boolean captureMode = false;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                Checker checker = board.get(i).get(j);
                if (checker.color == color) {
                    Vector<Move> moves;
                    moves = checker.getPossibleMoves(this);
                    if (!moves.isEmpty()) {
                        if (!captureMode && !moves.get(0).isCapture)
                            result.addElement(moves);
                        else if (!captureMode && moves.get(0).isCapture) {
                            result.clear();
                            captureMode = true;
                            result.addElement(moves);
                        }
                        else if (captureMode && moves.get(0).isCapture) {
                            result.addElement(moves);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * this function returns the all possible moves of the player whose turn it is
     * @param player number representing the player whose turn it is
     * @return a list of Move objects which describe possible moves
     */
    public Vector<Vector<Move>> getAllPossibleMoves(int player) {
        return this.getAllPossibleMoves((player == 1) ? "B" : "W");
    }

    /**
     * checks if a proposed move is valid or not.
     * @param chess_row row of the object whose move we are checking
     * @param chess_col col of the object whose move we are checking
     * @param target_row row where the object would end up
     * @param target_col col where the object would end up
     * @param turn tracks turn player 1(white) or player 2(black)
     * @return a bool which is True if valid, False otherwise
     */
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


    void Undo(){
        if (!saved_move_list.isEmpty())
        {
            Saved_Move temp_saved_move = saved_move_list.lastElement();
            Position original_position = temp_saved_move.made_move.seq.firstElement();
            Position target_position = temp_saved_move.made_move.seq.lastElement();

            this.board.get(original_position.getX()).get(original_position.getY()).color =
                        this.board.get(target_position.getX()).get(target_position.getY()).color;

            if (temp_saved_move.become_king)
                this.board.get(original_position.getX()).get(original_position.getY()).isKing = false;
            else
                this.board.get(original_position.getX()).get(original_position.getY()).isKing =
                        this.board.get(target_position.getX()).get(target_position.getY()).isKing;

            if (!(target_position.equals(original_position)))
            {
                this.board.get(target_position.getX()).get(target_position.getY()).color = ".";
                this.board.get(target_position.getX()).get(target_position.getY()).isKing = false;
            }
            for (int i = 0; i < temp_saved_move.enemy_list.size(); i++)
            {
                int x = temp_saved_move.enemy_list.elementAt(i).get(0);
                int y = temp_saved_move.enemy_list.elementAt(i).get(1);
                int c = temp_saved_move.enemy_list.elementAt(i).get(2);
                int k = temp_saved_move.enemy_list.elementAt(i).get(3);

                this.board.get(x).get(y).color = (c == 1? "B" : "W");
                this.board.get(x).get(y).isKing = (k == 0? false : true);
                
            }
            this.tieCount -= 1;
            saved_move_list.remove(saved_move_list.size()-1);
        }
        this.blackCount = 0;
        this.whiteCount = 0;
        for (int row = 0; row < this.row; row++) {
            for (int col = 0; col < this.col; col++) {
                if (this.board.get(row).get(col).color.equals("W"))
                {
                    this.whiteCount++;
                }
                else if (this.board.get(row).get(col).color.equals("B"))
                {
                    this.blackCount++;
                }
            
            }
        }
    }



}
