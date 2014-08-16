package findingLocations;

import java.util.Comparator;

/**
 * This class exists purely for the benefit of the Board class's move method.
 * This class also must make use of the Comparator and Comparable interfaces
 * to be used in the Priority Queue I used for the A* search
 * */
public class Tupple implements Comparator<Tupple>, Comparable<Tupple>{

	public final Node startNode;
	public final Node parentNode;
	public final int length;
	public final int estimate;
	
	/**
	 * CONSTRUCTOR
	 * 
	 * @param startNode The current Node. If the destination and the node match, we're
	 * at the destination - if length <= number of squares the character is allowed to
	 * move, then the corresponding Location can be reached from where the character 
	 * started  
	 * 
	 * @param parentNode The node startNode came from - if necessary, this can be used
	 * to trace an actual shortest path all the way back to the start.
	 * 
	 * @param length How many squares it's taken to get to startNode. If this is
	 * larger than the number of squares a character is allowed to move, it's a FAILURE
	 * (i.e. if roll = 12, but length = 13, startNode - and the location it represents is
	 * OUT OF RANGE).
	 * 
	 * @param estimate The estimated number of squares left to the goal. The Heuristic used
	 * is |startNode.x - destinationNode.x| + |startNode.y - destinationNode.y| (i.e. the 
	 * absolute difference of x and y added together). This is what lets A* search work (IF
	 * it is Admissible and Consistent - it UNDERESTIMATES the remaining cost to the destination
	 * and the estimate gets MORE ACCURATE as it gets closer). To the best of my knowledge, this
	 * Heuristic is admissible and consistent (enough) for my purposes.
	 * */
	public Tupple(Node startNode, Node parentNode, int length, int estimate){
		this.length = length;
		this.estimate = estimate;
		this.startNode = startNode;
		this.parentNode = parentNode;
	}

	@Override
	public int compare(Tupple arg0, Tupple arg1) {
		return (int) Math.round(arg0.estimate - arg1.estimate);
	}
	
	@Override
	public int compareTo(Tupple arg0) {
		return (int) Math.round(this.estimate - arg0.estimate);
	}
}
