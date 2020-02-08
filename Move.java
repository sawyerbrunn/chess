package chess;

class Move {
	Square from;
	Square to;
	Board b;

	Move(Square f, Square t, Board brd) {
		from = f;
		to = t;
		b = brd;
		//System.out.println("New move!");
	}

	Square getFrom() {
		return from;
	}

	Square getTo() {
		return to;
	}

	/* Used by AIs that always make legal moves. Returns TRUE is pawn promotion is needed. */
	boolean makeMove() {
		from.getPiece().move(from, to);
		if (to.getPiece() instanceof Pawn && to.getPiece().getColor().equals("White")) {
			return to.gety() == b.SIZE;
		} else {
			return to.gety() == 0;
		}
	}

	/*
	String getSymbol() {
		return "(" + String.valueOf(from.getx()) + " " + String.valueOf(from.gety()) + ") to (" +
		String.valueOf(to.getx()) + " " + String.valueOf(to.gety()) + ")";
	}
	*/
	String getSymbol() {
		return getStr(from.getx()) + String.valueOf(from.gety() + 1) + " " + getStr(to.getx())
		+ String.valueOf(to.gety() + 1);
	}

	static String getStr(int c) {
		if (c == 0) {
			return "a";
		} else if (c == 1) {
			return "b";
		} else if (c == 2) {
			return "c";
		} else if (c == 3) {
			return "d";
		} else if (c == 4) {
			return "e";
		} else if (c == 5) {
			return "f";
		} else if (c == 6) {
			return "g";
		} else {
			return "h";
		}
	}

}