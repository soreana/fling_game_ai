package table;

/**
 * Created by sinakashipazha on 10/2/16.
 */
public interface Ball {
    boolean canMoveUp();

    boolean canMoveDown();

    boolean canMoveLeft();

    boolean canMoveRight();

    void moveUp();

    void moveDown();

    void moveLeft();

    void moveRight();

}
