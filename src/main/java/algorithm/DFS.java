package algorithm;

import table.*;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by sinakashipazha on 9/30/16.
 */
public class DFS implements Algorithm {
    private Board board;
    private Stack<StackNode> movesStack;

    private class StackNode {
        private ArrayList<PossibleMove> otherPossibleMoves;
        private Move currentMove;

        private StackNode(Move currentMove, ArrayList<PossibleMove> possibleMoves) {
            this.currentMove = currentMove;
            this.otherPossibleMoves = possibleMoves;
        }

        private boolean haveOtherPossibleMove() {
            return !otherPossibleMoves.isEmpty();
        }

        private void doOtherPossibleMove() {
            undo();
            currentMove = otherPossibleMoves.remove(0).move();
        }

        private void undo() {
            currentMove.undo();
        }

        @Override
        public String toString(){
            return currentMove.toString();
        }
    }

    public DFS(Board board) {
        this.board = board;
        movesStack = new Stack<>();
    }

    @Override
    public void start() {
        board.reset();
        runDFSAlgorithm();
    }

    private StackNode nextMove() {
        ArrayList<PossibleMove> temp = board.getPossibleMoves();
        return new StackNode(temp.remove(0).move(), temp);
    }

    private void backToUncheckedCondition() {
        while (!movesStack.peek().haveOtherPossibleMove())
            movesStack.pop().undo();
        movesStack.peek().doOtherPossibleMove();
    }

    private void runDFSAlgorithm() {
        if (board.failed()) {
            backToUncheckedCondition();
            runDFSAlgorithm();
            return;
        }

        movesStack.push(nextMove());

        if (board.solved()) {
            System.out.println("You Win.");
            printResult();
            return;
        }

        runDFSAlgorithm();
    }

    private void printResult() {
        board.reset();
        System.out.println(board);

        for (StackNode node : movesStack)
            System.out.println(node);
    }
}
