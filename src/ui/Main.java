package ui;

import gameObjects.Character;
import gameObjects.Data;
import gameObjects.Location;
import gameObjects.Piece;
import gameObjects.CARDS.Card;
import gameObjects.ROOMS.Room;
import gameObjects.WEAPONS.Weapon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import control.GameState;

/**
 * 
 * This class is the main UI class
 * 
 * @author Wendell
 * */
public class Main {

	public static final int WINDOW_WIDTH = 1000;
	public static final int WINDOW_HEIGHT = 700;

	private String title = "  .:CLUEDO:.  ";

	public static String imagePath = "../images/";

	public Point initClick;

	// color configuration here
	private Color menuBarBgColor1 = new Color(83, 83, 83);
	private Color menuBarBgColor2 = new Color(40, 40, 40);
	private Color menuFontColor = new Color(228, 249, 228);
	private Color menuBgColor = new Color(38, 38, 38);
	private Color menuItemBgColor = new Color(38, 38, 38);
	private Color menuItemFontColor = new Color(228, 249, 228);
	private Color mainPanelBgColor = new Color(38, 38, 38);
	private Color mainPanelFontColor = new Color(228, 249, 228);
	private Color mainPanelfocusColor = new Color(180, 180, 180);
	private Color labelFontColor = Color.green;

	// button fields here
	JButton rollDiceButton, usePassageButton, endTurnButton, suggestButton,
			accuseButton;

	// comboBox fields here
	JComboBox<String> charCB, weaponCB, roomCB;

	// image panel fields here
	JLabel charImage, weaponImage, roomImage, diceImage1, diceImage2;

	private final int CARD_WIDTH = 86;
	private final int CARD_HEIGHT = 135;
	
	// Card image JLabel fields here
	private Set<JLabel> playerHand = new HashSet<>();

	BufferedImage img;

	Frame frame;
	GameState game;
	JTabbedPane tabbedPane;

	JFrame dataFrame;
	JTextArea dataTextArea;
	
