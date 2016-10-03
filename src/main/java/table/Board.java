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
    private int nodeCount = 0;
    private ArrayList<ArrayList<Node>> board;

    private class Node implements Ball {
        private int i;
        private int j;

        Node(int i, int j) {
            this.i = i;
            this.j = j;
        }

        private boolean adjacent(int i, int j) {
            return this.i - i == 1 || i - this.i == 1 || this.j - j == 1 || j - this.j == 1;
        }

        @Override
        public boolean canMoveUp() {
            for (int j = this.j - 1; j >= 0; j--)
                if (thereIsANode(j, i) && !adjacent(i, j))
                    return true;
            return false;
        }

        @Override
        public boolean canMoveDown() {
            for (int j = this.j + 1; j < board.size(); j++)
                if (thereIsANode(j, i) && !adjacent(i, j))
                    return true;
            return false;
        }

        @Override
        public boolean canMoveLeft() {
            for (int i = this.i - 1; i >= 0; i--)
                if (thereIsANode(j, i) && !adjacent(i, j))
                    return true;
            return false;
        }

        @Override
        public boolean canMoveRight() {
            for (int i = this.i + 1; i < board.get(j).size(); i++)
                if (thereIsANode(j, i) && !adjacent(i, j))
                    return true;
            return false;
        }

        private void removeNode(Node node) {
            board.get(node.j).add(node.i, null);
            board.get(node.j).remove(node.i + 1);
        }

        private void moveNodeTo(int j, int i) {
            board.get(j).add(i, this);
            board.get(j).remove(i + 1);
            this.j = j;
            this.i = i;
        }

        @Override
        public void moveUp() {
            for (int j = this.j - 1; j >= 0; j--)
                if (thereIsANode(j, i) && !adjacent(i, j)) {
                    removeNode(this);

                    moveNodeTo(j + 1, this.i);

                    while (j >= 0 && thereIsANode(j, i))
                        j--;

                    removeNode(board.get(j + 1).get(i));
                    nodeCount--;

                    return;
                }
        }

        @Override
        public void moveDown() {
            for (int j = this.j + 1; j < board.size(); j++)
                if (thereIsANode(j, i) && !adjacent(i, j)) {
                    removeNode(this);

                    moveNodeTo(j - 1, i);

                    while (j < board.size() && thereIsANode(j, i))
                        j++;

                    removeNode(board.get(j - 1).get(i));
                    nodeCount--;

                    return;
                }

        }

        @Override
        public void moveLeft() {
            for (int i = this.i - 1; i >= 0; i--)
                if (thereIsANode(j, i) && !adjacent(i, j)) {
                    removeNode(this);

                    moveNodeTo(this.j, i + 1);

                    while (i >= 0 && thereIsANode(j, i))
                        i--;

                    removeNode(board.get(j).get(i + 1));
                    nodeCount--;

                    return;
                }
        }

        @Override
        public void moveRight() {
            for (int i = this.i + 1; i < board.get(j).size(); i++)
                if (thereIsANode(j, i) && !adjacent(i, j)) {
                    removeNode(this);

                    moveNodeTo(this.j, i - 1);

                    while (i < board.get(j).size() && thereIsANode(j, i))
                        i++;

                    removeNode(board.get(j).get(i - 1));
                    nodeCount--;

                    return;
                }
        }
    }

    private class Iterator implements BoardIterator {
        private Node currentNode = null;

        private Iterator() {
            for (ArrayList<Node> row : board)
                for (Node node : row)
                    if (node != null) {
                        currentNode = node;
                        return;
                    }
        }

        @Override
        public Ball current() {
            return currentNode;
        }

        private int calculateNextI(int i, int j) {
            i = i + 1;

            if (i >= board.get(j).size()) {
                i = 0;
            }

            return i;
        }

        private int calculateNextJ(int i, int j) {
            if (i == 0)
                j++;

            if (j >= board.size())
                j = 0;

            return j;
        }

        @Override
        public Ball next() {
            int i = calculateNextI(currentNode.i, currentNode.j);
            int j = calculateNextJ(i, currentNode.j);

            while (true) {
                if (thereIsANode(j, i)) {
                    currentNode = board.get(j).get(i);
                    return currentNode;
                }

                i = calculateNextI(i, j);
                j = calculateNextJ(i, j);
            }

        }

        private int calculatePreviousI(int i, int j) {
            i--;

            if (i < 0)
                if (j == 0)
                    i = board.get(board.size() - 1).size() - 1;
                else
                    i = board.get(j - 1).size() - 1;

            return i;
        }

        private int calculatePreviousJ(int i, int j) {
            if (j == 0) {
                if (i == board.get(board.size() - 1).size() - 1)
                    j--;
            } else if (i == (board.get(j - 1).size() - 1))
                j--;

            if (j < 0)
                j = board.size() - 1;

            return j;
        }

        @Override
        public Ball previous() {
            int i = calculatePreviousI(currentNode.i, currentNode.j);
            int j = calculatePreviousJ(i, currentNode.j);

            while (true) {
                if (thereIsANode(j, i)) {
                    currentNode = board.get(j).get(i);
                    return currentNode;
                }

                i = calculatePreviousI(i, j);
                j = calculatePreviousJ(i, j);
            }
        }

        @Override
        public String toString(){
            return tableWithCurrentToString(this);
        }

        private boolean currentNodeEquals(Node node) {
            return node == currentNode;
        }
    }

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
        int j = 0;
        while (inputFile.hasNext()) {
            board.add(putALineToBoard(inputFile.nextLine(), j++));
        }
    }

    private ArrayList<Node> putALineToBoard(String line, int j) {
        ArrayList<Node> temp = new ArrayList<>();

        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '0')
                temp.add(null);
            else if (line.charAt(i) == '1') {
                temp.add(new Node(i, j));
                nodeCount++;
            }
        }

        return temp;
    }

    private boolean thereIsANode(int j, int i) {
        return board.get(j).get(i) != null;
    }

    private String tableWithCurrentToString(Iterator iterator) {
        String result = "";
        int tableWidth = board.get(0).size();

        for (int i = 0; i < tableWidth; i++)
            result += "__";
        result += "_\n";

        for (ArrayList<Node> aBoard : board) {
            for (Node node : aBoard) {
                if (node == null)
                    result += "|_";
                else if (iterator != null && iterator.currentNodeEquals(node))
                    result += "|C";
                else
                    result += "|#";
            }
            result += "|\n";
        }

        return result;
    }

    @Override
    public String toString() {
        return tableWithCurrentToString(null);
    }

    public BoardIterator getIterator() {
        return new Iterator();
    }


    /**
     * Check if there is a node in board that can moves.
     *
     * @return true if board already solved, false otherwise.
     */

    public boolean solved() {
        return !canMove() && nodeCount <= 1;
    }

    public boolean canMove(Ball ball) {
        return ball.canMoveUp() || ball.canMoveDown() || ball.canMoveLeft() || ball.canMoveRight();
    }


    /**
     * Start from (0,0) and check each node to see it can moves or not.
     * If it can moves terminates search and return true, if it can not moves,
     * check next node;
     *
     * @return true if there is a node that can moves
     */

    public boolean canMove() {
        for (ArrayList<Node> line : board) {
            for (Node node : line) {
                if (node == null) continue;
                if (canMove(node))
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

    public boolean failed() {
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