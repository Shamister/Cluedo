package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import ui.Board;
import gameObjects.Character;
import gameObjects.Location;
import gameObjects.CARDS.Card;

public class TestSuite {

	List<Character> chars;
	Board board;

	/**
	 * Auto Generates the list of Characters for the board.
	 * @param num. The number of Characters to generate
	 * @return A list of Generated Characters.
	 * */
	private List<Character> generateCharacters(int num) {
		List<Character> chars = new ArrayList<Character>();
		for (int i = 1; i <= num; i++)
			chars.add(new Character("Player " + i, i));
		return chars;
	}
	
	//////////////////////////////////////////////////////
	////////////////////	TESTS	//////////////////////
	//////////////////////////////////////////////////////
	
	/**
	 * Test that less than three characters throws exception
	 * */
	@Test(expected=IllegalArgumentException.class)
	public void invalidNumOfCharacterTest1() {
		chars = generateCharacters(2);
		board = new Board(chars);
		fail("There has to be AT LEAST 3 characters!");
	}
	
	/**
	 * Test that more than six characters throws exception
	 * */
	@Test(expected=IllegalArgumentException.class)
	public void invalidNumOfCharacterTest2() {
		chars = generateCharacters(7);
		board = new Board(chars);
		fail("There can be NO MORE THAN 6 characters!");
	}
	
	/**
	 * Test that 3 characters get 6 cards
	 * */
	@Test
	public void numOfCardsHeldByCharacters1() {
		chars = generateCharacters(3);
		board = new Board(chars);
		for (int i = 0; i < 3; i++)
			assertTrue("If there are 3 characters, "
					+ "then EACH PLAYER should have 6 cards", 
					chars.get(i).getHand().size() == 6);
	}
	
	/**
	 * Test that 6 characters get 3 cards
	 * */
	@Test
	public void numOfCardsHeldByCharacters2() {
		chars = generateCharacters(6);
		board = new Board(chars);
		for (int i = 0; i < 6; i++)
			assertTrue("If there are 6 characters, "
					+ "then EACH PLAYER should have 3 cards", 
					chars.get(i).getHand().size() == 3);
	}
	
	/**
	 * Test that the envelope has 3 cards - regardless of player number
	 * */
	@Test
	public void envelopeHas3Cards() {
		for (int c = 3; c < 7; c++){
			chars = generateCharacters(c);
			board = new Board(chars);
			
			Card card1 = board.getEnvelope().getcCard();
			Card card2 = board.getEnvelope().getrCard();
			Card card3 = board.getEnvelope().getwCard();
			
			assertTrue("The Envelope MUST have 3 cards, no "
					+ "matter how many players there are", 
					(card1 != null && card2 != null && card3 != null));
		}
	}
	
	/**
	 * Test that the envelope has 3 cards of different types
	 * */
	@Test
	public void envelopeHas3DifferentKindsOfCards() {
		for (int c = 3; c < 7; c++){
			chars = generateCharacters(c);
			board = new Board(chars);
			
			Card card1 = board.getEnvelope().getcCard();
			Card card2 = board.getEnvelope().getrCard();
			Card card3 = board.getEnvelope().getwCard();
			
			if (card1 == null || card2 == null || card3 == null){
				fail("The envelope needs 3 cards");
			}
			
			String type1 = card1.getCardType();
			String type2 = card2.getCardType();
			String type3 = card3.getCardType();
			
			boolean condition = (!type1.equals(type2) && 
			!type1.equals(type3) && !type3.equals(type2));
			
			assertTrue("The 3 cards in the Envelope MUST be different"
					+ "types", condition);
		}
	}
	
	/**
	 * Test that there is a total of 21 cards in existence
	 * */
	@Test
	public void twentyOneCardsExist() {
		for (int c = 3; c < 7; c++){
			chars = generateCharacters(c);
			board = new Board(chars);
			
			Card card1 = board.getEnvelope().getcCard();
			Card card2 = board.getEnvelope().getrCard();
			Card card3 = board.getEnvelope().getwCard();
			
			if (card1 == null || card2 == null || card3 == null){
				fail("The envelope needs 3 cards");
			}
			
			int cardNum = 3;
			for (Character character : board.getCharacters()){
				cardNum += character.getHand().size();
			} 
			
			assertEquals("There should be 21 cards only - " + cardNum + " found", 21, cardNum);
		}
	}
	
	/*
	 * 
	 * NOW making a new set of tests focused on the board layout and movement
	 * 
	 * */
		
