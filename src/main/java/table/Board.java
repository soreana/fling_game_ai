package table;

import algorithm.Algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by sinakashipazha on 9/30/16.
 */
abstract public class Board {
    private int nodeCount =0;

    private class Node {
    }

    private ArrayList<ArrayList<Node>> board;

    Board() {
        board = new ArrayList<>();
        File fling = new File("target/fling.txt");
        Scanner inputFile = null;

        try {
            inputFile = new Scanner(fling);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        inputBoard(inputFile);
    }

    private void inputBoard(Scanner inputFile) {
        while (inputFile.hasNext()) {
            board.add(putALineToBoard(inputFile.nextLine()));
        }
    }

    private ArrayList<Node> putALineToBoard(String line) {
        ArrayList<Node> temp = new ArrayList<>();

        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '0')
                temp.add(null);
            else if (line.charAt(i) == '1') {
                temp.add(new Node());
                nodeCount++;
            }
        }

        return temp;
    }

    @Override
    public String toString() {
        String result = "";
        int tableWidth = board.get(0).size();

        for (int i = 0; i < tableWidth; i++)
            result += "__";
        result += "_\n";

        for (ArrayList<Node> aBoard : board) {
            tableWidth = aBoard.size();
            for (int i = 0; i < tableWidth; i++) {
                if (aBoard.get(i) == null)
                    result += "|_";
                else
                    result += "|#";
            }
            result += "|\n";
        }

        return result;
    }

    /**
     *  Check if there is a node in board that can moves.
     *
     * @return true if board already solved, false otherwise.
     */

    public boolean solved() {
        return !canMove() && nodeCount <= 1;
    }

    // TODO implement this method

    public boolean canMove(Node node) {
        return false;
    }


    /**
     * Start from (0,0) and check each node to see it can moves or not.
     * If it can moves terminates search and return true, if it can not moves,
     * check next node;
     *
     * @return true if there is a node that can moves
     */

    public boolean canMove(){
        for (ArrayList<Node> line : board){
            for( Node node : line){
                if (node == null) continue;
                if ( canMove(node))
                    return true;
            }
        }
        return false;
    }


    /**
     * if board can't be solved return true, false otherwise.
     *
     * @return true if board failed, false otherwise
     */

    public boolean failed(){
        return !solved() && !canMove();
    }


    /**
     * Implement this method based on your preferred algorithm
     *
     * @param algorithmName name of solver algorithm
     * @return algorithm you want to solve
     */

    public abstract Algorithm getSolvingAlgorithm(String algorithmName);
}
