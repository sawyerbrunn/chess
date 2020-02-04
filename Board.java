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
	private Piece[][] map;

	/* The last move played (for undo) */
	private Move lastMove;

	/* The piece whose turn it is */
	private Piece turn;

	/* The piece that has won the game */
	private Piece winner;

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
		turn = WHITE;
		winner = EMPTY;
		map = new Piece[SIZE][SIZE];
		blackMoves = 0;
		whiteMoves = 0;
		whitepieces = new ArrayList<Piece>();
		blackpieces = new ArrayList<Piece>();
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				put(i, j, EMPTY);
			}
		}
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
			put(i, 6, BLACK_PAWN);
			put(i, 1, WHITE_PAWN);
		}
	}

	/* Places knights on the initial board */
	void placeKnights() {
		put(1, 0, WHITE_KNIGHT);
		put(6, 0, WHITE_KNIGHT);
		put(1, 7, BLACK_KNIGHT);
		put(6, 7, BLACK_KNIGHT);

	}

	/* Places Rooks on the initial board */
	void placeRooks() {
		put(0, 0, WHITE_ROOK);
		put(7, 0, WHITE_ROOK);
		put(0, 7, BLACK_ROOK);
		put(7, 7, BLACK_ROOK);
	}

	/* Places Bishops on the initial board */
	void placeBishops() {
		put(2, 0, WHITE_BISHOP);
		put(5, 0, WHITE_BISHOP);
		put(2, 7, BLACK_BISHOP);
		put(5, 7, BLACK_BISHOP);
	}

	/* Places Queens on the initial board */
	void placeQueens() {
		put(3, 0, WHITE_QUEEN);
		put(3, 7, BLACK_QUEEN);
	}

	/* Places Kings on the initial board */
	void placeKings() {
		put(4, 0, WHITE_KING);
		put(4, 7, BLACK_KING);
	}

	/* Places p on square (i, j) */
	private void put(int i, int j, Piece p) {
		map[i][j] = p;
	}

	/* Returns the piece on square (i, j) */
	private Piece get(int i, int j) {
		return map[i][j];
	}



	@Override
    public String toString() {
        String r = "";
        for (int row = SIZE - 1; row >= 0; row--) {
            for (int col = 0; col < SIZE; col++) {
                if (col == SIZE - 1) {
                    r += get(col, row).getSymbol();
                } else if (col == 0) {
                    r +=  "   " + get(col, row).getSymbol() + " ";
                } else {
                    r += get(col, row).getSymbol() + " ";
                }
            }
            r += "\n";

        }
        return r;

    }





}

