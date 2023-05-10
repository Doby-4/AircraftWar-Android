package edu.hitsz.strategy;

import edu.hitsz.bullet.BaseBullet;

import java.util.List;

public interface ShootStrategy {
    List<BaseBullet> shoot(int LocationX, int LocationY, int speedY, int power, int shootNum, int direction);
}
