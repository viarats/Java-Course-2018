package bg.sofia.uni.fmi.mjt.carstore;

import bg.sofia.uni.fmi.mjt.carstore.car.Car;
import bg.sofia.uni.fmi.mjt.carstore.enums.Model;
import bg.sofia.uni.fmi.mjt.carstore.exception.CarNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CarStore {
    private Set<Car> store;

    public CarStore() {
        this.store = new TreeSet<>();
    }

    public boolean add(Car car) {
        if (car != null) {
            return store.add(car);
        }
        return false;
    }

    public boolean addAll(Collection<Car> cars) {
        if (cars != null) {
            return store.addAll(cars);
        }
        return false;
    }

    public boolean remove(Car car) {
        if (store.contains(car)) {
            return store.remove(car);
        }
        return false;
    }

    public Collection<Car> getCarsByModel(Model model) {
        List<Car> cars = new ArrayList<>();
        for (Car car : store) {
            if (car.getModel() == model) {
                cars.add(car);
            }
        }
        return cars;
    }

    public Car getCarByRegistrationNumber(String registrationNumber) {
        for (Car car : store) {
            if (car.getRegistrationNumber().equals(registrationNumber)) {
                return car;
            }
        }
        throw new CarNotFoundException("No Such Registration Number");
    }

    public Collection<Car> getCars() {
        if (!store.isEmpty()) {
            return new ArrayList<>(store);
        }
        return null;
    }

    public Collection<Car> getCars(Comparator<Car> comparator) {
        if (!store.isEmpty()) {
            List<Car> sorted = new ArrayList<>(store);
            sorted.sort(comparator);

            return sorted;
        }
        return null;
    }

    public Collection<Car> getCars(Comparator<Car> comparator, boolean isReversed) {
        if (!store.isEmpty()) {
            List<Car> sorted = new ArrayList<>(store);
            sorted.sort(comparator);

            if (isReversed) {
                Collections.reverse(sorted);
            }
            return sorted;
        }
        return null;
    }

    public int getNumberOfCars() {
        if (!store.isEmpty()) {
            return store.size();
        }
        return 0;
    }

    public int getTotalPriceForCars() {
        if (!store.isEmpty()) {
            int totalPrice = 0;

            for (Car car : store) {
                totalPrice += car.getPrice();
            }
            return totalPrice;
        }
        return 0;
    }
}