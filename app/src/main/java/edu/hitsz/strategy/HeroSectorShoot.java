package edu.hitsz.strategy;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class HeroSectorShoot implements ShootStrategy {
    @Override
    public List<BaseBullet> shoot(int LocationX, int LocationY, int speedY, int power, int shootNum, int direction) {
        List<BaseBullet> res = new LinkedList<>();
        int x = LocationX;
        int y = LocationY + direction * 2;
        int speedX = 0;
        shootNum = 5;
        speedY = speedY + direction * 5;
        BaseBullet bullet;
        for (int i = 0; i < shootNum; i++) {
            bullet = new HeroBullet(x + (i * 2 - shootNum + 1) * 10, y, speedX + (i - 1) * 2, speedY - 10, power);
            res.add(bullet);
        }
        return res;
    }
}
