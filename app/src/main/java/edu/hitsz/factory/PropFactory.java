package edu.hitsz.factory;

import edu.hitsz.props.AbstractProps;

/**
 * @author doby
 */
public interface PropFactory {
    AbstractProps createProp(int locationX, int locationY, int speedX, int speedY);
}
