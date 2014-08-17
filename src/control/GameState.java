package control;

import gameObjects.Location;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import javax.swing.JOptionPane;

import ui.Board;
import ui.BoardCanvas;

/**
 * This class:
 * 1) Keeps track of who's turn it is
 * 2) Keeps track of the turn state
 * 3) Keeps track of the game state
 * 
 * For example, when a player ends their turn:
 * The Character moves to the next in line (i.e. player1 ==> player2 ==> player3)
 * The TURN state resets to the first state (CHECK_POSSIBLE_ACTIONS)
 * 		CHECK_POSSIBLE_ACTIONS is for the event where:
 * 		The player starts in a room with a Secret Passage (meaning they can use it), OR
 * 
 * 		The Player started in a room they'd already made a suggestion in, BUT 
 * 			their exits are blocked (meaning they can only accuse or end turn), OR
 * 
 * 		The Player started in a room they HADN'T already made a suggestion in 
 * 			(meaning they can make a suggestion), OR
 * 
 * 		The Player starts OUTSIDE a room (meaning they roll the dice, then they can make
 * 			a suggestion IFF they end their moving phase in a room they weren't in directly previous)
 * */
public class GameState {

	/*
	 * Example turnState flows:
	 * CHECK_POSSIBLE_ACTIONS ==> END_TURN; no options
	 * CHECK_POSSIBLE_ACTIONS ==> MOVE_VIA_XXX ==> END_TURN; move, end turn outside room
	 * CHECK_POSSIBLE_ACTIONS ==> MAKE_SUGGESTION ==> END_TURN; start in room, make a suggestion, end turn inside room.
	 * CHECK_POSSIBLE_ACTIONS ==> MOVE_VIA_XXX ==> MAKE_ACCUSATION; state = GAME_OVER; move, make an accusation. EITHER:
	 * 																				   Everyone is eliminated, OR
	 * 																				   Accusation was correct. 
	 */
	public static enum TurnState {
		CHECK_POSSIBLE_ACTIONS,	// See what the Character is allowed to do this turn.
		MOVE_VIA_DICE,			// Let the Character move by rolling the dice
			CHOOSE_SPACE,			// Character must now choose a space - MOVE_VIA_DICE leads to CHOOSE_SPACE
		MOVE_VIA_SP,			// Let the Character move by using a rooms' Secret Passage
		MAKE_SUGGESTION,		// Make a Suggestion IFF Character is in a room they haven't been in since their last turn.
		MAKE_ACCUSATION,		// Make an accusation - if the Character is correct, they win. Otherwise, they lose.
		END_TURN,				// End turn, having no more available options
	}

	public static enum StateOfGame {
		NOTHINGS_HAPPENING, 	// To start the game, the player/s have to press the 
								// new game button - this lets them configure the game.
		SETUP_GAME, 			// This is where the initialisation of objects .
								// and the deciding of Characters takes place
		READY, 					// Players choose squares hiding numbers to decide turn order.
		PLAYING, 				// THIS STATE WILL LOOP/STAY ACTIVE. It will only change if a 
								// new game is started, all Players are eliminated or a correct 
								// Accusation is made.
		GAME_OVER, 				// Either all Players have been eliminated or a correct accusation 
								// was made. This state leads back to EXIT.
		EXIT					// The application is closed.
	}

	public static StateOfGame state = StateOfGame.NOTHINGS_HAPPENING;

	public static int expectedNumPlayers = 0;

	private Queue<gameObjects.Character> turnOrder = new ArrayDeque<>();	// The character at the head of this queue is the current character.
	private List<TurnState> possibleActions = new ArrayList<>();			// This is the list of possible actions.

	private BoardCanvas boardCanvas;

	public GameState(){
		boardCanvas = new BoardCanvas(this);		
	}

	/**
	 * Returns the BoardCanvas
	 * @return The boardCanvas which everything gets drawn on.
	 * */
	public BoardCanvas getBoardCanvas() {
		return boardCanvas;
	}

