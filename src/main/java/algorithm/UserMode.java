package algorithm;

import table.Board;

import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by sinakashipazha on 10/2/16.
 */
public class UserMode implements Algorithm {
    private final Board board;


    private void printWelcomeMessage() {
        System.out.println();
        System.out.println("Hello here is a key you can use.");
        System.out.println("\tUp: k");
        System.out.println("\tDown: j");
        System.out.println("\tLeft: h");
        System.out.println("\tRight: l");
        System.out.println("\tNext Node: n");
        System.out.println("\tPrevious Node: p");
        System.out.println("\tExit: x");
    }

    public UserMode(Board board) {
        this.board = board;
    }

    public void start() {
        boolean exit = false;

        printWelcomeMessage();
        Scanner console = new Scanner(System.in);
        board.showCurrentNode();
        System.out.println(board);
        while (!exit) {
            switch (console.next()) {
                case "k":
                case "j":
                case "h":
                case "l":
                case "n":
                    board.getIterator().next();
                    System.out.println(board);
                    break;
                case "p":
                    board.getIterator().previous();
                    System.out.println(board);
                    break;
                case "x":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid input.");
            }
        }
        board.doNotShowCurrentNode();
    }
}
