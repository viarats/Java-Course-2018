package bg.sofia.uni.fmi.mjt.carstore.car;

import bg.sofia.uni.fmi.mjt.carstore.enums.EngineType;
import bg.sofia.uni.fmi.mjt.carstore.enums.Model;
import bg.sofia.uni.fmi.mjt.carstore.enums.Region;

import java.util.Objects;

public abstract class Car implements Comparable<Car> {
    private final int ASCII_CODE_OF_A = 65;
    private final int ASCII_CODE_OF_Z = 90;
    private final int REGISTRATION_NUMBER_START = 1000;

    private Model model;
    private int year;
    private int price;
    private EngineType engineType;
    private Region region;
    private String registrationNumber;
    private static int alfaRomeoCounter;

    private static int audiCounter;
    private static int bmwCounter;
    private static int mercedesCounter;
    private static int ferrariCounter;
    private static int opelCounter;

    public Car(Model model, int year, int price, EngineType engineType, Region region) {
        this.model = model;
        this.year = year;
        this.price = price;
        this.engineType = engineType;
        this.region = region;

        this.registrationNumber = setRegNumber(model, region);

        switch (model) {
            case ALFA_ROMEO: alfaRomeoCounter++;
                break;
            case AUDI: audiCounter++;
                break;
            case BMW: bmwCounter++;
                break;
            case MERCEDES: mercedesCounter++;
                break;
            case FERRARI: ferrariCounter++;
                break;
            default: opelCounter++;
        }
    }

    private String setRegNumber(Model model, Region region) {
        StringBuilder sb = new StringBuilder();
        sb.append(region.getPrefix());

        switch (model) {
            case ALFA_ROMEO: sb.append(alfaRomeoCounter + REGISTRATION_NUMBER_START);
                break;
            case AUDI: sb.append(audiCounter + REGISTRATION_NUMBER_START);
                break;
            case BMW: sb.append(bmwCounter + REGISTRATION_NUMBER_START);
                break;
            case MERCEDES: sb.append(mercedesCounter + REGISTRATION_NUMBER_START);
                break;
            case FERRARI: sb.append(ferrariCounter + REGISTRATION_NUMBER_START);
                break;
            default: sb.append(opelCounter + REGISTRATION_NUMBER_START);
        }

        sb.append((char)(ASCII_CODE_OF_A + (int)(Math.random() *
                ((ASCII_CODE_OF_Z - ASCII_CODE_OF_A) + 1))));

        sb.append((char)(ASCII_CODE_OF_A + (int)(Math.random() *
                ((ASCII_CODE_OF_Z - ASCII_CODE_OF_A) + 1))));

        return sb.toString();
    }

    public Model getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public int getPrice() {
        return price;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public int compareTo(Car other) {
        int m = model.toString().compareTo(other.model.toString());
        int y = Integer.compare(year, other.year);

        return m != 0 ? m : y != 0 ? y : registrationNumber.compareTo(other.registrationNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return year == car.year &&
                model == car.model;
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, year);
    }
}