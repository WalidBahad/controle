package com.carrental.rentalservice.validation;

import com.carrental.rentalservice.dto.RentalRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

/**
 * Implémentation du validateur pour l'annotation ValidDateRange.
 */
public class DateRangeValidator implements ConstraintValidator<ValidDateRange, RentalRequest> {

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
    }

    @Override
    public boolean isValid(RentalRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        if (startDate == null || endDate == null) {
            return true; // Laisser @NotNull gérer la validation nulle
        }

        return endDate.isAfter(startDate);
    }
}
