package agency;

import util.TimeProvider;

public abstract class AbstractVehicle {
    private String brand;
    private String model;
    private int productionYear;

    public AbstractVehicle(String brand, String model, int productionYear) {
        if (productionYear < 1900 || productionYear > TimeProvider.currentYearValue()) {
            throw new IllegalArgumentException("Invalid production year: " + productionYear);
        }

        this.brand = brand;
        this.model = model;
        this.productionYear = productionYear;
    }


    /**
     * renvoie la marque du véhicule sous la forme d’une chaîne de caractères
     *
     * @return la marque du véhicule
     */
    public String getBrand() {
        return brand;
    }

    /**
     * renvoie l’année de production du véhicule
     *
     * @return l’année de production du véhicule
     */
    public int getProductionYear() {
        return productionYear;
    }

    /**
     * renvoie le prix journalier de location du véhicule
     *
     * @return le prix journalier de location du véhicule
     */
    public double dailyRentalPrice() {
        return 0;
    }

    /**
     * teste l’égalité entre le véhicule et l’objet o
     *
     * @return true si le véhicule est égal à l’objet o, false sinon
     */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (o.getClass() != this.getClass()) {
            return false;
        }
        AbstractVehicle v = (AbstractVehicle) o;
        return v.brand.equals(brand) && v.model.equals(model) && v.productionYear == productionYear;

    }

    public String getDetails() {
        return "";
    }

    /**
     * renvoie une chaîne de caractères décrivant le véhicule
     *
     * @return une chaîne de caractères décrivant le véhicule
     */
    public String toString() {
        return brand + " " + model + " " + productionYear + " " + getDetails() + ": " + dailyRentalPrice() + "€";
    }
}
