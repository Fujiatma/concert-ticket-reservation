package com.edts.domain.repositories;

import com.edts.domain.model.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {
    List<Ticket> findByConcertId(Long concertId);

    Optional<Ticket> findByTicketNumber(String ticketNumber);
}