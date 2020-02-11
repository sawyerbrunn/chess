package chess;

class Move {
	private Square from;
	private Square to;
	private Board b;
	/* The captured piece. If this move is undone, this piece belongs on:
	1) TO if this move is NOT special
	2) a CORNER SQUARE is this move is special and a king is moving (captured holds the rook)
	3) a PAST SQUARE is this move is special and a pawn is moving (captured holds the pawn taken en passant)
	*/
	private Piece captured;
	/* The old hasMoved */
	private boolean oldHasMoved;
	/* The piece that is moving */	
	private Piece moving;
	/* Tracks if this move is a castle or an en passant capture */
	private boolean special;
	/* Used to track en passant captured pieces */
	String color;

	Move(Square f, Square t, Board brd) {
		from = f;
		to = t;
		b = brd;
		oldHasMoved = false;
		special = false;
		//System.out.println("New move!");
	}

	Move(Move m, Board brd) {
		b = brd;
		from = b.get(m.getFrom().getx(), m.getFrom().gety());
		to = b.get(m.getTo().getx(), m.getTo().gety());
		special = m.isSpecial();
		copyCapturedPiece(m);
		copyMovingPiece(m);
	}

	void copyMovingPiece(Move model) {
		if (model.moving() instanceof Bishop) {
			moving = new Bishop(from, b, model.moving().getColor());
		} else if (model.moving() instanceof King) {
			moving = new King(from, b, model.moving().getColor());
		} else if (model.moving() instanceof Knight) {
			moving = new Knight(from, b, model.moving().getColor());
		} else if (model.moving() instanceof Pawn) {
			moving = new Pawn(from, b, model.moving().getColor());
			((Pawn) moving).setDoubleMoved(model.moving().doubleMoved());
		} else if (model.moving() instanceof Queen) {
			moving = new Queen(from, b, model.moving().getColor());
		} else if (model.moving() instanceof Rook) {
			moving = new Rook(from, b, model.moving().getColor());
		}
		if (moving != null) {
			moving.setHasMoved(model.oldHasMoved());
		}
	}

	void copyCapturedPiece(Move model) {
		if (model.captured() instanceof Bishop) {
			captured = new Bishop(from, b, model.captured().getColor());
		} else if (model.captured() instanceof King) {
			captured = new King(from, b, model.captured().getColor());
		} else if (model.captured() instanceof Knight) {
			captured = new Knight(from, b, model.captured().getColor());
		} else if (model.captured() instanceof Pawn) {
			captured = new Pawn(from, b, model.captured().getColor());
			((Pawn) captured).setDoubleMoved(model.captured().doubleMoved());
		} else if (model.captured() instanceof Queen) {
			captured = new Queen(from, b, model.captured().getColor());
		} else if (model.captured() instanceof Rook) {
			captured = new Rook(from, b, model.captured().getColor());
		}
		if (captured != null) {
			captured.setHasMoved(model.captured().hasMoved());
		}
	}

	boolean isInverseOf(Move m2) {
		return getFrom().getSymbol().equals(m2.getTo().getSymbol()) && getTo().getSymbol().equals(m2.getFrom().getSymbol());
	}

	boolean isSpecial() {
		return special;
	}

	Piece moving() {
		return moving;
	}

	Piece captured() {
		return captured;
	}

	boolean oldHasMoved() {
		return oldHasMoved;
	}

	Square getFrom() {
		return from;
	}

	Square getTo() {
		return to;
	}

	/* Used by AIs that always make legal moves. Returns TRUE is pawn promotion is needed. */
	boolean makeMove() {
		//System.out.println(to.getSymbol());
		moving = from.getPiece();
		captured = to.getPiece();
		//System.out.println(captured.getSymbol());
		oldHasMoved = from.getPiece().hasMoved();
		//from.getPiece().move(from, to);
		if (!moving.isLegal(from, to)) {
			return false;
		}
		if (moving instanceof Pawn) {
			if (((Pawn) moving).enPassant(from, to)) {
            from.empty();
            moving.setSquare(to);
            to.put(moving);
            if (moving.getColor().equals("White")) {
            	captured = b.get(to.getx(), to.gety() - 1).getPiece();
                b.get(to.getx(), to.gety() - 1).toEmpty();
            } else {
            	captured = b.get(to.getx(), to.gety() + 1).getPiece();
                b.get(to.getx(), to.gety() + 1).toEmpty();
            }
            special = true;
            b.turn();
            moving.setHasMoved(true);
            b.addMove(this);
            return true;
        } 
        if (((Pawn) moving).doubleMove(from, to)) {
            ((Pawn) moving).setDoubleMoved(b.getMoveNumber());
        }
        from.empty();
        moving.setSquare(to);
        to.toEmpty();
        to.put(moving);
        b.turn();
        moving.setHasMoved(true);
        if (((Pawn) moving).canPromote(to)) {
            b.promote = to;
            b.promotePawn(to, b.getOtherTurn(), "Q");
            // NEED SPECIAL HERE?
        }
        b.addMove(this);
        return true;
		}
		if (moving instanceof King) {
            if (((King) moving).canCastle(from, to)) {
                Piece rook = (from.getx() < to.getx()) ? b.get(to.getx() + 1, to.gety()).getPiece() : b.get(to.getx() - 2, to.gety()).getPiece();
                from.empty();
                moving.setSquare(to);
                to.put(moving);
                moving.setHasMoved(true);
                rook.getSquare().empty();
                rook.setSquare((to.getx() > from.getx()) ? b.get(to.getx() - 1, to.gety()) : b.get(to.getx() + 1, to.gety()));
                rook.getSquare().put(rook);
                captured = rook;
                if (moving.getColor().equals("White")) {
                    b.wking = to;
                }   else {
                    b.bking = to;
                }
                b.turn();
                b.addMove(this);
                special = true;
                return true;
            }
    		from.empty();
    		to.toEmpty();
    		moving.setSquare(to);
    		to.put(moving);
            b.turn();
            moving.setHasMoved(true);
            if (moving.getColor().equals("White")) {
                b.wking = to;
            } else {
                b.bking = to;
            }
            b.addMove(this);
            return true;
        }
		from.empty();
		to.toEmpty();
		moving.setSquare(to);
		to.put(moving);
        b.turn();
        moving.setHasMoved(true);
        b.addMove(this);
		if (to.getPiece() instanceof Pawn && to.getPiece().getColor().equals("White")) {
			return to.gety() == b.SIZE;
		} else if ((to.getPiece() instanceof Pawn && to.getPiece().getColor().equals("Black"))) {
			return to.gety() == 0;
		}
		return true;
	}

	/*
	String getSymbol() {
		return "(" + String.valueOf(from.getx()) + " " + String.valueOf(from.gety()) + ") to (" +
		String.valueOf(to.getx()) + " " + String.valueOf(to.gety()) + ")";
	}
	*/
	String getSymbol() {
		return getStr(from.getx()) + String.valueOf(from.gety() + 1) + " " + getStr(to.getx())
		+ String.valueOf(to.gety() + 1);
	}

	static String getStr(int c) {
		if (c == 0) {
			return "a";
		} else if (c == 1) {
			return "b";
		} else if (c == 2) {
			return "c";
		} else if (c == 3) {
			return "d";
		} else if (c == 4) {
			return "e";
		} else if (c == 5) {
			return "f";
		} else if (c == 6) {
			return "g";
		} else {
			return "h";
		}
	}

}