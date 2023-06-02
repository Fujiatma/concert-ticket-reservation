package com.edts.domain.model.dto;

import com.edts.domain.model.entities.TicketCategory;
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
public class ConcertDTO {
    private String id;
    private String name;
    private String artist;
    private Timestamp date;
    private List<TicketCategory> ticketCategories;
}
