package table;

import algorithm.Algorithm;
import algorithm.BFS;
import algorithm.UserMode;

/**
 * Created by sinakashipazha on 9/30/16.
 */
public class MyBoard extends Board {

    public Algorithm getSolvingAlgorithm(String algorithmName) {
        if("bfs".equals(algorithmName.toLowerCase()))
            return new BFS();
        if("usermode".equals(algorithmName.toLowerCase()))
            return new UserMode();
        throw new RuntimeException("Algorithm not found.");
    }
}
