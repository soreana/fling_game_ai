package table;

/**
 * Created by sinakashipazha on 10/2/16.
 */
public interface Ball {
    boolean canMoveUp();

    boolean canMoveDown();

    boolean canMoveLeft();

    boolean canMoveRight();

    Move moveUp();

    Move moveDown();

    Move moveLeft();

    Move moveRight();
}
