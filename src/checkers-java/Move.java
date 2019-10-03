import java.util.Vector;

/*
This module has the Move Class which is the class which handles moves on the board.

We are following the javadoc docstring format which is:
@param Tag Describes the input parameters of the function
@return Description of what the function returns
@throws Tag Describes the errors this function can raise
 */

/**
 * This class is used to describe the moves being made on the board.
 */
public class Move {
	Vector<Position> seq = new Vector<Position>();
	public boolean isCapture = false;

	/**
	 * Initializes Move Object
	 * @param m a sequence of position that the checker pieces will take during the execution of this move
	 *               |  |
	 *             --------
	 *               | X|
	 *             --------
	 *               |  |
	 *             --------
	 *               | X|
	 *             ________
	 *             O |  |
	 *
	 *         In the example above, m should be [(0,0),(2,2),(0,4)]
	 */
	public Move( Move m )
	{
		this.seq = m.seq;
	}

	/**
	 * Initializes Move Object
	 * @param s a sequence of position that the checker pieces will take during the execution of this move
	 */
	public Move( Vector<Position> s)
	{
		this.seq = s;
	}

	/**
	 * Initializes Move Object
	 * @param input a sequence of position (in String format) that the checker pieces will take during the execution of this move
	 *              a string example would be "0, 0 - 2, 2 - 0, 4", given the example above
	 */
	public Move(String input)
	{
		//int len = input.length();
		if(input.equals("-1"))
		{
			seq = new Vector<Position>();
		}else{
	    String delimiter = "-";
	    String[] points = input.split(delimiter);
	    for (int i = 0; i<points.length;++i)
	    {
	        String point = points[i];
	        String result = new String();
	        for (int j = 1; j<point.length()-1;++j)
	        {
	            result = result + point.charAt(j);
	        }
	        String[] xy = result.split(",");
	        int x = Integer.parseInt(xy[0]);
	        int y = Integer.parseInt(xy[1]);
	        Position to_add = new Position(x,y);
	        seq.addElement(to_add);
	    }
		}
	}

	public Vector<String> split(String input,String delimiter) {
	    Vector<String> result = new Vector<String> ();
	    int pos = 0;
	    String token;
	    pos = input.indexOf(delimiter);
	    while (pos != -1)
	    {
	        token = input.substring(0, pos);
	        result.addElement(token);
	        input.replace(input.substring(0, pos+delimiter.length()), "");
		    pos = input.indexOf(delimiter);
	    }
	    result.addElement(input);
	    return result;
	}

	/**
	 * Generates a string for this Move object
	 * @return a string that represents this Move object
	 */
	public String toString()
	{
	    String result = new String();
	    for (int i = 0;i<seq.size();++i)
	    {
	        result += "("+Integer.toString(seq.elementAt(i).getX())+","+Integer.toString(seq.elementAt(i).getY())+")";
	        if (i != seq.size()-1)
	        {
	            result += "-";
	        }
	    }
	    return result;
	}
}
