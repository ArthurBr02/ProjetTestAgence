package util;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("util")
class TimeProviderTest {

    @Test
    void currentYearValue() {
        // Given
        int expected = 2024;

        // When
        int result = TimeProvider.currentYearValue();

        // Then
        assertThat(result).isEqualTo(expected);
    }
}