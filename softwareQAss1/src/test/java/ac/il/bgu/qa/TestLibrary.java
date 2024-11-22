package ac.il.bgu.qa;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ac.il.bgu.qa.errors.*;
import ac.il.bgu.qa.services.DatabaseService;
import ac.il.bgu.qa.services.NotificationService;
import ac.il.bgu.qa.services.ReviewService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class TestLibrary {
    Library library;
    @Mock
    DatabaseService mockDatabaseService;
    @Mock
    NotificationService mockNotificationService;
    @Mock
    ReviewService mockReviewService;



    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        library = new Library(mockDatabaseService, mockReviewService);
    }

    @Test
    public void givenValidBook_whenAddBook_thenBookAdded() {
        Book mockBook = mock(Book.class);
        when(mockBook.getISBN()).thenReturn("9781501110368");
        when(mockBook.getTitle()).thenReturn("It ends with us");
        when(mockBook.getAuthor()).thenReturn("Coleen Hoover");
        when(mockBook.isBorrowed()).thenReturn(false);
        when(mockDatabaseService.getBookByISBN("9781501110368")).thenReturn(null);
        
        library.addBook(mockBook);
        
        verify(mockDatabaseService).addBook(mockBook.getISBN(), mockBook);
    }

    @Test
    public void givenNullBook_whenAddBook_thenIllegalArgumentException() {
        Book mockBook = null;
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.addBook(mockBook);
        });

        assertEquals("Invalid book.", thrown.getMessage());

        verify(mockDatabaseService, never()).addBook(anyString(), eq(mockBook));
    }

    @Test
    public void givenInvalidISBN_whenAddBook_thenIllegalArgumentException() {
        Book mockBook = mock(Book.class);
        when(mockBook.getISBN()).thenReturn("978150111036");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.addBook(mockBook);
        });

        assertEquals("Invalid ISBN.", thrown.getMessage());

        verify(mockBook, never()).getTitle();
        verify(mockBook, never()).getAuthor();
        verify(mockBook, never()).isBorrowed();
        verify(mockDatabaseService, never()).getBookByISBN(anyString());
        verify(mockDatabaseService, never()).addBook(anyString(), eq(mockBook));
    }

    @Test
    public void givenNullTitle_whenAddBook_thenIllegalArgumentException() {
        Book mockBook = mock(Book.class);
        when(mockBook.getISBN()).thenReturn("9781501110368");
        when(mockBook.getTitle()).thenReturn(null);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.addBook(mockBook);
        });

        assertEquals("Invalid title.", thrown.getMessage());

        verify(mockBook, never()).getAuthor();
        verify(mockBook, never()).isBorrowed();
        verify(mockDatabaseService, never()).getBookByISBN(anyString());
        verify(mockDatabaseService, never()).addBook(anyString(), eq(mockBook));
    }

    @Test
    public void givenEmptyTitle_whenAddBook_thenIllegalArgumentException() {
        Book mockBook = mock(Book.class);
        when(mockBook.getISBN()).thenReturn("9781501110368");
        when(mockBook.getTitle()).thenReturn("");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.addBook(mockBook);
        });

        assertEquals("Invalid title.", thrown.getMessage());

        verify(mockBook, never()).getAuthor();
        verify(mockBook, never()).isBorrowed();
        verify(mockDatabaseService, never()).getBookByISBN(anyString());
        verify(mockDatabaseService, never()).addBook(anyString(), eq(mockBook));
    }

    @Test
    public void givenAuthorInvalid_whenAddBook_thenIllegalArgumentException() {
        Book mockBook = mock(Book.class);
        when(mockBook.getISBN()).thenReturn("9781501110368");
        when(mockBook.getTitle()).thenReturn("It ends with us");
        when(mockBook.getAuthor()).thenReturn("");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.addBook(mockBook);
        });

        assertEquals("Invalid author.", thrown.getMessage());
        verify(mockBook, never()).isBorrowed();
        verify(mockDatabaseService, never()).getBookByISBN(anyString());
        verify(mockDatabaseService, never()).addBook(anyString(), eq(mockBook));
    }

    @Test
    public void givenBookIsBorrowed_whenAddBook_thenIllegalArgumentException() {
        Book mockBook = mock(Book.class);
        when(mockBook.getISBN()).thenReturn("9781501110368");
        when(mockBook.getTitle()).thenReturn("It ends with us");
        when(mockBook.getAuthor()).thenReturn("Coleen Hoover");
        when(mockBook.isBorrowed()).thenReturn(true);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.addBook(mockBook);
        });

        assertEquals("Book with invalid borrowed state.", thrown.getMessage());
        verify(mockDatabaseService, never()).getBookByISBN(anyString());
        verify(mockDatabaseService, never()).addBook(anyString(), eq(mockBook));
    }

    @Test
    public void givenNotNullBookByISBN_whenAddBook_thenIllegalArgumentException() {
        Book mockBook = mock(Book.class);
        when(mockBook.getISBN()).thenReturn("9781501110368");
        when(mockBook.getTitle()).thenReturn("It ends with us");
        when(mockBook.getAuthor()).thenReturn("Coleen Hoover");
        when(mockBook.isBorrowed()).thenReturn(false);
        when(mockDatabaseService.getBookByISBN("9781501110368")).thenReturn(mock(Book.class));
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.addBook(mockBook);
        });

        assertEquals("Book already exists.", thrown.getMessage());
        verify(mockDatabaseService, never()).addBook(anyString(), eq(mockBook));
    }

    @Test
    public void givenValidUser_whenRegisterUser_thenUserRegistered() {
        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn("102030405060");
        when(mockUser.getName()).thenReturn("Coleen Hoover");
        when(mockUser.getNotificationService()).thenReturn(mockNotificationService);
        when(mockDatabaseService.getUserById(anyString())).thenReturn(null);

        library.registerUser(mockUser);

        verify(mockDatabaseService).registerUser(mockUser.getId(), mockUser);
    }

    @Test
    public void givenNullUser_whenRegisterUser_thenIllegalArgumentException() {
        User mockUser = null;
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.registerUser(mockUser);
        });

        assertEquals("Invalid user.", thrown.getMessage());

        verify(mockDatabaseService, never()).registerUser(anyString(), eq(mockUser));
    }

    @Test
    public void givenNullAuthorId_whenRegisterUser_thenIllegalArgumentException() {
        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(null);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.registerUser(mockUser);
        });

        assertEquals("Invalid user Id.", thrown.getMessage());

        verify(mockUser, never()).getName();
        verify(mockUser, never()).getNotificationService();
        verify(mockDatabaseService, never()).getUserById(anyString());
        verify(mockDatabaseService, never()).registerUser(anyString(), eq(mockUser));
    }

    @ParameterizedTest
    @ValueSource(strings = { "10203040506" , "1020304050601" })
    public void givenInvalidAuthorId_whenRegisterUser_thenIllegalArgumentException(String userId) {
        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(userId);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.registerUser(mockUser);
        });

        assertEquals("Invalid user Id.", thrown.getMessage());

        verify(mockUser, never()).getName();
        verify(mockUser, never()).getNotificationService();
        verify(mockDatabaseService, never()).getUserById(anyString());
        verify(mockDatabaseService, never()).registerUser(anyString(), eq(mockUser));
    }

    @Test
    public void givenNullName_whenRegisterUser_thenIllegalArgumentException() {
        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn("102030405060");
        when(mockUser.getName()).thenReturn(null);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.registerUser(mockUser);
        });

        assertEquals("Invalid user name.", thrown.getMessage());

        verify(mockUser, never()).getNotificationService();
        verify(mockDatabaseService, never()).getUserById(anyString());
        verify(mockDatabaseService, never()).registerUser(anyString(), eq(mockUser));
    }

    @Test
    public void givenInvalidName_whenRegisterUser_thenIllegalArgumentException() {
        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn("102030405060");
        when(mockUser.getName()).thenReturn("");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.registerUser(mockUser);
        });

        assertEquals("Invalid user name.", thrown.getMessage());

        verify(mockUser, never()).getNotificationService();
        verify(mockDatabaseService, never()).getUserById(anyString());
        verify(mockDatabaseService, never()).registerUser(anyString(), eq(mockUser));
    }

    @Test
    public void givenNullNotificationService_whenRegisterUser_thenIllegalArgumentException() {
        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn("102030405060");
        when(mockUser.getName()).thenReturn("Coleen Hoover");
        when(mockUser.getNotificationService()).thenReturn(null);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.registerUser(mockUser);
        });

        assertEquals("Invalid notification service.", thrown.getMessage());

        verify(mockDatabaseService, never()).getUserById(anyString());
        verify(mockDatabaseService, never()).registerUser(anyString(), eq(mockUser));
    }

    @Test
    public void givenNotNullUserById_whenRegisterUser_thenIllegalArgumentException() {
        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn("102030405060");
        when(mockUser.getName()).thenReturn("Coleen Hoover");
        when(mockUser.getNotificationService()).thenReturn(mockNotificationService);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mock(User.class));
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.registerUser(mockUser);
        });

        assertEquals("User already exists.", thrown.getMessage());

        verify(mockDatabaseService, never()).registerUser(anyString(), eq(mockUser));
    }


    @Test
    public void givenValidISBNAndUserID_whenBorrowBook_thenBookBorrowed() {

        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBook = mock(Book.class);
        User mockUser = mock(User.class);

        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        when(mockBook.isBorrowed()).thenReturn(false);

        library.borrowBook(ISBN, userId);

        verify(mockBook).borrow();
        verify(mockDatabaseService).borrowBook(ISBN, userId);
    }

    @Test
    public void givenInvalidISBN_whenBorrowBook_thenIllegalArgumentException() {
        String ISBN = "978150111036";
        String userId = "102030405060";
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.borrowBook(ISBN,userId);
        });

        assertEquals("Invalid ISBN.", thrown.getMessage());

        verify(mockDatabaseService,never()).getBookByISBN(ISBN);
        verify(mockDatabaseService, never()).getUserById(userId);
        verify(mockDatabaseService, never()).borrowBook(ISBN, userId);
    }

    @Test
    public void givenNullBook_whenBorrowBook_thenBookNotFoundException() {

        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBook = null;
        User mockUser = mock(User.class);
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);

        BookNotFoundException thrown = assertThrows(BookNotFoundException.class, () -> {
            library.borrowBook(ISBN,userId);
        });

        assertEquals("Book not found!", thrown.getMessage());

        verify(mockDatabaseService, never()).getUserById(userId);
        verify(mockDatabaseService, never()).borrowBook(ISBN, userId);
    }


    @Test
    public void givenNullUser_whenBorrowBook_thenUserNotRegisteredException() {

        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBook = mock(Book.class);
        User mockUser = null;
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);

        UserNotRegisteredException thrown = assertThrows(UserNotRegisteredException.class, () -> {
            library.borrowBook(ISBN,userId);
        });

        assertEquals("User not found!", thrown.getMessage());

        verify(mockBook, never()).isBorrowed();
        verify(mockBook, never()).borrow();
        verify(mockDatabaseService, never()).borrowBook(ISBN, userId);
    }

    @Test
    public void givenNullUserId_whenBorrowBook_thenIllegalArgumentException() {

        String ISBN = "9781501110368";
        String userId = null;
        Book mockBook = mock(Book.class);
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.borrowBook(ISBN,userId);
        });

        assertEquals("Invalid user Id.", thrown.getMessage());

        verify(mockBook, never()).isBorrowed();
        verify(mockBook, never()).borrow();
        verify(mockDatabaseService, never()).getUserById(userId);
        verify(mockDatabaseService, never()).borrowBook(ISBN, userId);
    }

    @ParameterizedTest
    @ValueSource(strings = { "10203040506" , "1020304050601" })
    public void givenInvalidUserId_whenBorrowBook_thenIllegalArgumentException(String userId) {
        String ISBN = "9781501110368";
        Book mockBook = mock(Book.class);
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.borrowBook(ISBN,userId);
        });

        assertEquals("Invalid user Id.", thrown.getMessage());

        verify(mockDatabaseService, never()).getUserById(userId);
        verify(mockBook, never()).isBorrowed();
        verify(mockBook, never()).borrow();
        verify(mockDatabaseService, never()).getUserById(userId);
    }

    @Test
    public void givenBorrowedBook_whenBorrowBook_thenBookAlreadyBorrowedException() {
        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBook = mock(Book.class);
        User mockUser = mock(User.class);
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        when(mockBook.isBorrowed()).thenReturn(true);

        BookAlreadyBorrowedException thrown = assertThrows(BookAlreadyBorrowedException.class, () -> {
            library.borrowBook(ISBN,userId);
        });

        assertEquals("Book is already borrowed!", thrown.getMessage());

        verify(mockBook, never()).borrow();
        verify(mockDatabaseService, never()).borrowBook(ISBN, userId);
    }


    @Test
    public void givenBorrowedBook_whenReturnBook_thenBookReturned() {
        String ISBN = "9781501110368";
        Book mockBook = mock(Book.class);
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockBook.isBorrowed()).thenReturn(true);

        library.returnBook(ISBN);

        verify(mockBook).returnBook();
        verify(mockDatabaseService).returnBook(ISBN);
    }

    @Test
    public void givenInvalidISBN_whenReturnBook_thenIllegalArgumentException() {
        String ISBN = "978150111036";
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.returnBook(ISBN);
        });

        assertEquals("Invalid ISBN.",thrown.getMessage());

        verify(mockDatabaseService, never()).getBookByISBN(ISBN);
        verify(mockDatabaseService, never()).returnBook(ISBN);
    }

    @Test
    public void givenNullBook_whenReturnBook_thenBookNotFoundException() {
        String ISBN = "9781501110368";
        Book mockBook = null;
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);

        BookNotFoundException thrown = assertThrows(BookNotFoundException.class, () -> {
            library.returnBook(ISBN);
        });

        assertEquals("Book not found!", thrown.getMessage());

        verify(mockDatabaseService, never()).returnBook(ISBN);
    }

    @Test
    public void givenNotBorrowedBook_whenReturnBook_thenBookNotBorrowedException() {
        String ISBN = "9781501110368";
        Book mockBook = mock(Book.class);
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockBook.isBorrowed()).thenReturn(false);

        BookNotBorrowedException thrown = assertThrows(BookNotBorrowedException.class, () -> {
            library.returnBook(ISBN);
        });

        assertEquals("Book wasn't borrowed!", thrown.getMessage());

        verify(mockBook, never()).returnBook();
        verify(mockDatabaseService, never()).returnBook(ISBN);
    }

    @Test
    public void givenValidISBNAndUserID_whenNotifyUserWithBookReviews_thenUserNotified() {
        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBook = mock(Book.class);
        when(mockBook.getTitle()).thenReturn("It ends with us");
        User mockUser = mock(User.class);
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);

        List<String> mockReviews = new ArrayList<>();
        List<String> spyReviews = spy(mockReviews);
        spyReviews.add("This book is exceptional! I loved it so much!");

        when(mockReviewService.getReviewsForBook(ISBN)).thenReturn(spyReviews);
        String notificationMessage = "Reviews for 'It ends with us':\nThis book is exceptional! I loved it so much!";

        assertDoesNotThrow(() -> library.notifyUserWithBookReviews(ISBN, userId));

        verify(mockReviewService).close();
        verify(mockUser, times(1)).sendNotification(notificationMessage);
    }

    @Test
    public void givenNullUserId_whenNotifyUserWithBookReviews_thenIllegalArgumentException() {
        String ISBN = "9781501110368";
        String userId = null;

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.notifyUserWithBookReviews(ISBN, userId);
        });

        assertEquals("Invalid user Id.", thrown.getMessage());

        verify(mockDatabaseService, never()).getBookByISBN(ISBN);
        verify(mockDatabaseService, never()).getUserById(userId);
        verify(mockReviewService, never()).getReviewsForBook(ISBN);
        verify(mockReviewService, never()).close();
    }

    @ParameterizedTest
    @ValueSource(strings = { "10203040506" , "1020304050601" })
    public void givenInvalidUserId_whenNotifyUserWithBookReviews_thenIllegalArgumentException(String userId) {
        String ISBN = "9781501110368";

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.notifyUserWithBookReviews(ISBN, userId);
        });

        assertEquals("Invalid user Id.", thrown.getMessage());

        verify(mockDatabaseService, never()).getBookByISBN(ISBN);
        verify(mockDatabaseService, never()).getUserById(userId);
        verify(mockReviewService, never()).getReviewsForBook(ISBN);
        verify(mockReviewService, never()).close();
    }

    @Test
    public void givenInvalidISBN_whenNotifyUserWithBookReviews_thenIllegalArgumentException() {
        String ISBN = "978150111036";
        String userId = "102030405060";

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.notifyUserWithBookReviews(ISBN, userId);
        });

        assertEquals("Invalid ISBN.", thrown.getMessage());

        verify(mockDatabaseService, never()).getBookByISBN(ISBN);
        verify(mockDatabaseService, never()).getUserById(userId);
        verify(mockReviewService, never()).getReviewsForBook(ISBN);
        verify(mockReviewService, never()).close();
    }


    @Test
    public void givenNullBook_whenNotifyUserWithBookReviews_thenBookNotFoundException() {
        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBook = null;
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);

        BookNotFoundException thrown = assertThrows(BookNotFoundException.class, () -> {
            library.notifyUserWithBookReviews(ISBN, userId);
        });

        assertEquals("Book not found!", thrown.getMessage());

        verify(mockDatabaseService, never()).getUserById(userId);
        verify(mockReviewService, never()).getReviewsForBook(ISBN);
        verify(mockReviewService, never()).close();
    }

    @Test
    public void givenNullUser_whenNotifyUserWithBookReviews_thenUserNotRegisteredException() {
        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBook = mock(Book.class);
        User mockUser = null;
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);

        UserNotRegisteredException thrown = assertThrows(UserNotRegisteredException.class, () -> {
            library.notifyUserWithBookReviews(ISBN, userId);
        });

        assertEquals("User not found!", thrown.getMessage());

        verify(mockReviewService, never()).getReviewsForBook(ISBN);
        verify(mockReviewService, never()).close();
    }

    @Test
    public void givenNullReviews_whenNotifyUserWithBookReviews_thenNoReviewsFoundException() {
        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBook = mock(Book.class);
        User mockUser = mock(User.class);
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        when(mockReviewService.getReviewsForBook(ISBN)).thenReturn(null);

        NoReviewsFoundException thrown = assertThrows(NoReviewsFoundException.class, () -> {
            library.notifyUserWithBookReviews(ISBN, userId);
        });

        assertEquals("No reviews found!", thrown.getMessage());
        verify(mockReviewService).close();
        verify(mockUser, never()).sendNotification(anyString());

    }

    @Test
    public void givenEmptyReviews_whenNotifyUserWithBookReviews_thenNoReviewsFoundException() {
        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBook = mock(Book.class);
        User mockUser = mock(User.class);
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        List<String> mockReviews = new ArrayList<>();
        List<String> spyReviews = spy(mockReviews);
        when(mockReviewService.getReviewsForBook(ISBN)).thenReturn(spyReviews);

        NoReviewsFoundException thrown = assertThrows(NoReviewsFoundException.class, () -> {
            library.notifyUserWithBookReviews(ISBN, userId);
        });

        assertEquals("No reviews found!", thrown.getMessage());
        verify(mockReviewService).close();
        verify(mockUser, never()).sendNotification(anyString());

    }


    @Test
    public void givenReviewServiceUnavailable_whenNotifyUserWithBookReviews_thenReviewServiceUnavailableException() {
        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBook = mock(Book.class);
        User mockUser = mock(User.class);
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);

        List<String> mockReviews = new ArrayList<>();
        List<String> spyReviews = spy(mockReviews);
        spyReviews.add("This book is exceptional! I loved it so much!");

        when(mockReviewService.getReviewsForBook(ISBN)).thenThrow(new ReviewException("Something went wrong with review service!"));

        ReviewServiceUnavailableException thrown = assertThrows(ReviewServiceUnavailableException.class, () -> {
            library.notifyUserWithBookReviews(ISBN, userId);
        });

        assertEquals("Review service unavailable!", thrown.getMessage());

        verify(mockReviewService).close();
        verify(mockUser, never()).sendNotification(anyString());
    }

    @Test
    public void givenSendNotificationsFails5Times_whenNotifyUserWithBookReviews_thenNotificationException() {
        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBook = mock(Book.class);
        when(mockBook.getTitle()).thenReturn("It ends with us");
        User mockUser = mock(User.class);
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);

        List<String> mockReviews = new ArrayList<>();
        List<String> spyReviews = spy(mockReviews);
        spyReviews.add("This book is exceptional! I loved it so much!");

        when(mockReviewService.getReviewsForBook(ISBN)).thenReturn(spyReviews);
        String notificationMessage = "Reviews for 'It ends with us':\nThis book is exceptional! I loved it so much!";
        doThrow(new NotificationException("Something went wrong with sending the Notification!")).when(mockUser).sendNotification(notificationMessage);

        NotificationException thrown = assertThrows(NotificationException.class, () -> {
            library.notifyUserWithBookReviews(ISBN, userId);
        });

        assertEquals("Notification failed!", thrown.getMessage());

        verify(mockReviewService).close();
        verify(mockUser, times(5)).sendNotification(notificationMessage);
    }

    @Test
    public void givenValidISBNAndUserID_whenGetBookByISBN_thenBookByISBNFound(){
        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBook = mock(Book.class);
        when(mockBook.getTitle()).thenReturn("It ends with us");
        User mockUser = mock(User.class);
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        when(mockBook.isBorrowed()).thenReturn(false);

        //make sure notify user won't fail
        List<String> mockReviews = new ArrayList<>();
        List<String> spyReviews = spy(mockReviews);
        spyReviews.add("This book is exceptional! I loved it so much!");
        when(mockReviewService.getReviewsForBook(ISBN)).thenReturn(spyReviews);

        Book resultBook = library.getBookByISBN(ISBN, userId);

        assertEquals("It ends with us" ,resultBook.getTitle());
    }

    @Test
    public void givenInvalidISBN_whenGetBookByISBN_thenIllegalArgumentException(){
        String ISBN = "978150111036";
        String userId = "102030405060";

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.getBookByISBN(ISBN, userId);
        });

        assertEquals("Invalid ISBN.", thrown.getMessage());

        verify(mockDatabaseService, never()).getBookByISBN(ISBN);
    }

    @Test
    public void givenNullUserID_whenGetBookByISBN_thenIllegalArgumentException() {
        String ISBN = "9781501110368";
        String userId = null;

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.getBookByISBN(ISBN, userId);
        });

        assertEquals("Invalid user Id.", thrown.getMessage());

        verify(mockDatabaseService, never()).getBookByISBN(ISBN);
    }

    @ParameterizedTest
    @ValueSource(strings = { "10203040506" , "1020304050601" })
    public void givenInvalidUserID_whenGetBookByISBN_thenIllegalArgumentException(String userId) {
        String ISBN = "9781501110368";

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.getBookByISBN(ISBN, userId);
        });

        assertEquals("Invalid user Id.", thrown.getMessage());

        verify(mockDatabaseService, never()).getBookByISBN(ISBN);
    }


    @Test
    public void givenNullBook_whenGetBookByISBN_thenBookNotFoundException() {
        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBook = null;
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);

        BookNotFoundException thrown = assertThrows(BookNotFoundException.class, () -> {
            library.getBookByISBN(ISBN, userId);
        });

        assertEquals("Book not found!", thrown.getMessage());
    }

    @Test
    public void givenBookAlreadyBorrowed_whenGetBookByISBN_thenBookAlreadyBorrowedException(){
        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBook = mock(Book.class);
        when(mockBook.getTitle()).thenReturn("It ends with us");
        User mockUser = mock(User.class);
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        when(mockBook.isBorrowed()).thenReturn(true);
        BookAlreadyBorrowedException thrown = assertThrows(BookAlreadyBorrowedException.class, () -> {
            library.getBookByISBN(ISBN, userId);
        });

        assertEquals("Book was already borrowed!", thrown.getMessage());
    }


    @Test
    public void givenUserNotificationFailed_whenGetBookByISBN_thenBookByISBNFound(){
        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBook = mock(Book.class);
        when(mockBook.getTitle()).thenReturn("It ends with us");
        User mockUser = mock(User.class);
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        when(mockBook.isBorrowed()).thenReturn(false);

        //make sure notify user will fail
        List<String> mockReviews = new ArrayList<>();
        List<String> spyReviews = spy(mockReviews);
        when(mockReviewService.getReviewsForBook(ISBN)).thenReturn(spyReviews);

        Book resultBook = library.getBookByISBN(ISBN, userId);

        assertEquals("It ends with us", resultBook.getTitle());

    }
}
