package bg.sofia.uni.fmi.mjt.christmas;

public class Elf extends Thread {
    private Workshop workshop;
    private int elfId;
    private int giftsCrafted;

    public Elf(int id, Workshop workshop) {
        this.workshop = workshop;
        elfId = id;
        giftsCrafted = 0;
    }

    public void craftGift() {
        Gift gift = workshop.nextGift();

        while (gift != null) {
            try {
                Thread.sleep(gift.getCraftTime());
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            giftsCrafted++;
            gift = workshop.nextGift();
        }
    }

    public int getTotalGiftsCrafted() {
        return giftsCrafted;
    }

    public int getElfId() {
        return elfId;
    }

    public void run() {
       craftGift();
    }
}