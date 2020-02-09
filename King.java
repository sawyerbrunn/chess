package chess;
import java.util.*;
import java.lang.Math;
class King extends Piece {

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
    King(Square sq, Board brd, String col) {
        //System.out.println("Making king with color " + col);
        s = sq;
        b = brd;
        hasMoved = false;
        color = col;
    }

    @Override
    boolean move(Square from, Square to) {
    	if (isLegal(from, to)) {
            if (canCastle(from, to)) {
                //System.out.println(to.getx() + 1);
                //System.out.println(to.gety());
                //System.out.println(b.get(to.getx() + 1, to.gety()).getPiece().getSymbol());
                Piece rook = (from.getx() < to.getx()) ? b.get(to.getx() + 1, to.gety()).getPiece() : b.get(to.getx() - 2, to.gety()).getPiece();
                s.empty();
                s = to;
                s.put(this);
                hasMoved = true;
                rook.getSquare().empty();
                rook.setSquare((to.getx() > from.getx()) ? b.get(to.getx() - 1, to.gety()) : b.get(to.getx() + 1, to.gety()));
                rook.getSquare().put(rook);
                rook.hasMoved = true;
                if (color.equals("White")) {
                    b.wking = s;
                }   else {
                    b.bking = s;
                }
                b.turn();
                b.addMove(new Move(from, to, b));
                return true;
            }
    		s.empty();
    		to.toEmpty();
    		s = to;
    		s.put(this);
            b.turn();
            hasMoved = true;
            if (color.equals("White")) {
                b.wking = s;
            } else {
                b.bking = s;
            }
            b.addMove(new Move(from, to, b));
            return true;
    	}
        return false;
    }

    @Override
    void setSquare(Square sq) {
        s = sq;
    }

    @Override
    Square getSquare() {
        return s;
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
        if (canCastle(from, to)) {
            return true;
        }
        if (to.getPiece().getColor().equals(b.getTurn())) {
            return false;
        }
        if (Math.abs(from.getx() - to.getx()) > 1 || Math.abs(from.gety() - to.gety()) > 1) {
            return false;
        }
        return b.noCheck(from, to);
    }

    boolean canCastle(Square from, Square to) {
        if (hasMoved()) {
            return false;
        } else if (from.gety() != to.gety()) {
            return false;
        } else if (b.inCheck(color)) {
            return false;
        }
        if (from.getx() < to.getx()) {
            if (to.getx() != 6) {
                return false;
            }
            if (!(b.get(to.getx() + 1, to.gety()).getPiece() instanceof Rook)) {
                return false;
            } else if (!(b.get(to.getx() + 1, to.gety()).getPiece().getColor().equals(b.getTurn()))) {
                return false;
            } else if (b.get(to.getx() + 1, to.gety()).getPiece().hasMoved()) {
                return false;
            }
        } else if (from.getx() > to.getx()) {
            if (to.getx() != 2) {
                return false;
            }
            if (!(b.get(to.getx() - 2, to.gety()).getPiece() instanceof Rook)) {
                return false;
            } else if (!(b.get(to.getx() - 2, to.gety()).getPiece().getColor().equals(b.getTurn()))) {
                return false;
            } else if (b.get(to.getx() - 2, to.gety()).getPiece().hasMoved()) {
                return false;
            }
        }
        /* For every square between FROM and TO, we need:
        1) the square is empty, and
        2) the square is not attacked by any of my opponent's pieces.
        */
        //System.out.println("sanity check");
        if (from.getx() < to.getx()) {
            /* casting right (SHORT CASTLE) */
            for (int x = from.getx() + 1; x <= to.getx(); x++) {
                if (!b.get(x, from.gety()).isEmpty() || b.attacked(b.get(x, from.gety()), color)) {
                    System.out.println(!b.get(x, from.gety()).isEmpty());
                    System.out.println(b.attacked(b.get(x, from.gety()), color));
                    return false;
                }
            }
            return true;
        } else {
            /* casting left (LONG CASTLE) */
            for (int x = from.getx() - 1; x >= to.getx(); x--) {
                if (!b.get(x, from.gety()).isEmpty() || b.attacked(b.get(x, from.gety()), color)) {
                    return false;
                }
            }
            return true;
        }
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
        return (color.equals("White")) ? "WK" : "BK";
    }

    Iterator<Move> legalMoves() {
        return new LegalMoveIterator(color);
    }

    private class LegalMoveIterator implements Iterator<Move> {

        String color;
        int dir;
        int dist;
        Move m;


        LegalMoveIterator(String c) {
            color = c;
            dir = -1;
            m = null;
            dist = 0;
            toNext();

        }

        @Override
        public boolean hasNext() {
            return dir < 8;
            //return false;
        }

        @Override 
        public Move next() {
            Move r = m;
            toNext();
            return r;

        }

        void toNext() {
            dist++;
            while (hasNext()) {
                Square to = s.getDir(dir, dist);
                if (to == null) {
                    dist = 1;
                    dir++;
                } else if (isLegal(s, to)) {
                    m = new Move(s, to, b);
                    break;
                } else {
                    dist = 1;
                    dir++;
                }
            }
        }
    }



}