package chess;

import java.util.*;

class AIPlayer2 extends Player {

	Board b;
	String color;
	Move _move;
	private int INFY = 999999;


	AIPlayer2(Board brd, String col) {
		b = brd;
		color = col;
	}

	@Override
	String makeMove() {
		//System.out.println(pawnEvalWhite[1][6]);
		boolean max = color.equals("White");
		Board copy = new Board(b);
		//System.out.print("Board score: "); System.out.println(score(copy));
		copy.doNoCheck = true;
		Move found = minimaxroot(findDepth(b), copy, max);
		Move making = new Move(found, b);
		System.out.println(making.getSymbol());
		if (!making.makeMove()) {
			System.out.println("uh oh");
			System.out.println(b.toString());
			System.out.println(making.getSymbol());
			System.out.println(color);
			System.out.println(b.getTurn());
			Iterator<Move> it = b.allMoves();
			while (it.hasNext()) {
				System.out.println(it.next().getSymbol());
			}
			System.exit(1);
		}
		Square sq = b.askPromote();
		if (sq != null) {
			b.promotePawn(sq, color, "Q");
		}

		return "None";
	}

	private Move minimaxroot(int depth, Board brd, boolean isMaximisingPlayer) {
		if (isMaximisingPlayer) {
			Iterator<Move> it = brd.allMoves();
			List<Move> uglyMoves = new ArrayList<Move>();
			while (it.hasNext()) { uglyMoves.add(it.next()); }
			double bestMove = -INFY;
			Move bestMoveFound = null;
			int count = 0;
			for(Move m : uglyMoves) {
				if(m.makeMove()) {
					Square sq = brd.askPromote();
					if (sq != null) {
						brd.promotePawn(sq, "White", "Q");
					}
					double value = minimax(depth - 1, brd, -INFY - 1, INFY + 1, !isMaximisingPlayer);
					//System.out.println(brd.toString());
					//System.out.print("This score: "); System.out.println(value);
					//System.out.print("Best score: "); System.out.println(bestMove);
					brd.undo();
				
				//System.out.println("Should not be changing: ");
					if (value >= bestMove) {
						if (value == bestMove) {
							count++;
							if (new Random().nextInt(count) == 0) {
								bestMove = value;
								bestMoveFound = m;
							}
						} else {
							bestMove = value;
							bestMoveFound = m;
							count = 0;
						}
					}
				}
			}
			System.out.print("Best score: "); System.out.println(bestMove);
			return bestMoveFound;
		} else {
			Iterator<Move> it = brd.allMoves();
			List<Move> uglyMoves = new ArrayList<Move>();
			while (it.hasNext()) { uglyMoves.add(it.next()); }
			double bestMove = INFY;
			Move bestMoveFound = null;
			for(Move m : uglyMoves) {
				if(m.makeMove()) {
					Square sq = brd.askPromote();
					if (sq != null) {
						brd.promotePawn(sq, "Black", "Q");
					}
					double value = minimax(depth - 1, brd, -INFY, INFY, !isMaximisingPlayer);
					brd.undo();
				//System.out.println("Should not be changing: ");
					if (value <= bestMove) {
						bestMove = value;
						bestMoveFound = m;
					}
				}
			}
			System.out.print("Best score: "); System.out.println(bestMove);
			return bestMoveFound;
		}
	}

	double max (double x, double y) {
		return (x > y) ? x : y;
	}

	double min (double x, double y) {
		return (x > y) ? y : x;
	}

