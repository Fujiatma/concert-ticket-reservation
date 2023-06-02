package com.edts.domain;

import com.edts.domain.controllers.ConcertController;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ConcertControllerTest {
    @InjectMocks
    private ConcertController concertController;

    @Mock
    private ConcertService concertService;

    @Mock
    private TicketCategoryService ticketCategoryService;

    @Mock
    private TicketService ticketService;

    @Mock
    private BookingService bookingService;

    @Test
    public void testCreateConcert_Success() {
        // Mock input
        ConcertCreateDTO concertCreateDTO = new ConcertCreateDTO();
        // Set properties of concertCreateDTO

        // Mock service method
        Concert createdConcert = new Concert();
        // Set properties of createdConcert
        Mockito.when(concertService.createConcert(concertCreateDTO)).thenReturn(createdConcert);

        ResponseEntity<?> response = concertController.createConcert(concertCreateDTO);

        // Verify the response
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(createdConcert, response.getBody());
    }

    @Test
    public void testCreateConcert_Failure() {
        // Mock input
        ConcertCreateDTO concertCreateDTO = new ConcertCreateDTO();
        // Set properties of concertCreateDTO

        // Mock service method to throw an exception
        Mockito.when(concertService.createConcert(concertCreateDTO)).thenThrow(new RuntimeException("Failed to create concert"));

        ResponseEntity<?> response = concertController.createConcert(concertCreateDTO);

        // Verify the response
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("Failed to create concert", response.getBody());
    }

    @Test
    public void testGetAvailableConcerts() {
        // Mock service method
        List<ConcertDTO> availableConcerts = new ArrayList<>();
        // Add concertDTO objects to availableConcerts
        Mockito.when(bookingService.getAvailableConcerts()).thenReturn(availableConcerts);

        ResponseEntity<List<ConcertDTO>> response = concertController.getAvailableConcerts();

        // Verify the response
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(availableConcerts, response.getBody());
    }

    @Test
    public void testGetAllConcertWithAllTicket() {
        // Mock service method
        List<Concert> allConcerts = new ArrayList<>();
        // Add Concert objects to allConcerts
        Mockito.when(concertService.getAllConcerts()).thenReturn(allConcerts);

        List<Concert> result = concertController.getAllConcertWithAllTicket();

        // Verify the result
        Assertions.assertEquals(allConcerts, result);
    }

    @Test
    public void testGetAllTicketCategoriesByConcertID() {
        String concertId = "123";
        // Mock service method
        List<TicketCategory> ticketCategories = new ArrayList<>();
        // Add TicketCategory objects to ticketCategories
        Mockito.when(ticketCategoryService.getAllTicketCategoriesByConcertID(concertId)).thenReturn(ticketCategories);


        List<TicketCategory> result = concertController.getAllTicketCategoriesByConcertID(concertId);

        // Verify the result
        Assertions.assertEquals(ticketCategories, result);
    }

    @Test
    public void testBookTicket_Success() throws NotAvailableException {
        // Mock input
        BookingRequestDTO bookingRequestDTO = new BookingRequestDTO();
        // Set properties of bookingRequestDTO


        ResponseEntity<String> response = concertController.bookTicket(bookingRequestDTO);

        // Verify the response
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Ticket booked successfully", response.getBody());

        // Verify that the bookingService method is called
        Mockito.verify(bookingService).bookTicket(bookingRequestDTO);
    }

    @Test
    public void testBookTicket_NotAvailableException() throws NotAvailableException {
        // Mock input
        BookingRequestDTO bookingRequestDTO = new BookingRequestDTO();
        // Set properties of bookingRequestDTO

        // Mock service method to throw NotAvailableException
        Mockito.doThrow(new NotAvailableException("Ticket is not available")).when(bookingService).bookTicket(bookingRequestDTO);


        ResponseEntity<String> response = concertController.bookTicket(bookingRequestDTO);

        // Verify the response
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Ticket is not available", response.getBody());

        // Verify that the bookingService method is called
        Mockito.verify(bookingService).bookTicket(bookingRequestDTO);
    }

    @Test
    public void testMultipleBooking_Success() throws NotAvailableException {
        // Mock input
        List<BookingRequestDTO> bookingRequests = new ArrayList<>();
        // Add BookingRequestDTO objects to bookingRequests


        ResponseEntity<String> response = concertController.multipleBooking(bookingRequests);

        // Verify the response
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Multiple booking successful", response.getBody());

        // Verify that the bookingService method is called
        Mockito.verify(bookingService).multipleBooking(bookingRequests);
    }

    @Test
    public void testMultipleBooking_NotAvailableException() throws NotAvailableException {
        // Mock input
        List<BookingRequestDTO> bookingRequests = new ArrayList<>();
        // Add BookingRequestDTO objects to bookingRequests

        // Mock service method to throw NotAvailableException
        Mockito.doThrow(new NotAvailableException("Tickets are not available")).when(bookingService).multipleBooking(bookingRequests);


        ResponseEntity<String> response = concertController.multipleBooking(bookingRequests);

        // Verify the response
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Tickets are not available", response.getBody());

        // Verify that the bookingService method is called
        Mockito.verify(bookingService).multipleBooking(bookingRequests);
    }

}

