package chess;
import java.util.*;


/* A basic AI that plays chess by random selecting a move amongst all of its legal
moves and promoting pawns to queens. */
class AIPlayer0 extends Player {


	Board b;
	String color;
	Scanner s;
	List<Piece> pieces;


	AIPlayer0(Board brd, String col) {
		b = brd;
		color = col;
	}

	String makeMove() {
		List<Move> moves = new ArrayList<Move>();
		Iterator<Move> it = b.allMoves();
		while (it.hasNext()) {
			moves.add(it.next());
		}
		Collections.shuffle(moves);
		Move m = moves.get(0);
		m.makeMove();
		Square sq = b.askPromote();
		if (sq != null) {
			if (b.getOtherTurn() == "White") {
				b.whitePieces.remove(sq.getPiece());
			} else {
				b.blackPieces.remove(sq.getPiece());
			}
			b.put(sq, new Queen(sq, b, b.getOtherTurn()));
		}
		System.out.println(m.getSymbol());
		return "None";

	}
}