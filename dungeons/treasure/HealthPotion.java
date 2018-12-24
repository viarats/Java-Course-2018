package bg.uni.sofia.fmi.mjt.dungeon.treasure;

import bg.uni.sofia.fmi.mjt.dungeon.actor.Hero;

public class HealthPotion implements Treasure {
    private int healingPoints;

    public HealthPotion(int healingPoints) {
        this.healingPoints = healingPoints;
    }

    public int heal() {
        return healingPoints;
    }

    public String collect(Hero hero) {
        if (hero.getHealth() > 0) {
            hero.takeHealing(healingPoints);
            return "Health potion found!" + healingPoints + "health points added to your hero!";
        }
        return null;
    }
}