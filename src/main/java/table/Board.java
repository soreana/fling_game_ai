package table;

import algorithm.Algorithm;
import javafx.geometry.Pos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Created by sinakashipazha on 9/30/16.
 */

abstract public class Board {
    private ArrayList<ArrayList<Node>> initialBoard;
    final private int initialNodeCount;

    private int nodeCount = 0;
    private ArrayList<ArrayList<Node>> board;

    public ArrayList<PossibleMove> getPossibleMoves() {
        ArrayList<PossibleMove> result = new ArrayList<>();

        for (ArrayList<Node> row : board)
            for (Node node : row)
                if (node != null)
                    result.addAll(node.getPossibleMoves());

        return result;
    }

    private class Move implements table.Move, PossibleMove {
        private Node deletedNode = null;
        private Node movedNode = null;
        private int startPointI, startPointJ;
        final private MoveState moveState;
        private String stringRepresentation;

        private Move(MoveState canNotMove) {
            moveState = canNotMove;
        }

        private Move(Node movedNode, MoveState moveState) {
            this.movedNode = movedNode;
            this.moveState = moveState;
        }

        private boolean canUndo() {
            return !thereIsANode(deletedNode.j, deletedNode.i) &&
                    !thereIsANode(startPointJ, startPointI) &&
                    board.get(movedNode.j).get(movedNode.i) == movedNode;
        }

        public void undo() {
            if (!canUndo())
                throw new RuntimeException("Can't undo move.");

            // undo move
            board.get(deletedNode.j).set(deletedNode.i, deletedNode);

            board.get(movedNode.j).set(movedNode.i, null);
            board.get(startPointJ).set(startPointI, movedNode);

            movedNode.i = startPointI;
            movedNode.j = startPointJ;
            nodeCount++;
        }

        private Move(Node deletedNode, Node movedNode, MoveState moveState
                , int startPointJ, int startPintI) {
            this.deletedNode = deletedNode;
            this.movedNode = movedNode;
            this.moveState = moveState;
            this.startPointJ = startPointJ;
            this.startPointI = startPintI;
        }

        @Override
        public table.Move move() {
            Move result = null;

            String stringRepresentationOfThisMove = "Move: [" + movedNode + " - " + moveState.toString().toLowerCase() + "]\n" ;

            switch (moveState) {
                case UP:
                    result = movedNode.moveUp();
                    break;
                case DOWN:
                    result = movedNode.moveDown();
                    break;
                case LEFT:
                    result = movedNode.moveLeft();
                    break;
                case RIGHT:
                    result = movedNode.moveRight();
                    break;
            }

            stringRepresentationOfThisMove += tableWithCurrentToString(null);

            result.setStringRepresentation(stringRepresentationOfThisMove);


            return result;
        }

        @Override
        public String toString(){
            return stringRepresentation;
        }

        private void setStringRepresentation(String stringRepresentation) {
            this.stringRepresentation = stringRepresentation;
        }
    }

    private enum MoveState {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        CAN_NOT_MOVE;
    }

    private class Node implements Ball {
        private int i;
        private int j;

        Node(int i, int j) {
            this.i = i;
            this.j = j;
        }

