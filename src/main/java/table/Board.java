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
    private class Node {

    }

    private ArrayList<ArrayList<Node>> board;

    Board(){
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

    private void inputBoard(Scanner inputFile){
        while (inputFile.hasNext()){
            board.add(putALineToBoard(inputFile.nextLine()));
        }
    }

    private ArrayList<Node> putALineToBoard(String line){
        ArrayList<Node> temp = new ArrayList<>();

        for(int i=0; i< line.length() ; i++) {
            if(line.charAt(i) == '0')
                temp.add(null);
            else if (line.charAt(i) == '1')
                temp.add(new Node());
        }

        return temp;
    }

    @Override
    public String toString(){
        String result = "";
        int tableWidth = board.get(0).size();

        for (int i=0 ; i<tableWidth; i++)
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
     * Implement this method based on your choice
     *
     * @param algorithmName name of solver algorithm
     * @return algorithm you want to solve
     */

    public abstract Algorithm getSolvingAlgorithm (String algorithmName );
}
