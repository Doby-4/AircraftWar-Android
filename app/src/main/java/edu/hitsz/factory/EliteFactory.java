package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.activity.MainActivity;
import edu.hitsz.strategy.EnemyStraightShoot;


/**
 * @author Doby Shao
 */
public class EliteFactory implements EnemyFactory {
    @Override
    public AbstractAircraft createEnemy() {
        return new EliteEnemy((int) (Math.random() * (MainActivity.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * MainActivity.WINDOW_HEIGHT * 0.05),
                0,
                7,
                30,
                new EnemyStraightShoot()
        );
    }
}
