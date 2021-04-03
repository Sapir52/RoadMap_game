package Mediator;

import java.util.ArrayList;
import java.util.Observable;

import components.Vehicle;

public class BigBrother extends Observable{
	static private volatile  BigBrother instance=null;
	private boolean flag=false;
	private ArrayList<Integer> reportCount=new ArrayList();
	private int numbR=0;
	/**.
	 * @return singleton instance of the Big Brother
	 */
	public static BigBrother getInstance() {
		if (instance == null)
			synchronized(BigBrother.class){
				if (instance == null) 
					instance = new BigBrother();
			}
		return instance;
	}

	/**
	 * private empty Ctor for  Big Brother
	 */
	private BigBrother() {
	}
	/**
	 * 
	 * @param vehicle
	 * @return
	 */
	public boolean getUpdate(Vehicle vehicle) {
		return flag;
	}
	/**
	 * 	
	 * @param flagVal
	 */
	public void setUpdate(boolean flagVal) {
		flag=flagVal;
	}
	/**
	 * 
	 * @param vehicle
	 * @return numbR number for Report
	 */
	public int Reports(Vehicle vehicle) {
		numbR+=1;
		boolean f=false;
		for(int i=0;i<reportCount.size();i++) {
			if(reportCount.get(i)==numbR) {
				f=true;
				numbR=reportCount.get(reportCount.size()-1);
			}
			numbR+=1;
			reportCount.add(numbR);
			
			
		}
		return numbR;
	}
}
