package components;
import java.util.ArrayList;

/**Represents the traffic lights with random choice of road that receives a green light */
public class RandomTrafficLights extends TrafficLights {
	/**Constructor
	 * @param roads
	 */
	public RandomTrafficLights(ArrayList<Road> roads) {
		super(roads);
	}
	/**
	 * changeIndex function
	 */
	@Override
	public synchronized void changeIndex() {	
		this.setGreenLightIndex((this.getGreenLightIndex()+getRandomInt(1,200))%this.getRoads().size());//increment index
		
	}
	/**
	 * Prints fields of the class
	 */	
	@Override
	public String toString() {
		return new String("Random "+super.toString());
	}
	
	

}
