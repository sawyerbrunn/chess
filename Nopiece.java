package chess;

class Nopiece extends Piece {

	 /* The name of this piece */
    String name;

    /* The squre this piece occupies */
    Square s;

    /* The board this piece occupies */
    Board b;

    /* Tracks if I have moved */
    boolean hasMoved;

    String color;

    Nopiece(Board brd) {
    	color = null;
    	b = brd;

    }

    @Override
    Square move(Square from, Square to) {
    	return null;
    }

    @Override
    boolean isLegal(Square from, Square to) { return false; }

    @Override
    boolean hasMoved() { return false; }

    @Override
    String getSymbol() {
        return "--";
    }
}