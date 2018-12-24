package bg.sofia.uni.fmi.mjt.christmas;

public class Santa extends Thread {
    private Workshop workshop;
    private int sleighCapacity;

    public Santa(Workshop workshop, int sleighCapacity) {
        this.workshop = workshop;
        this.sleighCapacity = sleighCapacity;
    }

    public void load() {
        while (workshop.getWarehouseSize() >= 0) {
            workshop.loadSlay();
            if (workshop.getIsChristmasTime() && workshop.getWarehouseSize() < sleighCapacity) {
                break;
            }
        }
    }

    public void run() {
        load();
    }
}