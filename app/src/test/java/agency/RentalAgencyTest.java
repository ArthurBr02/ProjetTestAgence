package agency;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("agency")
class RentalAgencyTest {
    RentalAgency agency;

    @Mock
    AbstractVehicle vehicle;

    @Mock
    Client client;

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        agency = new RentalAgency();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void add_rentable_vehicle() {
        // When
        boolean result = agency.add(vehicle);
        boolean result2 = agency.add(vehicle);

        // Then
        assertThat(result).isTrue();
        assertThat(agency.contains(vehicle)).isTrue();
        assertThat(result2).isFalse();
    }

    @Test
    void remove_vehicle_from_agency() {
        // Given
        agency.add(vehicle);

        // When
        agency.remove(vehicle);

        // Then
        assertThat(agency.contains(vehicle)).isFalse();
    }

    @Test
    void remove_when_vehicle_not_in_agency() {
        // When
        var result = catchThrowable(() -> agency.remove(vehicle));

        // Then
        assertThat(result).isInstanceOf(UnknownVehicleException.class);
    }

    @Test
    void agency_contains() {
        // Given
        agency.add(vehicle);

        // When
        boolean result = agency.contains(vehicle);
        boolean result2 = agency.contains(mock(AbstractVehicle.class));

        // Then
        assertThat(result).isTrue();
        assertThat(result2).isFalse();
    }

    @Test
    void getVehicles_from_agency() {
        // Given
        AbstractVehicle vehicle2 = mock(AbstractVehicle.class);

        agency.add(vehicle);
        agency.add(vehicle2);

        // When
        var result = agency.getVehicles();

        // Then
        assertThat(result).containsExactly(vehicle, vehicle2);
    }

    @Test
    void select_with_max_price() {
        // Given
        AbstractVehicle roma = new Car("Ferrari", "Roma", 2022, 4);
        AbstractVehicle multipla = new Car("Fiat", "Multipla", 2005, 6);
        AbstractVehicle moto = new Motorbike("BMW", "Test", 2022, 500);

        agency.add(roma);
        agency.add(multipla);
        agency.add(moto);

        // When
        var result = agency.select(new MaxPriceCriterion(120));
        var result2 = agency.select(new MaxPriceCriterion(400));

        // Then
        assertThat(result).containsExactly(multipla);
        assertThat(result2).containsExactly(roma, multipla, moto);
    }

    @Test
    void select_with_brand() {
        // Given
        AbstractVehicle roma = new Car("Ferrari", "Roma", 2022, 4);
        AbstractVehicle multipla = new Car("Fiat", "Multipla", 2005, 6);

        agency.add(roma);
        agency.add(multipla);

        // When
        var result = agency.select(new BrandCriterion("Ferrari"));
        var result2 = agency.select(new BrandCriterion("Fiat"));

        // Then
        assertThat(result).containsExactly(roma);
        assertThat(result2).containsExactly(multipla);
    }

    @Test
    void printSelectedVehicles_with_criterion() {
        // Given
        AbstractVehicle roma = new Car("Ferrari", "Roma", 2022, 4);
        AbstractVehicle multipla = new Car("Fiat", "Multipla", 2005, 6);
        AbstractVehicle moto = new Motorbike("BMW", "Test", 2022, 500);

        agency.add(roma);
        agency.add(multipla);
        agency.add(moto);

        agency.printSelectedVehicles(new MaxPriceCriterion(120));

        // Then
        assertThat(outputStreamCaptor.toString().trim()).isEqualTo("Car Fiat Multipla 2005 (6 seats): 120.0€");
    }

    @Test
    void rentVehicle_from_agency_with_existing_vehicle() {
        // Given
        agency.add(vehicle);
        when(vehicle.dailyRentalPrice()).thenReturn(100.0);

        // When
        double result = agency.rentVehicle(client, vehicle);

        // Then
        assertThat(result).isEqualTo(vehicle.dailyRentalPrice());
    }

    @Test
    void rentVehicle_from_agency_with_non_existing_vehicle() {
        // When
        var result = catchThrowable(() -> agency.rentVehicle(client, vehicle));

        // Then
        assertThat(result).isInstanceOf(UnknownVehicleException.class);
    }

