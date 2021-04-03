package AbstractFactory;

import utilities.VehicleType;

public class FourWheel implements IWheel {
/**
 *@param y - String y
 */
	public VehicleType  getVehicle(String y) {
		if(y.equals("private"))
			return VehicleType.car;
		else if(y.equals("work")) 
			return VehicleType.truck;
		else // y.equals("public") 
			return VehicleType.bus;
		
	}

}