	double minimax(int depth, Board brd, double alpha, double beta, boolean isMaximisingPlayer) {
		if (depth == 0) {
			return score(brd);
		}
		brd = new Board(brd);
		brd.doNoCheck = true;
		Iterator<Move> it = brd.allMoves();
		List<Move> uglyMoves = new ArrayList<Move>();
		while (it.hasNext()) { uglyMoves.add(it.next()); }
		if (isMaximisingPlayer) {
			double bestMove = -INFY;
	        for (Move m : uglyMoves) {
	            if (m.makeMove()) {
	            	Square sq = brd.askPromote();
					if (sq != null) {
						brd.promotePawn(sq, "White", "Q");
					}
	            	bestMove = max(bestMove, minimax(depth - 1, brd, alpha, beta, !isMaximisingPlayer));
	            	brd.undo();
	            }
	            alpha = max(alpha, bestMove);
	            if (beta <= alpha) {
	                return bestMove;
	            }
	        }
	        return bestMove;
		} else {
			double bestMove = INFY;
	        for (Move m : uglyMoves) {
	            if (m.makeMove()) {
	            	Square sq = brd.askPromote();
					if (sq != null) {
						brd.promotePawn(sq, "Black", "Q");
					}
	        		bestMove = min(bestMove, minimax(depth - 1, brd, alpha, beta, !isMaximisingPlayer));
	           	 	brd.undo();
	            }
	            beta = min(beta, bestMove);
	            if (beta <= alpha) {
	                return bestMove;
	            }
	        }
	        return bestMove;
		}
	}

	/*

	private Move findMove() {
		if (color.equals("White")) {
			findMove(b, findDepth(b), true, 1, -10000, 10000);
		} else {
			findMove(b, findDepth(b), true, -1, -10000, 10000);
		}
		return new Move(b.get(_move.getFrom().getx(), _move.getFrom().gety()),
						b.get(_move.getTo().getx(), _move.getTo().gety()), b);
	}

	private int findMove(Board board, int depth, boolean saveMove, int sense, int alpha, int beta) {
		if (depth == 0) {
			return score(board);
		}
		Board copy = new Board(board);
		Iterator<Move> it = copy.allMoves();
		List<Move> moves = new ArrayList<Move>();
		while (it.hasNext()) {
			moves.add(it.next());
		}
		Move bestMove = moves.get(0);
		for (Move m : moves) {
			m.makeMove();
			if (sense == 1) {
				int response = findMove(copy, depth - 1, false, -1, alpha, beta);
				if (response >= alpha) {
					bestMove = m;
					alpha = response;
					if (beta <= alpha) {
						copy.undo();
						break;
					}
				}
			} else {
				int response = findMove(copy, depth - 1, false, 1, alpha, beta);
				if (response < beta) {
					bestMove = m;
					beta = response;
					if (alpha >= beta) {
						copy.undo();
						break;
					}
				}
			}
			copy.undo();
		}
		if (saveMove) {
			_move = bestMove;
		}
		//bestMove.makeMove();
		return score(copy);

	}
	*/

	int findDepth(Board brd) {
		pieces = (color.equals("White")) ? brd.whitePieces : brd.blackPieces;
		int s = pieces.size();
		if (s > 4) {
			return 4;
		}
		//return (s == 1) ? 7 : 5;
		return (s == 1) ? 5 : 4;
	}


	/* Scores the Board b heuristically */
	double score(Board brd) {
			List<Piece> whitePieces = brd.whitePieces;
		List<Piece> blackPieces = brd.blackPieces;
		
		String winner = brd.winner();
		if (winner != null) {
			if (winner.equals("White")) {
				return -INFY - 1;
			} else if (winner.equals("Black")) {
				return INFY + 1;
			} else {
				return 0;
			}
		}
		return count(whitePieces) + count(blackPieces);
	}

	double count(List<Piece> pieces) {
		double r = 0;
		for (Piece p : pieces) {
			if (p.getColor().equals("White")) {
				r += getAbsoluteValue(p, p.getSquare().getx(), p.getSquare().gety());
			} else {
				r -= getAbsoluteValue(p, p.getSquare().getx(), p.getSquare().gety());
			}
		}
		return r;
	}

	double getAbsoluteValue(Piece piece, int x , int y) {
        if (piece instanceof Pawn) {
            return  (10 + ((piece.getColor().equals("White") ? pawnEvalWhite[y][x] : pawnEvalBlack[y][x])));
        } else if (piece instanceof Rook) {
            return  (50 + ((piece.getColor().equals("White")) ? rookEvalWhite[y][x] : rookEvalBlack[y][x]));
        } else if (piece instanceof Knight) {
            return  (30 + knightEval[y][x]);
        } else if (piece instanceof Bishop) {
            return  (30 + ((piece.getColor().equals("White")) ? bishopEvalWhite[y][x] : bishopEvalBlack[y][x]));
        } else if (piece instanceof Queen) {
            return  (90 + evalQueen[y][x]);
        } else if (piece instanceof King) {
            return  (900 + ((piece.getColor().equals("White")) ? kingEvalWhite[y][x] : kingEvalBlack[y][x]));
        }
        return 0;
    };

