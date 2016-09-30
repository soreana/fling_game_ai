package table;

import algorithm.Algorithm;

/**
 * Created by sinakashipazha on 9/30/16.
 */
abstract public class Board {
    private class Node {

    }

    /**
     * Implement this method based on your choice
     *
     * @param algorithmName name of solver algorithm
     * @return algorithm you want to solve
     */

    public abstract Algorithm getSolvingAlgorithm (String algorithmName );
}
