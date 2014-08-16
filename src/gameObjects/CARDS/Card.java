package gameObjects.CARDS;

import gameObjects.Character;

public class Card{

	private String name;
	private String information;
	private String type;
	private boolean inEnvelope = false;	// Is the card in the envelope? If NOT, it's held by a character
	private Character holder = null;	// Who's holding this Card? holder = null <=> Card is in the envelope 
	
	/**
	 * CONSTRUCTOR
	 * 
	 * @param name The NAME of the Card
	 * 
	 * @param information Trivia about the Card - Mainly pointless though...
	 * 
	 * @param type The TYPE of the Card - CRUCIAL for distinguishing which cards
	 * can be put into an envelope.
	 * */
	public Card(String name, String information, String type){
		this.name = name;
		this.information = information;
		this.type = type;
	}
	
	/**
	 * This method returns the name of the card.
	 * This is used mainly to check against actual objects - e.g. checking Ballroom to Ballroom.
	 * */
	public String getCardName() {	return name;	}

	/**
	 * This method returns the Information of the card.
	 * Purely Cosmetic. Remove?
	 * */
	public String getCardInformation() {	return information;	}

	/**
	 * This method returns the type of the card.
	 * Extra Redundancy to ensure that there is ABSOLUTELY *NO WAY* that, for example, a character card is confused for a room card.
	 * This is especially important for the envelope.
	 * */
	public String getCardType() {	return type;	}

	/**		This method returns whether the card is in the envelope or not.		*/
	public boolean isInEnvelope() {	return inEnvelope;	}
	
	/**
	 * This method sets whether the CardInterface is in the envelope.
	 * 
	 * PLEASE!! DO *NOT* USE THIS AFTER THE GAME HAS BEEN SET UP!!
	 * */
	protected void setInEnvelope(boolean inEnvelope) {	this.inEnvelope = inEnvelope;	}

	/**		This method returns the Character holding the CardInterface, or null if it's in the Envelope.		*/
	public Character getHolder() {	return holder;	}

	/**
	 * This method sets which Character is holding the CardInterface.
	 * 
	 * PLEASE!! DO *NOT* USE THIS AFTER THE GAME HAS BEEN SET UP!!
	 * */
	protected void setHolder(Character holder) {	this.holder = holder;	}
	
	public String toString(){
		return "Card Type:" + type + "\nName:" + name + "\nMisc. Info: " + information + "\n"; 
	}
}
