package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.*;

import AbstractFactory.Factory;
import Factory.IJunction;
import Factory.JFactory;
import Mediator.BigBrother;
import components.Map;
import utilities.Timer;
import utilities.Utilities;
import utilities.VehicleType;
import components.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CyclicBarrier;

public class RoadSystemPanel extends JPanel implements ActionListener, Utilities {
   private static final long serialVersionUID = 1L;
   private Main frame;
   private JPanel p1;
   private JButton[] b_num;
   private String[] names = {"Create road system","Start","Stop","Resume","Info"};
   private JScrollPane scrollPane;
   private boolean isTableVisible;
   private Driving driving = null;
   private int colorInd = 0;

private boolean started = false;
   private String [][] data;
   /**
    * constructor
    * @param f
    */
   public RoadSystemPanel(Main f) {
	    frame = f;
	    isTableVisible = false;
	    setBackground(new Color(255,255,255));
	    p1=new JPanel();
		p1.setLayout(new GridLayout(1,7,0,0));
		p1.setBackground(new Color(0,150,255));
		b_num=new JButton[names.length];
		
		for(int i=0;i<names.length;i++) {
		    b_num[i]=new JButton(names[i]);
		    b_num[i].addActionListener(this);
		    b_num[i].setBackground(Color.lightGray);
		    p1.add(b_num[i]);		
		}

		setLayout(new BorderLayout());
		add("South", p1);
   }	
   
   /**
    * 
    * @param junc
    * @param cars
    */
   public void createNewRoadSystem(int junc, int cars) {
	    if (driving !=null)
	    	driving.setStop();
		driving = new Driving(junc,cars);
		driving.setPanel(this); 
		started = false;
		repaint();
   }
   
	/**
	 * @param g- type Graphics
	 */
   public void paintComponent(Graphics g) {
	   	super.paintComponent(g);	
	   	
	   	
	   	if (driving==null) return;
	   	int delta = 10;
   
	   	Map map = driving.getMap();
	   		
	   	for(Road road: map.getRoads()) 
	   		road.drawRoads(g,delta);
	   	
	   	for(Road road: map.getRoads()) 
	   		road.drawRoadGreenLight(g,delta);
	   	
	   	for(Junction junc: map.getJunctions())
	   		junc.drawJunction(g,delta);


		for(Vehicle veh: driving.getVehicles()) {
	   		veh.drawVehicle(g, delta, colorInd);
	   	}

   		frame.getM6().addActionListener(new ActionListener() {

	        public void actionPerformed(ActionEvent e) {
	        		Vehicle v=driving.getVehicles().get(0).clone().upgrade(driving.getMap().getRoads().get(1), 25);
	        		Timer timer=driving.getAllTimedElements().get(1);
			   		Thread t = new Thread((Runnable) timer);
			   		t.start();
	        }


	    });
   		
   		frame.getM7().addActionListener(new ActionListener() {

	        public void actionPerformed(ActionEvent e) {
	    	     viewReports();

	        }



	    });
	
   }
	/**
	 * info table
	 */
	public void info() {
		   if (driving == null) return;
		   if(isTableVisible == false) {
			 int i=0;
			  String[] columnNames = {"Vehicle #", "Type", "Location","Time on loc","Speed"};
			  ArrayList<Vehicle> vehicles = driving.getVehicles();
		      data = new String[vehicles.size()][columnNames.length];
		      for(Vehicle v : vehicles) {
		    	  data[i][0] = ""+v.getID();
		    	  data[i][1] = ""+v.getVehicleType();
		    	  RouteParts rp = v.getCurrentRoutePart();
		    	  if (rp instanceof Road) {
		    		  Road r = (Road) rp;
		    		  data[i][2] = "Road "+r.getStartJunction().getJunctionName()+"-"+r.getEndJunction().getJunctionName();
		    		  data[i][4] = ""+Math.min(r.getMaxSpeed(), v.getVehicleType().getAverageSpeed());
		    	  }
		    	  else {
		    		  Junction j = (Junction) rp;
		    		  data[i][2] = "Junction "+j.getJunctionName();
		    		  data[i][4] = "0";
		    	  }
		    	  
		    	  data[i][3] = ""+v.getTimeOnCurrentPart();
		    	  i++;
		      }
		      
		      JTable table = new JTable(data, columnNames);
		      scrollPane = new JScrollPane(table);
		      scrollPane.setSize(450,table.getRowHeight()*(vehicles.size())+24);
		      add( scrollPane, BorderLayout.CENTER );
		      isTableVisible = true;
		   }
		   else
			   isTableVisible = false;
		   
		   scrollPane.setVisible(isTableVisible);
	       repaint();
	   }
		
	

  
  /**
   *  
   * @param ind
   */
   public void setColorIndex(int ind) {
	   this.colorInd = ind;
	   repaint();
   }
   
