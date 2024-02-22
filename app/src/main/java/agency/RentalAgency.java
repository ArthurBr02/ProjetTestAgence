package agency;

import java.util.*;
import java.util.function.Predicate;

public class RentalAgency {
    private List<AbstractVehicle> vehicles;
    private Map<Client, AbstractVehicle> rentedVehicles;

    public RentalAgency() {
        this(new ArrayList<>());
    }

    public RentalAgency(List<AbstractVehicle> vehicles) {
        this.vehicles = new ArrayList<>(vehicles);
        rentedVehicles = new HashMap<>();
    }

    /**
     * Ajoute un véhicule à l'agence si il n'est pas déjà présent
     * @param vehicle le véhicule
     * @return true si le véhicule a été ajouté, false sinon
     */
    public boolean add(AbstractVehicle vehicle) {
        if (contains(vehicle)) return false;
        vehicles.add(vehicle);
        return true;
    }

    /**
     * Supprime un véhicule de l'agence si il est présent ou lève une exception
     * @param vehicle le véhicule
     * @throws UnknownVehicleException si le véhicule n'est pas présent dans l'agence
     */
    public void remove(AbstractVehicle vehicle) {
        if (!contains(vehicle)) throw new UnknownVehicleException(vehicle);
        vehicles.remove(vehicle);
    }

    /**
     * Retourne true si le véhicule est présent dans l'agence, false sinon
     * @param vehicle le véhicule
     * @return true si le véhicule est présent dans l'agence, false sinon
     */
    public boolean contains(AbstractVehicle vehicle) {
        return vehicles.contains(vehicle);
    }

    public List<AbstractVehicle> getVehicles() {
        return new ArrayList<>(vehicles);
    }

    /**
     * Retourne la liste des véhicules de l'agence qui satisfont le critère
     * @param criterion le critère
     * @return la liste des véhicules de l'agence qui satisfont le critère
     */
    public List<AbstractVehicle> select(Predicate<AbstractVehicle> criterion) {
        return vehicles.stream().filter(criterion).toList();
    }

    /**
     * Affiche les véhicules de l'agence qui satisfont le critère
     * @param criterion le critère
     */
    public void printSelectedVehicles(Predicate<AbstractVehicle> criterion) {
        select(criterion).forEach(System.out::println);
    }

    /**
     * Loue un véhicule à un client
     * @param client le client
     * @param vehicle le véhicule
     * @throws UnknownVehicleException si le véhicule n'est pas présent dans l'agence
     * @throws IllegalStateException si le client loue déjà un véhicule
     * @return le prix journalier de location du véhicule
     */
    public double rentVehicle(Client client, AbstractVehicle vehicle) {
        if (!contains(vehicle)) throw new UnknownVehicleException(vehicle);
        if (aVehicleIsRentedBy(client)) throw new IllegalStateException("Client already has a rented vehicle");
        if (vehicleIsRented(vehicle)) throw new IllegalStateException("Vehicle already rented");
        rentedVehicles.put(client, vehicle);
        return vehicle.dailyRentalPrice();
    }

    /**
     * Retourne true si le client loue un véhicule, false sinon
     * @param client le client
     * @return true si le client loue un véhicule, false sinon
     */
    public boolean aVehicleIsRentedBy(Client client) {
        return rentedVehicles.containsKey(client);
    }

    /**
     * Retourne true si le véhicule est loué, false sinon
     * @param vehicle le véhicule
     * @return true si le véhicule est loué, false sinon
     */
    public boolean vehicleIsRented(AbstractVehicle vehicle) {
        return rentedVehicles.containsValue(vehicle);
    }

    public void returnVehicle(Client client) {
        if (!aVehicleIsRentedBy(client)) throw new IllegalStateException("Client has no rented vehicle");
        rentedVehicles.remove(client);
    }

    public Collection<AbstractVehicle> allRentedVehicles() {
        return rentedVehicles.values();
    }
}
