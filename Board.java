package chess;

import java.util.*;
import static chess.Piece.*;
import java.util.Iterator;

class Board {

	boolean doNoCheck;

	/* The size of the board */
	static final int SIZE = 8;

	/* A list of white's pieces */
	List<Piece> whitePieces;

	/* A list of black's pieces */
	List<Piece> blackPieces;

	/* A map of all pieces on the board */
	private Square[][] map;

	/* The last move played (for undo) */
	private Move lastMove;

	/* The piece whose turn it is */
	private String turn;

	/* The piece that has won the game */
	private String winner;

	/* The number of moves made by black */
	public int blackMoves;

	/* The number of moves made by white */
	public int whiteMoves;

	List<Move> log;

	Square promote;

	Square wking;

	Square bking;

	/* Initialize a new chess board */
	Board() {
		init();
	}

	/* Copies model */
	Board(Board model) {
		copy(model);
	}

	/* copies model */
	void copy(Board model) { 
		whitePieces = new ArrayList<Piece>();
		blackPieces = new ArrayList<Piece>();
		map = new Square[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				map[i][j] = new Square(model.get(i, j), this);
				//add to white/blackpieces here
				//System.out.println(get(i, j).getColor());
				//System.out.println(get(i, j).getColor().equals("White"));
				//System.out.println(get(i, j).getColor().equals("Black"));
				if (get(i, j).getPiece().getColor().equals("White")) {
					whitePieces.add(get(i, j).getPiece());
				} else if (get(i, j).getPiece().getColor().equals("Black")) {
					blackPieces.add(get(i, j).getPiece());
				}
				//System.out.println(whitePieces.size());
				//System.out.println(blackPieces.size());
			}
		}
		doNoCheck = false;
		wking = model.wking;
		bking = model.bking;
		promote = model.promote;
		turn = model.getTurn();
		winner = null;
		blackMoves = model.blackMoves;
		whiteMoves = model.whiteMoves;
		//log = model.log;
		log = new ArrayList<Move>();
		for (int i = 0; i < model.log.size(); i++) {
			log.add(new Move(model.log.get(i), this));
		}

	}

	Move getLastMove() {
		if (log.size() == 0) {
			return null;
		}
		return log.get(log.size() - 1);
	}

	void addMove(Move m) {
		log.add(m);
	}

	void reset() {
		init();
	}

	/** Undoes the last move made on this board */
	void undo() {
		Move lastMove = new Move(log.get(log.size() - 1), this);
		if (lastMove.isSpecial()) {
			if (lastMove.moving() instanceof King) {
				// Handle castling
				get(lastMove.getTo().getx(), lastMove.getTo().gety()).toEmpty();
				get(lastMove.getFrom().getx(), lastMove.getFrom().gety()).toEmpty();
				put(lastMove.getFrom().getx(), lastMove.getFrom().gety(), lastMove.moving());
				if (lastMove.getFrom().getx() < lastMove.getTo().getx()) {
					// was a short castle, handle rook
					get(lastMove.getTo().getx() - 1, lastMove.getTo().gety()).toEmpty();
					put(lastMove.getTo().getx() + 1, lastMove.getTo().gety(), lastMove.captured());
					lastMove.captured().setSquare(get(lastMove.getTo().getx() + 1, lastMove.getTo().gety()));
				} else {
					// was a long castle, handle rook
					get(lastMove.getTo().getx() + 1, lastMove.getTo().gety()).toEmpty();
					put(lastMove.getTo().getx() - 2, lastMove.getTo().gety(), lastMove.captured());
					lastMove.captured().setSquare(get(lastMove.getTo().getx() - 2, lastMove.getTo().gety()));
				}
			} else if (lastMove.moving() instanceof Pawn) {
				// Handle en passant
				get(lastMove.getTo().getx(), lastMove.getTo().gety()).toEmpty();
				get(lastMove.getFrom().getx(), lastMove.getFrom().gety()).toEmpty();
				put(lastMove.getFrom().getx(), lastMove.getFrom().gety(), lastMove.moving());
				if (lastMove.captured() != null && lastMove.moving().getColor().equals("White")) {
					put(lastMove.getTo().getx(), lastMove.getTo().gety() - 1, lastMove.captured());
					lastMove.captured().setSquare(get(lastMove.getTo().getx(), lastMove.getTo().gety() - 1));
				} else if (lastMove.captured() != null && lastMove.moving().getColor().equals("Black")) {
					put(lastMove.getTo().getx(), lastMove.getTo().gety() + 1, lastMove.captured());
					lastMove.captured().setSquare(get(lastMove.getTo().getx(), lastMove.getTo().gety() + 1));
				}
			}
		} else {

			/* toEmpty() the To square */
			get(lastMove.getTo().getx(), lastMove.getTo().gety()).toEmpty();

			/* toEmpty() the From square. */
			get(lastMove.getFrom().getx(), lastMove.getFrom().gety()).toEmpty();

			/* put the moving piece on from */
			put(lastMove.getFrom().getx(), lastMove.getFrom().gety(), lastMove.moving());

			/* Put the captured piece back on To, if needed */
			if (lastMove.captured() != null) {
				put(lastMove.getTo().getx(), lastMove.getTo().gety(), lastMove.captured());
				lastMove.captured().setSquare(get(lastMove.getTo().getx(), lastMove.getTo().gety()));
			}
		}
		turn = getOtherTurn();
		if (getTurn().equals("White")) {
			whiteMoves--;
		} else {
			blackMoves--;
		}
		// HANDLE PAWN PROMOTIONS
		// HANDLE KING SQUARES
		// HANDLE EN PASSANT
		log.remove(log.get(log.size() - 1));
	}

	String winner() {
		if (whitePieces.size() == 1 && blackPieces.size() == 1) {
			return "Draw";
		}
		Iterator<Move> currentMoves = allMoves();
		if (!currentMoves.hasNext()) {
			if (inCheck(getTurn())) {
				winner = getOtherTurn();
				return winner;
			}
			return "Draw";
		}
		return null;
	}

	/* Sets up a new chess board in initial position */
	void init() {
		log = new ArrayList<Move>();
		doNoCheck = true;
		wking = null;
		bking = null;
		promote = null;
		turn = "White";
		winner = null;
		map = new Square[SIZE][SIZE];
		blackMoves = 0;
		whiteMoves = 0;
		whitePieces = new ArrayList<Piece>();
		blackPieces = new ArrayList<Piece>();
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				map[i][j] = new Square(i, j, this);
			}
		}
		/*
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				put(i, j, new Nopiece(this));
			}
		}
		*/
		placePawns();
		placeKnights();
		placeRooks();
		placeBishops();
		placeQueens();
		placeKings();



	}
	/* Places pawns on the initial board. */
	void placePawns() {
		for (int i = 0; i < 8; i++) {
			put(i, 6, new Pawn(get(i, 6), this, "Black"));
			put(i, 1, new Pawn(get(i, 1), this, "White"));
			/*whitePieces.add(get(i, 6).getPiece());
			blackPieces.add(get(i, 1).getPiece()); */
		}
	}

	/* Places knights on the initial board */
	void placeKnights() {
		put(1, 0, new Knight(get(1, 0), this, "White"));
		put(6, 0, new Knight(get(6, 0), this, "White"));
		put(1, 7, new Knight(get(1, 7), this, "Black"));
		put(6, 7, new Knight(get(6, 7), this, "Black"));
		/*whitePieces.add(get(1, 0).getPiece());
		whitePieces.add(get(6, 0).getPiece());
		blackPieces.add(get(1, 7).getPiece());
		blackPieces.add(get(6, 7).getPiece());*/

	}

	/* Places Rooks on the initial board */
	void placeRooks() {
		put(0, 0, new Rook(get(0, 0), this, "White"));
		put(7, 0, new Rook(get(7, 0), this, "White"));
		put(0, 7, new Rook(get(0, 7), this, "Black"));
		put(7, 7, new Rook(get(7, 7), this, "Black"));
		/*whitePieces.add(get(0, 0).getPiece());
		whitePieces.add(get(7, 0).getPiece());
		blackPieces.add(get(0, 7).getPiece());
		blackPieces.add(get(7, 7).getPiece());*/
	}

	/* Places Bishops on the initial board */
	void placeBishops() {
		put(2, 0, new Bishop(get(2, 0), this, "White"));
		put(5, 0, new Bishop(get(5, 0), this, "White"));
		put(2, 7, new Bishop(get(2, 7), this, "Black"));
		put(5, 7, new Bishop(get(5, 7), this, "Black"));
		/*whitePieces.add(get(2, 0).getPiece());
		whitePieces.add(get(5, 0).getPiece());
		blackPieces.add(get(2, 7).getPiece());
		blackPieces.add(get(5, 7).getPiece());*/
	}

	/* Places Queens on the initial board */
	void placeQueens() {
		put(3, 0, new Queen(get(3, 0), this, "White"));
		put(3, 7, new Queen(get(3, 7), this, "Black"));
		/*whitePieces.add(get(3, 0).getPiece());
		blackPieces.add(get(3, 7).getPiece());*/
	}

	/* Places Kings on the initial board */
	void placeKings() {
		put(4, 0, new King(get(4, 0), this, "White"));
		put(4, 7, new King(get(4, 7), this, "Black"));
		/*whitePieces.add(get(4, 0).getPiece());
		blackPieces.add(get(4, 7).getPiece());*/
		wking = get(4, 0);
		bking = get(4, 7);
	}

	Square askPromote() {
		if (promote == null) {
			return null;
		}
		Square r = promote;
		promote = null;
		return r;
	}

	/* For tesing */
	boolean noCheck() {
		return true;
	}

	/* return true iff COLOR's square s is attacked by an opponent's piece. */
	boolean attacked(Square s, String color) {
		List<Piece> pieces = (color.equals("White")) ? blackPieces : whitePieces;
		for (int i = 0; i < pieces.size(); i++) {
			if (pieces.get(i).attacks(s)) {
				return true;
			}
		}
		return false;
	}

	/* Returns true iff color's king is currently in check */
	boolean inCheck(String color) {
		Board b = new Board(this);
		if (color.equals("White")) {
			for (int i = 0; i < b.blackPieces.size(); i++) {
				if (!(b.blackPieces.get(i) instanceof King) && b.blackPieces.get(i).attacks(b.wking)) {
					//System.out.println("This move would place White in check.");
					return true;
				}
			}
			return false;
		} else {
			for (int i = 0; i < b.whitePieces.size(); i++) {
				if (!(b.whitePieces.get(i) instanceof King) && b.whitePieces.get(i).attacks(b.bking)) {
					//System.out.println("This move would place Black in check.");
					return true;
				}
			}
			return false;
		}
	}

	/* Returns whether or not this move checks TURN's king. Will implement later */
	boolean noCheck(Square from, Square to) {
		if (!doNoCheck) {
			return true;
		}
		//System.out.println("Check");
		Board b = new Board(this);
		Piece p = b.get(from.getx(), from.gety()).getPiece();
		//b.get(from.getx(), from.gety()).empty();
		//b.put(to.getx(), to.gety(), p);
		b.get(from.getx(), from.gety()).empty();
		b.put(to.getx(), to.gety(), p);
		if (p instanceof King) {
			if (b.getTurn().equals("White")) {
				b.wking = b.get(to.getx(), to.gety());
			} else if (b.getTurn().equals("Black")) {
				b.bking = b.get(to.getx(), to.gety());
			}
		}
		//System.out.println("Printing copied board:");
		//System.out.println(b.toString());
		if (b.getTurn() == "White") {
			for (int i = 0; i < b.blackPieces.size(); i++) {
				if (b.blackPieces.get(i).attacks(b.wking)) {
					//System.out.println("This move would place White in check.");
					return false;
				}
			}
			return true;
		} else {
			//System.out.println("its Black's turn");
			//System.out.println(b.whitePieces.size());
			for (int i = 0; i < b.whitePieces.size(); i++) {
				//System.out.println("checking " + whitePieces.get(i).getSymbol());
				if (b.whitePieces.get(i).attacks(b.bking)) {
					//System.out.println("This move would place Black in check.");
					return false;
				}
			}
			return true;
		}
		//need to write attacks for each piece, and copy (above) for this to work.
	}

	/* Places p on square (i, j) */
	void put(int i, int j, Piece p) {
		//System.out.println("Putting piece with color: " + p.getColor());
		map[i][j].put(p);
	}

	void put(Square s, Piece p) {
		s.put(p);
	}

	/* Returns the square (i, j) */
	Square get(int i, int j) {
		return map[i][j];
	}

	/* Switch whose turn it is */
	void turn() {
		if (turn == "White") {
			whiteMoves++;
		} else {
			blackMoves++;
		}
		turn = (turn == "White") ? "Black" : "White";
	}

	int getWhiteMoves() {
		return whiteMoves;
	}

	int getBlackMoves() {
		return blackMoves;
	}


	@Override
    public String toString() {
        String r = "";
        for (int row = SIZE - 1; row >= 0; row--) {
            for (int col = 0; col < SIZE; col++) {
                if (col == SIZE - 1) {
                    r += get(col, row).getPiece().getSymbol();
                } else if (col == 0) {
                    r +=  "  " + Integer.toString(row+1) + " " + get(col, row).getPiece().getSymbol() + " ";
                } else {
                    r += get(col, row).getPiece().getSymbol() + " ";
                }
            }
            r += "\n";

        }
        r += "     A  B  C  D  E  F  G  H  \n";
        return r;

    }

    void promotePawn(Square s, String color, String promo) {
    	if (color.equals("White")) {
    		whitePieces.remove(s.getPiece());
    	} else {
    		blackPieces.remove(s.getPiece());
    	}
    	if (promo.toUpperCase().equals("Q")) {
			put(s, new Queen(s, this, color));
		} else if ((promo.toUpperCase().equals("K"))) {
			put(s, new Knight(s, this, color));
		} else if ((promo.toUpperCase().equals("R"))) {
			put(s, new Rook(s, this, color));
		} else if ((promo.toUpperCase().equals("B"))) {
			put(s, new Bishop(s, this, color));
		}
    }

    int getMoveNumber() {
    	return (getTurn() == "White") ? whiteMoves : blackMoves;
    }

    /* Returns whose turn it is */
    String getTurn() {
    	return turn;
    }

    void tempTurn() {
    	turn = (turn == "White") ? "Black" : "White";
    }

    String getOtherTurn() {
    	return (turn == "White") ? "Black" : "White";
    }

    Iterator<Move> allMoves() {
    	return new allMoveIterator(getTurn());
    }


    private class allMoveIterator implements Iterator<Move> {
    	int i;
    	String color;
    	List<Piece> pieces;
    	Move m;
    	Iterator<Move> curr;

    	allMoveIterator(String col) {
    		color = col;
    		curr = null;
    		i = -1;
    		pieces = (col.equals("White")) ? whitePieces : blackPieces;
    		m = null;
    		toNext();
    	}

    	@Override
    	public boolean hasNext() {
    		return i < pieces.size();
    	}

    	@Override 
    	public Move next() {
    		//System.out.println("Current piece: " + pieces.get(i).getSymbol());
    		Move r = m;
    		toNext();
    		return r;

    	}

    	void toNext() {
    		if (curr != null && curr.hasNext()) {
    			m = curr.next();
    		} else {
    			while( curr == null || !curr.hasNext()) {
    				i++;
    				if (!hasNext()) {
    					return;
    				}
    				curr = pieces.get(i).legalMoves();
    			}
    			m = curr.next();
    		}
    	}



    }





}

