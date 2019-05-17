import java.util.HashMap;
import java.util.Vector;


public class Checker {
	String color;
    int  row;
    int col;
    static final HashMap<String, String> opponent = new HashMap<String,String> () {{
    	put("W","B");
    	put("B","W");
    	}};
    Boolean isKing;
    
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
    
    public void becomeKing() {
    	this.isKing = true;
    }
	
    public void becomeMan() {
    	this.isKing = false;
    }
    
	public Vector<Move> getPossibleMoves(Board board) {
		Direction direction= new Direction();
	    Vector<Move> result = new Vector<Move>();
	    if (this.color == ".")
	    {
	        return result;
	    }
	    Vector<Vector<Position>> multiple_jump = new Vector<Vector<Position>> ();
	    Board new_board = new Board(board);
	    Vector<Position> explore_direction = direction.at(color);
	    if (isKing)
	    {
	        for  (int i = 0; i<direction.at(opponent.get(color)).size(); i++)
	        {
	            explore_direction.addElement(direction.at(opponent.get(color)).get(i));
	        }
	    }
	    for (int i = 0; i<explore_direction.size();++i) {
	        int pos_x = row+explore_direction.get(i).getX();
	        int pos_y = col+explore_direction.get(i).getY();
	        if (new_board.isInBoard(pos_x,pos_y))
	        {
	            if (new_board.board.get(pos_x).get(pos_y).color == ".")
	            {
	            	Vector<Position> x = new Vector<Position> () {{
	            		addElement(new Position(row,col));
	            		addElement(new Position(pos_x,pos_y));
	            	}};
	                result.addElement(new Move(x));
	            }
	        }
	    }
	    Vector<Position> temp_v=new Vector<Position> ();
	    new_board.board.get(row).get(col).color = ".";
	    binary_tree_traversal(row,col,multiple_jump, new_board, explore_direction, temp_v);
	    if (!multiple_jump.isEmpty())
	    {
	        result.clear();
	    }
	    for(int x = 0; x<multiple_jump.size();x++) {
	    	multiple_jump.get(x).insertElementAt(new Position(row,col),0);
	    	result.addElement(new Move(multiple_jump.get(x)));
	    }
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
	
	public void changeColor_helper(String c) {
		this.color = c;}
	

	public void binary_tree_traversal(int pos_x,int pos_y,Vector<Vector<Position>> multiple_jump,Board board,Vector<Position> direction,Vector<Position> move)
	{
		boolean flag = true;
		for (int i = 0;i<direction.size();i++) {
			int temp_x = pos_x + direction.get(i).getX();
			int temp_y = pos_y + direction.get(i).getY();
			Position temp= new Position(temp_x,temp_y);
			if((board.isInBoard(temp_x, temp_y)) 
					&& (board.board.get(temp_x).get(temp_y).color == opponent.get(this.color)) 
					&& (board.board.get(temp_x + direction.get(i).getX()).get(temp_y+direction.get(i).getY()).color == "."))
			{
				flag = false;
				break;
			}
		}
		if (flag) {
	        if (!move.isEmpty()) {
	            multiple_jump.addElement(move);
	            return;
	        }
	    }
		for(int i = 0;i<direction.size();++i) {
			int temp_x = pos_x + direction.get(i).getX();
			int temp_y = pos_y + direction.get(i).getY();
			Position temp= new Position(temp_x,temp_y);
			if((board.isInBoard(temp_x, temp_y)) 
					&& (board.board.get(temp_x).get(temp_y).color == opponent.get(this.color))) {
				if(board.isInBoard(pos_x+direction.get(i).getX()+direction.get(i).getX(), pos_y+direction.get(i).getY()+direction.get(i).getY()) &&
						board.board.get(pos_x+direction.get(i).getX()+direction.get(i).getX()).get(pos_y+direction.get(i).getY()+direction.get(i).getY()).color==".") {
					Position temptemp= new Position((pos_x+direction.get(i).getX()+direction.get(i).getX()),(pos_y+direction.get(i).getY()+direction.get(i).getY()));
					String backup = board.board.get(pos_x+direction.get(i).getX()).get(direction.get(i).getY()).color;
					board.board.get(pos_x+direction.get(i).getX()).get(pos_y+direction.get(i).getY()).changeColor_helper(".");
					move.addElement(temptemp);
					this.binary_tree_traversal(temptemp.getX(), temptemp.getY(), multiple_jump, board, direction, move);
					move.removeElementAt(move.size()-1);
					board.board.get(pos_x+direction.get(i).getX()).get(pos_y+direction.get(i).getY()).changeColor_helper(backup);
				}
			}
		}
	    

	}
}



