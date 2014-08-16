package ui;

import findingLocations.Node;
import findingLocations.Tupple;
import gameObjects.Character;
import gameObjects.Envelope;
import gameObjects.Location;
import gameObjects.NPC;
import gameObjects.Piece;
import gameObjects.CARDS.CardBuilder;
import gameObjects.ROOMS.Room;
import gameObjects.ROOMS.RoomBuilder;
import gameObjects.WEAPONS.Weapon;
import gameObjects.WEAPONS.WeaponBuilder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * This is where the game takes place, so this is where I'll put the logic. Hopefully, this
 * will marry up nicely with Wendell's code.
 * 
 * Number of characters MUST be decided before cardBuilder is constructed, otherwise 
 * cardBuilder's dealCards() method will break.
 *  
 * */
public class Board {

	public static final int BOARD_WIDTH = 24;	
	public static final int BOARD_HEIGHT = 25;
	public static final int TILE_SIZE = 28;
	
	private List<Piece> piece = new ArrayList<>();
	private List<Character> characters = new ArrayList<>();
	private List<Weapon> weaponry;
	private List<Room> rooms;
	private Envelope envelope;
	private Location[][] spaces = new Location[BOARD_WIDTH][BOARD_HEIGHT];
	private Location[] startSpaces = new Location[6]; /* 0 = Miss Scarlett
	// 1 = Colonel Mustard
	// 2 = Mrs. White
	// 3 = The Reverend Green
	// 4 = Mrs. Peacock
	// 5 = Professor Plum */
	
	String[][] boardRep;

	// Color fields here
	Color roomColor = new Color(0, 121, 200);
	Color wallColor = new Color(117, 76, 36);
	Color lineColor = new Color(40, 40, 40);
	Color pointerColor = Color.BLACK;

	// background image
	BufferedImage bg;

	private Location oldSelectedSpace = null;
	private Location selectedSpace = null;
	private int movementAllowed = 0;
	
	// This list is for the purpose of drawing the spaces a Character may move to.
	private List<Location> goodMoves = new ArrayList<>(); 
	
	// DIMENSIONS ARE 24 x 25

	/**
	 * Cheers to Wendell for this VASTLY, SUPER CONDENSED Constructor :)
	 * @param characters 
	 * */
	public Board(List<Character> characters){
		setUpLocations();
		init(characters);
	}
	
	/**
	 * ESPECIALLY Cheers to Wendell for the removal of the Builder fields I had :D
	 * @param characters 
	 * */
	public void init(List<Character> characters) {
		this.characters = characters;
		
		//set up a list of numbers to resemble tokens...
		List<Integer> tokens = new ArrayList<>();
		for (int i = 1; i <= 6; i++){
			tokens.add(i);
		}
		//... remove any token numbers that are taken by Players...
		for (Character c : this.characters){
			tokens.remove((Integer)c.token);
			piece.add(c);
		}
		//... and the rest of the token numbers become NPCs
		for (Integer i : tokens)
			piece.add(new NPC(i, startSpaces[i-1]));

		Collections.sort(piece, new Comparator<Piece>() {

			@Override
			public int compare(Piece arg0, Piece arg1) {
				return arg0.getToken() - arg1.getToken();
			}
		});
		
		for (int i = 0; i < 6; i++)
			startSpaces[i].setAccessible(false);
		// -------------------------------------------
		rooms = new RoomBuilder(spaces).getRooms();
		envelope = new CardBuilder(characters).getChosenCards();
		weaponry = new WeaponBuilder().getWeapons();
		for (int i = 0; i < weaponry.size(); i++){
			weaponry.get(i).setRoom(rooms.get(i));
			rooms.get(i).setWeapon(weaponry.get(i));
		}
		
		// load image
		try {
			bg = ImageIO.read(this.getClass().getResource(Main.imagePath + "board/bg.jpg"));
		} catch (IOException e) {}
		
		/*
		 * 14/08/2014 - 15:09 (3:09 pm)
		 * This was a good idea, but if this isn't in the draw method (it is now, I moved it),
		 * then the board would NEVER CHANGE. I have preserved this code and moved it into the draw 
		 * method.
		 * 
		 * 	// get string of board representation
			boardRep = new String[BOARD_HEIGHT][BOARD_WIDTH];
			Scanner sc = new Scanner(toString());
	
			for (int j = 0; j < BOARD_HEIGHT; j++) {
				for (int i = 0; i < BOARD_WIDTH; i++) {
					boardRep[j][i] = sc.next();
				}
			}
			sc.close();
		 * */
	}
		
