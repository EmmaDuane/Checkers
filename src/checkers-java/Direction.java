import java.util.HashMap;
import java.util.Vector;

public class Direction {
    
    
    public final HashMap<String, Vector<Position>> list = new HashMap<String, Vector<Position>> () 
    {{
    	put("W", new Vector<Position> () {{
    										addElement(new Position(-1,-1)); 
    										addElement(new Position(-1,1));
    									}});
    	put("B", new Vector<Position> () {{
											addElement(new Position(1,-1)); 
											addElement(new Position(1,1));
										}});
    }};
    
    public Vector<Position> at(String index)
    {
    	return list.get(index);
    }
    
    public Direction()
    {
    }
}