	public void createBoard(List<gameObjects.Character> characters){
		if (characters.size() != expectedNumPlayers) 
			return;
		state = StateOfGame.SETUP_GAME;
		boardCanvas.createBoard(characters);
		
		state = StateOfGame.READY;
		gameObjects.Character cc = getCurrentCharacter();
		cc.setActive(true);
		// Check if in a room and a suggestion can be made.
		if (cc.isInRoom() && !cc.isSuggestionMade())
			possibleActions.add(TurnState.MAKE_SUGGESTION);
		// Check if in a room and a Secret Passage exists.
		if (cc.isInRoom() && cc.getRoom().hasSecretPassage() && !cc.isMovementMade() && cc.isMovementOK())
			possibleActions.add(TurnState.MOVE_VIA_SP);
		// Check if can move via dice and OUTSIDE A ROOM (i.e. has a location)
				if (cc.getPosition() != null && !getBoard().getMoves(cc.getPosition(), 1).isEmpty() && !cc.isMovementMade() && cc.isMovementOK())
					possibleActions.add(TurnState.MOVE_VIA_DICE);
				/*
				 * Check if can move via dice and is INSIDE A ROOM (i.e. location == null)
				 * This is slightly harder; I need to find the square/s that are next to the doors AND ACCESSIBLE - 
				 * SO, I need to find out if there's a door the player can get out of THAT IS NOT BLOCKED BY ANOTHER
				 * PLAYER.
				 */
				else{
					List<Location> startPoints = getBoard().findClearExits(cc.getRoom());
					if (startPoints.size() == 0){	/* NO CLEAR EXIT - CAN'T MOVE WITH DICE */	}
					else {	possibleActions.add(TurnState.MOVE_VIA_DICE);	}
				}
		// Make Accusation and End Turn are ALWAYS included in moves.
		possibleActions.add(TurnState.MAKE_ACCUSATION);
		possibleActions.add(TurnState.END_TURN);
		state = StateOfGame.PLAYING;
		getBoard().clearValidMoves();
		boardCanvas.repaint();
	}
	
	/**
	 * Returns the Board that belongs to the BoardCanvas
	 * @return The Board which belongs to the BoardCanvas. The Board is like a hub
	 * of information, so it's important we get it. 
	 * */
	public Board getBoard() {
		return boardCanvas.getBoard();
	}

	/**
	 * Gets the WHOLE turnOrder queue - can be used if, e.g. we want to show 
	 * the turn order graphically or textually. Will also be used for suggestions 
	 * - the responsibility of disproving a suggestion (showing a card) will be 
	 * passed around in the turn order. 
	 * @return The queue which represents the order in which Characters take their
	 * turns.
	 * */
	public Queue<gameObjects.Character> getTurnOrder() {
		return turnOrder;
	}

	/**
	 * Returns the current character - which is the one at the head of the queue.
	 * @return The head of the queue which represents the order in which Characters 
	 * take their turns - this specifically is the current Character.
	 * */
	public gameObjects.Character getCurrentCharacter() {
		if (turnOrder != null && !turnOrder.isEmpty())
			return turnOrder.peek();
		else throw new UnsupportedOperationException("NO. THERE ARE NO PLAYERS IN YOUR QUEUE!");
	}

	/**
	 * Sets the ENTIRE turnOrder queue IFF the queue being passed in is empty
	 * or null (re-initialising everything for a new game), or the current 
	 * turnOrder queue is empty (setting it up for the new game).
	 * 
	 * @param turnOrder. A new turnOrder queue that will replace the old one.
	 * Because replacing this during gameplay can cause problems, we will only
	 * allow this before/while setting up the game and after the gameplay has ended
	 * (i.e. NOTHINGS_HAPPENING, SETUP_GAME, GAME_OVER or EXIT).
	 * */
	public void setTurnOrder(Queue<gameObjects.Character> turnOrder) {
		if ((this.turnOrder.isEmpty() || turnOrder == null || turnOrder.isEmpty()) && 
				(state == StateOfGame.NOTHINGS_HAPPENING) || (state == StateOfGame.SETUP_GAME) || 
				(state == StateOfGame.GAME_OVER) 		  || (state == StateOfGame.EXIT))
			this.turnOrder = turnOrder;
		else throw new UnsupportedOperationException("Either the current turnOrder or the one being passed must be empty/null,"
				+ "AND the turnOrder may NOT be changed during Gameplay.");
	}

