package gameObjects;

import gameObjects.CARDS.Card;

/** 
 * The Envelope's purpose for existence is to hold 3 cards - a Weapon Card, a Room Card and
 * a Character Card. When a Character thinks they can win (i.e. deduce the murder weapon, the 
 * room the murder happened in, and the murderer), they'll make an accusation involving
 * a Weapon, a Room and a Character, then check their accusation against the Cards in the
 * Envelope. If they're right on all counts, they win. Otherwise, they're eliminated.
 * */
public class Envelope {

	private Card roomCard = null;
	private Card weaponCard = null;
	private Card charCard = null;	 

	public Envelope(Card rc, Card wc, Card cc){
		roomCard = rc;
		weaponCard = wc;
		charCard = cc;
	}

	/**
	 * Gets the RoomCard that's in the envelope.
	 * */
	public Card getrCard() {
		return roomCard;
	}

	/**
	 * Gets the WeaponCard that's in the envelope.
	 * */
	public Card getwCard() {
		return weaponCard;
	}

	/**
	 * Gets the CharacterCard that's in the envelope.
	 * */
	public Card getcCard() {
		return charCard;
	}

	/**
	 * Sets the RoomCard that's in the envelope IF IT
	 * *HAS NOT* BEEN SET.
	 * */
	public void setrCard(Card rCard) {
		if (this.roomCard == null) this.roomCard = rCard;
	}

	/**
	 * Sets the WeaponCard that's in the envelope IF IT
	 * *HAS NOT* BEEN SET.
	 * */
	public void setwCard(Card wCard) {
		if (this.weaponCard == null) this.weaponCard = wCard;
	}

	/**
	 * Sets the CharacterCard that's in the envelope IF IT
	 * *HAS NOT* BEEN SET.
	 * */
	public void setcCard(Card cCard) {
		if (this.charCard == null) this.charCard = cCard;
	}
	
	/**
	 * Returns whether the Character's accusation is correct.
	 * */
	public boolean accusationIsCorrect(Card cc, Card rc, Card wc){
		if (rc != null && wc != null && cc != null)
			return (rc.isInEnvelope() && wc.isInEnvelope() && cc.isInEnvelope());
		else
			throw new IllegalArgumentException("You used the wrong method parameters.");
	}
	
	/**
	 * Returns whether the Character's accusation is correct.
	 * This is alternative that uses strings.
	 * */
	public boolean accusationIsCorrect(String cc, String rc, String wc){
		if (rc != null && wc != null && cc != null)
			return (charCard.getCardName().equals(cc) && roomCard.getCardName().equals(rc) && weaponCard.getCardName().equals(wc));
		else
			throw new IllegalArgumentException("You used the wrong method parameters.");
	}
}
