package agency;

public class UnknownVehicleException extends RuntimeException {
    private final AbstractVehicle vehicle;

    public UnknownVehicleException(AbstractVehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public String getMessage() {
        return "Unknown vehicle: " + vehicle.toString();
    }
}
