package gameObjects.CARDS;

import gameObjects.Character;
import gameObjects.Data;
import gameObjects.Envelope;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Builder Class: The whole purpose of this class is to:
 * Build the cards, 
 * Shuffle the cards, 
 * Choose a WeaponCard, a CharacterCard and a RoomCard, and add them to an envelope,
 * Remove the "Sealed Cards" (the ones in the envelope) from the set of available cards, and finally,
 * Deal the remaining cards out to all the players until it runs out.
 * */
public class CardBuilder {

	private List<Card> allCards = new ArrayList<>();
	private Card ChosenRoom = null;		// The Room card that goes in the envelope
	private Card ChosenWeapon = null;	// The Weapon card that goes in the envelope
	private Card ChosenCharacter = null;// The Character card that goes in the envelope
	private List<Character> characters;
	
	/**
	 * CONSTUCTOR
	 * 
	 * @param characters The list of Characters - this lets the CardBuilder Deal cards out to them.
	 * */
	public CardBuilder(List<Character> characters){
		this.characters = characters;
		buildDeck();
		Collections.shuffle(allCards);
		chooseCards();
		dealCards();
	}
	
	/**
	 * Constructs the Deck of Cards
	 * */
	private void buildDeck(){
		buildCharacterCards();
		buildWeaponCards();
		buildRoomCards();
	}
	
	/**
	 * A method that makes all of the Character Cards
	 * */
	private void buildCharacterCards(){
		for (int i = 0; i < 6; i++)
			allCards.add(new Card(Data.charNames[i], Data.charDes[i], "Character"));
		}
	
	/**
	 * A method that makes all of the Weapon Cards
	 * */
	private void buildWeaponCards(){
		for (int i = 0; i < 6; i++)
			allCards.add(new Card(Data.weaponNames[i], Data.weaponDes[i], "Weapon"));
	}
	
	/**
	 * A method that makes all of the Room Cards
	 * */
	private void buildRoomCards(){
		for (int i = 0; i < 9; i++)
			allCards.add(new Card(Data.roomNames[i], Data.roomDes[i], "Room"));
	}
	
	/**
	 * This method selects the cards to be put into the envelope.
	 * Crucially, it also REMOVES the cards from the deck, so they can't be dealt to the Characters.
	 * */
	private void chooseCards(){
		for (int i = 0; i < allCards.size(); i++){
			if (ChosenWeapon != null && ChosenCharacter != null && ChosenRoom != null)	return;
			
			if (allCards.get(i).getCardType().equals("Room") && ChosenRoom == null)
				ChosenRoom = allCards.remove(i--);
			
			else if (allCards.get(i).getCardType().equals("Weapon") && ChosenWeapon == null)
				ChosenWeapon = allCards.remove(i--);
			
			else if (allCards.get(i).getCardType().equals("Character") && ChosenCharacter == null)
				ChosenCharacter = allCards.remove(i--);	
		}
		ChosenRoom.setInEnvelope(true);
		ChosenWeapon.setInEnvelope(true);
		ChosenCharacter.setInEnvelope(true);
	}
	
	/**	Returns the Envelope with the chosen cards inside	*/
	public Envelope getChosenCards(){
		return new Envelope(ChosenRoom, ChosenWeapon, ChosenCharacter);
	}
	
	/**	Deals all the remaining cards to all the characters	*/
	private void dealCards() {
		if (characters.size() < 3 || characters.size() > 6)
			throw new IllegalArgumentException("You must have between 3 and 6 players.");
		while (!allCards.isEmpty()){
			for (int i = 0; i < characters.size(); i++){
				if (!allCards.isEmpty()){
					Card card = allCards.remove(0);
					card.setHolder(characters.get(i));
					characters.get(i).addToHand(card);
					characters.get(i).setData(card + "was added to your hand!\nTherefore, " + card.getCardName() +
							"CANNOT be in the envelope!");
				}
			}
		}
	}
}
