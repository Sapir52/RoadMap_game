package components;

import java.util.ArrayList;

import javax.swing.JPanel;

import utilities.Timer;
import utilities.Utilities;


public class Driving implements Utilities, Timer, Runnable{
	private Map map;
	private ArrayList<Vehicle> vehicles;
	private int drivingTime;
	private ArrayList<Timer> allTimedElements;
	private JPanel panel;
	private boolean threadSuspend = false;
	private boolean stop = false;
	/**Constructor
	 * @param junctionsNum quantity of junctions
	 * @param numOfVehicles quantity of vehicles
	 */
	public Driving(int junctionsNum, int numOfVehicles) {
		vehicles=new ArrayList<Vehicle>();
		allTimedElements=new ArrayList<Timer>();
		drivingTime=0;
		map=new Map(junctionsNum);
		System.out.println("\n================= CREATING VEHICLES =================");
		while(vehicles.size()<numOfVehicles) {
			Road temp=map.getRoads().get(getRandomInt(0,map.getRoads().size()));//random road from the map
			if( temp.getEnabled())
				vehicles.add(new Vehicle(temp));
		
		}
		allTimedElements.addAll(vehicles);
		allTimedElements.addAll(map.getLights());
		Vehicle.objectsCount=1;
	}
	
	/**
	 * 	
	 * @param panel
	 */
	public void setPanel(JPanel panel) {
		this.panel = panel;
	}
	
	/**
	 * @return the map
	 */
	public Map getMap() {
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(Map map) {
		this.map = map;
	}

	/**
	 * @return the vehicles
	 */
	public ArrayList<Vehicle> getVehicles() {
		return vehicles;
	}

	/**
	 * @param vehicles the vehicles to set
	 */
	public void setVehicles(ArrayList<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	/**
	 * @return the drivingTime
	 */
	public int getDrivingTime() {
		return drivingTime;
	}

	/**
	 * @param drivingTime the drivingTime to set
	 */
	public void setDrivingTime(int drivingTime) {
		this.drivingTime = drivingTime;
	}

	/**
	 * @return the allTimedElements
	 */
	public ArrayList<Timer> getAllTimedElements() {
		return allTimedElements;
	}

	/**
	 * @param allTimedElements the allTimedElements to set
	 */
	public void setAllTimedElements(ArrayList<Timer> allTimedElements) {
		this.allTimedElements = allTimedElements;
	}
	/**method runs the game for given quantity of turns
	 * @param turns
	 */
	public void drive(int turns) {
		System.out.println("\n================= START DRIVING=================");

		drivingTime=0;
		for (int i=0; i<turns;i++) {
			incrementDrivingTime();
		}
	}

	@Override
	public void incrementDrivingTime() {
		System.out.println("\n***************TURN "+drivingTime++ +"***************");
		for(Timer element: allTimedElements) {
			System.out.println(element);
			element.incrementDrivingTime();
		}
		
	}
	
	/**
	 * run function of treads
	 */
	@Override
	public void run() {	
	   	for(Timer timer: allTimedElements) {
	   		Thread t = new Thread((Runnable) timer);
	   		t.start();
	   	}
	   	
		while(true) {
	   		try {
				Thread.sleep(100);
				synchronized(this) {
	                while (threadSuspend)
	                	wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	   		if (stop) return;
	   		panel.repaint();
   		}
	}
	

	/**
	 * Prints fields of the class
	 */
	@Override
	public String toString() {
		return "Driving [map=" + map + ", vehicles=" + vehicles + ", drivingTime=" + drivingTime + ", allTimedElements="
				+ allTimedElements + "]";
	}
	/**
	 * set Suspend 
	 */
	@Override
	public synchronized void setSuspend() {
	   	for(Timer timer: allTimedElements) 
	   		timer.setSuspend();
	   	threadSuspend = true;
	}
	/**
	 * set Resume
	 */
	@Override
	public synchronized void setResume() {
	   	for(Timer timer: allTimedElements) 
	   		timer.setResume();
	   	threadSuspend = false;
	   	notify();
	}
	/**
	 * 	set Resume
	 */
	public void setStop() {
		setResume();
	   	for(Timer timer: allTimedElements) 
	   		timer.setStop();
	   	stop = true;
	}
	/**
	 * 
	 * @param junctionsNum
	 * @param numOfVehicles
	 * @param sBuilder
	 */
	public Driving(int junctionsNum, int numOfVehicles, String sBuilder) {

		System.out.println("\n================= CREATING VEHICLES =================");
		vehicles=new ArrayList<Vehicle>();
		allTimedElements=new ArrayList<Timer>();
		drivingTime=0;
		map=new Map(junctionsNum,sBuilder);
		
		if(sBuilder.equals("city")) {
			while(vehicles.size()<numOfVehicles) {
				Road temp=map.getRoads().get(getRandomInt(0,map.getRoads().size()));//random road from the map
				if( temp.getEnabled())
					vehicles.add(new Vehicle(temp,sBuilder));
			}
		}
		else {	
			for(int i=0;i<this.map.getRoads().size() ;i++)
				vehicles.add(new Vehicle(this.map.getRoads().get(i),sBuilder));
		}

		vehicles.toString();
		allTimedElements.addAll(vehicles);
		allTimedElements.addAll(map.getLights());
		Vehicle.objectsCount=1;
	}
}
