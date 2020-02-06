package chess;

import java.util.*;
import static chess.Piece.*;

class Board {

	/* The size of the board */
	static final int SIZE = 8;

	/* A list of white's pieces */
	List<Piece> whitepieces;

	/* A list of black's pieces */
	List<Piece> blackpieces;

	/* A map of all pieces on the board */
	private Square[][] map;

	/* The last move played (for undo) */
	private Move lastMove;

	/* The piece whose turn it is */
	private String turn;

	/* The piece that has won the game */
	private String winner;

	/* The number of moves made by black */
	private int blackMoves;

	/* The number of moves made by white */
	private int whiteMoves;

	/* Initialize a new chess board */
	Board() {
		init();
	}

	/* Copies model */
	Board(Board model) {
		copy(model);
	}

	/* copies model */
	void copy(Board model) { }

	/* Sets up a new chess board in initial position */
	void init() {
		turn = "White";
		winner = null;
		map = new Square[SIZE][SIZE];
		blackMoves = 0;
		whiteMoves = 0;
		whitepieces = new ArrayList<Piece>();
		blackpieces = new ArrayList<Piece>();
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
		}
	}

	/* Places knights on the initial board */
	void placeKnights() {
		put(1, 0, new Knight(get(1, 0), this, "White"));
		put(6, 0, new Knight(get(6, 0), this, "White"));
		put(1, 7, new Knight(get(1, 7), this, "Black"));
		put(6, 7, new Knight(get(6, 7), this, "Black"));

	}

	/* Places Rooks on the initial board */
	void placeRooks() {
		put(0, 0, new Rook(get(0, 0), this, "White"));
		put(7, 0, new Rook(get(7, 0), this, "White"));
		put(0, 7, new Rook(get(0, 7), this, "Black"));
		put(7, 7, new Rook(get(7, 7), this, "Black"));
	}

	/* Places Bishops on the initial board */
	void placeBishops() {
		put(2, 0, new Bishop(get(2, 0), this, "White"));
		put(5, 0, new Bishop(get(5, 0), this, "White"));
		put(2, 7, new Bishop(get(2, 7), this, "Black"));
		put(5, 7, new Bishop(get(5, 7), this, "Black"));
	}

	/* Places Queens on the initial board */
	void placeQueens() {
		put(3, 0, new Queen(get(3, 0), this, "White"));
		put(3, 7, new Queen(get(3, 7), this, "Black"));
	}

	/* Places Kings on the initial board */
	void placeKings() {
		put(4, 0, new King(get(4, 0), this, "White"));
		put(4, 7, new King(get(4, 7), this, "Black"));
	}

	/* Returns whether or not this move checks TURN's king. Will implement later */
	boolean noCheck() {
		//System.out.println("Check");
		return true;
	}

	/* Places p on square (i, j) */
	void put(int i, int j, Piece p) {
		map[i][j].put(p);
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
                    r +=  "   " + get(col, row).getPiece().getSymbol() + " ";
                } else {
                    r += get(col, row).getPiece().getSymbol() + " ";
                }
            }
            r += "\n";

        }
        return r;

    }

    int getMoveNumber() {
    	return (getTurn() == "White") ? whiteMoves : blackMoves;
    }

    /* Returns whose turn it is */
    String getTurn() {
    	return turn;
    }





}

