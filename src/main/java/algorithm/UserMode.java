package algorithm;

import table.Ball;
import table.Board;
import table.BoardIterator;

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

        BoardIterator boardIterator = board.getIterator();
        Ball currentBall = boardIterator.current();

        while (!exit) {
            System.out.println(boardIterator);
            switch (console.next()) {
                case "k":
                    currentBall.moveUp();
                    break;
                case "j":
                    currentBall.moveDown();
                    break;
                case "h":
                    currentBall.moveLeft();
                    break;
                case "l":
                    currentBall.moveRight();
                    break;
                case "n":
                    currentBall = boardIterator.next();
                    break;
                case "p":
                    currentBall = boardIterator.previous();
                    break;
                case "x":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid input.");
            }
            if (board.solved()) {
                System.out.println("You Win.");
                break;
            } else if (board.failed()){
                System.out.println("You Lost.");
            }
        }
    }
}
