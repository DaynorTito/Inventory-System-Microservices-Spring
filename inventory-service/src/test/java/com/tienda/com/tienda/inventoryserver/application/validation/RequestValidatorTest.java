package com.tienda.com.tienda.inventoryserver.application.validation;

import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
class RequestValidatorTest {
    @Test
    public void validateRangeDates_ShouldThrowValidationException_WhenEndDateIsBeforeStartDate() {
        LocalDate startDate = LocalDate.of(2025, 3, 25);
        LocalDate endDate = LocalDate.of(2025, 3, 24);

        ValidationException thrown = assertThrows(ValidationException.class,
                () -> RequestValidator.validateRangeDates(startDate, endDate));
        assertEquals("La fecha de adquisicion no puede ser posterior a la fecha de vencimiento", thrown.getMessage());
    }

    @Test
    public void validateRangeDates_ShouldNotThrowException_WhenEndDateIsAfterStartDate() {
        LocalDate startDate = LocalDate.of(2025, 3, 24);
        LocalDate endDate = LocalDate.of(2025, 3, 25);
        assertDoesNotThrow(() -> RequestValidator.validateRangeDates(startDate, endDate));
    }

    @Test
    public void validateExpirationAdquisitionDate_ShouldThrowValidationException_WhenAdquisitionDateIsAfter() {
        LocalDate adquisitionDate = LocalDate.of(2025, 3, 25);
        LocalDate expirationDate = LocalDate.of(2025, 3, 24);
        ValidationException thrown = assertThrows(ValidationException.class,
                () -> RequestValidator.validateExpirationAdquisitionDate(adquisitionDate, expirationDate));
        assertEquals("La fecha de adquisicion no puede ser posterior a la fecha de vencimiento", thrown.getMessage());
    }

    @Test
    public void validateExpirationAdquisitionDate_ShouldThrowValidationException_WhenExpirationDateIsTooClose() {
        LocalDate adquisitionDate = LocalDate.of(2025, 3, 24);
        LocalDate expirationDate = LocalDate.now().plusDays(2);
        ValidationException thrown = assertThrows(ValidationException.class,
                () -> RequestValidator.validateExpirationAdquisitionDate(adquisitionDate, expirationDate));
        assertEquals("La fecha de vencimiento esta demasiado proxima, minimo 3 dias de diferencia", thrown.getMessage());
    }

    @Test
    public void validateExpirationAdquisitionDate_ShouldNotThrowException_WhenAdquisitionDateIsBeforeExpirationDate() {
        LocalDate adquisitionDate = LocalDate.of(2025, 3, 24);
        LocalDate expirationDate = LocalDate.now().plusDays(4);
        assertDoesNotThrow(() -> RequestValidator.validateExpirationAdquisitionDate(adquisitionDate, expirationDate));
    }
}