	double[][] reverseArray(double[][] arr) {
		double[][] r = new double[8][8];
		for (int row = 0; row < arr.length; row++) {
			for (int col = 0; col < arr[row].length; col++) {
				r[7 - row][col] = arr[row][col];
			}
		}
		return r;
	}

	double[][] pawnEvalBlack = new double[][]{
        {0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0},
        {5.0,  5.0,  5.0,  5.0,  5.0,  5.0,  5.0,  5.0},
        {1.0,  1.0,  2.0,  3.0,  3.0,  2.0,  1.0,  1.0},
        {0.5,  0.5,  1.0,  2.5,  2.5,  1.0,  0.5,  0.5},
        {0.0,  0.0,  0.0,  2.0,  2.0,  0.0,  0.0,  0.0},
        {0.5, -0.5, -1.0,  0.0,  0.0, -1.0, -0.5,  0.5},
        {0.5,  1.0, 1.0,  -2.0, -2.0,  1.0,  1.0,  0.5},
        {0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0}
    };


    double[][] pawnEvalWhite = reverseArray(pawnEvalBlack);

    double[][] knightEval = new double[][]{
        {-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0},
        {-4.0, -2.0,  0.0,  0.0,  0.0,  0.0, -2.0, -4.0},
        {-3.0,  0.0,  1.0,  1.5,  1.5,  1.0,  0.0, -3.0},
        {-3.0,  0.5,  1.5,  2.0,  2.0,  1.5,  0.5, -3.0},
        {-3.0,  0.0,  1.5,  2.0,  2.0,  1.5,  0.0, -3.0},
        {-3.0,  0.5,  1.0,  1.5,  1.5,  1.0,  0.5, -3.0},
        {-4.0, -2.0,  0.0,  0.5,  0.5,  0.0, -2.0, -4.0},
        {-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0}
    };

    double[][] bishopEvalBlack = new double[][]{
	    { -2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0},
	    { -1.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -1.0},
	    { -1.0,  0.0,  0.5,  1.0,  1.0,  0.5,  0.0, -1.0},
	    { -1.0,  0.5,  0.5,  1.0,  1.0,  0.5,  0.5, -1.0},
	    { -1.0,  0.0,  1.0,  1.0,  1.0,  1.0,  0.0, -1.0},
	    { -1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0, -1.0},
	    { -1.0,  0.5,  0.0,  0.0,  0.0,  0.0,  0.5, -1.0},
	    { -2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0}
	};

	double[][] bishopEvalWhite = reverseArray(bishopEvalBlack);

	double[][] rookEvalBlack = new double[][]{
	    {0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0},
	    {0.5,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  0.5},
	    {-0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
	    {-0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
	    {-0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
	    {-0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
	    {-0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
	    {0.0,   0.0, 0.0,  0.5,  0.5,  0.0,  0.0,  0.0}
	};

	double[][] rookEvalWhite = reverseArray(rookEvalBlack);

	double[][] evalQueen = new double[][]{
	    {-2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0},
	    {-1.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -1.0},
	    {-1.0,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -1.0},
	    {-0.5,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -0.5},
	    { 0.0,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -0.5},
	    { -1.0,  0.5,  0.5,  0.5,  0.5,  0.5,  0.0, -1.0},
	    { -1.0,  0.0,  0.5,  0.0,  0.0,  0.0,  0.0, -1.0},
	    { -2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0}
	};

	double[][] kingEvalBlack = new double[][]{
	    { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
	    { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
	    { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
	    { -3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
	   	{ -2.0, -3.0, -3.0, -4.0, -4.0, -3.0, -3.0, -2.0},
	    { -1.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -1.0},
	    {  2.0,  2.0,  0.0,  0.0,  0.0,  0.0,  2.0,  2.0 },
    	{  2.0,  3.0,  1.0,  0.0,  0.0,  1.0,  3.0,  2.0 }
	};

	double[][] kingEvalWhite = reverseArray(kingEvalBlack);







	
}