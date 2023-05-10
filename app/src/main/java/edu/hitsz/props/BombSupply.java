package edu.hitsz.props;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Game;
import edu.hitsz.observePattern.Bomber;
import edu.hitsz.soundEffect.MusicThread;

import java.util.List;

public class BombSupply extends AbstractProps {
    private List<Bomber> bombers = new java.util.ArrayList<Bomber>();

    public BombSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void active(HeroAircraft heroAircraft) {
        if (Game.soundEffectEnable) {
//            new MusicThread("src/videos/get_supply.wav", false).start();
//            new MusicThread("src/videos/bomb_explosion.wav", false).start();
        }
        System.out.println("BombSupply active!");
        notifyAllBombers();
    }

    public void addBomber(Bomber bomber) {
        bombers.add(bomber);
    }

    public void removeBomber(Bomber bomber) {
        bombers.remove(bomber);
    }

    public void notifyAllBombers() {
        for (Bomber bomber : bombers) {
            bomber.update();
        }
    }

}
