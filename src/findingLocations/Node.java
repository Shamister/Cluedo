package findingLocations;

import gameObjects.Location;

/**
 * This class is used to help the Board find locations characters can move to.
 * I *COULD HAVE* used the Location class for this, but then I would need to add
 * all the parameters to it, which would have made incohesive.
 * */
public class Node {

	public final int x, y;
	private boolean visited = false;
	private int costToHere = -1;		// The cost from Nodes before this Node, to this Node
    private int costToNeigh = 1;		// The cost to the Node's neighbours - ALWAYS = 1
    private int totalCostToGoal = -1;	// costToNeigh + estimateToGoal
    private int estimateToGoal = -1;	// An estimate from this Node to the target ==> Math.abs(x - targetX) + Math.abs(y - targetY)
    private Node parentNode = null;	
    private Location location;			
    
    /**
     * CONSTRUCTOR
     * 
     * @param x, y The x and y coordinates of the Node - this is used to
     * help represent the location the node represents WITHOUT constantly
     * getting the location itself (mainly to save space).
     * 
     * @param visited This is to let the A* Search algorithm know that this
     * node has already been seen, so that it doesn't waste time trying to 
     * visit it again.
     * 
     * @param location The actual location this Node represents - this is
     * to help the node find its neighbours to continue the search. 
     * */
	public Node(int x, int y, boolean visited, Location location) {
		this.x = x;
		this.y = y;
		this.visited = visited;
		setLocation(location);		
	}
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Node getParentNode() {
		return parentNode;
	}

	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

	public boolean isVisited(){
		return visited;
	}
	
	public void setVisited(boolean b){
		visited = b;
	}

	public int getCostToHere() {
		return costToHere;
	}

	public void setCostToHere(int costToHere) {
		this.costToHere = costToHere;
	}

	public int getCostToNeigh() {
		return costToNeigh;
	}

	public void setCostToNeigh(int costToNeigh) {
		this.costToNeigh = costToNeigh;
	}

	public int getTotalCostToGoal() {
		return totalCostToGoal;
	}

	public void setTotalCostToGoal(int totalCostToGoal) {
		this.totalCostToGoal = totalCostToGoal;
	}

	public int getEstimateToGoal() {
		return estimateToGoal;
	}

	public void setEstimateToGoal(int estimateToGoal) {
		this.estimateToGoal = estimateToGoal;
	}
}
