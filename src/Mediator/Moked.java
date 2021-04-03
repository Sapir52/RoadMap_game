package Mediator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import components.Vehicle;

public class Moked implements Mediator{
	static private volatile  Moked instance=null;

	/**.
	 * @return singleton instance of the Big Brother
	 */
	public static Moked getInstance() {
		if (instance == null)
			synchronized(Moked.class){
				if (instance == null) 
					instance = new Moked();
			}
		return instance;
	}


	/**
	 * private empty Ctor for Moked
	 */
	private Moked() {
	}

	/**
	 * vehicle- type Vehicle
	 */
	@Override
	public void sendMessage(Vehicle vehicle) throws IOException {
		Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
	    if(BigBrother.getInstance().getUpdate(vehicle)==true) {
	    		System.out.println(vehicle.getID()+" "+vehicle.getVehicleType()+" "+BigBrother.getInstance().Reports(vehicle)+ "  "+ ft.format(dNow));
	    	    File file = new File("reports.txt");
	    	    FileWriter fr = new FileWriter(file, true);
	    	    fr.write(vehicle.getID()+" "+vehicle.getVehicleType()+" "+BigBrother.getInstance().Reports(vehicle)+ "  "+ ft.format(dNow));
	    	    fr.close();
	    }

	}





}