	public Main() {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				/**
				 * FRAME
				 */
				frame = new Frame(title);

				// set look and feel
				setLookAndFeel();

				frame.setLocationRelativeTo(null);
				frame.setResizable(false);
				frame.setUndecorated(true);
				frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

				frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				// popup the message box to ask user if they want to exit
				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						confirmExit();
					}
				});

				/**
				 * MENU BAR
				 */
				@SuppressWarnings("serial")
				JMenuBar menuBar = new JMenuBar() {
					@Override
					public void paintComponent(Graphics g) {
						super.paintComponent(g);

						// gradient color
						GradientPaint gradient = new GradientPaint(0, 0,
								menuBarBgColor1, 0, 12, menuBarBgColor2);

						Graphics2D g2d = (Graphics2D) g;
						g2d.setPaint(gradient);

						g2d.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
					}
				};
				menuBar.add(Box.createRigidArea(new Dimension(5, 22)));

				// add mouse listener to respond when the menu is pressed and
				// dragged
				menuBar.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						initClick = e.getPoint();
						frame.getComponentAt(initClick);
					}
				});
				menuBar.addMouseMotionListener(new MouseMotionAdapter() {
					@Override
					public void mouseDragged(MouseEvent e) {

						// get location of Window
						int thisX = frame.getLocation().x;
						int thisY = frame.getLocation().y;

						// Determine how much the mouse moved since the initial
						// click
						int xMoved = (thisX + e.getX()) - (thisX + initClick.x);
						int yMoved = (thisY + e.getY()) - (thisY + initClick.y);

						// Move window to this position
						int X = thisX + xMoved;
						int Y = thisY + yMoved;
						frame.setLocation(X, Y);
					}
				});

				/**
				 * FILE MENU
				 */
				JMenu menu = new JMenu("File");
				// add menu items to file menu
				addMenuItems(menu);
				// add file menu to menu bar
				menuBar.add(menu);

				/**
				 * OPTIONS MENU
				 */
				menu = new JMenu("Options");
				// add menu items to options menu
				addMenuItems(menu);
				// add menu to options menu bar
				menuBar.add(menu);

				/**
				 * EXIT MENU
				 */
				menu = new JMenu("Exit");
				// add listener to exit menu
				menu.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						confirmExit();
					}
				});
				// add menu to options menu bar
				menuBar.add(menu);

				// add menu to menu bar
				frame.add(menuBar, BorderLayout.PAGE_START);

				/**
				 * MAIN PANEL
				 */
				JPanel mainPanel = new JPanel();
				mainPanel.setLayout(new BorderLayout());
				mainPanel.setPreferredSize(new Dimension(WINDOW_WIDTH,
						WINDOW_HEIGHT));

				/**
				 * CONTROL PANEL
				 */
				JPanel controlPanel = new JPanel();
				controlPanel.setPreferredSize(new Dimension(mainPanel
						.getPreferredSize().width - BoardCanvas.CANVAS_WIDTH,
						BoardCanvas.CANVAS_HEIGHT));
				// add control panel to frame
				mainPanel.add(controlPanel, BorderLayout.EAST);

				/**
				 * TABBED PANE
				 */
				tabbedPane = new JTabbedPane();
				tabbedPane.setPreferredSize(new Dimension(328, WINDOW_HEIGHT));

				JPanel tabPanel = addTabPanel("Status");
				tabbedPane.addTab("Status", tabPanel);

				tabPanel = addTabPanel("Cards");
				tabbedPane.addTab("Cards", tabPanel);

				tabPanel = addTabPanel("Actions");
				tabbedPane.addTab("Actions", tabPanel);				
				
				tabbedPane.addChangeListener(new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent e) {
						if (tabbedPane.getSelectedIndex() == 1)
							tabbedPane.setComponentAt(1, addTabPanel("Cards"));
						System.out.println("CHANGED TAB!");
					}
				});
				
				controlPanel.add(tabbedPane, BorderLayout.WEST);
				// The following line enables to use scrolling tabs.
				tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

				/**
				 * GAME PANEL
				 */
				game = new GameState();
				// add game panel to main panel
				mainPanel.add(game.getBoardCanvas(), BorderLayout.WEST);

				// add main panel to frame
				frame.add(mainPanel);
				frame.pack();

				// display the window
				frame.setVisible(true);
			}
		});
	}

	private void setLookAndFeel() {
		UIDefaults ui = UIManager.getLookAndFeelDefaults();
		// menu and menubar
		ui.put("Menu.foreground", menuFontColor);
		ui.put("Menu.background", menuBgColor);
		ui.put("MenuItem.background", menuItemBgColor);
		ui.put("MenuItem.foreground", menuItemFontColor);
		// tabbed pane
		ui.put("TabbedPane.contentAreaColor", mainPanelBgColor);
		ui.put("TabbedPane.background", mainPanelBgColor);
		ui.put("TabbedPane.foreground", mainPanelFontColor);
		ui.put("TabbedPane.selected", mainPanelBgColor);
		ui.put("TabbedPane.borderHightlightColor", mainPanelBgColor);
		ui.put("TabbedPane.focus", mainPanelfocusColor);
		// label
		ui.put("Label.foreground", labelFontColor);
		// panel
		ui.put("Panel.background", mainPanelBgColor);
		ui.put("Panel.foreground", mainPanelFontColor);
		// option pane
		ui.put("OptionPane.background", mainPanelBgColor);
		ui.put("OptionPane.foreground", mainPanelFontColor);
		ui.put("OptionPane.messageForeground", mainPanelFontColor);
	}

	protected JPanel addTabPanel(String text) {
		JPanel panel = new JPanel();
		if (text.equals("Status")) {
			JPanel subPanel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			subPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();

			gbc.fill = GridBagConstraints.HORIZONTAL;
			// add padding by 5px
			gbc.insets = new Insets(5, 0, 0, 0);
			gbc.ipadx = 80;
			gbc.gridy = 0;

			JPanel dicePanel = new JPanel();

			// dice image
			diceImage1 = new JLabel();
			diceImage1.setHorizontalAlignment(JLabel.CENTER);
			diceImage1.setPreferredSize(new Dimension(40, 40));

			dicePanel.add(diceImage1, BorderLayout.WEST);

			diceImage2 = new JLabel();
			diceImage2.setHorizontalAlignment(JLabel.CENTER);
			diceImage2.setPreferredSize(new Dimension(40, 40));

			dicePanel.add(diceImage2, BorderLayout.EAST);

			subPanel.add(dicePanel, gbc);

			gbc.gridy += 1;

			// roll dice button
			rollDiceButton = new JButton("Roll Dice");
			rollDiceButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (game != null && game.getBoard() != null
							&& GameState.state == GameState.StateOfGame.PLAYING) {
						if (!game.getPossibleActions().contains(
								GameState.TurnState.MOVE_VIA_DICE)) {
							JOptionPane.showMessageDialog(null,
									"Sorry, but you can't move now.");
							rollDiceButton.setEnabled(false);
							return;
						}
						game.goThroughTurn(GameState.TurnState.MOVE_VIA_DICE);
						game.goThroughTurn(GameState.TurnState.MOVE_VIA_SP);
						game.addPossibleActions(GameState.TurnState.CHOOSE_SPACE);
						int i;
						if (game.getCurrentCharacter().isInRoom())
							i = (int) (2 + Math.round(Math.random() * 10));
						else
							i = (int) (2 + Math.round(Math.random() * 10));
						game.getBoard().setMovementAllowed(i);
						// show dice
						showDice(i);

						System.out.println("Number of spaces you can move = "
								+ i);
						if (!game.getCurrentCharacter().isInRoom()) {
							List<Location> locs = game.getBoard().getMoves(
									game.getCurrentCharacter(), i);
							game.getBoard().setValidMoves(locs);
						} else {
							List<List<Location>> locLists = new ArrayList<>();
							List<Location> exits = game.getBoard()
									.findClearExits(
											game.getCurrentCharacter()
													.getRoom());
							List<Location> validMoves = new ArrayList<>();
							for (Location L : game.getCurrentCharacter()
									.getRoom().doors)
								L.setAccessible(false);
							for (Location l : exits) {
								locLists.add(game.getBoard().getMoves(l,
										game.getBoard().getMovementAllowed()));
							}
							for (List<Location> list : locLists) {
								for (Location l : list) {
									validMoves.add(l);
								}
							}
							game.getBoard().setValidMoves(validMoves);
						}
						rollDiceButton.setEnabled(false);
						usePassageButton.setEnabled(false);
						game.getBoardCanvas().repaint();
					}
				}
			});

			subPanel.add(rollDiceButton, gbc);
			gbc.gridy += 1;

			// add padding by 5px
			gbc.insets = new Insets(5, 0, 0, 0);

			// use passage button
			usePassageButton = new JButton("Use Passage");
			usePassageButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (game != null && game.getBoard() != null
							&& GameState.state == GameState.StateOfGame.PLAYING) {
						if (!game.getPossibleActions().contains(
								GameState.TurnState.MOVE_VIA_SP)) {
							JOptionPane
									.showMessageDialog(null,
											"Sorry, but you can't use a Secret Passage now.");
							usePassageButton.setEnabled(false);
							return;
						}
						int value = JOptionPane.showConfirmDialog(null,
								"Are you serious?", "Use Secret Passage",
								JOptionPane.YES_NO_OPTION);
						if (value == 0) {
							game.goThroughTurn(GameState.TurnState.MOVE_VIA_DICE);
							game.goThroughTurn(GameState.TurnState.MOVE_VIA_SP);
							Character c = game.getCurrentCharacter();
							Room room = game.getCurrentCharacter().getRoom();
							List<Room> rooms = game.getBoard().getRooms();
							for (Room r : rooms) {
								if (room.getConnectingRoom().equals(
										r.getRoomName())) {
									c.setRoom(r);
									c.setOldRoom(r);
									for (Location l : c.getRoom().spaces) {
										if (l.getPieceOn() == null
												&& !c.getRoom().doors
														.contains(l)) {
											c.setPosition(l);
											break;
										}
									}
									c.setSuggestionMade(false);
									if (!game
											.getPossibleActions()
											.contains(
													GameState.TurnState.MAKE_SUGGESTION))
										game.addPossibleActions(GameState.TurnState.MAKE_SUGGESTION);
									JOptionPane.showMessageDialog(null, c.name
											+ " has moved from the "
											+ room.getRoomName().toUpperCase()
											+ " to the "
											+ r.getRoomName().toUpperCase()
											+ "!");
									break;
								}
							}
							rollDiceButton.setEnabled(false);
							usePassageButton.setEnabled(false);
							game.getBoardCanvas().repaint();
						}
					}
				}
			});
			subPanel.add(usePassageButton, gbc);

			gbc.gridy += 1;

			// end Turn button
			endTurnButton = new JButton("End Turn");
			endTurnButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (game != null && game.getBoard() != null
							&& GameState.state == GameState.StateOfGame.PLAYING) {
						if (game.getCurrentCharacter().isEliminated()) {
							rollDiceButton.setEnabled(true);
							usePassageButton.setEnabled(true);
							suggestButton.setEnabled(true);
							accuseButton.setEnabled(true);

							if (game.getCurrentCharacter().isInRoom())
								for (Location l : game.getCurrentCharacter()
										.getRoom().doors)
									l.setAccessible(true);
							else
								game.getCurrentCharacter()
										.setPosition(
												game.getBoard()
														.getStart(
																game.getCurrentCharacter().token - 1));
							diceImage1.setIcon(null);
							diceImage2.setIcon(null);
							if (dataFrame != null)
								dataFrame.dispose();
							dataFrame = null;
							game.EndTurnNow();
							game.getBoardCanvas().repaint();
							return;
						}
						int value = JOptionPane.showConfirmDialog(null,
								"Do you want to End your turn?",
								"End Turn Confirmation",
								JOptionPane.YES_NO_OPTION);
						if (value == 0
								&& !game.getPossibleActions().contains(
										GameState.TurnState.CHOOSE_SPACE)
								&& !game.getPossibleActions().contains(
										GameState.TurnState.MAKE_SUGGESTION)) {
							rollDiceButton.setEnabled(true);
							usePassageButton.setEnabled(true);
							suggestButton.setEnabled(true);
							accuseButton.setEnabled(true);

							if (game.getCurrentCharacter().isInRoom())
								for (Location l : game.getCurrentCharacter()
										.getRoom().doors)
									l.setAccessible(true);
							diceImage1.setIcon(null);
							diceImage2.setIcon(null);
							game.EndTurnNow();
							game.getBoardCanvas().repaint();
						} else if (value == 0
								&& game.getPossibleActions().contains(
										GameState.TurnState.CHOOSE_SPACE)) {
							value = JOptionPane.showConfirmDialog(null,
									"... Ummmmm... You CAN actually move...\n"
											+ "Really End your Turn?",
									"End Turn Confirmation",
									JOptionPane.YES_NO_OPTION);
							if (value == 0) {
								JOptionPane
										.showMessageDialog(null,
												"... Wow. Uhhhh... Okaaaaaaaay then...?");
								rollDiceButton.setEnabled(true);
								usePassageButton.setEnabled(true);
								suggestButton.setEnabled(true);
								accuseButton.setEnabled(true);

								if (game.getCurrentCharacter().isInRoom())
									for (Location l : game
											.getCurrentCharacter().getRoom().doors)
										l.setAccessible(true);
								diceImage1.setIcon(null);
								diceImage2.setIcon(null);
								game.EndTurnNow();
								game.getBoardCanvas().repaint();
								return;
							}
						} else if (value == 0
								&& game.getPossibleActions().contains(
										GameState.TurnState.MAKE_SUGGESTION)) {
							value = JOptionPane
									.showConfirmDialog(
											null,
											"You have NOT made a suggestion, despite being"
													+ "able to do so (Actions Tab).\nReally end your turn?",
											"End Turn Confirmation",
											JOptionPane.YES_NO_OPTION);
							if (value == 0) {
								rollDiceButton.setEnabled(true);
								usePassageButton.setEnabled(true);
								suggestButton.setEnabled(true);
								accuseButton.setEnabled(true);

								if (game.getCurrentCharacter().isInRoom())
									for (Location l : game
											.getCurrentCharacter().getRoom().doors)
										l.setAccessible(true);
								diceImage1.setIcon(null);
								diceImage2.setIcon(null);
								game.EndTurnNow();
								game.getBoardCanvas().repaint();
								return;
							}
						}
					}
				}
			});

			subPanel.add(endTurnButton, gbc);
			panel.add(subPanel);

		} else if (text.equals("Cards")) {
			//FIXME: CARDS CAN DRAW OFF THE EDGE!!
			JPanel subPanel = new JPanel();
			// sub panel
			subPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();

			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.ipadx = 60;
			// add padding by 3px
			gbc.insets = new Insets(3, 0, 0, 0);
			gbc.gridy = 0;
			
			if (game != null && game.getTurnOrder() != null && 
					!game.getTurnOrder().isEmpty() && game.getCurrentCharacter() != null){
				playerHand.clear();
				List<Card> hand = game.getCurrentCharacter().getHand();
				
				JLabel label = new JLabel(game.getCurrentCharacter().name + "'s Cards:");
				subPanel.add(label, gbc);
				gbc.gridy += 1;
				
				for (int i = 0; i < hand.size(); i++){
					label = new JLabel();
					playerHand.add(label);
					String cardType = hand.get(i).getCardType();
					String type = null;
					String cardName = hand.get(i).getCardName();
					switch (cardType){
					case "Character": 
						type = "chars";
						break;
					case "Weapon": 
						type = "weapons";
						break;
					case "Room": 
						type = "rooms";
						break;
					}
					ImageIcon ii;
					if (!type.equals("weapons"))
						ii = new ImageIcon(this.getClass().getResource(
									imagePath + "cards/" + type + "/" + cardName
											+ ".png"));
					else
						ii = new ImageIcon(this.getClass().getResource(
								imagePath + "cards/" + type + "/" + cardName
										+ ".jpg"));
					label.setIcon(ii);
					label.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
					subPanel.add(label, gbc);
					label.setVisible(true);
					subPanel.repaint();
					
					if (i % 2 == 0){
						gbc.gridy += 1;
						gbc.gridx = 0;
					}
					else 
						gbc.gridx += 1;
				}
			}
			panel.add(subPanel);
			//FIXME: CARDS CAN DRAW OFF THE EDGE!!
		}

		else if (text.equals("Actions")) {
			JPanel subPanel = new JPanel();
			// sub panel
			subPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();

			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.ipadx = 80;
			// add padding by 5px
			gbc.insets = new Insets(5, 0, 0, 0);
			gbc.gridy = 0;

			JLabel label = new JLabel("Select Character");
			subPanel.add(label, gbc);

			gbc.gridy += 1;

			charCB = new JComboBox<String>(Data.charNames);
			charCB.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					selectChar();
				}
			});
			subPanel.add(charCB, gbc);

			gbc.gridy += 1;

			charImage = new JLabel();
			charImage.setHorizontalAlignment(JLabel.CENTER);
			charImage.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
			subPanel.add(charImage, gbc);

			gbc.gridy += 1;

			label = new JLabel("Select Weapon");
			subPanel.add(label, gbc);

			gbc.gridy += 1;

			weaponCB = new JComboBox<String>(Data.weaponNames);
			weaponCB.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					selectWeapon();
				}
			});
			subPanel.add(weaponCB, gbc);

			gbc.gridy += 1;

			weaponImage = new JLabel();
			weaponImage.setHorizontalAlignment(JLabel.CENTER);
			weaponImage.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
			subPanel.add(weaponImage, gbc);

			gbc.gridy += 1;

			label = new JLabel("Select Room");
			subPanel.add(label, gbc);

			gbc.gridy += 1;

			roomCB = new JComboBox<String>(Data.roomNames);
			roomCB.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					selectRoom();
				}
			});
			subPanel.add(roomCB, gbc);

			gbc.gridy += 1;

			roomImage = new JLabel();
			roomImage.setHorizontalAlignment(JLabel.CENTER);
			roomImage.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
			subPanel.add(roomImage, gbc);

			gbc.gridy += 1;

			// suggest button
			suggestButton = new JButton("Make Suggestion");
			suggestButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (game != null && game.getBoard() != null
							&& GameState.state == GameState.StateOfGame.PLAYING) {
						if (!game.getPossibleActions().contains(
								GameState.TurnState.MAKE_SUGGESTION)) {
							if (game.getCurrentCharacter().isInRoom())
								JOptionPane
										.showMessageDialog(null,
												"Sorry, but you can't make a suggestion now.");
							else
								JOptionPane
										.showMessageDialog(null,
												"GET A ROOM! (Pro Tip: Enter a room to make a Suggestion!)");
							return;
						}
						Queue<Character> characters = game.getTurnOrder();
						Character SUGGESTINGPlayer = game.getCurrentCharacter();
						Character currentPlayer = game.getCurrentCharacter();
						if (SUGGESTINGPlayer.isSuggestionMade()
								|| !game.getPossibleActions().contains(
										GameState.TurnState.MAKE_SUGGESTION)) {
							JOptionPane
									.showMessageDialog(null,
											"Sorry, but you can't make a suggestion now.\nMove to a different room.");
							return;
						}
						rollDiceButton.setEnabled(false);
						usePassageButton.setEnabled(false);
						suggestButton.setEnabled(false);
						SUGGESTINGPlayer.setSuggestionMade(true);
						String room = currentPlayer.getRoom().getRoomName();
						String character = (String) charCB.getSelectedItem();
						String weapon = (String) weaponCB.getSelectedItem();
						// MOVE CHARACTER
						int token = 0;
						for (int i = 0; i < Data.charNames.length; i++) {
							if (character.equals(Data.charNames[i])) {
								token = i + 1;
								break;
							}
						}
						Piece accuesed = null;
						for (Piece p : game.getBoard().getPiece()) {
							if (p.getToken() == token) {
								accuesed = p;
								break;
							}
						}
						if (accuesed.getRoom() != null
								&& !accuesed.getRoom().equals(
										SUGGESTINGPlayer.getRoom())) {
							accuesed.setInRoom(true);
							accuesed.setRoom(SUGGESTINGPlayer.getRoom());
							accuesed.setOldRoom(SUGGESTINGPlayer.getRoom());
							for (Location l : SUGGESTINGPlayer.getRoom().spaces) {
								if (l.getPieceOn() == null
										&& !SUGGESTINGPlayer.getRoom().doors
												.contains(l)) {
									accuesed.setPosition(l);
									break;
								}
							}
							if (accuesed.isCharacter()) {
								Character a = (Character) accuesed;
								a.setSuggestionMade(false);
							}
						}
						game.getBoardCanvas().repaint();
						// MOVE WEAPONS
						Weapon wep = null;
						for (int i = 0; i < Data.weaponNames.length; i++) {
							if (weapon.equals(Data.weaponNames[i])) {
								wep = game.getBoard().getWeaponry().get(i);
							}
						}
						if (wep == null
								|| (wep == null && !wep.getRoom().equals(
										SUGGESTINGPlayer.getRoom()))) {
							for (Room r : game.getBoard().getRooms()) {
								if (r.getWeapon().equals(wep)) {
									Weapon tempWep = SUGGESTINGPlayer.getRoom()
											.getWeapon();
									SUGGESTINGPlayer.getRoom().setWeapon(
											r.getWeapon());
									r.setWeapon(tempWep);
									break;
								}
							}
						}
						accuesed.setInRoom(true);
						accuesed.setRoom(SUGGESTINGPlayer.getRoom());
						accuesed.setOldRoom(SUGGESTINGPlayer.getRoom());
						for (Location l : SUGGESTINGPlayer.getRoom().spaces) {
							if (l.getPieceOn() == null
									&& !SUGGESTINGPlayer.getRoom().doors
											.contains(l)) {
								accuesed.setPosition(l);
								break;
							}
						}
						// LET THE USER SEE WHAT THEY'RE SUGGESTING
						String Dialoge = "You suggest the crime was committed in the "
								+ room
								+ " by "
								+ character
								+ " with the "
								+ weapon + "...";
						JOptionPane.showMessageDialog(null, Dialoge);
						game.getCurrentCharacter().setData(Dialoge);
						characters.offer(characters.poll());
						currentPlayer = game.getCurrentCharacter();
						boolean refuted = false;
						Card refutingCard = null;
						Character refutingChar = null;
						while (!SUGGESTINGPlayer.equals(currentPlayer)
								&& !refuted) {
							List<Card> hand = currentPlayer.getHand();
							for (Card c : hand) {
								System.out.println(c);
								if (c.getCardName().equals(room)
										|| c.getCardName().equals(weapon)
										|| c.getCardName().equals(character)) {
									refuted = true;
									refutingCard = c;
									refutingChar = currentPlayer;
									currentPlayer = game.getCurrentCharacter();
									break;
								}
							}
							characters.offer(characters.poll());
							currentPlayer = characters.peek();
						}
						if (refuted)
							Dialoge = refutingChar
									+ " refuted your suggestion by showing you the "
									+ refutingCard.getCardName().toUpperCase()
									+ " Card!\nTherefore, the "
									+ refutingCard.getCardName().toUpperCase()
									+ " Card CANNOT be in the envelope.";
						else
							Dialoge = "NO ONE was able to refute the suggestion you made regarding the crime being "
									+ "committed in the "
									+ room.toUpperCase()
									+ " by "
									+ character.toUpperCase()
									+ " with the " + weapon.toUpperCase() + "!";
						JOptionPane.showMessageDialog(null, Dialoge);
						SUGGESTINGPlayer.setData(Dialoge);
						game.goThroughTurn(GameState.TurnState.MAKE_SUGGESTION);
						game.goThroughTurn(GameState.TurnState.MOVE_VIA_DICE);
						game.goThroughTurn(GameState.TurnState.MOVE_VIA_SP);
						while (!SUGGESTINGPlayer.equals(game
								.getCurrentCharacter())) {
							characters.offer(characters.poll());
						}
						if (dataTextArea != null)
							dataTextArea.setText(SUGGESTINGPlayer.getData());
					}
				}
			});
			subPanel.add(suggestButton, gbc);

			gbc.gridy += 1;

			// accuse button
			accuseButton = new JButton("Make Accusation");
			accuseButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (game != null && game.getBoard() != null
							&& GameState.state == GameState.StateOfGame.PLAYING) {
						if (game.getPossibleActions().contains(
								GameState.TurnState.MAKE_ACCUSATION)) {
							JOptionPane
									.showMessageDialog(
											null,
											"WARNING!! YOU ARE ABOUT TO MAKE AN ACCUSATION!\nIF YOU ARE "
													+ "INCORRECT ON ANY DETAIL, YOU WILL BE ASSA- I MEAN- ELIMINATED!!");
							int value = JOptionPane
									.showConfirmDialog(
											null,
											"Do you really want to make an accusation?",
											"Make an Accusation",
											JOptionPane.YES_NO_OPTION);
							if (value == 0) {
								value = JOptionPane.showConfirmDialog(null,
										"Are you Serious?",
										"You are Making an Accusation...",
										JOptionPane.YES_NO_OPTION);
								if (value == 0) {
									JOptionPane
											.showMessageDialog(null,
													"Okay then, I hope you're right...");
									String room = (String) roomCB
											.getSelectedItem();
									String character = (String) charCB
											.getSelectedItem();
									String weapon = (String) weaponCB
											.getSelectedItem();
									String Dialoge = "You accuse "
											+ character
											+ " of committing the crime in the "
											+ room + " with the " + weapon
											+ "...";
									JOptionPane
											.showMessageDialog(null, Dialoge);
									boolean correct = game
											.getBoard()
											.getEnvelope()
											.accusationIsCorrect(character,
													room, weapon);
									if (correct) {
										Dialoge = "CORRECT ON ALL COUNTS! YOU WIN!!";
										game.endGame();
									} else {
										Dialoge = "WRONG!! THE CARDS WERE:\n"
												+ game.getBoard().getEnvelope()
														.getcCard()
												+ "\n"
												+ game.getBoard().getEnvelope()
														.getrCard()
												+ "\n"
												+ game.getBoard().getEnvelope()
														.getwCard()
												+ "\n\nYOU HAVE BEEN ELIMINATED!";
									}
									JOptionPane
											.showMessageDialog(null, Dialoge);
									game.goThroughTurn(GameState.TurnState.MAKE_ACCUSATION);
									if (!correct) {
										game.eliminateCharacter();
										endTurnButton.doClick();
									}
								}
							}
						}
					}
				}
			});
			subPanel.add(accuseButton, gbc);
			panel.add(subPanel, BorderLayout.NORTH);

		}
		return panel;
	}
	

	/**
	 * show dice image when user rolled the dice
	 * 
	 * @param i
	 *            total number of dices
	 */
	public void showDice(int i) {
		if (i > 0) {
			if (i <= 6) {
				ImageIcon ii = new ImageIcon(this.getClass().getResource(
						imagePath + "dice/" + i + ".png"));
				diceImage1.setIcon(ii);
			} else {
				ImageIcon ii = new ImageIcon(this.getClass().getResource(
						imagePath + "dice/" + 6 + ".png"));
				diceImage1.setIcon(ii);
				ii = new ImageIcon(this.getClass().getResource(
						imagePath + "dice/" + i % 6 + ".png"));
				diceImage2.setIcon(ii);
			}
		}
	}
	

	/**
	 * Show character card selected in Action Tab
	 */
	public void selectChar() {
		String name = (String) charCB.getSelectedItem();
		for (int i = 0; i < Data.charNames.length; i++) {
			if (name.equals(Data.charNames[i])) {
				ImageIcon ii = new ImageIcon(this.getClass()
						.getResource(
								imagePath + "cards/chars/" + Data.charNames[i]
										+ ".png"));
				charImage.setIcon(ii);
			}
		}
	}
	

	/**
	 * Show weapon card selected in Action Tab
	 */
	public void selectWeapon() {
		String name = (String) weaponCB.getSelectedItem();
		for (int i = 0; i < Data.weaponNames.length; i++) {
			if (name.equals(Data.weaponNames[i])) {
				ImageIcon ii = new ImageIcon(this.getClass().getResource(
						imagePath + "cards/weapons/" + Data.weaponNames[i]
								+ ".jpg"));
				weaponImage.setIcon(ii);
			}
		}
	}
	

	/**
	 * Show room card selected in Action Tab
	 */
	public void selectRoom() {
		String name = (String) roomCB.getSelectedItem();
		for (int i = 0; i < Data.roomNames.length; i++) {
			if (name.equals(Data.roomNames[i])) {
				ImageIcon ii = new ImageIcon(this.getClass()
						.getResource(
								imagePath + "cards/rooms/" + Data.roomNames[i]
										+ ".png"));
				roomImage.setIcon(ii);
			}
		}
	}
	

	/**
	 * Pop up dialog to ask total players participating the game
	 * 
	 * @return the total players
	 */
	public int askTotalPlayer() {
		int playerNum = 0;
		while (playerNum == 0) {
			String s = JOptionPane
					.showInputDialog("Please insert the number of players (3 - 6).");
			try {
				playerNum = Integer.parseInt(s);
				GameState.expectedNumPlayers = playerNum;
				if (playerNum < 3 || playerNum > 6) {
					JOptionPane.showMessageDialog(null,
							"Please choose a number 3 - 6 (inclusive).");
					playerNum = 0;
				}
			} catch (NumberFormatException ex) {
				playerNum = 0;
				JOptionPane.showMessageDialog(null,
						"... I said NUMBER please. A NUMBER from 3 - 6.\n"
								+ "That means 3, 4, 5 or 6.");
			}
		}
		return playerNum;
	}
	

	/**
	 * Ask each player name and character playing the game
	 * 
	 * @param playerNum
	 *            total player participating
	 * @return Queue of character playing
	 */
	public Queue<Character> askPlayerDetails(int playerNum) {
		final List<Character> characters = new ArrayList<>();
		final Queue<Character> order = new ArrayDeque<>();

		for (int i = 0; i < playerNum; i++) {
			final JFrame frame = new JFrame("Character Selection");
			frame.setAlwaysOnTop(true);
			frame.setAutoRequestFocus(true);
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

			frame.setPreferredSize(new Dimension(175, 300));
			frame.setLocationRelativeTo(null);
			JPanel panel = new JPanel();
			final JLabel nameLabel = new JLabel("Name: ");
			final JLabel charLabel = new JLabel("Character: ");
			ButtonGroup characterGroup = new ButtonGroup();

			final JTextField field = new JTextField("Player " + (i + 1));
			field.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					nameLabel.setText("Name: " + field.getText());
				}
			});

			// create radio button so player can choose character
			final List<JRadioButton> radioButtons = new ArrayList<JRadioButton>();

			for (int j = 0; j < Data.charNames.length; j++) {
				JRadioButton radioButton = new JRadioButton(Data.charNames[j]);
				final int index = j;
				radioButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						charLabel.setText("Character: " + (index + 1) + "("
								+ Data.charNames[index] + ")");
					}
				});
				radioButtons.add(radioButton);
			}

			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean no = false;
					boolean charSelected = false;

					// check if radio button is selected
					for (int j = 0; j < radioButtons.size(); j++) {
						if (radioButtons.get(j).isSelected()) {
							charSelected = true;
							break;
						}
					}

					if (field.getText().equals("") || !charSelected) {
						JOptionPane
								.showMessageDialog(null,
										"Please enter your name and choose a character.");
					} else {
						for (Character c : characters) {
							int i = c.token;
							if (Integer.parseInt(charLabel.getText().substring(
									11, 12)) == i) {
								no = true;
								JOptionPane.showMessageDialog(null,
										"Sorry, this character is taken.");

								radioButtons.get(i - 1).setEnabled(false);
							}
						}
						if (!no) {
							characters.add(new Character(field.getText(),
									Integer.parseInt(charLabel.getText()
											.substring(11, 12))));
							order.offer(characters.get(characters.size() - 1));
							game.createBoard(characters);
							frame.dispose();
						}
					}
				}
			});
			frame.setContentPane(panel);
			panel.add(nameLabel);
			panel.add(field);
			panel.add(charLabel);

			for (int j = 0; j < radioButtons.size(); j++) {
				characterGroup.add(radioButtons.get(j));
				panel.add(radioButtons.get(j));
			}
			panel.add(okButton);
			frame.pack();
			frame.setVisible(true);
		}

		return order;
	}


	/**
	 * This method is adding items to menu bar
	 * 
	 * @param menu
	 *            the menu bar of the main game
	 */
	public void addMenuItems(JMenu menu) {
		if (menu.getText().equals("File")) {
			JMenuItem fileMenuItem = new JMenuItem("New Game");

			fileMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int value = JOptionPane.showConfirmDialog(null,
							"Do you want to start a new game?",
							"Start New Game", JOptionPane.YES_NO_OPTION);
					if (value == 0) {
						GameState.state = GameState.StateOfGame.NOTHINGS_HAPPENING;
						rollDiceButton.setEnabled(true);
						usePassageButton.setEnabled(true);
						suggestButton.setEnabled(true);

						int playerNum = askTotalPlayer();

						GameState.expectedNumPlayers = playerNum;

						game.setTurnOrder(askPlayerDetails(playerNum));
					}
				}
			});
			menu.add(fileMenuItem);

			fileMenuItem = new JMenuItem("Save Game");
			fileMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// save game
				}
			});
			menu.add(fileMenuItem);

			fileMenuItem = new JMenuItem("Load Game");
			fileMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// load game
				}
			});
			menu.add(fileMenuItem);
		} else if (menu.getText().equals("Options")) {
			JMenuItem fileMenuItem = new JMenuItem("Help");
			fileMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					final JFrame frame = new JFrame("About the Game");
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frame.setPreferredSize(new Dimension(300, 600));
					frame.setLocationRelativeTo(null);
					JTextArea textArea = new JTextArea();
					JScrollPane scrollPane = new JScrollPane(textArea);
					textArea.setEditable(false);
					textArea.setLineWrap(true);
					textArea.setWrapStyleWord(true);
					frame.setContentPane(scrollPane);
					frame.setResizable(false);
					
					textArea.setText(Data.helpDes);
					textArea.setCaretPosition(0);
					
					frame.pack();
					frame.setVisible(true);
				}
			});
			menu.add(fileMenuItem);

			fileMenuItem = new JMenuItem("Show Data");
			fileMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (game != null && !game.getTurnOrder().isEmpty() && game.getCurrentCharacter() != null){ 
						Character c = game.getCurrentCharacter();
						
						final JFrame frame = new JFrame(c.name + "'s Notes");
						frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
						frame.addWindowListener(new WindowAdapter() {
							@Override
							public void windowClosing(WindowEvent e) {
								dataFrame = null;
								dataTextArea = null;
								frame.dispose();
							}
						});
						frame.setPreferredSize(new Dimension(300, 600));
						frame.setLocationRelativeTo(null);
						JTextArea textArea = new JTextArea();
						JScrollPane scrollPane = new JScrollPane(textArea);
						textArea.setEditable(false);
						textArea.setLineWrap(true);
						textArea.setWrapStyleWord(true);
						frame.setContentPane(scrollPane);
						frame.setResizable(false);
						
						textArea.setText(c.getData());
						textArea.setCaretPosition(0);
						
						frame.pack();
						frame.setVisible(true);
						dataFrame = frame;
						dataTextArea = textArea;
					
					}
				}
			});
			menu.add(fileMenuItem);
		}
	}

	/**
	 * Pop up dialog to configure the game
	 */
	public void options() {

	}

	/**
	 * Pop up dialog to ask user if he wants to exit the game
	 */
	public void confirmExit() {
		int value = JOptionPane.showConfirmDialog(null,
				"Do you want to exit the game?", "Exit Confirmation",
				JOptionPane.YES_NO_OPTION);
		if (value == 0)
			System.exit(0);
	}

	public static void main(String[] args) {
		new Main();
	}
}
