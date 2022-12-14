package org.example.services;

import org.example.BookingRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Nested
class BookingServiceTestTwo {

    @Mock
    private RoomService roomService;
    @Mock
    private MailService mailService;
    @Mock
    private PaymentService paymentService;
    @InjectMocks
    private BookingService bookingService;
    @Captor
    private ArgumentCaptor<Integer> payArgumentCaptor;

    @Nested
    class ClassOne {
        @Test
        public void should_payRightAmountOfMoney_When_prepaid() {
            BookingRequest bookingRequest = new BookingRequest("1", 3, 1, true, "1");
            bookingService.bookRoom(bookingRequest);
            verify(paymentService, times(1)).pay(payArgumentCaptor.capture());
            int amountToPay = payArgumentCaptor.getValue();
            // System.out.println(amountToPay);
            assertEquals(150, amountToPay);
        }

        @Test
        public void two_bookings_should_pay_right_amount_of_money_when_prepaid() {
            BookingRequest bookingRequest = new BookingRequest("1", 3, 1, true, "1");
            BookingRequest bookingRequest2 = new BookingRequest("2", 4, 2, true, "1");
            bookingService.bookRoom(bookingRequest);
            bookingService.bookRoom(bookingRequest2);
            verify(paymentService, times(2)).pay(payArgumentCaptor.capture());
            List<Integer> actualValues = payArgumentCaptor.getAllValues();
            List<Integer> expectedValues = Arrays.asList(150, 400);
            //System.out.println(amountToPay);
            assertEquals(expectedValues, actualValues);
        }
    }

    @Nested
    class ClassTwo {
        @ParameterizedTest
        @DisplayName("Parameterized Test")
        @ValueSource(ints = {1, 2, 33, 4, 5, 11})
        public void testingParametrizedTest(int days) {
            Assumptions.assumeTrue(days > 10);
            BookingRequest bookingRequest = new BookingRequest("1", 3, days, true, "1");
            int actual = bookingService.calculatePrice(bookingRequest);
            int expected = 50 * 3 * days;
            assertEquals(expected, actual);
            //System.out.println(actual);
        }
    }


    @Test
    public void three_bookings_should_pay_right_amount_of_money_when_prepaid() {
        BookingRequest bookingRequest = new BookingRequest("1", 3, 1, true, "1");
        BookingRequest bookingRequest2 = new BookingRequest("2", 4, 2, true, "1");
        BookingRequest bookingRequest3 = new BookingRequest("2", 1, 2, true, "1");
        bookingService.bookRoom(bookingRequest);
        bookingService.bookRoom(bookingRequest2);
        bookingService.bookRoom(bookingRequest3);
        verify(paymentService, times(3)).pay(payArgumentCaptor.capture());
        List<Integer> actualValues = payArgumentCaptor.getAllValues();
        List<Integer> expectedValues = Arrays.asList(150, 400, 100);
        //System.out.println(amountToPay);
        assertEquals(expectedValues, actualValues);
    }

    @Test
    @DisplayName("Here we are learning how to mock a static method")
    public void staticMethodToConvertSEKToEuro() {
        try (MockedStatic<CurrencyConverter> mockedStatic = mockStatic(CurrencyConverter.class)) {
            BookingRequest bookingRequest = new BookingRequest("1", 3, 1, true, "1");
            mockedStatic.when(() -> CurrencyConverter.toEuro(anyInt())).thenReturn(50);
            int actual = bookingService.calculatePriceInEuro(bookingRequest);
            System.out.println(actual);
            assertEquals(50, actual);
        }
    }


}