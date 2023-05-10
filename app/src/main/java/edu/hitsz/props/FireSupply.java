package edu.hitsz.props;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Game;
import edu.hitsz.soundEffect.MusicThread;
import edu.hitsz.strategy.HeroSectorShoot;
import edu.hitsz.strategy.HeroStraightShoot;

/**
 * @author Doby
 * <p>
 * FireSupply class
 * when the hero touches it, it will  upgrade the hero's fire power
 */
public class FireSupply extends AbstractProps {
    public FireSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void active(HeroAircraft heroAircraft) {
        if (Game.soundEffectEnable) {
//            new MusicThread("src/videos/get_supply.wav", false).start();
        }
        // fire upgrade last for 10 seconds
        System.out.println("FireSupply active!");
        if (!heroAircraft.isFireSupply) {
            heroAircraft.isFireSupply = true;
            heroAircraft.setShootStrategy(new HeroSectorShoot());
            new Thread(() -> {
                try {

                    for (int i = 0; i < 10; i++) {
                        if (heroAircraft.notValid()) {
                            break;
                        }
                        System.out.println("FireSupply last for " + (10 - i) + " seconds");
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                heroAircraft.setShootStrategy(new HeroStraightShoot());
                heroAircraft.isFireSupply = false;
            }).start();

        }
    }

}
