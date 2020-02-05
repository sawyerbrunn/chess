package chess;
import ucb.util.CommandArgs;

import java.io.*;
import java.util.Scanner;

class Main {

	static int getCoord(char c) {
		if (c == 'A') {
			return 0;
		} else if (c == 'B') {
			return 1;
		} else if (c == 'C') {
			return 2;
		} else if (c == 'D') {
			return 3;
		} else if (c == 'E') {
			return 4;
		} else if (c == 'F') {
			return 5;
		} else if (c == 'G') {
			return 6;
		} else {
			return 7;
		}
	}


	public static void main(String[] args) {
		Board b = new Board();
		System.out.println(b.toString());
		Scanner s = new Scanner(System.in);
		while(true) {
			System.out.println("Enter " + b.getTurn() + "'s Move:");
			String move = s.nextLine();
			if (move.toUpperCase().equals("EXIT")) {
				System.exit(1);
			}
			String from = move.substring(0, 2).toUpperCase();
			String to = move.substring(move.length() - 2, move.length()).toUpperCase();
			int y1 = Integer.parseInt(from.substring(1)) - 1;
			int y2 = Integer.parseInt(to.substring(1)) - 1;
			int x1 = getCoord(from.charAt(0));
			int x2 = getCoord(to.charAt(0));
			Square s1 = b.get(x1, y1);
			Square s2 = b.get(x2, y2);
			if (!s1.getPiece().isLegal(s1, s2)) {
				System.out.println("Please enter a legal move.");
			}
			System.out.println(s2.isEmpty());
			System.out.println(s2.getPiece().getColor().equals(s1.getPiece().getColor()));
			System.out.println(s1.getPiece().isLegal(s1, s2));
			s1.getPiece().move(s1, s2);
			System.out.println(b.toString());
		}

		
	}
}