	/**
	 * DESELECT all Locations,
	 * SELECT the clicked Location
	 * */
	public void selectSquare(int xl, int yl){
		if (selectedSpace != null)
			selectedSpace.setSelected(false);
		// convert x and y into "spaces safe numbers"
		// FOR EXAMPLE:
		// 400 / 22 = 18, 	(ROUNDED DOWN)
		// 500 / 22 = 22, 	(ROUNDED DOWN)
		// 400, 500 = 18, 22(ROUNDED DOWN)
		spaces[xl/TILE_SIZE][yl/TILE_SIZE].setSelected(true);
		selectedSpace = spaces[xl/TILE_SIZE][yl/TILE_SIZE];
	}

	private void setUpLocations() {
		//Create Locations
		for (int x = 0; x < BOARD_WIDTH; x++){
			for (int y = 0; y < BOARD_HEIGHT; y++){
				spaces[x][y] = new Location(x, y, true);
			}
		}
		//Make the whole outside inaccessible
		for (int x = 0; x < spaces.length; x++){
			spaces[x][0].setAccessible(false);
			if (x == 10 || x == 11 || x == 12 || x == 13 || x == 14){
				spaces[x][10].setAccessible(false);
				spaces[x][11].setAccessible(false);
				spaces[x][12].setAccessible(false);
				spaces[x][13].setAccessible(false);
				spaces[x][14].setAccessible(false);
				spaces[x][15].setAccessible(false);
				spaces[x][16].setAccessible(false);
			}
			spaces[x][24].setAccessible(false);
		}
		for (int y = 0; y < spaces[0].length; y++){
			spaces[0][y].setAccessible(false);
			spaces[23][y].setAccessible(false);
		}
		spaces[6][1].setAccessible(false);
		spaces[17][1].setAccessible(false);
		//Fix the few squares that SHOULD be accessible
		spaces[0][7].setAccessible(true);
		spaces[0][17].setAccessible(true);
		spaces[7][24].setAccessible(true);
		spaces[9][0].setAccessible(true);
		spaces[14][0].setAccessible(true);
		spaces[16][24].setAccessible(true);
		spaces[23][6].setAccessible(true);
		spaces[23][19].setAccessible(true);
		//Set up the start spaces
		startSpaces[0] = spaces[7][24];	// Miss Scarlett's Start Point
		startSpaces[1] = spaces[0][17];	// Colonel Mustards's Start Point
		startSpaces[2] = spaces[9][0];	// Mrs White's Start Point
		startSpaces[3] = spaces[14][0];	// The Reverend Green Start Point
		startSpaces[4] = spaces[23][6];	// Mrs. Peacock's Start Point
		startSpaces[5] = spaces[23][19];// Professor Plum's Start Point
	}

	public Location getStart(int i) {
		return startSpaces[i-1];
	}
	
	public List<Character> getCharacters() {
		return characters;
	}

	public List<Piece> getPiece() {
		return piece;
	}

	public void setCharacters(List<Character> c) {
		if (characters.isEmpty() || c.isEmpty())
		characters = c;
	}
	
