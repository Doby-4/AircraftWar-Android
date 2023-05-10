package edu.hitsz.aircraft;

import java.util.List;

import edu.hitsz.application.Game;
import edu.hitsz.application.ImageManager;
import edu.hitsz.activity.MainActivity;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.soundEffect.MusicThread;
import edu.hitsz.strategy.HeroStraightShoot;
import edu.hitsz.strategy.ShootStrategy;

/**
 * 英雄飞机，游戏玩家操控，遵循单例模式（singleton)
 * 【单例模式】
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {

    /**攻击方式 */
    /**
     * is firesupply on?
     */
    public boolean isFireSupply = false;
    /**
     * 子弹一次发射数量
     */
    private final int shootNum = 1;

    /**
     * 子弹伤害
     */
    private int power = 30;

    /**
     * 子弹射击方向 (向上发射：1，向下发射：-1)
     */
    private int direction = -1;

    /**
     * shoot strategy
     */
    private ShootStrategy shootStrategy;

/*
        volatile 修饰，
        singleton = new Singleton() 可以拆解为3步：
        1、分配对象内存(给singleton分配内存)
        2、调用构造器方法，执行初始化（调用 Singleton 的构造函数来初始化成员变量）。
        3、将对象引用赋值给变量(执行完这步 singleton 就为非 null 了)。
        若发生重排序，假设 A 线程执行了 1 和 3 ，还没有执行 2，B 线程来到判断 NULL，B 线程就会直接返回还没初始化的 instance 了。
        volatile 可以避免重排序。
    */
    /** 英雄机对象单例 */
    private static HeroAircraft instance = null;

    /**
     * 单例模式：私有化构造方法
     */
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp, ShootStrategy shootStrategy) {
        super(locationX, locationY, speedX, speedY, hp);
        this.shootStrategy = shootStrategy;
    }


    /**
     * 通过单例模式获得初始化英雄机
     * 【单例模式：双重校验锁方法】
     * @return 英雄机单例
     */
    public static synchronized HeroAircraft getInstance(){
        if (instance == null) {
            instance = new HeroAircraft(MainActivity.WINDOW_WIDTH / 2,
                    MainActivity.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                    0, 0, 5000,
                    new HeroStraightShoot()
            );
        }
        return instance;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    @Override
    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     */

    public List<BaseBullet> shoot() {
        if (Game.soundEffectEnable) {
            //new MusicThread("src/videos/bullet.wav", false).start();
        }
        return shootStrategy.shoot(this.getLocationX(), this.getLocationY(), this.getSpeedY(), this.power, this.shootNum, this.direction);
    }

    public void setShootStrategy(ShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }
}
