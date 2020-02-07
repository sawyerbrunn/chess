package chess;
import java.lang.Math;

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
    		to.toEmpty();
    		s = to;
    		s.put(this);
            b.turn();
            hasMoved = true;
            return true;
    	}
        return false;
    }

    @Override
    void copiedMove(Square from, Square to) {
        s.empty();
        to.toEmpty();
        s = to;
        s.put(this);
        b.turn();
        hasMoved = true;
    }

    @Override
    boolean isLegal(Square from, Square to) {
        if (!from.getPiece().getColor().equals(b.getTurn())) {
            return false;
        }
        if (to.getPiece().getColor().equals(b.getTurn())) {
            return false;
        }
        if (Math.abs(from.getx() - to.getx()) == 2 && Math.abs(from.gety() - to.gety()) == 1
            || Math.abs(from.getx() - to.getx()) == 1 && Math.abs(from.gety() - to.gety()) == 2) {
            return b.noCheck(from, to);
        }
        return false;
    }

    @Override
    boolean attacks(Square sq) {
        String t = b.getTurn();
        boolean r;
        if (t.equals(getColor())) {
            return isLegal(s, sq);
        } else {
            b.tempTurn();
            r = isLegal(s, sq);
        }
        b.tempTurn();
        return r;
        
    }

    @Override
    String getColor() {
        return color;
    }

    @Override
    boolean hasMoved() { return false; }

    @Override
    String getSymbol() {
        return (color.equals("White")) ? "WKn" : "BKn";
    }
}