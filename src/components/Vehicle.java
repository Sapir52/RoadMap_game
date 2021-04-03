package components;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

import AbstractFactory.Factory;
import Mediator.BigBrother;
import Mediator.Moked;
import utilities.Point;
import utilities.Timer;
import utilities.Utilities;
import utilities.VehicleType;


public class Vehicle extends Observable implements Utilities, Timer, Runnable, Cloneable { 
	private int id;
	private VehicleType vehicleType;
	private Route currentRoute;
	private RouteParts currentRoutePart;
	private int timeFromRouteStart;
	public static int objectsCount=1;
	private int timeOnCurrentPart;
	private Road lastRoad;
	private String status;
	private Color color;
	private boolean threadSuspend = false;
	private boolean stop = false;
	private Point point;
	private int speed;
	/**Random Constructor
	 * @param currentLocation
	 */
	public Vehicle (Road currentLocation) {// random constructor
		id=objectsCount++;
		vehicleType=currentLocation.getVehicleTypes()[getRandomInt(0,currentLocation.getVehicleTypes().length-1)];
		successMessage(this.toString());
		currentRoute=new Route(currentLocation, this); //creates a new route for the vehicle and checks it in
		lastRoad=currentLocation;
		status=null;
		color = new Color((int)(Math.random()*200),(int)(Math.random()*200),(int)(Math.random()*200));
		BigBrother.getInstance(); 
		System.out.println("big bro "+BigBrother.getInstance());
		this.point=new Point(currentLocation.getStartJunction().getX(),currentLocation.getEndJunction().getY());
		this.speed=this.getRandomInt(20, (int) (this.vehicleType.getAverageSpeed()*1.5));
	}
	
	public void calcPositionOnRoad() {
		Road road=(Road)currentRoutePart;
		int speed=Math.min(road.getMaxSpeed(), getVehicleType().getAverageSpeed());
		double x1=road.getStartJunction().getX();
		double y1 =road.getStartJunction().getY();
		double x2=road.getEndJunction().getX();
		double y2=road.getEndJunction().getY();
		double distance=timeOnCurrentPart*speed/10;

		double dX= (distance/road.length)*(x2-x1);
		double dY=(distance/road.length)*(y2-y1);
		this.point.setX(x1+dX);
		this.point.setY(y1+dY);
	}
	
	
	public int getObjectsCount() {
		return objectsCount;
	}
	
	public VehicleType getVehicleType() {
		return vehicleType;
	}
	/**
	 * @return the id
	 */
	public int getID() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id=id;
	}
	
	public void setVehicleType(VehicleType type) {
		vehicleType=type;
	}
	/**
	 * @return the vehicleType
	 */
	public Route getCurrentRoute() {
		return currentRoute;
	}
	/**
	 * @param vehicleType the vehicleType to set
	 */
	public void setCurrentRoute(Route newRoute) {
		currentRoute=newRoute;
	}
	/**
	 * @return the currentRoute
	 */
	public RouteParts getCurrentRoutePart() {
		return currentRoutePart;
	}
	/**
	 * @param currentRoute the currentRoute to set
	 */
	public void setCurrentRoutePart(RouteParts newPart) {
		currentRoutePart=newPart;
	}
	/**
	 * @return the timeFromRouteStart
	 */
	public int getTimeFromRouteStart() {
		return timeFromRouteStart;
	}
	/**
	 * @param timeFromRouteStart the timeFromRouteStart to set
	 */
	public void setTimeFromRouteStart(int time) {
		timeFromRouteStart=time;
	}
	/**
	 * @return the timeOnCurrentPart
	 */
	public int getTimeOnCurrentPart () {
		return timeOnCurrentPart;
	}
	/**
	 * @param timeOnCurrentPart the timeOnCurrentPart to set
	 */
	public void setTimeOnCurrentPart(int time) {
		timeOnCurrentPart=time;
	}
	/**
	 * @return the lastRoad
	 */
	public Road getLastRoad( ) {
		return lastRoad;
	}
	/**
	 * @param lastRoad the lastRoad to set
	 */
	public void setLastRoad(Road road) {
		lastRoad=road;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status=status;
	}
	/**
	 * @return the objectsCount
	 */
	@Override
	public void incrementDrivingTime() {
		timeFromRouteStart++;
		timeOnCurrentPart++;
		move();
	}
	/**controls the vehicle moving from one route part to the next one
	 * 
	 */
	public void move() {
		if (currentRoutePart.canLeave(this)) {
			currentRoutePart.checkOut(this);
			currentRoute.findNextPart(this).checkIn(this);
		}
		else {
			currentRoutePart.stayOnCurrentPart(this);
		}
	}
	
	/**
	 * Prints fields of the class
	 */
	@Override
	public String toString() {
		return new String("Vehicle "+id+": "+ getVehicleType().name()+", average speed: "+getVehicleType().getAverageSpeed());
	}
