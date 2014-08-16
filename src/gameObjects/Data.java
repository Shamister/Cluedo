package gameObjects;

public class Data {

	/**
	 * CHARACTERS
	 */
	public static final String[] charNames = { "Miss Scarlett",
			"Colonel Mustard", "Mrs White", "The Reverand Green",
			"Mrs Peacock", "Professor Plum" };

	public static final String[] charDes = {
			"This Character always started first in the old rules,\nuntil the idea of \"Roll for first start\" became popular.",
			"This Character was originally named \"Colonel Yellow\"\nbefore the game was published, but his name got changed.",
			"This Character starts close to the Kitchen, which is\n(allegedly) the hardest room to get to.",
			"This Character was had his name changed to \"Mr. Green\" in the\n2002 North American version of Cluedo (or \"Clue\" as it's known there).",
			"This Character (often) starts one space closer to a room \nthan any of the others. Lucky her!",
			"This Character starts close to Study, which has a Secret Passage\nto the Kitchen (which is allegedly the hardest room to get to).", };

	/**
	 * WEAPONS
	 */
	public static final String[] weaponNames = { "Candlestick", "Dagger",
			"Lead Pipe", "Revolver", "Rope", "Spanner" };

	public static final String[] weaponDes = {
			"I don't have any awesome nuggets of trivia for this.\nHey, research isn't easy on a schedule!!",
			"Or \"Knife\" if you have the the North American version.",
			"Called lead piping in earlier UK editions - the early tokens were\nactually made out of lead, posing a risk of lead poisoning.",
			"Beginning in 1972, all editions typically now represent an\nAllan & Thurber pepper-box revolver.",
			"I don't have any awesome nuggets of trivia for this.\nHey, research isn't easy on a schedule!!",
			"Or \"Wrench\" in the North American version." };

	/**
	 * ROOMS
	 */

	public static final String[] roomNames = { "Lounge", "Dining Room",
			"Kitchen", "Ballroom", "Conservatory", "Billiard Room", "Library",
			"Study", "Hall", };

	public static final String[] roomDes = {
			"This Room has a Secret Passage to the Conservatory.",
			"Colonel Mustard starts closer to this room than any\nother character.",
			"This room has a Secret Passage to the Study.\nAllegedly it's the hardest room to get to.",
			"This room has four doors spaced apart, offering the\nwidest selection of entries and exits.",
			"This Room has a Secret Passage to the Lounge.",
			"Mrs Peacock starts closer to this room than any\nother character.",
			"Professor Plum starts closer to this room than any\nother character. Also, it's so small I nearly forgot it!",
			"This Room has a Secret Passage to the Kitchen.\nAllegedly, the Kitchen is the hardest room to get to.",
			"This Room has three doors, though different versions give it four." };

	public static final String helpDes = "Overview:\n        Cluedo is a basically a \"whodunnit?\" game.\n\n"

			+ "Goal:\n        The goal of Cluedo is to solve a murder mystery. To do this, you must go around "
			+ "the board and make suggestions about the facts of the case: The Room the murder happened in, "
			+ "The Weapon used to kill the victim, and the Character responsible. Then your opponents must try "
			+ "to disprove your suggestion.\n\nIf they can't, then you may have found the facts!\n\nEventually, "
			+ "you'll want to make an accusation regarding a Room, a Weapon and a Character. But watch out! "
			+ "Accusations are far more serious than mere suggestions - get even ONE fact wrong and you're off "
			+ "the case (eliminated)!\n\n"

			+ "Starting Off:\n        To start a game, you will need to click on <File>, then on <New Game> "
			+ "(You will always be asked if you want start a new game - deal with it), then enter a number "
			+ "between 3 and 6 (inclusive) to decide on the number of players. After that, a number of "
			+ "frames corresponding to the number you entered will pop up - You MUST enter a name and choose a "
			+ "Character. Once everyone's chosen a Character (and you've gotten the blood out of the carpet), "
			+ "you can play!\n\n"

			+ "Gameplay - Squares and You:\n        Squares are colour-coded to mean different things:\n\n"
			+ "RED squares are doors - moving onto them allows you to enter a room.\n        SOME doors are on "
			+ "corners - in this case, you can only enter from directly underneath or above them.\n"
			+ "BLUE squares represent Rooms - you can only enter them through a DOOR (RED).\n"
			+ "BROWN squares are Inaccessible areas. You can't move there.\n"
			+ "BLACK squares and WHITE squares are other Pieces. The WHITE one is the one that's moving.\n\n"

			+ "Gameplay - MOVEMENT:\n        There are two ways to move. ONE way is to roll the dice (by clicking "
			+ "the button), OR you can use a Secret Passage.\n\n"

			+ "Using and moving with the dice:\n        If you decide to roll the dice, the highlighted "
			+ "squares on the board show you where you can move - the dice above the buttons show you how many "
			+ "spaces you can move. You CANNOT move through people - only around them!!\n\n"

			+ "Using and moving with Secret Passages:\n        If you decide to use a Secret Passage, you will "
			+ "be asked if you're serious. If you are, you'll be moved to the room diagonally opposite you. "
			+ "ONLY CORNER ROOMS have Secret Passages!\n\n"

			+ "Gameplay - Making Suggestions:\n        Two win, you need to CORRECTLY guess ALL 3 facts about "
			+ "the case. But there are a LOT of possibilities - to eliminate them, you go to a room and make "
			+ "a suggestion involving that room, a weapon and a character. At the start of the game you were dealt "
			+ "between 3 and 6 cards (depending on the number of players). 3 other cards were put aside in an "
			+ "envelope - these are the facts you must deduce. SO, when you make a suggestion, and an opponent "
			+ "shows you a card, it CANNOT be a possibility. This is how you narrow possibilities!\n"
			+ "        We've actually taken the liberty of recording the results of your suggestions for you."
			+ "To see these notes, please click on <Options>, then <Show Data>. This will only show the current "
			+ "player's notes though.\n\n";

}
