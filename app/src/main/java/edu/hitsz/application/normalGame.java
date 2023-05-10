package edu.hitsz.application;

import android.content.Context;
import android.os.Handler;

import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.application.Game;
import edu.hitsz.application.ImageManager;
import edu.hitsz.factory.BossFactory;
import edu.hitsz.factory.EliteFactory;
import edu.hitsz.factory.EnemyFactory;
import edu.hitsz.factory.MobFactory;
import edu.hitsz.soundEffect.MusicThread;

public class normalGame extends Game {

    private float difficulty = 1.0f;
    private float probability = 0.8f;
    public normalGame(Context context, Handler handler) {
        super(context,handler);
        this.backGround = ImageManager.BACKGROUND_IMAGE2;
        enemyMaxNumber = 7;
        System.out.println("normal mode, max quantity of enemies is:" + this.enemyMaxNumber);
        level = 2;
    }

    @Override
    protected void enemyIn() {
        //         新敌机产生
//        随机产生MobEnemy或者EliteEnemy,当分数是200的整数倍时，产生BossEnemy
        EnemyFactory enemyFactory;
        if (score % 200 == 0 & score != 0 & !isBossExist) {
            enemyFactory = new BossFactory();
            BossEnemy boss = (BossEnemy) enemyFactory.createEnemy();
            BOSS_HP = boss.getMaxHp();
            enemyAircrafts.add(boss);
            isBossExist = true;
            if (soundEffectEnable) {
//                bossMusic = new MusicThread("src/videos/bgm_boss.wav", true);
//                bossMusic.start();
            }
        }
        if (enemyAircrafts.size() < enemyMaxNumber) {
            if (Math.random() < probability) {
                enemyFactory = new MobFactory();
                MobEnemy enemy = (MobEnemy) enemyFactory.createEnemy();
                MOB_HP = (int) (difficulty * enemy.getMaxHp());
                enemy.setMaxHp(MOB_HP);
                enemyAircrafts.add(enemy);
            } else {
                enemyFactory = new EliteFactory();
                EliteEnemy enemy = (EliteEnemy) enemyFactory.createEnemy();
                ELITE_HP = (int) (difficulty * enemy.getMaxHp());
                enemy.setMaxHp(ELITE_HP);
                enemyAircrafts.add(enemy);
            }
        }

    }

    @Override
    protected void difficultyUp() {
        if (difficulty <= 2.5) {
            difficulty += 0.1;
            heroShootCycleDuration = (int) (600 / difficulty);
            enemyCycleDuration = ((int) (600 / difficulty));
        }
    }

    @Override
    protected void EliteProbabilityUp() {
        if (probability > 0.4) {
            probability -= 0.1;
        }
    }

    @Override
    protected void printDifficulty() {
        System.out.printf("Difficulty up! Elite enemy probability: %.2f, enemy attribute magnification: %.2f\n", probability, difficulty);
        System.out.printf("Elite enemy max HP:%d, Mob enemy max HP:%d, Boss enemy max HP:%d\n", ELITE_HP, MOB_HP, BOSS_HP);
        System.out.printf("Enemy firing rate: %d, hero firing rate: %d", (1200 - enemyCycleDuration), (1200 - heroShootCycleDuration));

    }
}
