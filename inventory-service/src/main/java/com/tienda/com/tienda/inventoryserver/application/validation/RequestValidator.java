package com.tienda.com.tienda.inventoryserver.application.validation;

import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception.ValidationException;

import java.time.LocalDate;

public class RequestValidator {

    /**
     * Validates that the acquisition date is not later than the expiration date.
     *
     * @param startDate The acquisition date.
     * @param endDate   The expiration date.
     * @throws ValidationException if the acquisition date is later than the expiration date.
     */
    public static void validateRangeDates(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)){
            throw new ValidationException("La fecha de adquisicion no puede ser posterior a la fecha de vencimiento");
        }
    }

    /**
     * Validates that the acquisition date is not later than the expiration date and that the expiration date
     * has at least 3 days of difference from the current date.
     *
     * @param adquisitionDate The acquisition date.
     * @param expirationDate  The expiration date.
     * @return true if the dates are valid.
     * @throws ValidationException if the acquisition date is later than the expiration date,
     *                             or if the expiration date is too close (less than 3 days away from today).
     */
    public static boolean validateExpirationAdquisitionDate(LocalDate adquisitionDate, LocalDate expirationDate) {
        validateRangeDates(adquisitionDate, expirationDate);
        if (adquisitionDate.isAfter(expirationDate))
            throw new ValidationException("La fecha de adquisicion no puede ser posterior a la fecha de vencimiento");
        if (expirationDate.isBefore(LocalDate.now().plusDays(3)))
            throw new ValidationException("La fecha de vencimiento esta demasiado proxima, minimo 3 dias de diferencia");
        return true;
    }
}
