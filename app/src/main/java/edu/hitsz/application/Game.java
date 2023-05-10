package edu.hitsz.application;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import edu.hitsz.activity.GameActivity;
import edu.hitsz.activity.MainActivity;
import edu.hitsz.activity.RankingActivity;
import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.BossFactory;
import edu.hitsz.factory.EliteFactory;
import edu.hitsz.factory.EnemyFactory;
import edu.hitsz.factory.MobFactory;
import edu.hitsz.factory.PropFactory;
import edu.hitsz.observePattern.Bomber;
import edu.hitsz.props.AbstractProps;
import edu.hitsz.props.BombSupply;
import edu.hitsz.ranking.Score;
import edu.hitsz.ranking.ScoreDAO;
import edu.hitsz.ranking.ScoreDAOImpl;
import edu.hitsz.soundEffect.MusicThread;

/**
 * 游戏逻辑抽象基类，遵循模板模式，action() 为模板方法
 * 包括：游戏主面板绘制逻辑，游戏执行逻辑。
 * 子类需实现抽象方法，实现相应逻辑
 * @author hitsz
 */
public abstract class Game extends SurfaceView implements SurfaceHolder.Callback, Runnable{

    public static final String TAG = "BaseGame";
    boolean mbLoop = false; //控制绘画线程的标志位
    private SurfaceHolder mSurfaceHolder;
    private Canvas canvas;  //绘图的画布
    private Paint mPaint;
    private Handler handler;

    //点击屏幕位置
    float clickX = 0, clickY=0;

    private int backGroundTop = 0;

    /**
     * 背景图片缓存，可随难度改变
     */
    protected Bitmap backGround;

    /**
     * 游戏难度等级 1，2，3
     */
    protected int level;

    /**
     * sound effect enable
     */
    public static boolean soundEffectEnable;

    /**
     * Scheduled 线程池，用于任务调度
     */
//    protected final ScheduledExecutorService executorService;



    /**
     * 时间间隔(ms)，控制刷新频率
     * 桌面版本为40，暂不清楚手机版本的最佳值
     */
    private int timeInterval = 16;

    private final HeroAircraft heroAircraft;

    protected final List<AbstractAircraft> enemyAircrafts;

    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    protected final List<AbstractProps> props;
    public static PropFactory propFactory;

    /**
     * 游戏背景图片
     * 需要修改适配
     * TODO:
     */
    //protected static BufferedImage background;

    protected int enemyMaxNumber = 5;

    /**
     * 标志是否有boss机存在
     */
    protected Boolean isBossExist = false;

    private boolean gameOverFlag = false;
    protected int score = 0;
    protected int time = 0;


    /**
     * 周期（ms)
     * 控制英雄机射击周期，默认值设为简单模式
     */
    protected int enemyCycleDuration = 600;
    protected int cycleTime1 = 0;

    private int cycleTime2 = 0;

    /**
     * ranking list DAO
     */
    protected ScoreDAO scoreDAO;

    /**
     * 游戏背景音乐线程
     */
    protected MusicThread bgMusic;

    /**
     * boss机线程
     */
    protected MusicThread bossMusic;

    /**
     * 难度参数
     */
    protected float tempDifficulty = 1.0f;
    protected float difficulty = 1.0f;
    protected float probability = 0.8f;
    protected int MOB_HP = 30;
    protected int ELITE_HP = 30;
    protected int BOSS_HP = 600;
    protected int cycleTime3;
    protected int heroShootCycleDuration = 600;
    protected int UpCycleDuration = 6000;
    static String name = "匿名";

    // 上下文
    protected Context context;




