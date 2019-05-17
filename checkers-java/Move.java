import java.util.Vector;

public class Move {
	Vector<Position> seq;
	
	public Move( Move m )
	{
		this.seq = m.seq;
	}
	
	public Move( Vector<Position> s)
	{
		this.seq = s;
	}

	public Move(String input)
	{
		//int len = input.length();

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
	        seq.addElement(new Position(x,y));
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
