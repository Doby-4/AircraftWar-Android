package edu.hitsz.factory;

import edu.hitsz.props.AbstractProps;
import edu.hitsz.props.FireSupply;

public class FireFactory implements PropFactory {
    @Override
    public AbstractProps createProp(int locationX, int locationY, int speedX, int speedY) {
        return new FireSupply(locationX, locationY, speedX, speedY);
    }
}

