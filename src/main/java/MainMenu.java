import algorithm.Algorithm;
import table.Board;
import table.MyBoard;

import java.util.Scanner;

/**
 * Created by sinakashipazha on 9/30/16.
 */
public class MainMenu {

    public static void main(String[] args) {

        while (true) {
            printMenuMessage();
            Algorithm algorithm = chooseAlgorithm();

            if (algorithm == null) break;

            algorithm.start();
        }
    }

    private static Algorithm chooseAlgorithm() {
        Scanner console = new Scanner(System.in);
        Board board = new MyBoard();

        while (true) {
            try {
                board.reset();
                String rawInput = console.next();
                switch (rawInput) {
                    case "0":
                        return board.getSolvingAlgorithm("DFS");
                    case "u":
                        return board.getSolvingAlgorithm("UserMode");
                    case "x":
                        return null;
                    default:
                        throw new RuntimeException("Algorithm not found.");
                }
            } catch (RuntimeException r) {
                if (r.getMessage().equals("Algorithm not found.")) {
                    System.out.println((char) 27 + "[31mErr : " + "Incorrect input, try again." + (char) 27 + "[0m");
                    System.out.print("Try again: ");
                }
            }
        }
    }

    private static void printMenuMessage() {
        System.out.println("0. DFS");
        System.out.println("u. User Mode");
        System.out.println("x. Exit");
        System.out.print("Please enter your command: ");
    }
}
