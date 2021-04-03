package Factory;

import components.Junction;

public interface IJunction {
	/**
	 * 
	 * @param i
	 * @return
	 */
	public Junction  CreateJunction(int i);
}
