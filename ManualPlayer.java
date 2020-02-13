package chess;
import java.util.*;
/* A Player that takes input from System.in to make a move */

class ManualPlayer extends Player {

	Board b;
	String color;
	Scanner s;
	List<Piece> pieces;

	ManualPlayer(Board brd, String col) {
		b = brd;
		color = col;
		s = new Scanner(System.in);
		pieces = (col.equals("White")) ? b.whitePieces : b.blackPieces;
	}

	@Override
	String makeMove() {
		System.out.println("Enter " + color + "'s move:");
		String move = s.nextLine();
		if (move.toUpperCase().equals("RESET")) {
			return "reset";
		} else if (move.toUpperCase().equals("EXIT")) {
			return "exit";
		}
		String from = move.substring(0, 2).toUpperCase();
		String to = move.substring(move.length() - 2, move.length()).toUpperCase();
		int y1 = Integer.parseInt(from.substring(1)) - 1;
		int y2 = Integer.parseInt(to.substring(1)) - 1;
		int x1 = getCoord(from.charAt(0));
		int x2 = getCoord(to.charAt(0));
		Square s1 = b.get(x1, y1);
		Square s2 = b.get(x2, y2);
		Move m = new Move(s1, s2, b);
		if (!m.makeMove()) {
			return "illegal";
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
		}
		return "None";
	}




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



}