    public Game(Context context, Handler handler){
        super(context);
        this.context = context;
        this.handler = handler;
        mbLoop = true;
        mPaint = new Paint();  //设置画笔
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        this.setFocusable(true);
        ImageManager.initImage(context);

        // 初始化英雄机
        heroAircraft = HeroAircraft.getInstance();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();
        //启动背景音乐
        if (soundEffectEnable) {
            //bgMusic = new MusicThread("src/videos/bgm.wav", true);
            //bgMusic.start();
        }

        /*
          Scheduled 线程池，用于定时任务调度
          关于alibaba code guide：可命名的 ThreadFactory 一般需要第三方包
          apache 第三方库： org.apache.commons.lang3.concurrent.BasicThreadFactory
         */
//        this.executorService = new ScheduledThreadPoolExecutor(1,
//                new BasicThreadFactory.Builder().namingPattern("game-action-%d").daemon(true).build());


        heroController();
    }
    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public final void action() {

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

            time += timeInterval;
            timeCountAndNewCycleJudge t = new timeCountAndNewCycleJudge(cycleTime1, UpCycleDuration);
            boolean flag = t.isNew();
            cycleTime1 = t.getCycleTime();
            if (level != 1 & flag) {
                difficultyUp();
                EliteProbabilityUp();
                printDifficulty();
            }

            // 周期性执行（控制频率）
            t = new timeCountAndNewCycleJudge(cycleTime2, enemyCycleDuration);
            flag = t.isNew();
            cycleTime2 = t.getCycleTime();
            if (flag) {
                //System.out.println(time);
                // 新周期，敌机入场
                enemyIn();
                // 敌机射出子弹
                enemyShoot();
            }

            t = new timeCountAndNewCycleJudge(cycleTime3, heroShootCycleDuration);
            flag = t.isNew();
            cycleTime3 = t.getCycleTime();
            if (flag) {

                heroShoot();
            }


            // 子弹移动
            bulletsMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            propsMoveAction();


            // 撞击检测
                try {

                    crashCheckAction();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            isBossExisting();

            // 后处理
                postProcessAction();


            // 游戏结束检查英雄机是否存活
            gameOverProcess();
            try {
                Thread.sleep(timeInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //}
        };
        new Thread(task).start();
        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */

        //executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }

    protected void enemyIn() {
//         新敌机产生
//        随机产生MobEnemy或者EliteEnemy,当分数是200的整数倍时，产生BossEnemy
        EnemyFactory enemyFactory;
        if (score % 200 == 0 & score != 0 & !isBossExist) {
            enemyFactory = new BossFactory();
            enemyAircrafts.add(enemyFactory.createEnemy());
            isBossExist = true;
            if (soundEffectEnable) {
                //bossMusic = new MusicThread("src/videos/bgm_boss.wav", true);
                //bossMusic.start();
            }
        }
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
    private void enemyShoot() {
        // 敌机射击
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {

            enemyBullets.addAll(enemyAircraft.shoot());

        }

    }

    private void heroShoot() {
        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());
    }

    public class timeCountAndNewCycleJudge {
        int cycleDuration;
        int cycleTime;

        timeCountAndNewCycleJudge(int cycleTime, int cycleDuration) {
            this.cycleDuration = cycleDuration;
            this.cycleTime = cycleTime;
        }

        boolean isNew() {
            cycleTime += timeInterval;
            if (cycleTime >= cycleDuration) {
                // 跨越到新的周期
                cycleTime %= cycleDuration;
                return true;
            } else {
                return false;
            }
        }

        int getCycleTime() {
            return cycleTime;
        }
    }

    private void propsMoveAction() {
        for (AbstractProps prop : props) {
            prop.forward();
        }
    }

    protected void printDifficulty() {
    }

    protected void EliteProbabilityUp() {
    }

    protected void difficultyUp() {

    }

    private void gameOverProcess() {
        if (heroAircraft.getHp() <= 0) {
            // 游戏结束
            //executorService.shutdown();
            gameOverFlag = true;
            System.out.println("Game Over!");
            if (soundEffectEnable) {
                //new MusicThread("src/videos/game_over.wav", false).start();
                // stop bg music
                //bgMusic.stopMusic();
//                if (isBossExist) {
//                    bossMusic.stopMusic();
//                }
            }
//TODO: swing移植
//            scoreDAO = new ScoreDAOImpl();
//            Score scoreForThisGame = new Score(this.score, name);
//            System.out.println(scoreForThisGame);
//            scoreDAO.addScore(scoreForThisGame);
//            scoreDAO.sortScore();
//            scoreDAO.saveScore();

            // 切换页面至RankingBoard
            Intent intent = new Intent(context, RankingActivity.class);
            getContext().startActivity(intent);


            //Main.cardPanel.add(new RankingBoard(scoreDAO).getMainPanel(), "RankingBoard");
            //Main.cardLayout.show(Main.cardPanel, "RankingBoard");

        }
    }

    private void isBossExisting() {
        boolean temp = false;
        for (AbstractAircraft boss : enemyAircrafts) {
            if (boss.getClass() == BossEnemy.class) {
                temp = true;
                break;
            }
        }
        isBossExist = temp;
    }



    public void heroController(){
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                clickX = motionEvent.getX();
                clickY = motionEvent.getY();
                heroAircraft.setLocation(clickX, clickY);

                if ( clickX<0 || clickX> MainActivity.WINDOW_WIDTH || clickY<0 || clickY>MainActivity.WINDOW_HEIGHT){
                    // 防止超出边界
                    return false;
                }
                return true;
            }
        });
    }


