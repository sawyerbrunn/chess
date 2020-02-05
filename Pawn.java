package chess;

class Pawn extends Piece {
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
    Pawn(Square sq, Board brd, String col) {
        s = sq;
        b = brd;
        hasMoved = false;
        color = col;
    }

    @Override
    Square move(Square from, Square to) {
    	if (isLegal(from, to)) {
    		s.empty();
    		to.empty();
    		s = to;
    		s.put(this);
            b.turn();
            hasMoved = true;
            return to;
    	}
        return null;
    }

    @Override
    boolean isLegal(Square from, Square to) {


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
            if (from.gety() > to.gety() && color == "White") {
                return false;
            }
            if (from.gety() < to.gety() && color == "Black") {
                return false;
            }
            System.out.println(42);
            return b.noCheck();
        } else {
            //pawn is not attacking, just moving
            if (from.getx() != to.getx()) {
                System.out.println(1);
                System.out.println(from.getx());
                System.out.println(to.getx());
                return false;
            }
            if (to.gety() - from.gety() > 2 || to.gety() - from.gety() < -2) {
                System.out.println("Haha");
                System.out.println(from.gety());
                System.out.println(to.gety());
                return false;
            }
            if (from.gety() <= to.gety() && color == "Black") {
                System.out.println(3);
                return false;
            } else if (from.gety() >= to.gety() && color == "White") {
                System.out.println(4);
                return false;
            }
            if (from.gety() == to.gety() + 2 || from.gety() == to.gety() - 2) {
                if (hasMoved) {
                    System.out.println(11);
                    return false;
                } else if (from.gety() == to.gety() + 2 && !b.get(from.gety() + 1, from.getx()).isEmpty()) {
                    System.out.println(12);
                    return false;
                } else if ((from.gety() == to.gety() - 2 && !b.get(from.gety() - 1, from.getx()).isEmpty())) {
                    System.out.println(13);
                    return false;
                } else {
                    System.out.println(52);
                    return b.noCheck();
                }
            }
            System.out.println(62);
            return b.noCheck();
            }
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
