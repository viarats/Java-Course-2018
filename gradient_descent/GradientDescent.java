package bg.sofia.fmi.kbs.gd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class GradientDescent {
    private List<Triple> data;
    private double b0;
    private double b1;
    private double b2;
    private static final double LEARNING_RATE = 0.015;

    public GradientDescent(String path) {
        data = new ArrayList<>(readData(path));
        data.forEach(e -> e.setAge(scaleAge(e.getAge())));                    // scale the data
        data.forEach(e -> e.setWeight(scaleWeight(e.getWeight())));
        data.forEach(e -> e.setHeight(scaleHeight(e.getHeight())));

        b0 = 0;
        b1 = 0;
        b2 = 0;

        computeCoefficients();
    }

    public double getGradientDescent(double age, double weight) {
        return b0 + b1 * age + b2 * weight;
    }

    private void computeCoefficients() {
        for (Triple triple : data) {
            double x1 = triple.getAge();
            double x2 = triple.getWeight();

            double y = triple.getHeight();

            double guess = b0 + b1 * x1 + b2 * x2;
            double error = y - guess;

            b0 = b0 + error * LEARNING_RATE;              // partial derivatives
            b1 = b1 + error * x1 * LEARNING_RATE;
            b2 = b2 + error * x2 * LEARNING_RATE;
        }
    }

    private List<Triple> readData(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            return reader.lines().map(Triple::new).collect(Collectors.toList());
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private double getAverageAge() {
        return data.stream().map(Triple::getAge).reduce((x, y) -> x + y).get() / data.size();
    }

    private double getSdOfAge() {
        double squaredSum =0;
        for (Triple line : data) {
            squaredSum += Math.pow(line.getAge() - getAverageAge(), 2);
        }

        return Math.sqrt(squaredSum / data.size());
    }

    private double scaleAge(double age) {
        return (age - getAverageAge()) / getSdOfAge();
    }

    private double getAverageWeight() {
        return data.stream().map(Triple::getWeight).reduce((x, y) -> x + y).get() / data.size();
    }

    private double getSdOfWeight() {
        double squaredSum =0;
        for (Triple line : data) {
            squaredSum += Math.pow(line.getWeight() - getAverageWeight(), 2);
        }

        return Math.sqrt(squaredSum / data.size());
    }

    private double scaleWeight(double weight) {
        return (weight - getAverageWeight()) / getSdOfWeight();
    }

    private double getAverageHeight() {
        return data.stream().map(Triple::getHeight).reduce((x, y) -> x + y).get() / data.size();
    }

    private double getSdOfHeight() {
        double squaredSum =0;
        for (Triple line : data) {
            squaredSum += Math.pow(line.getHeight() - getAverageHeight(), 2);
        }

        return Math.sqrt(squaredSum / data.size());
    }

    private double scaleHeight(double height) {
        return (height - getAverageHeight()) / getSdOfHeight();
    }

    public static void main(String[] args) {
        GradientDescent gd = new GradientDescent("bg/sofia/fmi/kbs/gd/data.txt");
        Scanner sc = new Scanner(System.in);
        DecimalFormat dec = new DecimalFormat("#0.00");

        double age;
        double weight;
        double height;

        while (true) {
            System.out.print("Enter age: ");
            age = Double.parseDouble(sc.nextLine());

            System.out.println();

            System.out.print("Enter weight: ");
            weight = Double.parseDouble(sc.nextLine());

            System.out.println();
            height = gd.getGradientDescent(age, weight);

            System.out.println("Height is: " + dec.format(height));
            System.out.println();
        }
    }
}