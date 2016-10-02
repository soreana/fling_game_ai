package table;

/**
 * Created by sinakashipazha on 10/2/16.
 */
public interface BoardIterator {
    Ball current();
    Ball next();
    Ball previous();
}
