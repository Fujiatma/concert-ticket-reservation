package com.edts.domain.services;

import com.edts.domain.model.dto.ConcertCreateDTO;
import com.edts.domain.model.dto.TicketCategoryRequestDTO;
import com.edts.domain.model.entities.Concert;
import com.edts.domain.model.entities.Ticket;
import com.edts.domain.model.entities.TicketCategory;
import com.edts.domain.repositories.ConcertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ConcertService {

    private final ConcertRepository concertRepository;

    @Autowired
    public ConcertService(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    public List<Concert> getAllConcerts() {
        return concertRepository.findAll();
    }

    @Transactional
    public Concert createConcert(ConcertCreateDTO concertCreateDTO) {
        Concert concert = new Concert();
        concert.setId(UUID.randomUUID().toString());
        concert.setName(concertCreateDTO.getName());
        concert.setArtist(concertCreateDTO.getArtist());
        concert.setDate(concertCreateDTO.getDate());
        LocalDateTime now = LocalDateTime.now();
        concert.setCreatedAt(Timestamp.valueOf(now));
        concert.setUpdatedAt(Timestamp.valueOf(now));

        List<TicketCategory> ticketCategories = new ArrayList<>();
        for (TicketCategoryRequestDTO ticketCategoryDTO : concertCreateDTO.getTicketCategories()) {
            TicketCategory ticketCategory = new TicketCategory();
            ticketCategory.setId(UUID.randomUUID().toString());
            ticketCategory.setName(ticketCategoryDTO.getName());
            ticketCategory.setCapacity(ticketCategoryDTO.getCapacity());
            ticketCategory.setRemainingCapacity(ticketCategoryDTO.getCapacity());
            ticketCategory.setCreatedAt(Timestamp.valueOf(now));
            ticketCategory.setUpdatedAt(Timestamp.valueOf(now));

            List<Ticket> tickets = new ArrayList<>();
            for (int i = 0; i < ticketCategoryDTO.getCapacity(); i++) {
                Ticket ticket = new Ticket();
                ticket.setId(UUID.randomUUID().toString());
                ticket.setTicketNumber(generateTicketNumber());
                ticket.setPrice(ticketCategoryDTO.getPrice());
                ticket.setCreatedAt(Timestamp.valueOf(now));
                ticket.setUpdatedAt(Timestamp.valueOf(now));
                ticket.setCategory(ticketCategory);
                ticket.setConcert(concert);
                tickets.add(ticket);
            }
            ticketCategory.setTickets(tickets);
            ticketCategories.add(ticketCategory);
        }

        concert.setTicketCategories(ticketCategories);

        // Set the concert for all ticket categories outside the loop
        for (TicketCategory ticketCategory : ticketCategories) {
            ticketCategory.setConcert(concert);
        }

        return concertRepository.save(concert);
    }

    private String generateTicketNumber() {
        // Logic to generate ticket number
        return "TICKET-" + UUID.randomUUID().toString();
    }

}
