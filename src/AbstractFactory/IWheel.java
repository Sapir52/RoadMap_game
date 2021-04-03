package AbstractFactory;

import utilities.VehicleType;

public interface IWheel {
	/**
	 * 
	 * @param string
	 * @return VehicleType
	 */
	public VehicleType getVehicle(String string);
}
