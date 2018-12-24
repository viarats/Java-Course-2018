package bg.sofia.uni.fmi.mjt.christmas;

import java.util.Arrays;

public class Christmas {
    private Workshop workshop;
    private int numberOfKids;
    private int christmasTime;

    public Christmas(Workshop workshop, int numberOfKids, int christmasTime) {
        this.workshop = workshop;
        this.numberOfKids = numberOfKids;
        this.christmasTime = christmasTime;
    }

    public static void main(String[] args) {
        Christmas christmas = new Christmas(new Workshop(), 120, 2000);
        christmas.celebrate();
    }

    public void celebrate() {
        Thread[] kids = new Thread[numberOfKids];

        for (int i = 0; i < numberOfKids; i++) {
            kids[i] = new Thread(new Kid(workshop));
            kids[i].start();
        }

        try {
            for (Thread kid : kids) {
                kid.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(christmasTime);

            synchronized (workshop) {
                workshop.setChristmasTime();
                workshop.notifyAll();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        printResults(workshop);
    }

    public void printResults(Workshop workshop) {
        System.out.println("Total kids: " + numberOfKids);
        System.out.println("Total wishes: " + workshop.getWishesCount());
        System.out.println();

        Arrays.stream(workshop.getElves())
                .forEach(e -> System.out.println("Elf id: " + e.getElfId() + ", Gifts: " + e.getTotalGiftsCrafted()));

        int totalGifts = Arrays.stream(workshop.getElves())
                                .map(Elf::getTotalGiftsCrafted)
                                .reduce((e1, e2) -> e1 + e2).get();

        System.out.println();
        System.out.println("Total gifts crafted: " + totalGifts);
        System.out.println("Warehouse size: " + workshop.getWarehouseSize());
    }

    public Workshop getWorkshop() {
        return workshop;
    }
}