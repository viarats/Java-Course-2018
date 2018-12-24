package bg.uni.sofia.fmi.mjt.dungeon.actor;

import bg.uni.sofia.fmi.mjt.dungeon.treasure.Weapon;
import bg.uni.sofia.fmi.mjt.dungeon.treasure.Spell;

public class Enemy implements Actor {
    private String name;
    private int health;
    private int mana;
    private Weapon weapon;
    private Spell spell;

    public Enemy(String name, int health, int mana, Weapon weapon, Spell spell) {
        this.name = name;
        this.health = health;
        this.mana = mana;
        this.weapon = weapon;
        this.spell = spell;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getMana() {
        return mana;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public Spell getSpell() {
        return spell;
    }

    public void takeDamage(int damagePoints) {
        if (health > damagePoints) {
            health -= damagePoints;
        }
        else {
            health = 0;
        }
    }

    public int attack() {
        if (spell != null && weapon != null) {

            if (spell.getDamage() > weapon.getDamage()) {

                if (mana >= spell.getManaCost()) {
                    mana -= spell.getManaCost();
                    return spell.getDamage();
                }
            }
        }
        if (weapon != null) {
            return weapon.getDamage();
        }
        if (spell != null) {

            if (mana >= spell.getManaCost()) {
                mana -= spell.getManaCost();
                return spell.getDamage();
            }
        }
        return 0;
    }
}
