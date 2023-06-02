package com.edts.domain.controllers;

import com.edts.domain.helper.NotAvailableException;
import com.edts.domain.model.dto.BookingRequestDTO;
import com.edts.domain.model.dto.ConcertCreateDTO;
import com.edts.domain.model.dto.ConcertDTO;
import com.edts.domain.model.entities.Concert;
import com.edts.domain.model.entities.TicketCategory;
import com.edts.domain.services.BookingService;
import com.edts.domain.services.ConcertService;
import com.edts.domain.services.TicketCategoryService;
import com.edts.domain.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/concert_ticket")
public class ConcertController {
    private final ConcertService concertService;
    private final TicketCategoryService ticketCategoryService;
    private final TicketService ticketService;

    private final BookingService bookingService;

    @Autowired
    public ConcertController(ConcertService concertService, TicketCategoryService ticketCategoryService,
                             TicketService ticketService, BookingService bookingService) {
        this.concertService = concertService;
        this.ticketCategoryService = ticketCategoryService;
        this.ticketService = ticketService;
        this.bookingService = bookingService;
    }


    // Create Data Concert, for Dummy data
    @PostMapping("/concerts")
    public ResponseEntity<?> createConcert(@RequestBody ConcertCreateDTO concertCreateDTO) {
        try {
            Concert concert = concertService.createConcert(concertCreateDTO);
            return ResponseEntity.ok(concert);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create concert");
        }
    }


    // Main Task
    // Get Available Concert Ticket
    @GetMapping("/available")
    public ResponseEntity<List<ConcertDTO>> getAvailableConcerts() {
        List<ConcertDTO> availableConcerts = bookingService.getAvailableConcerts();
        return ResponseEntity.ok(availableConcerts);
    }

    // Get all concert with each ticket own
    @GetMapping("/tickets")
    public List<Concert> getAllConcertWithAllTicket() {
        return concertService.getAllConcerts();
    }

    // Get all ticket categories by concert id
    @GetMapping("/{concertId}/tickets")
    public List<TicketCategory> getAllTicketCategoriesByConcertID(@PathVariable String concertId) {
        return ticketCategoryService.getAllTicketCategoriesByConcertID(concertId);
    }

    // Booking Single Concert Ticket
    @PostMapping("/booking")
    public ResponseEntity<String> bookTicket(@RequestBody BookingRequestDTO bookingRequestDTO) {
        try {
            bookingService.bookTicket(bookingRequestDTO);
            return ResponseEntity.ok("Ticket booked successfully");
        } catch (NotAvailableException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Booking Multiple Concert Ticket
    @PostMapping("/booking/multiple")
    public ResponseEntity<String> multipleBooking(@RequestBody List<BookingRequestDTO> bookingRequests) {
        try {
            bookingService.multipleBooking(bookingRequests);
            return ResponseEntity.ok("Multiple booking successful");
        } catch (NotAvailableException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
