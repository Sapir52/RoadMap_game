package Factory;

import components.Junction;
import components.LightedJunction;

public class City implements IJunction{
	/**
	 * @param i - type int
	 */
	@Override
	public Junction CreateJunction(int i) {
		System.out.println("city");
		return new LightedJunction();

	}

}