    @Test
    void rentVehicle_from_agency_with_already_rented_vehicle() {
        // Given
        agency.add(vehicle);
        when(vehicle.dailyRentalPrice()).thenReturn(100.0);
        agency.rentVehicle(client, vehicle);

        // When
        var result = catchThrowable(() -> agency.rentVehicle(mock(Client.class), vehicle));

        // Then
        assertThat(result).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void rentVehicle_from_agency_with_client_already_renting() {
        // Given
        agency.add(vehicle);
        when(vehicle.dailyRentalPrice()).thenReturn(100.0);
        agency.rentVehicle(client, vehicle);

        AbstractVehicle vehicle2 = mock(AbstractVehicle.class);
        agency.add(vehicle2);

        // When
        var result = catchThrowable(() -> agency.rentVehicle(client, vehicle2));

        // Then
        assertThat(result).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void aVehicleIsRentedBy() {
        // Given
        agency.add(vehicle);
        when(vehicle.dailyRentalPrice()).thenReturn(100.0);
        agency.rentVehicle(client, vehicle);

        // When
        boolean result = agency.aVehicleIsRentedBy(client);
        boolean result2 = agency.aVehicleIsRentedBy(mock(Client.class));

        // Then
        assertThat(result).isTrue();
        assertThat(result2).isFalse();
    }

    @Test
    void vehicleIsRented() {
        // Given
        agency.add(vehicle);
        when(vehicle.dailyRentalPrice()).thenReturn(100.0);
        agency.rentVehicle(client, vehicle);

        // When
        boolean result = agency.vehicleIsRented(vehicle);
        boolean result2 = agency.vehicleIsRented(mock(AbstractVehicle.class));

        // Then
        assertThat(result).isTrue();
        assertThat(result2).isFalse();
    }

    @Test
    void returnVehicle() {
        // Given
        agency.add(vehicle);
        when(vehicle.dailyRentalPrice()).thenReturn(100.0);
        agency.rentVehicle(client, vehicle);

        // When
        boolean result = agency.aVehicleIsRentedBy(client);
        agency.returnVehicle(client);

        // Then
        assertThat(result).isTrue();
        assertThat(agency.aVehicleIsRentedBy(client)).isFalse();
    }

    @Test
    void returnVehicle_with_no_vehicle_rented() {
        // When
        var result = catchThrowable(() -> agency.returnVehicle(client));

        // Then
        assertThat(result).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void allRentedVehicles() {
        // Given
        agency.add(vehicle);
        when(vehicle.dailyRentalPrice()).thenReturn(100.0);
        agency.rentVehicle(client, vehicle);

        // When
        var result = agency.allRentedVehicles();

        // Then
        assertThat(result).containsExactly(vehicle);
    }

    @Test
    void test_creation_client() {
        // Given
        Client client = new Client("Arthur", "BRATIGNY", 21);

        // Then
        assertThat(client.getName()).isEqualTo("Arthur");
        assertThat(client.getSurname()).isEqualTo("BRATIGNY");
        assertThat(client.getBirthYear()).isEqualTo(21);
    }

    @Test
    void test_print_car() {
        // Given
        AbstractVehicle car = new Car("Ferrari", "Roma", 2022, 4);

        // Then
        assertThat(car.toString()).isEqualTo("Car Ferrari Roma 2022 (4 seats): 160.0€");
    }

    @Test
    void test_print_motorbike() {
        // Given
        AbstractVehicle motorbike = new Motorbike("BMW", "Test", 2022, 500);

        // Then
        assertThat(motorbike.toString()).isEqualTo("Motorbike BMW Test 2022 (500cm³): 125.0€");
    }

    @Test
    void test_print_unknown_vehicle_exception() {
        // Given
        AbstractVehicle vehicle = new Car("Ferrari", "Roma", 2022, 4);
        UnknownVehicleException exception = new UnknownVehicleException(vehicle);

        // Then
        assertThat(exception.getMessage()).isEqualTo("Unknown vehicle: Car Ferrari Roma 2022 (4 seats): 160.0€");
    }

    @Test
    void test_abstract_vehicle_equals_to_null() {
        // Given
        AbstractVehicle vehicle = new Car("Ferrari", "Roma", 2022, 4);

        // Then
        assertThat(vehicle.equals(null)).isFalse();
    }

    @Test
    void test_vehicle_before_1900() {
        // When
        var result = catchThrowable(() -> new Car("Ferrari", "Roma", 1899, 4));

        // Then
        assertThat(result).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void test_car_with_0_seat() {
        // When
        var result = catchThrowable(() -> new Car("Ferrari", "Roma", 2022, 0));

        // Then
        assertThat(result).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void test_motorbike_with_0_cc() {
        // When
        var result = catchThrowable(() -> new Motorbike("BMW", "Test", 2022, 0));

        // Then
        assertThat(result).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void test_car_details() {
        // Given
        AbstractVehicle car = new Car("Ferrari", "Roma", 2022, 4);

        // Then
        assertThat(car.getDetails()).isEqualTo("(4 seats)");
    }
}