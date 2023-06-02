package com.edts.domain.services;

import com.edts.domain.helper.IDGenerator;
import com.edts.domain.helper.NotAvailableException;
import com.edts.domain.model.dto.BookingRequestDTO;
import com.edts.domain.model.dto.ConcertDTO;
import com.edts.domain.model.entities.*;
import com.edts.domain.repositories.BookingRepository;
import com.edts.domain.repositories.ConcertRepository;
import com.edts.domain.repositories.TicketRepository;
import com.edts.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ConcertRepository concertRepository;

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, ConcertRepository concertRepository, UserRepository userRepository, TicketRepository ticketRepository) {

        this.bookingRepository = bookingRepository;
        this.concertRepository = concertRepository;
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<ConcertDTO> getAvailableConcerts() {
        List<Concert> concerts = concertRepository.findAll();
        List<ConcertDTO> availableConcerts = new ArrayList<>();

        for (Concert concert : concerts) {
            ConcertDTO concertDTO = new ConcertDTO();
            concertDTO.setId(concert.getId());
            concertDTO.setName(concert.getName());
            concertDTO.setArtist(concert.getArtist());
            concertDTO.setDate(concert.getDate());

            List<TicketCategory> availableCategories = concert.getTicketCategories().stream()
                    .filter(category -> category.getRemainingCapacity() > 0)
                    .collect(Collectors.toList());

            concertDTO.setTicketCategories(availableCategories);

            if (!availableCategories.isEmpty()) {
                availableConcerts.add(concertDTO);
            }
        }

        return availableConcerts;
    }


    @Transactional
    public void bookTicket(BookingRequestDTO bookingRequest) {
        Concert concert = concertRepository.findById(bookingRequest.getConcertId())
                .orElseThrow(() -> new NotAvailableException("Concert not found"));

        User user = userRepository.findById(bookingRequest.getUserId())
                .orElseThrow(() -> new NotAvailableException("User not found"));

        Ticket ticket = ticketRepository.findById(bookingRequest.getTicketId())
                .orElseThrow(() -> new NotAvailableException("Ticket not found"));

        TicketCategory ticketCategory = concert.getTicketCategories().stream()
                .filter(category -> category.getId().equals(bookingRequest.getCategoryId()))
                .findFirst()
                .orElseThrow(() -> new NotAvailableException("Ticket category not found"));

        if (ticketCategory.getRemainingCapacity() < bookingRequest.getQuantity()) {
            throw new NotAvailableException("Ticket category is fully booked");
        }

        List<Ticket> tickets = ticketCategory.getTickets().stream()
                .limit(bookingRequest.getQuantity())
                .collect(Collectors.toList());

        if (tickets.size() < bookingRequest.getQuantity()) {
            throw new NotAvailableException("Not enough available tickets");
        }

        Booking booking = new Booking();
        booking.setId(IDGenerator.generateID());
        booking.setConcert(concert);
        booking.setUser(user);
        booking.setTicket(ticket);
        booking.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        booking.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        for (Ticket tkt : tickets) {
            tkt.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        }

        ticketCategory.setRemainingCapacity(ticketCategory.getRemainingCapacity() - bookingRequest.getQuantity());
        ticketCategory.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        bookingRepository.save(booking);
    }

    @Transactional
    public void multipleBooking(List<BookingRequestDTO> bookingRequests) {
        for (BookingRequestDTO bookingRequest : bookingRequests) {
            bookTicket(bookingRequest);
        }
    }
}