package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;

/**
 *
 * @author Doby
 */

/**
 * 敌机工厂
 */
public interface EnemyFactory {
    AbstractAircraft createEnemy();

}
