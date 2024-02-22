package agency;

import java.util.function.Predicate;

public class BrandCriterion implements Predicate<AbstractVehicle> {
    private String brand;

    public BrandCriterion(String brand) {
        this.brand = brand;
    }

    @Override
    public boolean test(AbstractVehicle abstractVehicle) {
        return brand.equals(abstractVehicle.getBrand());
    }
}
