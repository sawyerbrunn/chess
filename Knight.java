package chess;

class Knight extends Piece {

	 /* The name of this piece */
    String name;

    /* The squre this piece occupies */
    Square s;

    /* The board this piece occupies */
    Board b;

    /* Tracks if I have moved */
    boolean hasMoved;

    String color;

     /* Creates a new piece on Square sq of Board brd */
    Knight(Square sq, Board brd, String col) {
        s = sq;
        b = brd;
        hasMoved = false;
        color = col;
    }

    @Override
    boolean move(Square from, Square to) {
    	if (isLegal(from, to)) {
    		s.empty();
    		to.empty();
    		s = to;
    		s.put(this);
            return true;
    	}
        return false;
    }

    @Override
    boolean isLegal(Square from, Square to) { return false; }

    @Override
    boolean hasMoved() { return false; }

    @Override
    String getSymbol() {
        return (color.equals("White")) ? "WKn" : "BKn";
    }
}