	public List<Weapon> getWeaponry() {
		return weaponry;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public Envelope getEnvelope() {
		return envelope;
	}

	public Location getSelectedSpace() {
		return selectedSpace;
	}

	public int getMovementAllowed() {
		return movementAllowed;
	}

	public void setMovementAllowed(int movementAllowed) {
		this.movementAllowed = movementAllowed;
	}
	
	/**
	 * Checks if the Location the player wants to move to is
	 * in the list of Locations given by getMoves(Character c, int num)
	 * @param c The Character to be moved.
	 * @param loc The Location the character wants to move to.
	 * @param num The number of spaces the Character can move.
	 * */
	public void move(Character c, Location loc, int num){
		List<Location> list = getMoves(c, num);
		if (list.contains(loc)){
			int value = JOptionPane.showConfirmDialog(null, "Do you want to move here?", "Move Confirmation", JOptionPane.YES_NO_OPTION);
			if (value == 0){
				c.setPosition(loc);
				c.setSuggestionMade(false);
			}
		}
	}

	/**
	 * Checks if the Location the player wants to move to is
	 * in the list of Locations given by the list of Locations 
	 * @param c The Character to be moved.
	 * @param loc The Location the character wants to move to.
	 * @param num The number of spaces the Character can move.
	 * */
	public void moveFromRoom(Character c, Location loc, int num, List<Location> list2){
		if (list2.contains(loc)){	
			int value = JOptionPane.showConfirmDialog(null, "Do you want to move here?", "Move Confirmation", JOptionPane.YES_NO_OPTION);
			if (value == 0){
				c.setPosition(loc);
				for (Location l : c.getRoom().doors)
					l.setAccessible(true);
				c.setRoom(null);
				c.setInRoom(false);
				c.setSuggestionMade(false);
			}
		}
	}
	
	/**
	 * This method is only used to FORCE goodMoves to show something - useful
	 * for getting lists and adding them together.
	 * */
	public List<Location> getValidMoves(){
		return goodMoves;
	}
	
	/**
	 * This method is only used to FORCE goodMoves to show something - useful
	 * for getting lists and adding them together.
	 * */
	public void setValidMoves(List<Location> locs){
		goodMoves = locs;
	}
	
	/**
	 * A purely cosmetic method - The range of moves is drawn out on the board,
	 * so calling this is necessary to make it disappear.
	 * */
	public void clearValidMoves(){
		goodMoves.clear();
	}
	
	/**
	 * Finds the space outside the room's door/s.
	 * HOWEVER, the space is only added to the returned
	 * list if it is ACCESSIBLE
	 * */
	public List<Location> findClearExits(Room room) {
		List<Location> viableExits = new ArrayList<>();
		for (Location loc : room.doors){
			List<Location> possibilities = loc.getNeighbours(spaces);
			for (Location l : possibilities){
				if (l.getPartOfRoom() == null)
					viableExits.add(l);
			}
		}
		return viableExits;
	}
	
	/**
	 * Returns the list of Locations the given Character can move to 
	 * given the NUMber of spaces and land on a LEGAL (i.e. ACCESSIBLE) square
	 * @param c The Character to be moved.
	 * @param num The number of spaces the Character can move.
	 * @return A list of Locations the Character can move to.
	 * */
	public List<Location> getMoves(Character c, int num){
		List<Location> potentialLocations = new ArrayList<>();
		for (int x = c.getPosition().x - num; x <= c.getPosition().x + num; x++) {
			for (int y = c.getPosition().y - num; y <= c.getPosition().y + num; y++) {
				if (x >= 0 && y >= 0 && x < BOARD_WIDTH && y < BOARD_HEIGHT && spaces[x][y].getAccessible())
					if (Math.abs(x - c.getPosition().x) + Math.abs(y - c.getPosition().y) <= num)
						potentialLocations.add(spaces[x][y]);
			}
		}
		potentialLocations = searchRoutes(c.getPosition(), num, potentialLocations);
		goodMoves = potentialLocations;
		return potentialLocations;
	}

	/**
	 * Returns the list of Locations within range of the given NUMber of spaces.
	 * An alternative to the getMoves Method above.
	 * HOWEVER, will NOT change goodMoves - this must be done elsewhere somehow.
	 * @param c The Character to be moved.
	 * @param num The number of spaces the Character can move.
	 * @return A list of Locations the Character can move to.
	 * */
	public List<Location> getMoves(Location l, int num){
		List<Location> potentialLocations = new ArrayList<>();
		for (int x = l.x-num; x <= l.x+num; x++){
			for (int y = l.y-num; y <= l.y+num; y++){
				if (x >= 0 && y >= 0 && x < BOARD_WIDTH && y < BOARD_HEIGHT && spaces[x][y].getAccessible())
					if (Math.abs(x-l.x) + Math.abs(y-l.y) <= num)
						potentialLocations.add(spaces[x][y]);
			}
		}
		potentialLocations = searchRoutes(l, num, potentialLocations);
		return potentialLocations;
	}
	
	/**
	 * Goes through the list of potential locations to make sure it is reachable in the number of moves given.
	 * @param start The starting Location.
	 * @param num The number of spaces the Character can move.
	 * @param potentialLocations The list of all Locations the player can move to if unimpeded by obstacles.
	 * @return A list of Locations the Character can move to.
	 * */
	private List<Location> searchRoutes(Location start, int num, List<Location> potentialLocations) {
		List<Location> goodLocations = new ArrayList<>();
		for (Location l : potentialLocations){
			if (searchForSpecificRoute(start, num, l))
				goodLocations.add(l);
		}
		return goodLocations;
	}

	/**
	 * Attempts to reach a location from the starting location within the number of moves.
	 * Uses A* search.
	 * @param start The starting Location.
	 * @param num The number of spaces the Character can move.
	 * @param target The desired location.
	 * @return true if the target can be reached whilst avoiding obstacles and moving only num spaces or less.
	 * */
	private boolean searchForSpecificRoute(Location start, int num, Location target) {
		for (int y = 0; y < BOARD_HEIGHT; y++){
			for (int x = 0; x < BOARD_WIDTH; x++){
				spaces[x][y].getNode().setVisited(false);
				spaces[x][y].getNode().setCostToHere(-1);
				spaces[x][y].getNode().setCostToNeigh(1);
				spaces[x][y].getNode().setTotalCostToGoal(-1);
				spaces[x][y].getNode().setEstimateToGoal(-1);
			}
		}
		PriorityQueue<Tupple> fringe = new PriorityQueue<>();
		fringe.offer(new Tupple(start.getNode(), target.getNode(), 0, Math.abs(start.x-target.x) + Math.abs(start.y-target.y)));
		while (!fringe.isEmpty()){
			Tupple tupple = fringe.poll();
			if  (!tupple.startNode.isVisited()) {
				tupple.startNode.setVisited(true);
				tupple.startNode.setParentNode(tupple.parentNode);
				tupple.startNode.setCostToHere(tupple.length);
				if (tupple.startNode.getLocation().equals(target) && tupple.startNode.getCostToHere() <= num) 		{	return true;	}
				else if (tupple.startNode.getLocation().equals(target) && tupple.startNode.getCostToHere() > num) 	{	return false;	}
				for (Location l : tupple.startNode.getLocation().getNeighbours(spaces)){
					Node n = l.getNode();
					if (!n.isVisited()){
						tupple.startNode.setCostToNeigh(tupple.startNode.getCostToHere() + 1);
						int estimateToGoal = Math.abs(tupple.startNode.x - target.x) + Math.abs(tupple.startNode.y - target.y);
						tupple.startNode.setEstimateToGoal(estimateToGoal);
						fringe.offer(new Tupple(n, tupple.startNode, tupple.startNode.getCostToNeigh(), tupple.startNode.getEstimateToGoal()));
					}
				}
			}
		}
		return false;
	}

	public void movePointer(Graphics g, int x1, int y1, int x2, int y2) {

	}
	
	public void draw(Graphics g) {
		// draw background
		if (bg != null) {
			g.drawImage(bg, 0, 0, BOARD_WIDTH * TILE_SIZE, BOARD_HEIGHT
					* TILE_SIZE, null);
		}
		// get string of board representation
				boardRep = new String[BOARD_HEIGHT][BOARD_WIDTH];
				Scanner sc = new Scanner(toString());

				for (int j = 0; j < BOARD_HEIGHT; j++) {
					for (int i = 0; i < BOARD_WIDTH; i++) {
						boardRep[j][i] = sc.next();
					}
				}
				sc.close();
		
		Color oldColour = g.getColor();
		
		for (int y = 0; y < Board.BOARD_HEIGHT; y++) {
			for (int x = 0; x < Board.BOARD_WIDTH; x++) {
				String t = boardRep[y][x];
				if (t.equals("X")) {
					g.setColor(wallColor);
					g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE,
							TILE_SIZE);
				} else if (t.equals("-")) {
					// leave it blank
				} else if (t.equals("*")) {
					g.setColor(new Color((float) 0, (float) 1, (float) 0, (float) 0.5));
					g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE,
							TILE_SIZE);
				} else if (t.equals("k") || t.equals("b") || t.equals("c")
						|| t.equals("d") || t.equals("l") || t.equals("h")
						|| t.equals("s")) {
					g.setColor(roomColor);
					g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE,
							TILE_SIZE);
				} else if (t.equals("D")) {
					g.setColor(Color.RED);
					g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE,
							TILE_SIZE);
				} else if (t.equals("E")) {
					g.setColor(new Color(255, 128, 128));
					g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE,
							TILE_SIZE);
				} else {
					if (piece.get(Integer.parseInt(t)-1).isActive()){
						g.setColor(Color.WHITE);
						g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE,
								TILE_SIZE);
						g.setColor(Color.BLACK);
						g.drawString(t, (x*TILE_SIZE) + (TILE_SIZE/4), (y*TILE_SIZE)+(TILE_SIZE/4*3));
					}
					else {
						g.setColor(Color.BLACK);
						g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE,
								TILE_SIZE);
						g.setColor(Color.WHITE);
						g.drawString(t, (x*TILE_SIZE) + (TILE_SIZE/4), (y*TILE_SIZE)+(TILE_SIZE/4*3));
					}
				}
				g.setColor(lineColor);
				g.drawRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
			}
		}
		drawPointer(g);
		g.setColor(oldColour);
	}

	public void drawPointer(Graphics g) {
		if (selectedSpace != null) {
			g.setColor(pointerColor);
			g.drawRect(selectedSpace.x * TILE_SIZE,
					selectedSpace.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
			g.drawRect(selectedSpace.x * TILE_SIZE + TILE_SIZE / 4,
					selectedSpace.y * TILE_SIZE + TILE_SIZE / 4, TILE_SIZE / 2,
					TILE_SIZE / 2);
			g.drawOval(selectedSpace.x * TILE_SIZE,
					selectedSpace.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
			g.drawOval(selectedSpace.x * TILE_SIZE + TILE_SIZE / 4,
					selectedSpace.y * TILE_SIZE + TILE_SIZE / 4, TILE_SIZE / 2,
					TILE_SIZE / 2); // Line moving top left to bottom right
			g.drawLine(selectedSpace.x * TILE_SIZE,
					selectedSpace.y * TILE_SIZE, selectedSpace.x * TILE_SIZE
							+ TILE_SIZE, selectedSpace.y * TILE_SIZE
							+ TILE_SIZE); // Line moving top right to bottom
											// left
			g.drawLine(selectedSpace.x * TILE_SIZE + TILE_SIZE, selectedSpace.y
					* TILE_SIZE, selectedSpace.x * TILE_SIZE, selectedSpace.y
					* TILE_SIZE + TILE_SIZE); // Vertical line in middle
			g.drawLine(selectedSpace.x * TILE_SIZE + TILE_SIZE / 2,
					selectedSpace.y * TILE_SIZE, selectedSpace.x * TILE_SIZE
							+ TILE_SIZE / 2, selectedSpace.y * TILE_SIZE
							+ TILE_SIZE); // Horizontal line in middle
			g.drawLine(selectedSpace.x * TILE_SIZE, selectedSpace.y * TILE_SIZE
					+ TILE_SIZE / 2, selectedSpace.x * TILE_SIZE + TILE_SIZE,
					selectedSpace.y * TILE_SIZE + TILE_SIZE / 2);
		}
	}
	
	@Override
	public String toString(){
		String board = "";
		for (int y = 0; y < BOARD_HEIGHT; y++){
			for (int x = 0; x < BOARD_WIDTH; x++){
				if (!spaces[x][y].getAccessible()){
					boolean charPresent = false;
					String letter = "X";
					searchCharacters: for (Piece p : piece){
						if (p.getPosition() != null && p.getPosition().equals(spaces[x][y])){
							letter = "" + p.getToken();
							charPresent = true;
							break searchCharacters;
						}
					}
					if (!charPresent){
						searchRooms: for (Room r : rooms){
							if (r.spaces.contains(spaces[x][y])){
								if (spaces[x][y].getPieceOn() != null){
									letter = "" + spaces[x][y].getPieceOn().getToken();
								}
								else if (r.doors.contains(spaces[x][y])){
									//This is for when you exit a room - D is down below
									letter = "X";
								}
								else{
									letter = r.getRoomName().substring(0, 1).toLowerCase();
								}
								break searchRooms;
							}
						}
					}
					board += letter + " ";
				}
				else{
					String letter = "-";
					if (goodMoves.contains(spaces[x][y]))
						letter = "*";
					searchDoors: for (Room r : rooms){
						if (r.doors.contains(spaces[x][y])){
							if (letter.equals("-"))	letter = "D";
							else letter = "E";
							break searchDoors;
						}
					}
					board += letter + " ";
				}
			}
			board += "\n";
		}
		return board;
	}
}