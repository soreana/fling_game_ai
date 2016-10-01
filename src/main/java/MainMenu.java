import algorithm.Algorithm;
import table.Board;
import table.MyBoard;

import java.util.Scanner;

/**
 * Created by sinakashipazha on 9/30/16.
 */
public class MainMenu {
    private static Scanner console = new Scanner(System.in);
    private static Board board = new MyBoard();

    public static void main(String[] args) {
        boolean exit = false;

        // TODO set up board
        System.out.println(board);

        while (!exit) {
            printMenuMessage();
            Algorithm algorithm = chooseAlgorithm();
            if(algorithm == null)
                exit = true;

            // TODO algorithm things
        }

    }

    private static Algorithm chooseAlgorithm(){
        while (true) {
            try {
                String rawInput = console.next();
                switch (rawInput) {
                    case "0":
                        return board.getSolvingAlgorithm("BFS");
                    case "u":
                        return board.getSolvingAlgorithm("UserMode");
                    case "x":
                        return null;
                    default:
                        throw new RuntimeException("Algorithm not found.");
                }
            } catch (RuntimeException r) {
                if (r.getMessage().equals("Algorithm not found.")) {
                    System.out.println((char) 27 + "[31mErr : "+ "Incorrect input, try again."+ (char)27 + "[0m" );
                    System.out.print("Try again: ");
                }
            }
        }
    }

    private static void printMenuMessage() {
        System.out.println("0. BFS");
        System.out.println("u. User Mode");
        System.out.println("Please enter your algorithm number, or \'x\' to exit.");
    }
}
