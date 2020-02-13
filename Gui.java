package chess;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static javax.swing.JFrame.setDefaultLookAndFeelDecorated;
import static javax.swing.SwingUtilities.*;
import static javax.swing.JOptionPane.showMessageDialog;

/** CREDIT TO: github.com/amir650 for everything related to the
    GUI visuals part of this project, including all images used.
*/


/* A Gui to play chess on */
class Gui {

	static final long TOUT = 1500;

	private BoardPanel bp;
	private static JFrame game;
	private final String filePath = "chess/images/";

	private static Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
	private static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 35);
	private static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);

	private Square sourceTile;
	private Square destinationTile;
	private Piece humanMovedPiece;
	private Square aimoved;
	private List<Square> legalSquares;
	private boolean flip;

	static final Color LIGHT_TILE_COLOR = new Color(238, 207, 161);
	static final Color DARK_TILE_COLOR = new Color(158, 102, 46);
	static final Color CLICKED_TILE_COLOR = new Color(245, 197, 66);
	static final Color AI_MOVED_TILE_COLOR = new Color(66, 200, 245);
	static final Color LEGAL_SQUARE_COLOR = new Color(255, 222, 130);

	private Board b;
	private boolean winner_reported;

	/** The player playing white or black. If NULL, then AI inputs are used to
		make moves for this player. Otherwise, the gui will prompt the player 
		to make its moves.
	*/
	private Player white;
	private Player black;


	
	Gui(Board brd, Player p1, Player p2) {
		flip = false;
		winner_reported = false;
		b = brd;
		legalSquares = new ArrayList<Square>();
		game = new JFrame("Chess");
		game.setLayout(new BorderLayout());
		final JMenuBar gameMenuBar = new JMenuBar();
		populate(gameMenuBar);
		game.setJMenuBar(gameMenuBar);
		game.setSize(OUTER_FRAME_DIMENSION);
		bp = new BoardPanel();
		game.add(bp, BorderLayout.CENTER);
		game.setVisible(true);
		white = p1;
		black = p2;
		handle();
	}

	void flipBoard() {
		bp.flip();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				bp.drawBoard(b);
			}
		});
	}

	void unFlipBoard() {
		bp.unFlip();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				bp.drawBoard(b);
			}
		});
	}

	/** Handles the GUI for AI Players. This will:
		1) Make a move for an AI if it is their turn,
		2) repaint the board after each move,
		3) Repeatedly make moves and update the board
		if both players are AIs, and
		4) display a message when the game ends.
	*/
	void handle() {
		Player playing = (b.getTurn().equals("White")) ? white : black;
		String winner = b.winner();
		//System.out.println(b.winner());
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				bp.drawBoard(b);
			}
		});
		if (winner != null && !winner_reported) {
			winner_reported = true;
			if (winner.equals("Draw")) {
				System.out.println("The Game is a Draw.");
				showMessageDialog(null, "The Game is a Draw.");
				bp.drawBoard(b);
				return;
			} else  if (winner.equals("Draw rep")) {
				System.out.println("The Game is a Draw By Three-Fold Repetition.");
				showMessageDialog(null, "The Game is a Draw By Three-Fold Repetition.");
				bp.drawBoard(b);
				return;
			} else {
				System.out.println("Checkmate! " + winner + " Wins.");
				showMessageDialog(null, "Checkmate! " + winner + " Wins.");
				//System.out.println(b.toString());
				Iterator<Move> it = b.allMoves();
				//System.out.println("Printing legal moves for " + b.getTurn());
				while(it.hasNext()) { System.out.println(it.next().getSymbol()); }
				//System.out.println("Rook iterator starts here");
				//System.out.println(b.get(5, 0).getPiece().isLegal(b.get(5, 0), b.get(3, 0)));
				//Iterator<Move> rk = b.get(5, 0).getPiece().legalMoves();
				//while (rk.hasNext()) { System.out.println(rk.next().getSymbol()); }
				bp.drawBoard(b);
				return;
			}
		}
		if (playing != null) {
			playing.makeMove();
			aimoved = b.getLastMove().getTo();
			bp.drawBoard(b);
			winner = b.winner();
			if (winner != null) {
				if (winner.equals("Draw")) {
					System.out.println("The Game is a Draw.");
					showMessageDialog(null, "The Game is a Draw.");
					bp.drawBoard(b);
					return;
				} else  if (winner.equals("Draw rep")) {
					System.out.println("The Game is a Draw By Three-Fold Repetition.");
					showMessageDialog(null, "The Game is a Draw By Three-Fold Repetition.");
					bp.drawBoard(b);
					return;
				} else {
					System.out.println("Checkmate! " + winner + " Wins.");
					showMessageDialog(null, "Checkmate! " + winner + " Wins.");
					bp.drawBoard(b);
					return;
				}
			}
			/*SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					bp.drawBoard(b);
				}
			});*/
			long start = System.currentTimeMillis();
			/*
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					bp.drawBoard(b);
				}
			});*/
			
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				bp.drawBoard(b);
			}
		});

		playing = (b.getTurn().equals("White")) ? white : black;
		if (playing != null) {
			SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				handle();
			}
		});
			return;
		}
		
	}

	/*
	void makeMove(String color) {
		if (color.equals("Black")) {
			black.makeMove();
		} else if (color.equals("White")) {
			white.makeMove();
		}
	}*/

	

	private class TilePanel extends JPanel {

		private final int x;
		private final int y;

		TilePanel(BoardPanel bp, int i, int j) {
			super(new GridBagLayout());
			x = i;
			y = j;
			setSize(TILE_PANEL_DIMENSION);
			assignTileColor();
			assignIcon(b);

			addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					if (isLeftMouseButton(e)) {
						if (sourceTile == null) {
							//first click
							if (b.get(x, y).getPiece().getColor().equals(b.getTurn())) {
								sourceTile = b.get(x, y);
								sourceTile.click();
								humanMovedPiece = sourceTile.getPiece();
								Iterator<Move> it = sourceTile.getPiece().legalMoves();
								while (it.hasNext())  {
									legalSquares.add(it.next().getTo());
								}
								//System.out.println(legalSquares.size());
							}
							if (humanMovedPiece instanceof Nopiece) {
								sourceTile = null;
								destinationTile = null;
							}
						} else if (b.get(x, y).getPiece().getColor().equals(b.getTurn()) && b.get(x, y) != sourceTile) {
							sourceTile.unclick();
							legalSquares.clear();
							sourceTile = b.get(x, y);
							sourceTile.click();
							humanMovedPiece = sourceTile.getPiece();
							Iterator<Move> it = sourceTile.getPiece().legalMoves();
							while (it.hasNext())  {
								legalSquares.add(it.next().getTo());
							}

						} else {
							//second click
							destinationTile = b.get(x, y);
							boolean illegal = false;
							Move making = new Move(sourceTile, destinationTile, b);
							/*if (!b.get(sourceTile.getx(), sourceTile.gety()).getPiece().move(sourceTile, destinationTile)) {
								System.out.println("Illegal move!");
								illegal = true;
							}*/
							if (sourceTile.getPiece() == null || !(sourceTile.getPiece().isLegal(sourceTile, destinationTile))) {
								if (sourceTile != destinationTile) {
									System.out.println("Illegal Move!");
								}
								illegal = true;
							}
							making.makeMove();
							Square sq = b.askPromote();
							if (sq != null) {
								b.promotePawn(sq, b.getOtherTurn(), "Q");
							}
							sourceTile.unclick();
							legalSquares.clear();
							sourceTile = null;
							destinationTile = null;
							humanMovedPiece = null;
							if (!illegal) {
								aimoved = null;
							}
						}
						bp.drawBoard(b);
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								handle();
							}
						});


						//handle();

					} else if (isRightMouseButton(e)) {
						if (sourceTile != null) {
							sourceTile.unclick();
						}
						sourceTile = null;
						destinationTile = null;
						humanMovedPiece = null;
					}

				}

				@Override
				public void mousePressed(final MouseEvent e) {
					
				}

				@Override
				public void mouseReleased(final MouseEvent e) {
					
				}

				@Override
				public void mouseEntered(final MouseEvent e) {
					
				}

				@Override
				public void mouseExited(final MouseEvent e) {
					
				}
			});

			validate();
		}

		public void drawTile(Board b) {
			assignTileColor();
			assignIcon(b);
			highlightLegals(b);
			validate();
			repaint();
		}

		void assignIcon(Board b) {
			removeAll();
			if (!(b.get(x, y).getPiece() instanceof Nopiece)) {
				try {
					File f = new File(filePath + b.get(x, y).getPiece().getSymbol() + ".gif");
					final BufferedImage image = ImageIO.read(f);
					add(new JLabel(new ImageIcon(image)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		void highlightLegals(Board b) {
			try {
				if (legalSquares.contains(b.get(x, y))) {
					add(new JLabel(new ImageIcon(ImageIO.read(new File("chess/images/green_dot.png")))));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		void assignTileColor() {
			//System.out.println(x);
			//System.out.println(y);
			//System.out.println(b.get(x, y).getColor());
			if (aimoved != null && x == aimoved.getx() && y == aimoved.gety()) {
				setBackground(AI_MOVED_TILE_COLOR);
			} else if (b.get(x, y).isclicked()) {
				setBackground(CLICKED_TILE_COLOR);
			} else if (b.get(x, y).getColor().equals("Light")) {
				setBackground(LIGHT_TILE_COLOR);
			} else {
				setBackground(DARK_TILE_COLOR);
			}


		}

	}


	private class BoardPanel extends JPanel {

		java.util.List<TilePanel> boardTiles;

		BoardPanel() {
			super(new GridLayout(8, 8));
			boardTiles = new ArrayList<TilePanel>();
			for (int j = b.SIZE - 1; j >= 0; j--) {
				for (int i = 0; i < b.SIZE; i++) {
					TilePanel tile = new TilePanel(this, i, j);
					boardTiles.add(tile);
					add(tile);
				}
			}
			setSize(BOARD_PANEL_DIMENSION);
			validate();
		}

		public void flip() {
			boardTiles.clear();
			for (int j = 0; j < b.SIZE; j++) {
				for (int i = b.SIZE - 1; i >= 0; i--) {
					TilePanel tile = new TilePanel(this, i, j);
					boardTiles.add(tile);
					add(tile);
				}
			}
		}

		public void unFlip() {
			boardTiles.clear();
			for (int j = b.SIZE - 1; j >= 0; j--) {
				for (int i = 0; i < b.SIZE; i++) {
					TilePanel tile = new TilePanel(this, i, j);
					boardTiles.add(tile);
					add(tile);
				}
			}
		}

		public void drawBoard(Board b) {
			removeAll();
			for (TilePanel panel : boardTiles) {
				panel.drawTile(b);
				add(panel);
			}
			validate();
			repaint();
		}

	}








	private void populate(JMenuBar gameMenuBar) {
		gameMenuBar.add(createOptionsMenu());
		gameMenuBar.add(createWhiteMenu());
		gameMenuBar.add(createBlackMenu());
	}

	private JMenu createOptionsMenu() {

		JMenu optionsMenu = new JMenu("Options");
		JMenuItem reset = new JMenuItem("New Game");
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Time to reset the game!");
				winner_reported = false;
				b.reset();
				aimoved = null;
				bp.drawBoard(b);
				handle();
			}
		});
		optionsMenu.add(reset);

		JMenuItem undo = new JMenuItem("Undo");
		undo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Undoing last Move!");
				if (aimoved != null) {
					b.undo();
				}
				b.undo();
				winner_reported = false;
				aimoved = null;
				bp.drawBoard(b);
				handle();
			}
		});
		optionsMenu.add(undo);

		JMenuItem flipped = new JMenuItem("Flip");
		flipped.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Flipping Board Orientation.");
				flip = !flip;
				if (flip) {
					flipBoard();
				} else {
					unFlipBoard();
				}
				handle();
			}
		});
		optionsMenu.add(flipped);

		JMenuItem upload = new JMenuItem("Upload PGN");
		upload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Loading the log file");
				b.reset();
				b.upload();
				aimoved = null;
				handle();
			}
		});
		optionsMenu.add(upload);

		JMenuItem log = new JMenuItem("Log Game");
		log.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Logging game.");
				b.writeLog();
			}
		});
		optionsMenu.add(log);

		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Time to exit the game!");
				System.exit(0);
			}
		});
		optionsMenu.add(exit);





		return optionsMenu;
	}

	private JMenu createWhiteMenu() {
		JMenu whiteMenu = new JMenu("White");
		JMenuItem manual = new JMenuItem("Manual");
		manual.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Time to make white a Manual Player!");
				white = null;
			}
		});
		whiteMenu.add(manual);

		JMenuItem ai0 = new JMenuItem("AI Level 0");
		ai0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Time to make white an AI Level 0!");
				white = new AIPlayer0(b, "White");
				handle();
			}
		});
		whiteMenu.add(ai0);

		JMenuItem ai1 = new JMenuItem("AI Level 1");
		ai1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Time to make white an AI Level 1!");
				white = new AIPlayer1(b, "White");
				handle();
			}
		});
		whiteMenu.add(ai1);

		JMenuItem ai2 = new JMenuItem("AI Level 2");
		ai2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Time to make white an AI Level 2!");
				white = new AIPlayer2(b, "White");
				handle();
			}
		});
		whiteMenu.add(ai2);

		return whiteMenu;

	}

	private JMenu createBlackMenu() {
		JMenu blackMenu = new JMenu("Black");
		JMenuItem manual = new JMenuItem("Manual");
		manual.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Time to make black a Manual Player!");
				black = null;
			}
		});
		blackMenu.add(manual);

		JMenuItem ai0 = new JMenuItem("AI Level 0");
		ai0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Time to make black an AI Level 0!");
				black = new AIPlayer0(b, "Black");
				handle();
			}
		});
		blackMenu.add(ai0);

		JMenuItem ai1 = new JMenuItem("AI Level 1");
		ai1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Time to make black an AI Level 1!");
				black = new AIPlayer1(b, "Black");
				handle();
			}
		});
		blackMenu.add(ai1);

		JMenuItem ai2 = new JMenuItem("AI Level 2");
		ai2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Time to make black an AI Level 2!");
				black = new AIPlayer2(b, "Black");
				handle();
			}
		});
		blackMenu.add(ai2);

		return blackMenu;

	}

























}