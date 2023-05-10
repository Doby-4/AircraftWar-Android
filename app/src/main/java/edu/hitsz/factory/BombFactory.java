package edu.hitsz.factory;

import edu.hitsz.props.AbstractProps;
import edu.hitsz.props.BombSupply;

public class BombFactory implements PropFactory {
    @Override
    public AbstractProps createProp(int locationX, int locationY, int speedX, int speedY) {
        return new BombSupply(locationX, locationY, speedX, speedY);
    }
}

