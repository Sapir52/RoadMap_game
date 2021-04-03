package components;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import utilities.VehicleType;
public class Road implements RouteParts{
	private final static int[] allowedSpeedOptions= {30,40,50,55,60,70,80,90};
	Junction startJunction;
	Junction endJunction;
	private ArrayList<Vehicle> waitingVehicles;
	private boolean greenLight;
	private int maxSpeed;
	VehicleType [] vehicleTypes;
	double length;
	boolean enable;
	/**
	 * 	
	 * @param start
	 * @param end
	 */
	public Road(Junction start, Junction end) {
		startJunction=start;
		endJunction=end;
		waitingVehicles=new ArrayList<Vehicle>();
		greenLight=false;
		maxSpeed=allowedSpeedOptions[getRandomInt(0,7)];
		
		int numOfTypes=getRandomInt(3,VehicleType.values().length);
		
		vehicleTypes=new VehicleType[numOfTypes];
		VehicleType[] types=VehicleType.values();
		ArrayList <Integer> arr=getRandomIntArray(0,6,numOfTypes);
		
		
		for (int i=0;i<numOfTypes;i++) {
			vehicleTypes[i]=types[arr.get(i)];
		}
		this.getStartJunction().getExitingRoads().add(this);
		this.getEndJunction().getEnteringRoads().add(this);
		
		setLength();
		enable=!(getRandomBoolean()&&getRandomBoolean()&&getRandomBoolean());
		successMessage(this.toString());
	}
	
	/**
	 * 	
	 * @return startJunction - type Junction
	 */
	public Junction getStartJunction() {
		return startJunction;
	}
	/**
	 * 	
	 * @param junc
	 */
	public void setStartJunction(Junction junc) {
		startJunction=junc;
	}
	/**
	 * 	
	 * @return endJunction - type Junction
	 */
	public Junction getEndJunction() {
		return endJunction;
	}
	/**
	 * 	
	 * @param junc
	 */
	public void setEndJunction(Junction junc) {
		endJunction=junc;
	}
	/**
	 * 
	 * @return waitingVehicles- ArrayList<Vehicle>
	 */
	public ArrayList<Vehicle> getWaitingVehicles() {
		return waitingVehicles; 
	}
	/**
	 * 	
	 * @param vehicles
	 */
	public void setWaitingVehicles(ArrayList<Vehicle> vehicles) {
		waitingVehicles=vehicles;
	}
	/**
	 * 
	 * @param vehicle
	 */
	public void addVehicleToWaitingVehicles(Vehicle vehicle) {
		waitingVehicles.add(vehicle);
	}
	/**
	 * 
	 * @param vehicle
	 */
	public void removeVehicleFromWaitingVehicles(Vehicle vehicle) {
		waitingVehicles.remove(vehicle);
	}
	/**
	 * 
	 * @param isGreen
	 */
	public synchronized void setGreenLight(boolean isGreen) {
		this.greenLight=isGreen;
		for (Vehicle vehicle: this.waitingVehicles) {
			synchronized(vehicle) {
				vehicle.notify();
			}
		}
	}
	/**
	 * 
	 * @return greenLight- type boolean
	 */
	public synchronized boolean getGreenLight() {
		return greenLight;
	}
	/**
	 * 
	 * @return vehicleTypes- VehicleType[]
	 */
	public VehicleType[] getVehicleTypes() {
		return vehicleTypes;
	}
	/**
	 * 
	 * @param types
	 */
	public void setVehicleTypes(VehicleType[] types) {
		vehicleTypes=types;
	}
	/**
	 * 
	 * @return enable- type boolean
	 */
	public boolean getEnabled() {
		return enable;
	}
	/**
	 * 
	 * @param enable
	 */
	public void setEnabled(boolean enable) {
		this.enable=enable;
	}
	/**
	 * set length
	 */
	public void setLength() {
		length=calcLength();
	}
	/**
	 * 
	 * @return
	 */
	public double calcLength() {
		return startJunction.calcDistance(endJunction);
	}
	/**
	 * 
	 * @return maxSpeed- int maxSpeed
	 */
	public int getMaxSpeed() {
		return maxSpeed;
	}
	/**
	 * 
	 * @param speed
	 */
	public void setMaxSpeed(int speed) {
		maxSpeed=speed;
	}

	/**
	 * @param obj - type Object
	 */
	@Override
	public double calcEstimatedTime(Object obj) {
		Vehicle v=(Vehicle)obj;
		int speed=Math.min(maxSpeed, v.getVehicleType().getAverageSpeed());
		return (int)length/speed;
	}

