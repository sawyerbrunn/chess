package chess;

import java.util.*;

class AIPlayer1 extends Player {

	Board b;
	String color;

	AIPlayer1(Board brd, String col) {
		b = brd;
		color = col;
	}

	@Override
	String makeMove() {
		List<Move> considering = new ArrayList<Move>();
		int modifier = (color.equals("White")) ? 1 : -1;
		int bestScore = -10000;
		Board copy = new Board(b);
		copy.doNoCheck = true;
		Iterator<Move> it = copy.allMoves();
		while (it.hasNext()) {
			considering.add(it.next());
		}
		Move bestMove = considering.get(0);
		/*
		for (Move m : considering) {
			Move n = new Move(m, copy);
			if (n.makeMove()) {
				copy.promotePawn(m.getTo(), color, "Q");
			}
			int current = score(copy);
			if (current * modifier > bestScore) {
				bestMove = n;
				bestScore = current;
			}
			copy = new Board(b);
		}*/
		for (Move m : considering) {
			m.makeMove();
			Square sq = copy.askPromote();
			if (sq != null) {
				b.promotePawn(sq, color, "Q");
			}
			int current = score(copy);
			/*
			System.out.println("This board: ");
			System.out.println(copy.toString());
			System.out.print("This score: ");
			System.out.println(current);
			System.out.println("White/black piece size: ");
			System.out.println(copy.whitePieces.size());
			System.out.println(copy.blackPieces.size());
			System.out.print("Current best score: ");
			System.out.println(bestScore);
			System.out.print("Current chosen move: ");
			System.out.println(bestMove.getSymbol());*/
			if (current * modifier > bestScore) {
				bestMove = m;
				bestScore = current * modifier;
			} else if (current * modifier == bestScore && Math.random() > .5) {
				bestMove = m;
				bestScore = current * modifier;
			}
			copy.undo();
		}
		System.out.println(bestMove.getSymbol());
		Square from = b.get(bestMove.getFrom().getx(), bestMove.getFrom().gety());
		Square to = b.get(bestMove.getTo().getx(), bestMove.getTo().gety());
		Move making = new Move(from, to, b);
		making.makeMove();
		Square sq = b.askPromote();
		if (sq != null) {
			if (b.getOtherTurn() == "White") {
				b.whitePieces.remove(sq.getPiece());
			} else {
				b.blackPieces.remove(sq.getPiece());
			}
			b.put(sq, new Queen(sq, b, b.getOtherTurn()));
		}
		return "None";

	}

	/* Scores the Board b heuristically */
	int score(Board brd) {
		String winner = brd.winner();
		if (winner != null) {
			if (winner.equals("White")) {
				return 1000;
			} else if (winner.equals("Black")) {
				return -1000;
			} else {
				return 0;
			}
		}
		//System.out.println("Scoring");
		List<Piece> whitePieces = brd.whitePieces;
		List<Piece> blackPieces = brd.blackPieces;
		return count(whitePieces) - count(blackPieces);
	}

	int count(List<Piece> pieces) {
		int r = 0;
		for (Piece p : pieces) {
			if (p instanceof Pawn) {
				r++;
			} else if (p instanceof Rook) {
				r += 5;
			} else if (p instanceof Bishop) {
				r += 4;
			} else if (p instanceof Knight) {
				r += 4;
			} else if (p instanceof Queen) {
				r += 9;
			} else if (p instanceof King) {
				r += 20;
			}
		}
		//System.out.print("Score! ");
		//System.out.println(r);
		return r;
	}
}