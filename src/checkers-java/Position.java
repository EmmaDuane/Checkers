
public class Position {
    int x;
    int y;
    public Position(int x,int y)
    {
    	this.x = x;
    	this.y = y;
    }
    public int getX()
    {
    	return this.x;
    }
    
    public int getY()
    {
    	return this.y;
    }
    
    public boolean equals(Position lp)
    {
    	return (lp.getX() == this.x && lp.getY() == this.y);
    }
    
    public boolean lessThan(Position rp)
    {
    	 if (x < rp.x)
    	    {
    	        return true;
    	    }
    	    else if (x == rp.x)
    	    {
    	        if (y <  rp.y)
    	        {
    	            return true;
    	        }
    	        else
    	        {
    	            return false;
    	        }
    	    }
    	    else
    	    {
    	        return false;
    	    }
    }
}
