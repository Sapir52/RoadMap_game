package AbstractFactory;

import components.Vehicle;
import utilities.VehicleType;

public class Factory implements WheelFactory<IWheel>{
	/**
	 * 
	 * @param x
	 * @return  type IWheel
	 */
	public static IWheel  getFactory(int x) {
		if(x==2) 
			return new TwoWheels();
		else if(x==4)
			return new FourWheel();
		else //if(x==10)
			return new TenWheels();
		

	}
	
}
