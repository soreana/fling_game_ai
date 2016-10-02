package table;

/**
 * Created by sinakashipazha on 10/2/16.
 */
public interface BoardIterator {
    Ball next();
    Ball previous();
    boolean currentNodeEquals(Board.Node node);
}
