package bg.sofia.fmi.kbs.gd;

public class Triple {
    private double age;
    private double weight;
    private double height;

    public Triple(String line) {
        String[] toWords = line.split("\\s");
        age = Double.parseDouble(toWords[0]);
        weight = Double.parseDouble(toWords[1]);
        height = Double.parseDouble(toWords[2]);
    }

    public double getAge() {
        return age;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}