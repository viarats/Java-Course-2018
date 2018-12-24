package bg.sofia.uni.fmi.mjt.christmas;

public class Kid implements Runnable {
    private Workshop workshop;
    private static final int WISHING_TIME = 1000;

    public Kid(Workshop workshop) {
        this.workshop = workshop;
    }

    public void makeWish(Gift gift) {
        try {
            Thread.sleep(WISHING_TIME);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        workshop.postWish(gift);
    }

    public void run() {
        makeWish(Gift.getGift());
    }
}