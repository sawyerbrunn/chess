package chess;

class Square {
	String color;
	Piece piece;
	int i;
	int j;
	Board b;


	/* Initializes an empty square at coords (x, y) */
	Square(int x, int y, Board brd) {
		i = x;
		j = y;
		b = brd;
		piece = null;
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

	void empty() {
		if (false) {

		}
		piece = new Nopiece(b);
	}

	void put(Piece p) {
		piece = p;
	}



	
}