package edu.hitsz.aircraft;

import edu.hitsz.application.Game;
import edu.hitsz.activity.MainActivity;
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
 * EliteEnemy
 * able to shoot
 * if destroied, drop one prop
 *
 * @author Doby
 */
public class EliteEnemy extends AbstractAircraft implements Bomber {
    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp, ShootStrategy shootStrategy) {
        super(locationX, locationY, speedX, speedY, hp);
        this.shootStrategy = shootStrategy;
        this.score = 50;
    }

    /**
     * 子弹一次发射数量
     */
    private int shootNum = 1;

    /**
     * 子弹伤害
     */
    private int power = 30;

    /**
     * 子弹射击方向 (向上发射：1，向下发射：-1)
     */
    private int direction = 1;

    /**
     * shoot strategy
     */
    private ShootStrategy shootStrategy;

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= MainActivity.WINDOW_HEIGHT) {
            vanish();
        }
    }

    @Override
    public List<AbstractProps> dropProp(int locationX, int locationY, int speedX, int speedY) {
        //30%掉落火力道具、30%掉落加血道具、 30%掉落炸弹道具,10%不掉落道具
        List<AbstractProps> res = new LinkedList<>();
        int random = (int) (Math.random() * 100);
        if (random < 30) {
            Game.propFactory = new FireFactory();
            res.add(Game.propFactory.createProp(locationX, locationY, speedX, speedY));
        } else if (random < 60) {
            Game.propFactory = new HealingFactory();
            res.add(Game.propFactory.createProp(locationX, locationY, speedX, speedY));
        } else if (random < 90) {
            Game.propFactory = new BombFactory();
            res.add(Game.propFactory.createProp(locationX, locationY, speedX, speedY));
        } else {
            //不掉落道具
            return new LinkedList<>();
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
        this.decreaseHp(maxHp);
    }
}
