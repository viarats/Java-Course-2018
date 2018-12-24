package bg.uni.sofia.fmi.mjt.dungeon.actor;

import bg.uni.sofia.fmi.mjt.dungeon.treasure.Weapon;
import bg.uni.sofia.fmi.mjt.dungeon.treasure.Spell;

public class Hero implements Actor {
    private String name;
    private int health;
    private int maxHealth;
    private int mana;
    private int maxMana;
    private Weapon weapon;
    private Spell spell;

    public Hero(String name, int health, int mana) {
        this.name = name;
        this.health = health;
        maxHealth = health;
        this.mana = mana;
        maxMana = mana;
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

    public void takeHealing(int healingPoints) {
        if (health > 0) {
            if (health + healingPoints >= maxMana) {
                health = maxHealth;
            }
            else {
                health += healingPoints;
            }
        }
    }

    public void takeDamage(int damagePoints) {
        if (health > damagePoints) {
            health -= damagePoints;
        }
        else {
            health = 0;
        }
    }

    public void takeMana(int manaPoints) {
        if (mana + manaPoints >= maxMana) {
            mana = maxMana;
        }
        else mana += manaPoints;
    }

    public void equip(Weapon weapon) {
        if (this.weapon == null || this.weapon.getDamage() < weapon.getDamage()) {
            this.weapon = weapon;
        }
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void learn(Spell spell) {
        this.spell = spell;
    }

    public Spell getSpell() {
        return spell;
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