    private void shootAction() {
        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());
    }

    private boolean timeCountAndNewCycleJudge() {
        cycleTime1 += timeInterval;
        if (cycleTime1 >= enemyCycleDuration && cycleTime1 - timeInterval < cycleTime1) {
            // 跨越到新的周期
            cycleTime1 %= enemyCycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }


    /**
     * 碰撞检测：
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() throws InterruptedException {
        // 敌机子弹攻击英雄
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                // 英雄机损失一定生命值
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (soundEffectEnable) {
                        //new MusicThread("src/videos/bullet_hit.wav", false).start();
                    }
                    if (enemyAircraft.notValid()) {
                        // 获得分数，产生道具补给
                        // BossEnemy死亡，设置标志为false
                        if (enemyAircraft instanceof BossEnemy) {
                            if (soundEffectEnable) {
                                //bossMusic.stopMusic();
                            }
                        }
                        //dropProp
                        List<AbstractProps> tempProps = new LinkedList<>(enemyAircraft.dropProp(enemyAircraft.getLocationX(),
                                enemyAircraft.getLocationY(),
                                0,
                                (int) (enemyAircraft.getSpeedY() * 0.5)
                        ));
                        if (!tempProps.isEmpty()) {
                            //drop a prop
                            props.addAll(tempProps);
                        }
                        // boss score
                        score += enemyAircraft.score;
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    //hack
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // 我方获得道具，道具生效
        for (AbstractProps prop : props) {
            if (prop.notValid()) {
                continue;
            }
            if (heroAircraft.crash(prop)) {
                // 是否为BombSupply
                if (prop.getClass() == BombSupply.class) {
                    BombSupply bomb = (BombSupply) prop;
                    for (AbstractAircraft enemy : enemyAircrafts) {
                        bomb.addBomber((Bomber) enemy);
                        score += enemy.score;
                    }
                    for (BaseBullet enemyBullet : enemyBullets) {
                        bomb.addBomber((Bomber) enemyBullet);
                    }
                    bomb.active(heroAircraft);
                    bomb.vanish();
                } else {
                    prop.active(heroAircraft);
                    prop.vanish();
                }
            }
        }

    }
    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 检查英雄机生存
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);

        if (heroAircraft.notValid()) {
            gameOverFlag = true;
            mbLoop = false;
            Log.i(TAG, "heroAircraft is not Valid");

        }

    }

    public void draw() {
        canvas = mSurfaceHolder.lockCanvas();
        if(mSurfaceHolder == null || canvas == null){
            return;
        }

        //绘制背景，图片滚动
        canvas.drawBitmap(backGround,0,this.backGroundTop-backGround.getHeight(),mPaint);
        canvas.drawBitmap(backGround,0,this.backGroundTop,mPaint);
        backGroundTop +=1;
        if (backGroundTop == MainActivity.WINDOW_HEIGHT)
            this.backGroundTop = 0;

        //先绘制子弹，后绘制飞机
        paintImageWithPositionRevised(enemyBullets); //敌机子弹


        paintImageWithPositionRevised(heroBullets);  //英雄机子弹


        paintImageWithPositionRevised(enemyAircrafts);//敌机

        paintImageWithPositionRevised(props);//道具


        canvas.drawBitmap(ImageManager.HERO_IMAGE,
                heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY()- ImageManager.HERO_IMAGE.getHeight() / 2,
                mPaint);

        //画生命值
        paintScoreAndLife();

        mSurfaceHolder.unlockCanvasAndPost(canvas);

    }

    private void paintImageWithPositionRevised(List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            Bitmap image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            canvas.drawBitmap(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, mPaint);
        }
    }

    private void paintScoreAndLife() {
        int x = 10;
        int y = 40;

        mPaint.setColor(Color.RED);
        mPaint.setTextSize(50);

        canvas.drawText("SCORE:" + this.score, x, y, mPaint);
        y = y + 60;
        canvas.drawText("LIFE:" + this.heroAircraft.getHp(), x, y, mPaint);
    }
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        new Thread(this).start();
        Log.i(TAG, "start surface view thread");
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        MainActivity.WINDOW_WIDTH = i1;
        MainActivity.WINDOW_HEIGHT = i2;
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        mbLoop = false;
    }

    @Override
    public void run() {

        while (mbLoop){   //游戏结束停止绘制
            synchronized (mSurfaceHolder){
                action();
                draw();
            }
        }
        Message message = Message.obtain();
        message.what = 1 ;
        handler.sendMessage(message);
    }
}
