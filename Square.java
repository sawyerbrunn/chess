package chess;

class Square {
	String color;
	Piece piece;
	private int i;
	private int j;
	Board b;

	Square(Square s, Board brd) {
		b = brd;
		copy(s);
	}

	void copy(Square model) {
		color = model.color;
		i = model.getx();
		j = model.gety();
		Piece toCopy = model.getPiece();
		//System.out.println(toCopy.getSymbol());
		if (toCopy instanceof Nopiece) {
			piece = new Nopiece(b); 
		} else if (toCopy instanceof Bishop) {
			piece = new Bishop(this, b, toCopy.getColor());
			piece.hasMoved = toCopy.hasMoved;
		} else if (toCopy instanceof Rook) {
			piece = new Rook(this, b, toCopy.getColor());
			piece.hasMoved = toCopy.hasMoved();
		} else if (toCopy instanceof King) {
			piece = new King(this, b, toCopy.getColor());
			piece.hasMoved = toCopy.hasMoved;
		} else if (toCopy instanceof Knight) {
			piece = new Knight(this, b, toCopy.getColor());
			piece.hasMoved = toCopy.hasMoved;
		} else if (toCopy instanceof Pawn) {
			piece = new Pawn(this, b, toCopy.getColor());
			piece.hasMoved = toCopy.hasMoved;
			piece.doubleMoved = toCopy.doubleMoved;
		} else if (toCopy instanceof Queen) {
			piece = new Queen(this, b, toCopy.getColor());
			piece.hasMoved = toCopy.hasMoved;
		}
		//System.out.println(piece.getColor());

	}


	/* Initializes an empty square at coords (x, y) */
	Square(int x, int y, Board brd) {
		i = x;
		j = y;
		b = brd;
		piece = new Nopiece(brd);
		if (x % 2 == 0 && y % 2 == 0) {
			color = "Dark";
		} else if (x % 2 == 0 && y % 2 == 1) {
			color = "Light";
		} else if (x % 2 == 1 && y % 2 == 0) {
			color = "Light";
		} else {
			color = "Dark";
		}
	}

	String getColor() {
		return color;
	}

	Piece getPiece() {
		return piece;
	}

	boolean isEmpty() {
		return piece instanceof Nopiece;
	}

	void toEmpty() {
		if (!(piece instanceof Nopiece) && piece.getColor().equals("White")) {
			b.whitePieces.remove(piece);
		} else if ((!(piece instanceof Nopiece) && piece.getColor().equals("Black"))) {
			b.blackPieces.remove(piece);
		}
		piece = new Nopiece(b);
	}

	void empty() {
		piece = new Nopiece(b);
	}

	void put(Piece p) {
		piece = p;
		//System.out.println(p.getColor());
		if (p.getColor().equals("White") && !b.whitePieces.contains(p)) {
			b.whitePieces.add(p);
		} else if (p.getColor().equals("Black") && !b.blackPieces.contains(p)) {
			b.blackPieces.add(p);
		}
	}

	int getx() {
		return i;
	}

	int gety() {
		return j;
	}

	Square getDir(int dir, int dist) {
		Square r = null;
		switch (dir) {
			case 0:
				r = (gety() + dist < b.SIZE) ? b.get(getx(), gety() + dist) : null;
				break;
			case 1:
				r = (gety() + dist < b.SIZE && getx() + dist < b.SIZE) ? b.get(getx() + dist, gety() + dist) : null;
				break;
			case 2:
				r = (getx() + dist < b.SIZE) ? b.get(getx() + dist, gety()) : null;
				break;
			case 3:
				r = (gety() - dist >= 0 && getx() + dist < b.SIZE) ? b.get(getx() + dist, gety() - dist) : null;
				break;
			case 4:
				r = (gety() - dist >= 0) ? b.get(getx(), gety() - dist) : null;
				break;
			case 5:
				r = (gety() - dist >= 0 && getx() - dist >= 0) ? b.get(getx() - dist, gety() - dist) : null;
				break;
			case 6:
				r = (getx() - dist >= 0) ? b.get(getx() - dist, gety()) : null;
				break;
			case 7:
				r = (gety() + dist < b.SIZE && getx() - dist >= 0) ? b.get(getx() - dist, gety() + dist) : null;
				break;
			default:
				break;
		}
		return r;

	}



	
}