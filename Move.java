package chess;

class Move {
	Square from;
	Square to;

	Move(Square f, Square t) {
		from = f;
		to = t;
		//System.out.println("New move!");
	}

	Square getFrom() {
		return from;
	}

	Square getTo() {
		return to;
	}

	String getSymbol() {
		return "(" + String.valueOf(from.getx()) + " " + String.valueOf(from.gety()) + ") to (" +
		String.valueOf(to.getx()) + " " + String.valueOf(to.gety()) + ")";
	}

}