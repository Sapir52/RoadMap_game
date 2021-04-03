package AbstractFactory;

import utilities.VehicleType;

public class TenWheels implements IWheel{
	/**
	 *@param y - String y
	 */
	public VehicleType  getVehicle(String y) {
		if(y.equals("public")) 
			return VehicleType.tram;
		else //y.equals("work")
			return VehicleType.semitrailer;
			
		
	}
}
