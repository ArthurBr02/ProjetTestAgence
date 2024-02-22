package agency;

public class Motorbike extends AbstractVehicle {
    private int cylinderCapacity;

    public Motorbike(String brand, String model, int productionYear, int cylinderCapacity) {
        super(brand, model, productionYear);
        if (cylinderCapacity < 50) {
            throw new IllegalArgumentException("Invalid cylinder capacity: " + cylinderCapacity);
        }
        this.cylinderCapacity = cylinderCapacity;
    }

    @Override
    public double dailyRentalPrice() {
        return 0.25 * cylinderCapacity;
    }

    @Override
    public String getDetails() {
        return "(" + cylinderCapacity + "cm³)";
    }

    @Override
    public String toString() {
        return "Motorbike " + super.toString();
    }
}
