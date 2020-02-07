package chess;

import java.lang.Math;

class Pawn extends Piece {
	 /* The name of this piece */
    String name;

    /* The squre this piece occupies */
    Square s;

    /* The board this piece occupies */
    Board b;

    /* Tracks if I have moved */
    boolean hasMoved;

    /* My color */
    String color;

    /* The turn that I double moved (-1 if I never double moved) */
    int doubleMoved;

     /* Creates a new piece on Square sq of Board brd */
    Pawn(Square sq, Board brd, String col) {
        s = sq;
        b = brd;
        hasMoved = false;
        color = col;
        doubleMoved = -1;
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
    boolean move(Square from, Square to) {
    	if (!isLegal(from, to)) {
    		return false;
    	}
        if (enPassant(from, to)) {
            s.empty();
            s = to;
            s.put(this);
            if (color == "White") {
                b.get(to.getx(), to.gety() - 1).toEmpty();
            } else {
                b.get(to.getx(), to.gety() + 1).toEmpty();
            }
            b.turn();
            hasMoved = true;
            return true;
        } 
        if (doubleMove(from, to)) {
            doubleMoved = b.getMoveNumber();
        }
        s.empty();
        to.toEmpty();
        s = to;
        s.put(this);
        b.turn();
        hasMoved = true;
        if (canPromote(to)) {
            b.promote = to;
        }
        return true;
    }

    boolean canPromote(Square s) {
        if (color.equals("White")) {
            return s.gety() == Board.SIZE - 1;
        } else {
            return s.gety() == 0;
        }
    }

    /* returns when I double moved */
    int doubleMoved() {
        return doubleMoved;
    }

    boolean doubleMove(Square from, Square to) {
        return Math.abs(from.gety() - to.gety()) == 2;
    }

    /* return true iff move is legal en passant */
    boolean enPassant(Square from, Square to) {
        if (color.equals("White")) {
            return to.isEmpty() && b.get(to.getx(), to.gety() - 1).getPiece() instanceof Pawn
            && Math.abs(b.get(to.getx(), to.gety() - 1).getPiece().doubleMoved() - b.getMoveNumber()) <= 1
            && !b.get(to.getx(), to.gety() - 1).getPiece().getColor().equals("White")
            && Math.abs(from.getx() - to.getx()) == 1 && (to.gety() - from.gety() == 1);
        } else if (color.equals("Black")) {
            return to.isEmpty() && b.get(to.getx(), to.gety() + 1).getPiece() instanceof Pawn
            && Math.abs(b.get(to.getx(), to.gety() + 1).getPiece().doubleMoved() - b.getMoveNumber()) <= 1
            && !b.get(to.getx(), to.gety() - 1).getPiece().getColor().equals("Black")
            && Math.abs(from.getx() - to.getx()) == 1 && (to.gety() - from.gety() == -1);
        }
        return false;
    }

    @Override
    boolean isLegal(Square from, Square to) {
        if (!from.getPiece().getColor().equals(b.getTurn())) {
            return false;
        }
        if (enPassant(from, to)) {
            return b.noCheck(from, to);
        }
        if (!to.isEmpty()) {
            // pawn is attacking
            if (to.getPiece().getColor().equals(color)) {
                return false;
            }
            if (from.getx() == to.getx()) {
                return false;
            }
            if (!(from.getx() == to.getx() + 1 || from.getx() == to.getx() - 1)) {
                return false;
            }
            if (!(from.gety() == to.gety() + 1 || from.gety() == to.gety() - 1)) {
                return false;
            }
            if (from.gety() > to.gety() && b.getTurn() == "White") {
                return false;
            }
            if (from.gety() < to.gety() && b.getTurn() == "Black") {
                return false;
            }
            //System.out.println(42);
            return b.noCheck(from, to);
        } else {
            //pawn is not attacking, just moving
            if (from.getx() != to.getx()) {
                //System.out.println(1);
                //System.out.println(from.getx());
                //System.out.println(to.getx());
                return false;
            }
            if (to.gety() - from.gety() > 2 || to.gety() - from.gety() < -2) {
                //System.out.println("Haha");
                //System.out.println(to.gety() - from.gety() > 2);
                //System.out.println(to.gety() - from.gety() < -2);
                return false;
            }
            if (from.gety() <= to.gety() && b.getTurn() == "Black") {
                //System.out.println(3);
                return false;
            } else if (from.gety() >= to.gety() && b.getTurn() == "White") {
                //System.out.println(4);
                return false;
            }
            if (from.gety() == to.gety() + 2 || from.gety() == to.gety() - 2) {
                if (hasMoved) {
                    //System.out.println(11);
                    return false;
                } else if (from.gety() == to.gety() + 2 && !b.get(from.getx(), from.gety() - 1).isEmpty()) {
                    //System.out.println(12);
                    return false;
                } else if ((from.gety() == to.gety() - 2 && !b.get(from.getx(), from.gety() + 1).isEmpty())) {
                    //System.out.println(13);
                    return false;
                } else {
                    //System.out.println(52);
                    return b.noCheck(from, to);
                }
            }
            //System.out.println(62);
            return b.noCheck(from, to);
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

    String getColor() {
        return color;
    }    


    @Override
    boolean hasMoved() { return hasMoved; }


    @Override
    String getSymbol() {
        return (color.equals("White")) ? "WP" : "BP";
    }


}
