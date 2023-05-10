package edu.hitsz.aircraft;

import edu.hitsz.application.Game;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.BombFactory;
import edu.hitsz.factory.FireFactory;
import edu.hitsz.factory.HealingFactory;
import edu.hitsz.observePattern.Bomber;
import edu.hitsz.props.AbstractProps;
import edu.hitsz.strategy.ShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * @author doby
 */
public class BossEnemy extends AbstractAircraft implements Bomber {
    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp, ShootStrategy shootStrategy) {
        super(locationX, locationY, speedX, speedY, hp);
        this.shootStrategy = shootStrategy;
        this.score = 70;
    }

    private int shootNum = 3;

    private int power = 50;

    private int direction = 1;

    private ShootStrategy shootStrategy;

    @Override
    public List<AbstractProps> dropProp(int locationX, int locationY, int speedX, int speedY) {
        //掉落三个随机道具
        int propNum = 3;
        List<AbstractProps> res = new LinkedList<>();
        for (int i = -1; i < propNum - 1; i++) {
            int random = (int) (Math.random() * 90);
            if (random < 30) {
                Game.propFactory = new FireFactory();
                res.add(Game.propFactory.createProp(locationX + i * 40, locationY, 0, 7));
            } else if (random < 60) {
                Game.propFactory = new HealingFactory();
                res.add(Game.propFactory.createProp(locationX + i * 40, locationY, 0, 7));
            } else if (random < 90) {
                Game.propFactory = new BombFactory();
                res.add(Game.propFactory.createProp(locationX + i * 40, locationY, 0, 7));
            }
        }
        return res;
    }

    @Override
    public List<BaseBullet> shoot() {
        return shootStrategy.shoot(this.getLocationX(), this.getLocationY(), this.getSpeedY(), this.power, this.shootNum, this.direction);
    }

    public void setShootStrategy(ShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }

    @Override
    public void update() {
        this.decreaseHp(200);
    }
}
