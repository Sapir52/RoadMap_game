package AbstractFactory;

import utilities.VehicleType;

public class TwoWheels implements IWheel{
	/**
	 *@param y - String y
	 */
	public VehicleType  getVehicle(String y) {	
		if(y.equals("fast")) 
			return VehicleType.motorcycle;
		else //y.equals("slow") 
			return VehicleType.bicycle;
			
		
	}
}
