package Mediator;

import java.io.IOException;

import components.Vehicle;

public interface Mediator {
	/**
	 * 
	 * @param vehicle
	 * @throws IOException
	 */
	public void sendMessage(Vehicle vehicle) throws IOException;
}