/**
 * 	
 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false; 
	    if (getClass() != obj.getClass()) return false; 
	    if (! super.equals(obj)) return false;
		Vehicle other=(Vehicle)obj;
		if (this.currentRoute!=other.currentRoute||
			this.currentRoutePart!=other.currentRoutePart||
			this.id!=other.id||
			this.lastRoad!=other.lastRoad||
			this.status!=other.status||
			this.timeFromRouteStart!=other.timeFromRouteStart||
			this.timeOnCurrentPart!=other.timeOnCurrentPart||
			this.vehicleType!=other.vehicleType)
				return false;
		return true;
	}

	/**
	 * @param objectsCount the objectsCount to set
	 */
	public static void setObjectsCount(int objectsCount) {
		Vehicle.objectsCount = objectsCount;
	}
	
	private void drawRotetedVehicle(Graphics g, int x1, int y1, int x2, int y2, int d, int h) {
		int delta = 10;
	    int dx = x2 - x1, dy = y2 - y1;
	    double D = Math.sqrt(dx*dx + dy*dy);
	    double xm = delta, xn = xm, ym = h, yn = -h, x;
	    double xm1 = delta + d, xn1 = xm1, ym1 = h, yn1 = -h, xx; 
	    double sin = dy / D, cos = dx / D;

	    x = xm*cos - ym*sin + x1;
	    xx = xm1*cos - ym1*sin + x1;
	    
	    ym = xm*sin + ym*cos + y1;
	    ym1 = xm1*sin + ym1*cos + y1;
	    
	    xm = x;
	    xm1 = xx; 

	    x = xn*cos - yn*sin + x1;
	    xx = xn1*cos - yn1*sin + x1; 
	    
	    yn = xn*sin + yn*cos + y1;
	    yn1 = xn1*sin + yn1*cos + y1;
	    
	    xn = x;
	    xn1 = xx;

	    int[] xpoints = {(int) xm1, (int) xn1,  (int) xn, (int) xm};
	    int[] ypoints = {(int) ym1, (int) yn1, (int) yn, (int) ym};
	    

	    g.fillPolygon(xpoints, ypoints, 4);
	    g.setColor(Color.BLACK);
	    g.fillOval((int) xm1-2,(int) ym1-2,4,4);
	    g.fillOval((int) xn1-2,(int) yn1-2,4,4);
	    g.fillOval((int) xm-2,(int) ym-2,4,4);
	    g.fillOval((int) xn-2,(int) yn-2,4,4);
	    
	}
	
	/**
	 * 
	 * @param g
	 * @param delta
	 * @param colorInd
	 */
	public void drawVehicle(Graphics g, int delta, int colorInd) {
	    if (colorInd == 0)
	    	g.setColor(Color.blue); 
	    else if (colorInd == 1)
	    	g.setColor(Color.magenta);
	    else if (colorInd == 2)
	    	g.setColor(Color.orange);
	    else
	    	g.setColor(color);
	    
		if (currentRoutePart instanceof Junction) {
			Junction junc =(Junction) currentRoutePart;
			g.fillRect((int)junc.getX()+delta-4,(int) junc.getY()+delta-4, 8, 8);	
		    g.setColor(Color.BLACK);
		    g.fillOval((int)junc.getX() +delta-6 ,(int) junc.getY()+delta-6,4,4);
		    g.fillOval((int)junc.getX() +delta-6+8 ,(int) junc.getY()+delta-6,4,4);
		    g.fillOval((int)junc.getX() +delta-6 ,(int) junc.getY()+delta-6+8,4,4);
		    g.fillOval((int)junc.getX() +delta-6+8 ,(int) junc.getY()+delta-6+8,4,4);

		}
		else {
			calcPositionOnRoad();
			
			Road road=(Road) currentRoutePart;
			double x2=road.getEndJunction().getX();
			double y2=road.getEndJunction().getY();
	
			drawRotetedVehicle(g, (int)this.point.getX()+delta, (int)this.point.getY()+delta,  (int) x2+delta,  (int) y2+delta, 10,  4);
			//g.fillRect((int)getX()+delta-4, (int)getY()+delta-4, 8, 8);
		}

	}
	/**
	 * 	
	 * @return point
	 */
	public Point getPoint() {
		return point;
	}
	/**
	 * 
	 * @param point
	 */
	public void setPoint(Point point) {
		this.point = point;
	}
	/**
	 * run function
	 */
	@Override
	public void run() {
		while(true) {
			this.setChanged();
			if(this.hasChanged()) {
				if(this.speed>this.vehicleType.getAverageSpeed()) {
					BigBrother.getInstance().setUpdate(true);
				}
				else
					BigBrother.getInstance().setUpdate(false);
			}

			try {
				Moked.getInstance().sendMessage(this);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.notifyObservers(this);
			try {
				Thread.sleep(600);
				synchronized(this) {
	                while (threadSuspend)
	                	wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (stop) return;
			incrementDrivingTime();
		}
	}
	

	
	/**
	 * 
	 * @return speed
	 */
	public int getSpeed() {
		return speed;
	}
	/**
	 * set Speed
	 */
	public void setSpeed() {
		this.speed=this.getRandomInt(30, (int) (this.vehicleType.getAverageSpeed()*1.5));
	}
	/**
	 * set Suspend
	 */
	@Override
	public void setSuspend() {
		threadSuspend = true;
	}
	/**
	 * 	set Resume
	 */
	@Override
	public synchronized void setResume() {
		threadSuspend = false;
		notify();
	}
	/**
	 * set Stop
	 */
	@Override
	public void setStop() {
		stop = true;
	}
	/**
	 * 	
	 * @param currentLocation
	 * @param sBuilder
	 */
	public Vehicle (Road currentLocation,String sBuilder) {// random constructor
		id=objectsCount++;
		vehicleType=selectType(sBuilder);
		currentRoute=new Route(currentLocation, this); //creates a new route for the vehicle and checks it in
		lastRoad=currentLocation;
		status=null;
		color = new Color((int)(Math.random()*200),(int)(Math.random()*200),(int)(Math.random()*200));
		System.out.println(this.toString());
		BigBrother.getInstance();
		System.out.println("big bro "+BigBrother.getInstance());
		this.point=new Point(currentLocation.getStartJunction().getX(),currentLocation.getStartJunction().getY());
		this.speed=this.getRandomInt(20, (int) (this.vehicleType.getAverageSpeed()*1.5));

	}
	/**
	 * 
	 * @param sBuilder
	 * @return
	 */
	public VehicleType selectType(String sBuilder) {
		String[] type2 = {"fast","slow"};
		String[] type4 = {"private","public","work"};
		String[] type10 = {"public","work"};
		String r2=type2[new Random().nextInt(type2.length)];
		String r4=type4[new Random().nextInt(type4.length)];
		String r10=type10[new Random().nextInt(type10.length)];
		int[] f=  {2,3,4};
		int rf=f[new Random().nextInt(f.length)];
		if(sBuilder.equals("city")) {
			if(rf==10 && !r10.equals("work"))
				vehicleType=Factory.getFactory(10).getVehicle(r10);
			else if(rf==4 && !r4.equals("work"))
				vehicleType=Factory.getFactory(4).getVehicle(r4);
			else
				vehicleType=Factory.getFactory(2).getVehicle(r2);
		}
		else {
			if(rf==2 && !r2.equals("slow"))
				vehicleType=Factory.getFactory(2).getVehicle(r2);
			else if(rf==10 && !r10.equals("public"))
				vehicleType=Factory.getFactory(10).getVehicle(r10);
			else
				vehicleType=Factory.getFactory(4).getVehicle(r4);
		}
		return vehicleType;
	}
	/**
	 * cctor for prototype
	 * @param vehicle
	 */
	
	public Vehicle (Vehicle vehicle) {
		id=vehicle.getID();
		vehicleType=vehicle.vehicleType;
		currentRoute=vehicle.getCurrentRoute();
		lastRoad=vehicle.getLastRoad();
		status=null;
		color = vehicle.color;
	}
	/**
	 * clone function for prototype
	 */
	public Vehicle clone() { 
		return new Vehicle(this.getLastRoad()); 
	} 
	/**
	 * 	
	 * @param r
	 * @param id
	 * @return
	 */
	public Vehicle upgrade(Road r, int id) {
		this.lastRoad=r;
		this.id=id;
		return this;
	}


}
