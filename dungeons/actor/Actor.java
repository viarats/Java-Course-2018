package bg.uni.sofia.fmi.mjt.dungeon.actor;

public interface Actor {
    public boolean isAlive();

    public void takeDamage(int damagePoints);

    public int attack();
}