        Node(Node node) {
            this.i = node.i;
            this.j = node.j;
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

        private Node removeNode(Node node) {
            board.get(node.j).add(node.i, null);
            return board.get(node.j).remove(node.i + 1);
        }

        private void moveNodeTo(int j, int i) {
            board.get(j).add(i, this);
            board.get(j).remove(i + 1);
            this.j = j;
            this.i = i;
        }

        @Override
        public Move moveUp() {
            int startPointJ = this.j, startPointI = this.i;
            for (int j = this.j - 1; j >= 0; j--)
                if (thereIsANode(j, i) && !adjacent(i, j)) {
                    removeNode(this);

                    moveNodeTo(j + 1, this.i);

                    while (j >= 0 && thereIsANode(j, i))
                        j--;

                    Node removedNode = removeNode(board.get(j + 1).get(i));
                    nodeCount--;

                    return new Move(removedNode, this, MoveState.UP, startPointJ, startPointI);
                }

            return new Move(MoveState.CAN_NOT_MOVE);
        }

        @Override
        public Move moveDown() {
            int startPointJ = this.j, startPointI = this.i;
            for (int j = this.j + 1; j < board.size(); j++)
                if (thereIsANode(j, i) && !adjacent(i, j)) {
                    removeNode(this);

                    moveNodeTo(j - 1, i);

                    while (j < board.size() && thereIsANode(j, i))
                        j++;

                    Node removedNode = removeNode(board.get(j - 1).get(i));
                    nodeCount--;

                    return new Move(removedNode, this, MoveState.DOWN, startPointJ, startPointI);
                }

            return new Move(MoveState.CAN_NOT_MOVE);
        }

        @Override
        public Move moveLeft() {
            int startPointJ = this.j, startPointI = this.i;
            for (int i = this.i - 1; i >= 0; i--)
                if (thereIsANode(j, i) && !adjacent(i, j)) {
                    removeNode(this);

                    moveNodeTo(this.j, i + 1);

                    while (i >= 0 && thereIsANode(j, i))
                        i--;

                    Node removedNode = removeNode(board.get(j).get(i + 1));
                    nodeCount--;

                    return new Move(removedNode, this, MoveState.LEFT, startPointJ, startPointI);
                }

            return new Move(MoveState.CAN_NOT_MOVE);
        }

        @Override
        public Move moveRight() {
            int startPointJ = this.j, startPointI = this.i;
            for (int i = this.i + 1; i < board.get(j).size(); i++)
                if (thereIsANode(j, i) && !adjacent(i, j)) {
                    removeNode(this);

                    moveNodeTo(this.j, i - 1);

                    while (i < board.get(j).size() && thereIsANode(j, i))
                        i++;

                    Node removedNode = removeNode(board.get(j).get(i - 1));
                    nodeCount--;

                    return new Move(removedNode, this, MoveState.RIGHT, startPointJ, startPointI);
                }

            return new Move(MoveState.CAN_NOT_MOVE);
        }

        @Override
        public String toString(){
            return String.format("(%d,%d)",i,j);
        }

        private ArrayList<PossibleMove> getPossibleMoves() {
            ArrayList<PossibleMove> result = new ArrayList<>();

            if (this.canMoveUp())
                result.add(new Move(this, MoveState.UP));
            if (this.canMoveDown())
                result.add(new Move(this, MoveState.DOWN));
            if (this.canMoveLeft())
                result.add(new Move(this, MoveState.LEFT));
            if (this.canMoveRight())
                result.add(new Move(this, MoveState.RIGHT));

            return result;
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
        public String toString() {
            return tableWithCurrentToString(this);
        }

        private boolean currentNodeEquals(Node node) {
            return node == currentNode;
        }
    }

    Board() {
        initialBoard = new ArrayList<>();
        File fling = new File("target/fling.txt");
        Scanner inputFile = null;

        try {
            inputFile = new Scanner(fling);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        inputBoard(inputFile);

        board = cloneBoard();
        initialNodeCount = nodeCount;
    }

    private ArrayList<ArrayList<Node>> cloneBoard() {
        ArrayList<ArrayList<Node>> temp = new ArrayList<>();

        for (int j = 0; j < initialBoard.size(); j++) {
            temp.add(new ArrayList<Node>());

            for (Node node : initialBoard.get(j)) {
                if (node == null)
                    temp.get(j).add(null);
                else
                    temp.get(j).add(new Node(node));
            }
        }

        return temp;
    }

    private void inputBoard(Scanner inputFile) {
        int j = 0;
        while (inputFile.hasNext()) {
            initialBoard.add(putALineToBoard(inputFile.nextLine(), j++));
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

    /**
     * Restore board's nodes to their initial states.
     */

    public void reset() {
        nodeCount = initialNodeCount;
        board = cloneBoard();
    }

    /**
     * If ball can move in any direction returns true; false if
     * there is no move for passed ball.
     *
     * @param ball Method will test this parameter moves.
     * @return true if there is at least one move for passed ball, false otherwise.
     */

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