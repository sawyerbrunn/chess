package chess;
import ucb.util.CommandArgs;

import java.io.*;
import java.util.*;

class Main {

	static int getCoord(char c) {
		if (c == 'A') {
			return 0;
		} else if (c == 'B') {
			return 1;
		} else if (c == 'C') {
			return 2;
		} else if (c == 'D') {
			return 3;
		} else if (c == 'E') {
			return 4;
		} else if (c == 'F') {
			return 5;
		} else if (c == 'G') {
			return 6;
		} else {
			return 7;
		}
	}

	public static void main(String[] args) {
		Board b = new Board();
		//Gui gui = new Gui(b, null, new AIPlayer1(b, "Black"));
		Gui gui = new Gui(b, null, null);
		Player white;
		Player black;
		if (args.length == 0) {
			white = new ManualPlayer(b, "White");
			black = new ManualPlayer(b, "Black");
		} else if (args.length == 1 && args[0].toUpperCase().equals("AI")) {
			white = new ManualPlayer(b, "White");
			black = new AIPlayer0(b, "Black");
		} else {
			return;
		}
		String out;
		System.out.println(b.toString());
		while(true) {
			out = (b.getTurn().equals("White")) ? white.makeMove() : black.makeMove();
			if (out.toUpperCase().equals("EXIT")) {
				System.exit(1);
				return;
			} else if (out.toUpperCase().equals("RESET")) {
				b.reset();
				System.out.println(b.toString());
				continue;
			} else if (out.toUpperCase().equals("ILLEGAL")) {
				System.out.println(b.toString());
				System.out.println("Please enter a legal move");
				continue;
			}
			String winner = b.winner();
			while (winner != null) {
				String move;
				Scanner s = new Scanner(System.in);
				System.out.println(b.toString());
				if (winner.equals("Draw")) {
					System.out.println("The game is a draw.");
				} else {
					System.out.println("Checkmate! " + winner + " wins.");
				}
				System.out.println("Type reset for a new game.");
				move = s.nextLine();
				if (move.toUpperCase().equals("EXIT")) {
					System.exit(1);
					return;
				}
				if (move.toUpperCase().equals("RESET")) {
					b = new Board();
					break;
				} else {
					System.out.println("Unrecognized command.");
				}
			}
			System.out.println(b.toString());
		}
	}




/*
	public static void main(String[] args) {
		Board b = new Board();
		Scanner s = new Scanner(System.in);
		boolean legal = true;
		while(true) {
			//System.out.println(b.whitePieces.size());
			//System.out.println(b.blackPieces.size());
			if (legal) {
				System.out.println(b.toString());
			}
			//System.out.println("finding moves");
			for (int i = 0; i < b.whitePieces.size(); i++) {
				//System.out.println(b.whitePieces.get(i).getSymbol());
			}

			Iterator<Move> it = b.allMoves();
			List<Move> l = new ArrayList<Move>();
			while (it.hasNext()) {
				l.add(it.next());
			}
			for (int i = 0; i < l.size(); i++) {
				//System.out.println(l.get(i).getSymbol());
			}
			System.out.println("Enter " + b.getTurn() + "'s Move:");
			String move = s.nextLine();
			if (move.toUpperCase().equals("EXIT")) {
				System.exit(1);
			} else if (move.toUpperCase().equals("RESET")) {
				b = new Board();
				//System.out.println(b.toString());
				continue;
			}
			String from = move.substring(0, 2).toUpperCase();
			String to = move.substring(move.length() - 2, move.length()).toUpperCase();
			int y1 = Integer.parseInt(from.substring(1)) - 1;
			int y2 = Integer.parseInt(to.substring(1)) - 1;
			int x1 = getCoord(from.charAt(0));
			int x2 = getCoord(to.charAt(0));
			Square s1 = b.get(x1, y1);
			Square s2 = b.get(x2, y2);
			//System.out.println(s2.isEmpty());
			//System.out.println(s2.getPiece().getColor().equals(s1.getPiece().getColor()));
			//System.out.println(s1.getPiece().isLegal(s1, s2));
			if (!s1.getPiece().move(s1, s2)) {
				System.out.println(b.toString());
				System.out.println("Please enter a legal move.");
				legal = false;
			} else {
				Square sq = b.askPromote();
				if (sq != null) {
					if (b.getOtherTurn() == "White") {
						b.whitePieces.remove(sq.getPiece());
					} else {
						b.blackPieces.remove(sq.getPiece());
					}
					System.out.println("Promote Pawn to?");
					System.out.println("Enter one of: Q (Queen), K (Knight), R (Rook), B (Bishop)");
					while (true) {
						String promo = s.nextLine();
						if (promo.toUpperCase().equals("Q")) {
							b.put(sq, new Queen(sq, b, b.getOtherTurn()));
							break;
						} else if ((promo.toUpperCase().equals("K"))) {
							b.put(sq, new Knight(sq, b, b.getOtherTurn()));
							break;
						} else if ((promo.toUpperCase().equals("R"))) {
							b.put(sq, new Rook(sq, b, b.getOtherTurn()));
							break;
						} else if ((promo.toUpperCase().equals("B"))) {
							b.put(sq, new Bishop(sq, b, b.getOtherTurn()));
							break;
						} else {
							System.out.println("Please enter a valid promotion.");
						}
					}
				}
				legal = true;
			}
			String winner = b.winner();
			while (winner != null) {
				System.out.println(b.toString());
				if (winner.equals("Draw")) {
					System.out.println("The game is a draw.");
				} else {
					System.out.println("Checkmate! " + winner + " wins.");
				}
				System.out.println("Type reset for a new game.");
				move = s.nextLine();
				if (move.toUpperCase().equals("RESET")) {
					b = new Board();
					break;
				} else {
					System.out.println("Unrecognized command.");
				}
			}
		}


		
	}
	*/
}