package chess;

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