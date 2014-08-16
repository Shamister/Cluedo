package ui;

import gameObjects.Character;
import gameObjects.Location;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JPanel;

import control.GameState;

/**
 * Board represents the game board of cluedo game
 * 
 * @author Wendell
 * 
 */
@SuppressWarnings("serial")
public class BoardCanvas extends JPanel {

	public static final int CANVAS_WIDTH = Board.TILE_SIZE * Board.BOARD_WIDTH;
	public static final int CANVAS_HEIGHT = Board.TILE_SIZE* Board.BOARD_HEIGHT;

	Board board;
	GameState game;

	public BoardCanvas(GameState g) {
		game = g;
		setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
		setBackground(Color.RED);
		/*
		 * GET the x and y coordinates of the canvas, then select the square, THEN REPAINT IT.
		 * */
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (board == null)
					return;
				Character cc = game.getCurrentCharacter();
				int x = e.getX();
				int y = e.getY();
				if (getBoard().getSelectedSpace() != null &&
						(getBoard().getSelectedSpace().x != x/Board.TILE_SIZE || 
						getBoard().getSelectedSpace().y != y/Board.TILE_SIZE))					
					board.selectSquare(x, y);
				else if (getBoard().getSelectedSpace() == null)
					board.selectSquare(x, y);
				else {
					if (game.getPossibleActions().contains(GameState.TurnState.CHOOSE_SPACE)){
						Location loc = cc.getPosition();
						if (!cc.isInRoom()){
							board.move(cc, getBoard().getSelectedSpace(), getBoard().getMovementAllowed());
							if (!loc.equals(getBoard().getSelectedSpace()) && !cc.getPosition().equals(loc)){
								board.clearValidMoves();
								game.goThroughTurn(GameState.TurnState.CHOOSE_SPACE);
								cc.setSuggestionMade(false);
							}
						}
						else{
							board.moveFromRoom(cc, getBoard().getSelectedSpace(), getBoard().getMovementAllowed(), getBoard().getValidMoves());
							if (getBoard().getSelectedSpace() != null && cc.getPosition() != null){
								board.clearValidMoves();
								game.goThroughTurn(GameState.TurnState.CHOOSE_SPACE);
								game.goThroughTurn(GameState.TurnState.MAKE_SUGGESTION);
								cc.setSuggestionMade(false);
							}
						}
						// If you've landed on a DOOR
						if (cc.getPosition() != null && cc.getPosition().getPartOfRoom() != null){
							if (!game.getPossibleActions().contains(GameState.TurnState.MAKE_SUGGESTION)){
								game.addPossibleActions(GameState.TurnState.MAKE_SUGGESTION);
							}
							cc.setInRoom(true);
							cc.setOldRoom(cc.getPosition().getPartOfRoom());
							cc.setRoom(cc.getOldRoom());
							for (Location l : cc.getRoom().spaces){
								if (l.getPieceOn() == null && !cc.getRoom().doors.contains(l)){
									cc.setPosition(l);
									break;
								}
							}
						}
					}
				}
				repaint();
			}
		});		
	}

	public Board getBoard() {
		return board;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (GameState.state == GameState.StateOfGame.SETUP_GAME) {
			board.draw(g);
		}
		if (GameState.state == GameState.StateOfGame.READY) {
			board.draw(g);
		}
		if (GameState.state == GameState.StateOfGame.PLAYING) {
			board.draw(g);
		}
	}

	public void createBoard(List<Character> characters) {
		board = new Board(characters);
	}
}