	/**
	 * @param vehicle- type Vehicle
	 */
	@Override
	public RouteParts findNextPart(Vehicle vehicle) {
		return endJunction;
	}
	/**
	 * @param vehicle- type Vehicle
	 */
	@Override
	public synchronized void checkIn(Vehicle vehicle) {
		vehicle.setCurrentRoutePart(this);
		vehicle.setTimeOnCurrentPart(0);
		vehicle.setLastRoad(this);
		System.out.println("- is starting to move on "+ this + ", time to finish: " + calcEstimatedTime(vehicle)+ ".");
	}
	/**
	 *@param vehicle- type Vehicle 
	 */
	@Override
	public void stayOnCurrentPart(Vehicle vehicle) {
		System.out.println("- "+ this + ", time to arrive: "+ (calcEstimatedTime(vehicle)-vehicle.getTimeOnCurrentPart()));
	}
	/**
	 * @param vehicle- type Vehicle 
	 */
	@Override
	public synchronized void checkOut(Vehicle vehicle) {
		System.out.println("- has finished "+ this+ ", time spent on the road: "+vehicle.getTimeOnCurrentPart()+".");
		addVehicleToWaitingVehicles(vehicle);
		
	}
	/**
	 * Prints fields of the class
	 */
	@Override
	public String toString() {
		return new String("Road from "+getStartJunction()+" to "+getEndJunction()+ ", length: "+ (int)length+ ", max speed "+this.maxSpeed);
	}
	/**
	 * @param other- type Object
	 */
	@Override
	public boolean equals(Object other) {
		
		if (other == null) return false; 
	    if (getClass() != other.getClass()) return false; 
	    if (! super.equals(other)) return false;
	    Road otherRoad=(Road)other;
	    if (this.enable!=otherRoad.enable || 
	    	!this.endJunction.equals(otherRoad.endJunction) ||
	    	this.length!=otherRoad.length ||
	    	this.maxSpeed!=otherRoad.maxSpeed||
	    	this.startJunction!=otherRoad.startJunction||
	    	this.vehicleTypes!=otherRoad.vehicleTypes //compare by reference 
	    	) return false;
	    return true;
		}

	/**
	 *  @param other- type Objec
	 */
	@Override
	public boolean canLeave(Vehicle vehicle) {
		/*if (calcEstimatedTime(vehicle)-vehicle.getTimeOnCurrentPart()>0){
			vehicle.setStatus(new String("is still moving on "));
			return false;
		}*/
		vehicle.calcPositionOnRoad();
		if(vehicle.getPoint().calcDistance(this.endJunction)>14)
			return false;
		return true;
	}


	/**
	 * @return the length
	 */
	public double getLength() {
		return length;
	}


	/**
	 * @param length the length to set
	 */
	public void setLength(double length) {
		this.length = length;
	}


	/**
	 * @return the enable
	 */
	public boolean isEnable() {
		return enable;
	}


	/**
	 * @param enable the enable to set
	 */
	public void setEnable(boolean enable) {
		this.enable = enable;
	}


	/**
	 * @return the allowedspeedoptions
	 */
	public static int[] getAllowedspeedoptions() {
		return allowedSpeedOptions;
	}

	/**
	 * @param g- type Graphics
	 * @papam delta- type int
	 */
	public void drawRoads(Graphics g, int delta) {
		//if (!enable) return;
		g.setColor(Color.black); 
		g.drawLine((int) startJunction.getX()+delta, (int) startJunction.getY()+delta,(int) endJunction.getX()+delta, (int) endJunction.getY()+delta);
		//drawArrowLine(g,(int) startJunction.getX()+delta, (int) startJunction.getY()+delta,(int) endJunction.getX()+delta, (int) endJunction.getY()+delta,25,3);
	}	
	/**
	 * @param g- type Graphics
	 * @papam delta- type int
	 */
	
	public void drawRoadGreenLight(Graphics g, int delta) {
		//if (!enable) return;
		g.setColor(Color.black); 
		//g.drawLine((int) startJunction.getX()+delta, (int) startJunction.getY()+delta,(int) endJunction.getX()+delta, (int) endJunction.getY()+delta);
		if (this.greenLight)
			drawArrowLine(g,(int) startJunction.getX()+delta, (int) startJunction.getY()+delta,(int) endJunction.getX()+delta, (int) endJunction.getY()+delta,28,3);
	}
	/**
	 * 
	 * @param g
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param d
	 * @param h
	 */
	private void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2, int d, int h) {
		    int dx = x2 - x1, dy = y2 - y1;
		    double D = Math.sqrt(dx*dx + dy*dy);
		    double xm = D - d, xn = xm, ym = h, yn = -h, x;
		    double sin = dy / D, cos = dx / D;

		    x = xm*cos - ym*sin + x1;
		    ym = xm*sin + ym*cos + y1;
		    xm = x;

		    x = xn*cos - yn*sin + x1;
		    yn = xn*sin + yn*cos + y1;
		    xn = x;

		    int[] xpoints = {x2, (int) xm, (int) xn};
		    int[] ypoints = {y2, (int) ym, (int) yn};

		    //g.drawLine(x1, y1, x2, y2);
		    
		    g.setColor(Color.green);
		    g.fillPolygon(xpoints, ypoints, 3);
	}
	
}