	private String boardRep =  "X X X X X X X X X 3 X b b X 4 X X X X X X X X X \n"
							  +"k k k k k k X - - - b b b b - - - X c c c c c c \n"
							  +"k k k k k k - - b b b b b b b b - - c c c c c c \n"
							  +"k k k k k k - - b b b b b b b b - - c c c c c c \n" 
							  +"k k k k k k - - b b b b b b b b - - D c c c c c \n" 
							  +"k k k k k k - - D b b b b b b D - - - c c c c c \n" 
							  +"k k k k D k - - b b b b b b b b - - - - - - - 5 \n" 
							  +"- - - - - - - - b D b b b b D b - - - - - - - X \n" 
							  +"X - - - - - - - - - - - - - - - - - b b b b b b \n" 
							  +"d d d d d - - - - - - - - - - - - - D b b b b b \n" 
							  +"d d d d d d d d - - X X X X X - - - b b b b b b \n" 
							  +"d d d d d d d d - - X X X X X - - - b b b b b b \n" 
							  +"d d d d d d d D - - X X X X X - - - b b b b D b \n" 
							  +"d d d d d d d d - - X X X X X - - - - - - - - X \n" 
							  +"d d d d d d d d - - X X X X X - - - l l D l l X \n" 
							  +"d d d d d d D d - - X X X X X - - l l l l l l l \n" 
							  +"X - - - - - - - - - X X X X X - - D l l l l l l \n" 
							  +"2 - - - - - - - - - - - - - - - - l l l l l l l \n" 
							  +"X - - - - - - - - h h D D h h - - - l l l l l X \n" 
							  +"l l l l l l D - - h h h h h h - - - - - - - - 6 \n" 
							  +"l l l l l l l - - h h h h h h - - - - - - - - X \n" 
							  +"l l l l l l l - - h h h h h D - - D s s s s s s \n" 
							  +"l l l l l l l - - h h h h h h - - s s s s s s s \n" 
							  +"l l l l l l l - - h h h h h h - - s s s s s s s \n" 
							  +"X X X X X X X 1 X X h h h h X X - X X X X X X X \n";
	
	private String boardMov =  "X X X X X X X X X 3 X b b X 4 X X X X X X X X X \n"
			  				  +"k k k k k k X - - - b b b b - - - X c c c c c c \n"
			  				  +"k k k k k k - - b b b b b b b b - - c c c c c c \n"
			  				  +"k k k k k k - - b b b b b b b b - - c c c c c c \n" 
			  				  +"k k k k k k - - b b b b b b b b - - D c c c c c \n" 
			  				  +"k k k k k k - - D b b b b b b D - - - c c c c c \n" 
			  				  +"k k k k D k - - b b b b b b b b - - - - - - - 5 \n" 
			  				  +"- - - - - - - - b D b b b b D b - - - - - - - X \n" 
			  				  +"X - - - - - - - - - - - - - - - - - b b b b b b \n" 
			  				  +"d d d d d - - - - - - - - - - - - - D b b b b b \n" 
			  				  +"d d d d d d d d - - X X X X X - - - b b b b b b \n" 
			  				  +"d d d d d d d d - - X X X X X - - - b b b b b b \n" 
			  				  +"d d d d d d d D - - X X X X X - - - b b b b D b \n" 
			  				  +"d d d d d d d d * - X X X X X - - - - - - - - X \n" 
			  				  +"d d d d d d d d * * X X X X X - - - l l D l l X \n" 
			  				  +"d d d d d d E d * * X X X X X - - l l l l l l l \n" 
			  				  +"X - - * * * * * * * X X X X X - - D l l l l l l \n" 
			  				  +"2 - * * * * * * * * * * * - - - - l l l l l l l \n" 
			  				  +"X * * * * * * * * h h E D h h - - - l l l l l X \n" 
			  				  +"l l l l l l E * * h h h h h h - - - - - - - - 6 \n" 
			  				  +"l l l l l l l * * h h h h h h - - - - - - - - X \n" 
			  				  +"l l l l l l l * * h h h h h D - - D s s s s s s \n" 
			  				  +"l l l l l l l * * h h h h h h - - s s s s s s s \n" 
			  				  +"l l l l l l l * * h h h h h h - - s s s s s s s \n" 
			  				  +"X X X X X X X 1 X X h h h h X X - X X X X X X X \n";
	
	/**
	 * Test that the initial layout is correct.
	 * */
	@Test
	public void initialBoardIsOkay() {
		chars = generateCharacters(6);
		board = new Board(chars);
		
		if (!boardRep.equals(board))
			System.out.println(boardRep + "\n\n" + board);
		
		assertEquals("BOARD LAYOUT INCORRECT!", boardRep, board.toString());
	}
	
	/**
	 * Test that Player 1 can move 12 spaces.
	 * */
	@Test
	public void player1CanMove12SpacesFromStart1() {
		chars = generateCharacters(6);
		board = new Board(chars);
		
		board.getMoves(chars.get(0), 12);
				
		assertEquals("BOARD LAYOUT INCORRECT!", boardMov, board.toString());
	}
	
	/**
	 * Test that when Player 1 moves, the old location is accessible, 
	 * and the new one isn't
	 * 
	 * REQUIRES THE USER TO AGREE THAT THEY WANT TO MOVE!!
	 * */
	@Test
	public void player1Movement() {
		chars = generateCharacters(6);
		
		board = new Board(chars);
		
		Location start = chars.get(0).getPosition();
		
		board.getMoves(chars.get(0), 12);
		board.move(chars.get(0), board.getValidMoves().get(1), 12);
		
		Location end = chars.get(0).getPosition();
		assertNotEquals(start, end);
		assertTrue(start.getAccessible() && !end.getAccessible());
	}
}
