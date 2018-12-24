package bg.uni.sofia.fmi.mjt.dungeon;

import bg.uni.sofia.fmi.mjt.dungeon.actor.Position;
import bg.uni.sofia.fmi.mjt.dungeon.actor.Hero;
import bg.uni.sofia.fmi.mjt.dungeon.actor.Enemy;
import bg.uni.sofia.fmi.mjt.dungeon.treasure.Treasure;

public class GameEngine {
    private char[][] map;
    private Hero hero;
    private Enemy[] enemies;
    private Treasure[] treasures;
    private int currentTreasure;
    private int currentEnemy;

    public GameEngine(char[][] map, Hero hero, Enemy[] enemies,
                      Treasure[] treasures) {
        this.map = new char[map.length][];

        for (int i = 0; i < map.length; i++) {
            this.map[i] = new char[map[0].length];
        }

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                this.map[i][j] = map[i][j];
            }
        }

        this.hero = hero;

        this.enemies = new Enemy[enemies.length];

        for (int i = 0; i < enemies.length; i++) {
            this.enemies[i] = enemies[i];
        }

        this.treasures = new Treasure[treasures.length];

        for (int i = 0; i < treasures.length; i++) {
            this.treasures[i] = treasures[i];
        }
    }

    char[][] getMap() {
        return map;
    }

    public Position getHeroPosition() {
        if (findElementPosition('H') == null) {
            return findElementPosition('S');
        }
        return findElementPosition('H');
    }

    public String makeMove(Direction direction) {
        switch (direction) {
            case UP:
                return move(getHeroPosition().x - 1, getHeroPosition().y);
            case DOWN:
                return move(getHeroPosition().x + 1, getHeroPosition().y);
            case LEFT:
                return move(getHeroPosition().x, getHeroPosition().y - 1);
            default:
                return move(getHeroPosition().x, getHeroPosition().y + 1);
        }
    }

    public Hero getHero() {
        return hero;
    }

    public Treasure[] getTreasures() {
        return treasures;
    }

    public Enemy[] getEnemies() {
        return enemies;
    }

    private String move(int i, int j) {
        if (isValidIndex(i, j)) {
            if (map[i][j] == '.') {
                updateMap(i, j);
                return "You moved successfully to the next position.";
            }
            else if (map[i][j] == '#') {
                return "Wrong move. There is an obstacle and you cannot bypass it.";
            }
            else if (map[i][j] == 'T') {
                int temp = currentTreasure;
                currentTreasure++;
                updateMap(i, j);
                return treasures[temp].collect(hero);
            }
            else if (map[i][j] == 'E') {
                if (fight(hero, enemies[currentEnemy]) == Outcome.VICTORY) {
                    currentEnemy++;
                    updateMap(i, j);
                    return "Enemy died.";
                }
                else {
                    return "Hero is dead! Game over!";
                }
            }
            else {
                return "You have successfully passed through the dungeon. Congrats!";
            }
        }
        return "Unknown command entered.";
    }

    private boolean isValidIndex(int i, int j) {
        return i >= 0 && j >= 0 && i <= map.length - 1 && j <= map[0].length - 1;
    }

    private void updateMap(int i, int j) {
        if (findElementPosition('H') == null) {
            map[findElementPosition('S').x][findElementPosition('S').y] = '.';
            map[i][j] = 'H';
        }
        map[findElementPosition('H').x][findElementPosition('H').y] = '.';
        map[i][j] = 'H';
    }

    private Position findElementPosition(char cell) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == cell) {
                    return new Position(i, j);
                }
            }
        }
        return null;
    }

    private Outcome fight(Hero hero, Enemy enemy) {
        enemy.takeDamage(hero.attack());
        while (enemy.isAlive() && hero.isAlive()) {
            hero.takeDamage(enemy.attack());
            enemy.takeDamage(hero.attack());
        }
        if (hero.isAlive()) {
            return Outcome.VICTORY;
        }
        return Outcome.DEFEAT;
    }
}