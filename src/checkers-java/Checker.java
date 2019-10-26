import java.util.HashMap;
import java.util.Vector;

/*
This module has the Checkers Class that describes checker pieces.

We are following the javadoc docstring format which is:
@param tag describes the input parameters of the function
@return tag describes what the function returns
@throws tag describes the errors this function can raise
 */


public class Checker {
	String color;
    int  row;
    int col;
    static final HashMap<String, String> opponent = new HashMap<String,String> () {{
    	put("W","B");
    	put("B","W");
    	}};
    Boolean isKing;

	/**
	 * Initializes Checker pieces
	 * @param color stores the color of this checker
	 * @param r row of this checker
	 * @param c column of this checker
	 */
	public Checker(String color, int r, int c) {
    	if (color.equals("w"))
    	{
    		color = "W";
    	}
    	if (color.equals("b"))
    	{
    		color = "B";
    	}
    	this.color = color;
    	this.row = r;
    	this.col = c;
    	this.isKing = false;
    }

	/**
	 * Changes checker piece to king
	 */
	public void becomeKing() {
    	this.isKing = true;
    }

	/**
	 * Changes to regular piece
	 */
	public void becomeMan() {
    	this.isKing = false;
    }

	/**
	 * Get all possible moves of this checker. These moves may be filtered in the context of the board.
	 * @param board has the current state of the board
	 * @return a list of Move objects that describes the possible move for this checker
	 */
	public Vector<Move> getPossibleMoves(Board board) {
		Direction direction= new Direction();
	    Vector<Move> result = new Vector<Move>();
	    if (this.color == ".")
	    {
	        return result;
	    }
	    Vector<Vector<Position>> multiple_jump = new Vector<Vector<Position>> ();
	    Vector<Position> explore_direction = direction.at(color);
	    if (isKing)
	    {
	        for  (int i = 0; i<direction.at(opponent.get(color)).size(); i++)
	        {
	            explore_direction.addElement(direction.at(opponent.get(color)).get(i));
	        }
	    }
	    for (int i = 0; i<explore_direction.size();++i) {
	        final int pos_x = row+explore_direction.get(i).getX();
	        final int pos_y = col+explore_direction.get(i).getY();
	        if (board.isInBoard(pos_x,pos_y))
	        {
	            if (board.board.get(pos_x).get(pos_y).color == ".")
	            {
	            	Vector<Position> x = new Vector<Position> () {{
	            		addElement(new Position(row,col));
	            		addElement(new Position(pos_x,pos_y));
	            	}};
	                result.addElement(new Move(x));
	            }
	        }
	    }
	    String selfColor = board.board.get(row).get(col).color;
	    Vector<Position> temp_v=new Vector<Position> ();
	    board.board.get(row).get(col).color = ".";
	    binary_tree_traversal(row, col, multiple_jump, board, explore_direction, temp_v,selfColor);
	    if (!multiple_jump.isEmpty())
	    {
	        result.clear();
	    }
	    for(int x = 0; x<multiple_jump.size();x++) {
	    	multiple_jump.get(x).insertElementAt(new Position(row,col),0);
	    	Move tempMove = new Move(multiple_jump.get(x));
	    	tempMove.isCapture = true;
	    	result.addElement(tempMove);
	    }
	    board.board.get(row).get(col).color = selfColor;
	    return result;
	}
	
	public String toString() {
		if (this.isKing){
	        return this.color;
	    }
	    else
	    {
	        if (this.color == ".") { 
	        	return this.color;
	        }
	        else {
	        	return this.color.toLowerCase();
	        }
	    }
	}

	/**
	 * Change this checker's color to another color
	 * @param c the color that this checker will be changed to
	 */
	public void changeColor_helper(String c) {
		this.color = c;}

	/**
	 * Internal helper function for get_possible_moves. Students should not use this.
	 * This function handles the move chain if multiple jumps are possible for this checker piece
	 * @param pos_x x coordinate of the checker piece whose move is being explored
	 * @param pos_y y coordinate of the checker piece whose move is being explored
	 * @param multiple_jump a list of the current multiple jump moves found
	 * @param board current state of the board
	 * @param direction current direction to explore in
	 * @param move current move chain being explored
	 * @param selfColor
	 */
	public void binary_tree_traversal(int pos_x,int pos_y,Vector<Vector<Position>> multiple_jump,Board board,Vector<Position> direction,Vector<Position> move, String selfColor)
	{
	    // Check if there is a capture available (flag means no capture)
		boolean flag = true;
		for (int i = 0;i<direction.size();i++) {
			int temp_x = pos_x + direction.get(i).getX();
			int temp_y = pos_y + direction.get(i).getY();
			if((board.isInBoard(temp_x, temp_y)) && board.isInBoard(temp_x + direction.get(i).getX(), temp_y + direction.get(i).getY())
					&& (board.board.get(temp_x).get(temp_y).color == opponent.get(selfColor))
					&& (board.board.get(temp_x + direction.get(i).getX()).get(temp_y+direction.get(i).getY()).color == "."))
			{
				flag = false;
				break;
			}
		}
		if (flag) {
	        if (!move.isEmpty()) {
	            multiple_jump.addElement(new Vector<Position>(move));
	        }
            return;
	    }
		// Recursively does a binary search for further captures
		for(int i = 0;i<direction.size();++i) {
			int temp_x = pos_x + direction.get(i).getX();
			int temp_y = pos_y + direction.get(i).getY();
            if((board.isInBoard(temp_x, temp_y))
                    && (board.board.get(temp_x).get(temp_y).color == opponent.get(selfColor))) {
                if (board.isInBoard(temp_x + direction.get(i).getX(), temp_y + direction.get(i).getY()) &&
                        board.board.get(temp_x + direction.get(i).getX()).get(temp_y + direction.get(i).getY()).color == ".") {
                    Position temptemp = new Position((temp_x + direction.get(i).getX()), (temp_y + direction.get(i).getY()));
                    String backup = board.board.get(temp_x).get(temp_y).color;
                    board.board.get(temp_x).get(temp_y).changeColor_helper(".");
                    move.addElement(temptemp);
                    this.binary_tree_traversal(temptemp.getX(), temptemp.getY(), multiple_jump, board, direction, move, selfColor);
                    move.removeElementAt(move.size() - 1);
                    board.board.get(temp_x).get(temp_y).changeColor_helper(backup);
                }
            }
		}
	    

	}
}



