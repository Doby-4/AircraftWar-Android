package edu.hitsz.application;

import android.content.Context;
import android.os.Handler;

import edu.hitsz.factory.EliteFactory;
import edu.hitsz.factory.EnemyFactory;
import edu.hitsz.factory.MobFactory;

public class easyGame extends Game {

    public easyGame(Context context, Handler handler) {
        super(context,handler);
        this.backGround = ImageManager.BACKGROUND_IMAGE1;
        System.out.println("normal mode, max quantity of enemies is:" + this.enemyMaxNumber);
        level = 1;
    }

    @Override
    protected void enemyIn() {
        //         新敌机产生
//        随机产生MobEnemy或者EliteEnemy,当分数是200的整数倍时，产生BossEnemy
        EnemyFactory enemyFactory;
        if (enemyAircrafts.size() < enemyMaxNumber) {
            if (Math.random() < 0.8) {
                enemyFactory = new MobFactory();
                enemyAircrafts.add(enemyFactory.createEnemy());
            } else {
                enemyFactory = new EliteFactory();
                enemyAircrafts.add(enemyFactory.createEnemy());
            }
        }

    }


}
