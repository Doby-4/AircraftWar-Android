package edu.hitsz.factory;

import edu.hitsz.props.AbstractProps;
import edu.hitsz.props.HealingPackage;

public class HealingFactory implements PropFactory {
    @Override
    public AbstractProps createProp(int locationX, int locationY, int speedX, int speedY) {
        return new HealingPackage(locationX, locationY, speedX, speedY);
    }
}