   /**
    * 
    * @param num
    */
   public void setBackgr(int num) {
	   switch(num) {
	   case 0:
		   setBackground(new Color(255,255,255));
		   break;
	   case 1:
		   setBackground(new Color(0,150,255));
		   break;

	   }
	   repaint();
   }
   
   
   /**
    * 
    */
   public void add(){
	   CreateRoadSystemlDialog dial = new CreateRoadSystemlDialog(frame,this,"Create road system");
	   dial.setVisible(true);
   }
   
	/**
	 * start function
	 */
   public void start() {
	   if (driving == null || started) return;
	   started = true;
	   Thread t = new Thread(driving);
	   t.start(); 
   }
	/**
	 * resume function
	 */
	public void resume() {
		if (driving == null) return;
		driving.setResume();
   }
	/**
	 * stop function
	 */
 	public void stop() {
 		if (driving == null) return;
	   driving.setSuspend();
   }

 /**
  *   view reports table
  */
   public void viewReports() { 
	   if (driving == null) return;
	   if(isTableVisible == false) {
		 int i=0;
		  String[] columnNames = {"Vehicle #", "Type", "number Reports","speed"};
		  ArrayList<Vehicle> vehicles = driving.getVehicles();
		  String [][] dataR = new String[vehicles.size()][columnNames.length];
	      for(Vehicle v : vehicles) {
	    	  dataR[i][0] = ""+v.getID();
	    	  dataR[i][1] = ""+v.getVehicleType();
	    	  RouteParts rp = v.getCurrentRoutePart();
	    	  if (rp instanceof Road) {
	    		  Road r = (Road) rp;
	    		  dataR[i][2] = ""+BigBrother.getInstance().Reports(v);//""+v.getReports();
	    		  dataR[i][3] = ""+v.getSpeed();
	    	  }
	    	  else {
	    		  dataR[i][2] ="0";
	    		  dataR[i][3] = "0";
	    	  }

	    	  i++;
	      }
	      
	      JTable table = new JTable(dataR, columnNames);
	      scrollPane = new JScrollPane(table);
	      scrollPane.setSize(450,table.getRowHeight()*(vehicles.size())+24);
	      add( scrollPane, BorderLayout.CENTER );
	      isTableVisible = true;
	   }
	   else
		   isTableVisible = false;
	   
	   scrollPane.setVisible(isTableVisible);
       repaint();
   }

	/**
	 * destroy function
	 */
   public void destroy(){  	        
      System.exit(0);
   }
   
   /**
    * @param ActionEvent e
    */
   public void actionPerformed(ActionEvent e) {
	if(e.getSource() == b_num[0]) 
		add();
	else if(e.getSource() == b_num[1]) 
		start();
	else if(e.getSource() == b_num[2])  
		stop();
	else if(e.getSource() == b_num[3])  
		resume(); 
	else if(e.getSource() == b_num[4])  
		info();
   }
	/**
	 * 
	 * @param junc
	 * @param cars
	 * @param s
	 */
   public void createNewRoadSystemBuilder(int junc, int cars, String s) {
	    if (driving !=null)
	    	driving.setStop();
		driving = new Driving(junc,cars,s);
		driving.setPanel(this); 
		started = false;
		repaint();
 }
   /**
    * 
    * @param num
    */
   public void setBuilder(int num) {

	   switch(num) {
	   case 0:
	       this.createNewRoadSystemBuilder(12,this.getRandomInt(2, 10),"city");

		   break;
	   case 1:
	       this.createNewRoadSystemBuilder(6,5,"country");

		   break;

	   }
	   repaint();
   }




}