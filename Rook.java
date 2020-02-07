package chess;

class Rook extends Piece {

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
    Rook(Square sq, Board brd, String col) {
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
    boolean isLegal(Square from, Square to) {
        if (!from.getPiece().getColor().equals(b.getTurn())) {
            return false;
        }
        if (to.getPiece().getColor().equals(b.getTurn())) {
            return false;
        }
        if (from.getx() == to.getx() && from.gety() > to.gety()) {
            // moving vertically down
            for (int y = from.gety() - 1; y > to.gety(); y--) {
                if (!(b.get(to.getx(), y).getPiece() instanceof Nopiece)) {
                    return false;
                }
            }
            return b.noCheck(from, to);
        } else if (from.getx() == to.getx() && from.gety() < to.gety()) {
            //moving vertically up
            for (int y = from.gety() + 1; y < to.gety(); y++) {
                if (!(b.get(to.getx(), y).getPiece() instanceof Nopiece)) {
                    //System.out.println(y);
                    //System.out.println(to.gety());
                    return false;
                }
            }
            return b.noCheck(from, to);

        } else if (from.gety() == to.gety() && from.getx() < to.getx()) {
            for (int x = from.getx() + 1; x < to.getx(); x++) {
                if (!(b.get(x, to.gety()).getPiece() instanceof Nopiece)) {
                    return false;
                }
            }
            return b.noCheck(from, to);
        } else if (from.gety() == to.gety() && from.getx() > to.getx()) {
            for (int x = from.getx() - 1; x > to.getx(); x--) {
                if (!(b.get(x, to.gety()).getPiece() instanceof Nopiece)) {
                    return false;
                }
            }
            return b.noCheck(from, to);
        } else {
            return false;
        }
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
    boolean hasMoved() { return hasMoved; }

    @Override
    String getSymbol() {
        return (color.equals("White")) ? "WR" : "BR";
    }
}