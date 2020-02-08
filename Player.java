package chess;
import java.util.*;

abstract class Player {

	String color; // white or black

	Board b;

	List<Piece> pieces;

	/* Makes a move on board b for which it is my turn. Returns "None" if
	a move was successfully made, or a string corresponding to a command 
	issued by the player (such as reset or undo) otherwise. */


	abstract String makeMove();

}