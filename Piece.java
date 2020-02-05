package chess;

abstract class Piece {

    /* The name of this piece */
    String name;

    /* The squre this piece occupies */
    Square s;

    /* The board this piece occupies */
    Board b;

    /* Tracks if I have moved */
    boolean hasMoved;

    String color;

    /* Moves me from FROM to TO */
    abstract Square move(Square from, Square to);

    /* Checks if moving from FROM to TO is allowed. */
    abstract boolean isLegal(Square from, Square to);

    /* Checks if I've moved */
    abstract boolean hasMoved();

    abstract String getSymbol();

    String getColor() {
        return "None";
    }

}









/* 
enum Piece {

	EMPTY("-", null), WHITE("W", "White"), BLACK("B", "Black"),
	WHITE_KING("WK", "Wite King"), WHITE_QUEEN("WQ", "White Queen"),
    WHITE_BISHOP("WB", "Wite Bishop"), WHITE_ROOK("WR", "White Rook"),
    WHITE_KNIGHT("WKn", "Wite Knight"), WHITE_PAWN("WP", "White Pawn"),
    BLACK_KING("BK", "Black King"), BLACK_QUEEN("BQ", "Black Queen"),
    BLACK_BISHOP("BB", "Black Bishop"), BLACK_ROOK("BR", "Black Rook"),
    BLACK_KNIGHT("BKn", "Black Knight"), BLACK_PAWN("BP", "Black Pawn");
    

    private final String symbol;
    private final String name;

    Piece(String s, String n) {
    	symbol = s;
    	name = n;
    }

    @Override
    public String toString() {
        return symbol;
    }

    String getSymbol() {
    	return symbol;
    }

    String toName() {
    	return name;
    }








}
*/