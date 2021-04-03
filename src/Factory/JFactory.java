package Factory;

public class JFactory {
	/**
	 * 
	 * @param x
	 * @return
	 */
    public IJunction getJunction(String x) { 
    	IJunction junc = null; 
       // based on logic factory instantiates an object 
       if ("city".equals(x))  junc = new City(); 
       else if ("country".equals(x))  junc = new Country(); 
       return junc; 
    } 

}
