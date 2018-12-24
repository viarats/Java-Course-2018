package bg.sofia.uni.fmi.mjt.christmas;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Workshop {
    private static final int TOTAL_ELVES = 20;
    private static final int SLEIGH_CAPACITY = 100;

    private volatile boolean isChristmasTime;
    private static int elfId;

    private Elf[] elves;
    private Santa santa;
    private Queue<Gift> wishes;
    private int wishesCount;
    private Queue<Gift> warehouse;

    public Workshop() {
        isChristmasTime = false;

        wishes = new ConcurrentLinkedQueue<>();
        wishesCount = 0;

        warehouse = new ConcurrentLinkedQueue<>();

        elves = new Elf[TOTAL_ELVES];

        for (int i = 0; i  < TOTAL_ELVES; i++) {
            elves[i] = new Elf(elfId, this);
            elves[i].start();

            elfId++;
        }

        santa = new Santa(this, SLEIGH_CAPACITY);
        santa.start();
    }

    public synchronized void postWish(Gift gift) {
        wishes.add(gift);
        wishesCount++;

        this.notify();
    }

    public synchronized Gift nextGift() {
        while (!isChristmasTime && wishes.isEmpty()) {
            try {
                this.wait();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (wishes.isEmpty()) {
            return null;
        }

        warehouse.add(wishes.peek());
        return wishes.poll();
    }

    public void loadSlay() {
        synchronized (this) {
            while (warehouse.size() < SLEIGH_CAPACITY) {
                try {
                    this.wait();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        for (int i = 0; i < SLEIGH_CAPACITY; i++) {
            warehouse.remove();
        }
    }

    public Elf[] getElves() {
        return elves;
    }

    public int getWishesCount() {
        return wishesCount;
    }

    public void setChristmasTime() {
        this.isChristmasTime = true;
    }

    public boolean getIsChristmasTime() {
        return isChristmasTime;
    }

    public int getWarehouseSize() {
        return warehouse.size();
    }
}