package edu.hitsz.bullet;

import edu.hitsz.observePattern.Bomber;

/**
 * @Author hitsz
 */
public class EnemyBullet extends BaseBullet implements Bomber {


    public EnemyBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
    }

    @Override
    public void update() {
        vanish();
    }

}
