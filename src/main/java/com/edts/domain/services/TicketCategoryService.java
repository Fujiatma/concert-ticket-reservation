package com.edts.domain.services;

import com.edts.domain.model.entities.Concert;
import com.edts.domain.model.entities.TicketCategory;
import com.edts.domain.repositories.ConcertRepository;
import com.edts.domain.repositories.TicketCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TicketCategoryService {
    private final TicketCategoryRepository ticketCategoryRepository;
    private final ConcertRepository concertRepository;

    @Autowired
    public TicketCategoryService(TicketCategoryRepository ticketCategoryRepository, ConcertRepository concertRepository) {
        this.ticketCategoryRepository = ticketCategoryRepository;
        this.concertRepository = concertRepository;
    }

    public List<TicketCategory> getAllTicketCategoriesByConcertID(String concertId) {
        Optional<Concert> concertOptional = concertRepository.findById(concertId);
        if (concertOptional.isPresent()) {
            Concert concert = concertOptional.get();
            List<TicketCategory> ticketCategories = concert.getTicketCategories();
            return ticketCategories;
        }
        return Collections.emptyList();
    }

    // Other methods as needed
}