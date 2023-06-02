package com.edts.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingRequestDTO {
    @NotBlank(message = "Concert ID is required")
    private String concertId;

    @NotBlank(message = "Category ID is required")
    private String categoryId;

    @NotBlank(message = "Ticket ID is required")
    private String ticketId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @NotBlank(message = "User ID is required")
    private String userId;
}
