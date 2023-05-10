package edu.hitsz.props;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.activity.MainActivity;
import edu.hitsz.basic.AbstractFlyingObject;

/**
 * Abstract parent class of all kinds of props:
 * HealingPackage, FireSupply, BombSupply
 *
 * @author Doby
 */
public abstract class AbstractProps extends AbstractFlyingObject {
    public AbstractProps(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    /**
     * active the props
     *
     * @param heroAircraft
     * @return void
     */
    public abstract void active(HeroAircraft heroAircraft);


    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= MainActivity.WINDOW_HEIGHT) {
            vanish();
        }
    }

}
