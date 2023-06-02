package com.edts.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConcertCreateDTO {
    private String name;
    private String artist;
    private Timestamp date;
    private List<TicketCategoryRequestDTO> ticketCategories;
}
