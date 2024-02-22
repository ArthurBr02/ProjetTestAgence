package agency;

import java.util.List;
import java.util.function.Predicate;

public class MaxPriceCriterion implements Predicate<AbstractVehicle> {
    private double maxPrice;

    public MaxPriceCriterion(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    @Override
    public boolean test(AbstractVehicle abstractVehicle) {
        return abstractVehicle.dailyRentalPrice() <= maxPrice;
    }
}