	/**
	 * Ends the current Character's turn and changes it to the next Character.
	 * This is done by offering the turnOrder queue the polled head of itself.
	 * */
	public void nextCharactersTurn() {
		gameObjects.Character cc = getCurrentCharacter();
		cc.setActive(false);
		cc.setMovementMade(false);
		cc.setMovementOK(true);
		turnOrder.offer(turnOrder.poll());
		possibleActions.clear();
		cc = null;
		// check if eliminated
		while (cc == null){
			cc = getCurrentCharacter();
			if (cc.isEliminated()){
				turnOrder.offer(turnOrder.poll());
				cc = null;
			}
		}
		cc.setActive(true);
		// Check if in a room and a suggestion can be made.
		if (cc.isInRoom() && !cc.isSuggestionMade())
			possibleActions.add(TurnState.MAKE_SUGGESTION);
		// Check if in a room and a Secret Passage exists.
		if (cc.isInRoom() && cc.getRoom().hasSecretPassage() && !cc.isMovementMade() && cc.isMovementOK())
			possibleActions.add(TurnState.MOVE_VIA_SP);
		// Check if can move via dice and OUTSIDE A ROOM (i.e. has a location)
		if (cc.getPosition() != null && !getBoard().getMoves(cc, 1).isEmpty() && !cc.isMovementMade() && cc.isMovementOK())
			possibleActions.add(TurnState.MOVE_VIA_DICE);
		/*
		 * Check if can move via dice and is INSIDE A ROOM (i.e. location == null)
		 * This is slightly harder; I need to find the square/s that are next to the doors AND ACCESSIBLE - 
		 * SO, I need to find out if there's a door the player can get out of THAT IS NOT BLOCKED BY ANOTHER
		 * PLAYER.
		 */
		else{
			if (cc.getRoom() == null || (cc.getRoom() != null && getBoard().findClearExits(cc.getRoom()).size() == 0)){	
				/* NO CLEAR EXIT - CAN'T MOVE WITH DICE */
			}
			else {	possibleActions.add(TurnState.MOVE_VIA_DICE);	}
		}
		// Make Accusation and End Turn are ALWAYS included in moves.
		possibleActions.add(TurnState.MAKE_ACCUSATION);
		possibleActions.add(TurnState.END_TURN);
		getBoard().clearValidMoves();
		boardCanvas.repaint();
	}

	/**
	 * Gets the list of Actions the Character can take.
	 * @return The list of all actions the player can take.
	 * */
	public List<TurnState> getPossibleActions() {
		return possibleActions;
	}

	/**
	 * Sets the list of Actions the Character can take.
	 * @param possibleActions. The list of actions the player can take FROM THIS POINT IN TIME.
	 * */
	public void setPossibleActions(List<TurnState> possibleActions) {
		if (this.possibleActions.isEmpty())
			this.possibleActions = possibleActions;
	}

	/**
	 * Sets the list of Actions the Character can take.
	 * @param possibleActions. The list of actions the player can take FROM THIS POINT IN TIME.
	 * */
	public void addPossibleActions(TurnState t) {
		possibleActions.add(t);
	}
	
	/**
	 * Advances the StateOfGame - This method can NEVER cause state to be EXIT.
	 * */
	public void advanceGameState(){
		if (state == StateOfGame.NOTHINGS_HAPPENING)
			state = StateOfGame.SETUP_GAME;
		else if (state == StateOfGame.SETUP_GAME)
			state = StateOfGame.READY;
		else if (state == StateOfGame.READY)
			state = StateOfGame.PLAYING;
		else if (state == StateOfGame.PLAYING)
			state = StateOfGame.GAME_OVER;
		else if (state == StateOfGame.GAME_OVER)
			state = StateOfGame.NOTHINGS_HAPPENING;
	}

	/**
	 * Causes the current Character to end their turn.
	 * */
	public void EndTurnNow(){
		possibleActions.clear();
		possibleActions.add(TurnState.END_TURN);
		boolean stop = true;
		for (gameObjects.Character c : turnOrder){
			if (!c.isEliminated()){
				stop = false;
				break;
			}
		}
		if (stop){
			JOptionPane.showMessageDialog(null, "Game Over");
			endGame();
			return;
		}
		nextCharactersTurn();
	}

	/**
	 * Sets StateOfGame to EXIT.
	 * */
	public void endGame(){
		state = StateOfGame.GAME_OVER;
	}

	public void goThroughTurn(TurnState t){
		possibleActions.remove(t);
	}

	public void eliminateCharacter() {
		getCurrentCharacter().setEliminated(true);
	}
}