package agency;

import util.TimeProvider;

public class Car extends AbstractVehicle {
    private int numberOfSeats;

    public Car(String brand, String model, int productionYear, int numberOfSeats) {
        super(brand, model, productionYear);
        if (numberOfSeats < 1) {
            throw new IllegalArgumentException("Invalid number of seats: " + numberOfSeats);
        }

        this.numberOfSeats = numberOfSeats;
    }

    /**
     * renvoie une chaîne de caractères avec les détails du véhicule
     * @return une chaîne de caractères avec les détails du véhicule
     */
    public String getDetails() {
        return numberOfSeats == 1 ? "(1 seat)" : "(" + numberOfSeats + " seats)";
    }

    /**
     * Retourne si un véhicule à moins de 5 ans
     * @return si un véhicule à moins de 5 ans
     */
    public boolean isNew() {
        return TimeProvider.currentYearValue() - getProductionYear() <= 5;
    }

    @Override
    public double dailyRentalPrice() {
        return isNew() ? 40 * numberOfSeats : 20 * numberOfSeats;
    }

    @Override
    public String toString() {
        return "Car " + super.toString();
    }
}
