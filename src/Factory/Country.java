package Factory;

import components.Junction;
import components.LightedJunction;

public class Country implements IJunction{
	/**
	 * @param i - type int
	 */
	@Override
	public Junction CreateJunction(int i) {
		System.out.println("country");

		if(i%2==0)
			return new LightedJunction();
		else
			return new Junction();
		

		
	}

}
