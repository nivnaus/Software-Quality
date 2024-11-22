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
        // Arrange
        Book mockBook = mock(Book.class);
        when(mockBook.getISBN()).thenReturn("9781501110368");
        when(mockBook.getTitle()).thenReturn("It ends with us");
        when(mockBook.getAuthor()).thenReturn("Coleen Hoover");
        when(mockBook.isBorrowed()).thenReturn(false);
        when(mockDatabaseService.getBookByISBN("9781501110368")).thenReturn(null);

        // Act
        library.addBook(mockBook);

        // Assert
        verify(mockDatabaseService).addBook(mockBook.getISBN(), mockBook);
    }

    @Test
    public void givenNullBook_whenAddBook_thenIllegalArgumentException() {
        
        // Arrange
        Book mockBookNull = null;

        // Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.addBook(mockBookNull);
        });

        // Assert
        assertEquals("Invalid book.", thrown.getMessage());
        verify(mockDatabaseService, never()).addBook(anyString(), eq(mockBookNull));
    }

    @Test
    public void givenInvalidISBN_whenAddBook_thenIllegalArgumentException() {
        //Arrange
        Book mockBookInvalidISBN = mock(Book.class);
        when(mockBookInvalidISBN.getISBN()).thenReturn("978150111036");
        
        //Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.addBook(mockBookInvalidISBN);
        });

        //Assert
        assertEquals("Invalid ISBN.", thrown.getMessage());
        verify(mockBookInvalidISBN, never()).getTitle();
        verify(mockBookInvalidISBN, never()).getAuthor();
        verify(mockBookInvalidISBN, never()).isBorrowed();
        verify(mockDatabaseService, never()).getBookByISBN(anyString());
        verify(mockDatabaseService, never()).addBook(anyString(), eq(mockBookInvalidISBN));
    }

    @Test
    public void givenNullTitle_whenAddBook_thenIllegalArgumentException() {
        //Arrange
        Book mockBookNullTitle = mock(Book.class);
        when(mockBookNullTitle.getISBN()).thenReturn("9781501110368");
        when(mockBookNullTitle.getTitle()).thenReturn(null);

        //Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.addBook(mockBookNullTitle);
        });

        //Assert
        assertEquals("Invalid title.", thrown.getMessage());
        verify(mockBookNullTitle, never()).getAuthor();
        verify(mockBookNullTitle, never()).isBorrowed();
        verify(mockDatabaseService, never()).getBookByISBN(anyString());
        verify(mockDatabaseService, never()).addBook(anyString(), eq(mockBookNullTitle));
    }

    @Test
    public void givenEmptyTitle_whenAddBook_thenIllegalArgumentException() {
        //Arrange
        Book mockBookEmptyTitle = mock(Book.class);
        when(mockBookEmptyTitle.getISBN()).thenReturn("9781501110368");
        when(mockBookEmptyTitle.getTitle()).thenReturn("");

        //Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.addBook(mockBookEmptyTitle);
        });

        //Assert
        assertEquals("Invalid title.", thrown.getMessage());
        verify(mockBookEmptyTitle, never()).getAuthor();
        verify(mockBookEmptyTitle, never()).isBorrowed();
        verify(mockDatabaseService, never()).getBookByISBN(anyString());
        verify(mockDatabaseService, never()).addBook(anyString(), eq(mockBookEmptyTitle));
    }

    @ParameterizedTest
    @ValueSource(strings = { "" , "1Coleen Hoover", "Coleen Hoover2", "Colleen--Hoover", "Coleen''hoover" ,"Coleen&hoover"})
    public void givenAuthorInvalid_whenAddBook_thenIllegalArgumentException(String authorName) {
        //Arrange
        Book mockBookAuthorInvalid = mock(Book.class);
        when(mockBookAuthorInvalid.getISBN()).thenReturn("9781501110368");
        when(mockBookAuthorInvalid.getTitle()).thenReturn("It ends with us");
        when(mockBookAuthorInvalid.getAuthor()).thenReturn(authorName);
        
        //Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.addBook(mockBookAuthorInvalid);
        });

        //Assert
        assertEquals("Invalid author.", thrown.getMessage());
        verify(mockBookAuthorInvalid, never()).isBorrowed();
        verify(mockDatabaseService, never()).getBookByISBN(anyString());
        verify(mockDatabaseService, never()).addBook(anyString(), eq(mockBookAuthorInvalid));
    }

    @Test
    public void givenAuthorNameNullInvalid_whenAddBook_thenIllegalArgumentException() {
        //Arrange
        Book mockBookAuthorNameNullInvalid = mock(Book.class);
        when(mockBookAuthorNameNullInvalid.getISBN()).thenReturn("9781501110368");
        when(mockBookAuthorNameNullInvalid.getTitle()).thenReturn("It ends with us");
        when(mockBookAuthorNameNullInvalid.getAuthor()).thenReturn(null);
        
        //Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.addBook(mockBookAuthorNameNullInvalid);
        });

        //Assert
        assertEquals("Invalid author.", thrown.getMessage());
        verify(mockBookAuthorNameNullInvalid, never()).isBorrowed();
        verify(mockDatabaseService, never()).getBookByISBN(anyString());
        verify(mockDatabaseService, never()).addBook(anyString(), eq(mockBookAuthorNameNullInvalid));
    }

    @Test
    public void givenBookIsBorrowed_whenAddBook_thenIllegalArgumentException() {
        //Arrange
        Book mockBookIsBorrowed = mock(Book.class);
        when(mockBookIsBorrowed.getISBN()).thenReturn("9781501110368");
        when(mockBookIsBorrowed.getTitle()).thenReturn("It ends with us");
        when(mockBookIsBorrowed.getAuthor()).thenReturn("Coleen Hoover");
        when(mockBookIsBorrowed.isBorrowed()).thenReturn(true);
        
        //Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.addBook(mockBookIsBorrowed);
        });

        //Assert
        assertEquals("Book with invalid borrowed state.", thrown.getMessage());
        verify(mockDatabaseService, never()).getBookByISBN(anyString());
        verify(mockDatabaseService, never()).addBook(anyString(), eq(mockBookIsBorrowed));
    }

    @Test
    public void givenNotNullBookByISBN_whenAddBook_thenIllegalArgumentException() {
        //Arrange
        Book mockBookNotNullByISBN = mock(Book.class);
        when(mockBookNotNullByISBN.getISBN()).thenReturn("9781501110368");
        when(mockBookNotNullByISBN.getTitle()).thenReturn("It ends with us");
        when(mockBookNotNullByISBN.getAuthor()).thenReturn("Coleen Hoover");
        when(mockBookNotNullByISBN.isBorrowed()).thenReturn(false);
        when(mockDatabaseService.getBookByISBN("9781501110368")).thenReturn(mock(Book.class));
        
        //Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.addBook(mockBookNotNullByISBN);
        });

        //Assert
        assertEquals("Book already exists.", thrown.getMessage());
        verify(mockDatabaseService, never()).addBook(anyString(), eq(mockBookNotNullByISBN));
    }

    @Test
    public void givenValidUser_whenRegisterUser_thenUserRegistered() {
        //Arrange
        User mockValidUser = mock(User.class);
        when(mockValidUser.getId()).thenReturn("102030405060");
        when(mockValidUser.getName()).thenReturn("Coleen Hoover");
        when(mockValidUser.getNotificationService()).thenReturn(mockNotificationService);
        when(mockDatabaseService.getUserById(anyString())).thenReturn(null);

        //Act
        library.registerUser(mockValidUser);

        //Assert
        verify(mockDatabaseService).registerUser(mockValidUser.getId(), mockValidUser);
    }

    @Test
    public void givenNullUser_whenRegisterUser_thenIllegalArgumentException() {
        //Arrange
        User mockNullUser = null;
        
        //Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.registerUser(mockNullUser);
        });

        //Assert
        assertEquals("Invalid user.", thrown.getMessage());
        verify(mockDatabaseService, never()).registerUser(anyString(), eq(mockNullUser));
    }

    @Test
    public void givenNullAuthorId_whenRegisterUser_thenIllegalArgumentException() {
        //Arrange
        User mockUserNullAuthorId = mock(User.class);
        when(mockUserNullAuthorId.getId()).thenReturn(null);

        //Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.registerUser(mockUserNullAuthorId);
        });

        //Assert
        assertEquals("Invalid user Id.", thrown.getMessage());

        verify(mockUserNullAuthorId, never()).getName();
        verify(mockUserNullAuthorId, never()).getNotificationService();
        verify(mockDatabaseService, never()).getUserById(anyString());
        verify(mockDatabaseService, never()).registerUser(anyString(), eq(mockUserNullAuthorId));
    }

    @ParameterizedTest
    @ValueSource(strings = { "10203040506" , "1020304050601" })
    public void givenInvalidAuthorId_whenRegisterUser_thenIllegalArgumentException(String userId) {
        //Arrange
        User mockUserInvalidAuthorId = mock(User.class);
        when(mockUserInvalidAuthorId.getId()).thenReturn(userId);

        //Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.registerUser(mockUserInvalidAuthorId);
        });

        //Assert
        assertEquals("Invalid user Id.", thrown.getMessage());
        verify(mockUserInvalidAuthorId, never()).getName();
        verify(mockUserInvalidAuthorId, never()).getNotificationService();
        verify(mockDatabaseService, never()).getUserById(anyString());
        verify(mockDatabaseService, never()).registerUser(anyString(), eq(mockUserInvalidAuthorId));
    }

    @Test
    public void givenNullName_whenRegisterUser_thenIllegalArgumentException() {
        //Arrange
        User mockUserNullName = mock(User.class);
        when(mockUserNullName.getId()).thenReturn("102030405060");
        when(mockUserNullName.getName()).thenReturn(null);

        //Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.registerUser(mockUserNullName);
        });

        //Assert
        assertEquals("Invalid user name.", thrown.getMessage());
        verify(mockUserNullName, never()).getNotificationService();
        verify(mockDatabaseService, never()).getUserById(anyString());
        verify(mockDatabaseService, never()).registerUser(anyString(), eq(mockUserNullName));
    }

    @Test
    public void givenInvalidName_whenRegisterUser_thenIllegalArgumentException() {
        //Arrange
        User mockUserInvalidName = mock(User.class);
        when(mockUserInvalidName.getId()).thenReturn("102030405060");
        when(mockUserInvalidName.getName()).thenReturn("");

        //Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.registerUser(mockUserInvalidName);
        });

        //Assert
        assertEquals("Invalid user name.", thrown.getMessage());
        verify(mockUserInvalidName, never()).getNotificationService();
        verify(mockDatabaseService, never()).getUserById(anyString());
        verify(mockDatabaseService, never()).registerUser(anyString(), eq(mockUserInvalidName));
    }

    @Test
    public void givenNullNotificationService_whenRegisterUser_thenIllegalArgumentException() {
        //Arrange
        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn("102030405060");
        when(mockUser.getName()).thenReturn("Coleen Hoover");
        when(mockUser.getNotificationService()).thenReturn(null);

        //Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.registerUser(mockUser);
        });

        //Assert
        assertEquals("Invalid notification service.", thrown.getMessage());
        verify(mockDatabaseService, never()).getUserById(anyString());
        verify(mockDatabaseService, never()).registerUser(anyString(), eq(mockUser));
    }

    @Test
    public void givenNotNullUserById_whenRegisterUser_thenIllegalArgumentException() {
        //Arrange
        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn("102030405060");
        when(mockUser.getName()).thenReturn("Coleen Hoover");
        when(mockUser.getNotificationService()).thenReturn(mockNotificationService);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mock(User.class));

        //Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.registerUser(mockUser);
        });

        //Assert
        assertEquals("User already exists.", thrown.getMessage());
        verify(mockDatabaseService, never()).registerUser(anyString(), eq(mockUser));
    }


    @Test
    public void givenValidISBNAndUserID_whenBorrowBook_thenBookBorrowed() {
        //Arrange
        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBook = mock(Book.class);
        User mockUser = mock(User.class);

        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        when(mockBook.isBorrowed()).thenReturn(false);

        //Act
        library.borrowBook(ISBN, userId);

        //Assert
        verify(mockBook).borrow();
        verify(mockDatabaseService).borrowBook(ISBN, userId);
    }

    @Test
    public void givenInvalidISBN_whenBorrowBook_thenIllegalArgumentException() {
        //Arrange
        String invalidISBN = "978150111036";
        String userId = "102030405060";

        //Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.borrowBook(invalidISBN,userId);
        });

        //Assert
        assertEquals("Invalid ISBN.", thrown.getMessage());
        verify(mockDatabaseService,never()).getBookByISBN(invalidISBN);
        verify(mockDatabaseService, never()).getUserById(userId);
        verify(mockDatabaseService, never()).borrowBook(invalidISBN, userId);
    }

    @Test
    public void givenNullBook_whenBorrowBook_thenBookNotFoundException() {
        //Arrange
        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBookNull = null;
        User mockUser = mock(User.class);
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBookNull);

        //Act
        BookNotFoundException thrown = assertThrows(BookNotFoundException.class, () -> {
            library.borrowBook(ISBN,userId);
        });

        //Assert
        assertEquals("Book not found!", thrown.getMessage());
        verify(mockDatabaseService, never()).getUserById(userId);
        verify(mockDatabaseService, never()).borrowBook(ISBN, userId);
    }


    @Test
    public void givenNullUser_whenBorrowBook_thenUserNotRegisteredException() {
        //Arrange
        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBook = mock(Book.class);
        User mockUserNull = null;
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUserNull);

        //Act
        UserNotRegisteredException thrown = assertThrows(UserNotRegisteredException.class, () -> {
            library.borrowBook(ISBN,userId);
        });

        //Assert
        assertEquals("User not found!", thrown.getMessage());
        verify(mockBook, never()).isBorrowed();
        verify(mockBook, never()).borrow();
        verify(mockDatabaseService, never()).borrowBook(ISBN, userId);
    }

    @Test
    public void givenNullUserId_whenBorrowBook_thenIllegalArgumentException() {
        //Arrange
        String ISBN = "9781501110368";
        String nullUserId = null;
        Book mockBook = mock(Book.class);
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);

        //Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.borrowBook(ISBN,nullUserId);
        });

        //Assert
        assertEquals("Invalid user Id.", thrown.getMessage());
        verify(mockBook, never()).isBorrowed();
        verify(mockBook, never()).borrow();
        verify(mockDatabaseService, never()).getUserById(nullUserId);
        verify(mockDatabaseService, never()).borrowBook(ISBN, nullUserId);
    }

    @ParameterizedTest
    @ValueSource(strings = { "10203040506" , "1020304050601" })
    public void givenInvalidUserId_whenBorrowBook_thenIllegalArgumentException(String invalidUserId) {
        //Arrange
        String ISBN = "9781501110368";
        Book mockBook = mock(Book.class);
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);

        //Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.borrowBook(ISBN,invalidUserId);
        });

        //Assert
        assertEquals("Invalid user Id.", thrown.getMessage());
        verify(mockDatabaseService, never()).getUserById(invalidUserId);
        verify(mockBook, never()).isBorrowed();
        verify(mockBook, never()).borrow();
        verify(mockDatabaseService, never()).getUserById(invalidUserId);
    }

    @Test
    public void givenBorrowedBook_whenBorrowBook_thenBookAlreadyBorrowedException() {
        //Arrange
        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBookBorrowed = mock(Book.class);
        User mockUser = mock(User.class);
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBookBorrowed);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        when(mockBookBorrowed.isBorrowed()).thenReturn(true);

        //Act
        BookAlreadyBorrowedException thrown = assertThrows(BookAlreadyBorrowedException.class, () -> {
            library.borrowBook(ISBN,userId);
        });

        //Assert
        assertEquals("Book is already borrowed!", thrown.getMessage());
        verify(mockBookBorrowed, never()).borrow();
        verify(mockDatabaseService, never()).borrowBook(ISBN, userId);
    }


    @Test
    public void givenBorrowedBook_whenReturnBook_thenBookReturned() {
        //Arrange
        String ISBN = "9781501110368";
        Book mockBookBorrowed = mock(Book.class);
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBookBorrowed);
        when(mockBookBorrowed.isBorrowed()).thenReturn(true);

        //Act
        library.returnBook(ISBN);

        //Assert
        verify(mockBookBorrowed).returnBook();
        verify(mockDatabaseService).returnBook(ISBN);
    }

    @Test
    public void givenInvalidISBN_whenReturnBook_thenIllegalArgumentException() {
        //Arrange
        String invalidISBN = "978150111036";
        
        //Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.returnBook(invalidISBN);
        });

        //Assert
        assertEquals("Invalid ISBN.",thrown.getMessage());
        verify(mockDatabaseService, never()).getBookByISBN(invalidISBN);
        verify(mockDatabaseService, never()).returnBook(invalidISBN);
    }

    @Test
    public void givenNullBook_whenReturnBook_thenBookNotFoundException() {
        //Arrange
        String ISBN = "9781501110368";
        Book mockBookNull = null;
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBookNull);

        //Act
        BookNotFoundException thrown = assertThrows(BookNotFoundException.class, () -> {
            library.returnBook(ISBN);
        });

        //Assert
        assertEquals("Book not found!", thrown.getMessage());
        verify(mockDatabaseService, never()).returnBook(ISBN);
    }

    @Test
    public void givenNotBorrowedBook_whenReturnBook_thenBookNotBorrowedException() {
        //Arrange
        String ISBN = "9781501110368";
        Book mockBookNotBorrowed = mock(Book.class);
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBookNotBorrowed);
        when(mockBookNotBorrowed.isBorrowed()).thenReturn(false);

        //Act
        BookNotBorrowedException thrown = assertThrows(BookNotBorrowedException.class, () -> {
            library.returnBook(ISBN);
        });

        //Assert
        assertEquals("Book wasn't borrowed!", thrown.getMessage());
        verify(mockBookNotBorrowed, never()).returnBook();
        verify(mockDatabaseService, never()).returnBook(ISBN);
    }

    @Test
    public void givenValidISBNAndUserID_whenNotifyUserWithBookReviews_thenUserNotified() {
        //Arrange
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

        //Act
        assertDoesNotThrow(() -> library.notifyUserWithBookReviews(ISBN, userId));

        //Assert
        verify(mockReviewService).close();
        verify(mockUser, times(1)).sendNotification(notificationMessage);
    }

    @Test
    public void givenNullUserId_whenNotifyUserWithBookReviews_thenIllegalArgumentException() {
        //Arrange
        String ISBN = "9781501110368";
        String nullUserId = null;

        //Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.notifyUserWithBookReviews(ISBN, nullUserId);
        });

        //Assert
        assertEquals("Invalid user Id.", thrown.getMessage());
        verify(mockDatabaseService, never()).getBookByISBN(ISBN);
        verify(mockDatabaseService, never()).getUserById(nullUserId);
        verify(mockReviewService, never()).getReviewsForBook(ISBN);
        verify(mockReviewService, never()).close();
    }

    @ParameterizedTest
    @ValueSource(strings = { "10203040506" , "1020304050601" })
    public void givenInvalidUserId_whenNotifyUserWithBookReviews_thenIllegalArgumentException(String invalidUserId) {
        //Arrange
        String ISBN = "9781501110368";

        //Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.notifyUserWithBookReviews(ISBN, invalidUserId);
        });

        //Assert
        assertEquals("Invalid user Id.", thrown.getMessage());
        verify(mockDatabaseService, never()).getBookByISBN(ISBN);
        verify(mockDatabaseService, never()).getUserById(invalidUserId);
        verify(mockReviewService, never()).getReviewsForBook(ISBN);
        verify(mockReviewService, never()).close();
    }

    @Test
    public void givenInvalidISBN_whenNotifyUserWithBookReviews_thenIllegalArgumentException() {
        //Arrange
        String invalidISBN = "978150111036";
        String userId = "102030405060";

        //Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.notifyUserWithBookReviews(invalidISBN, userId);
        });

        //Assert
        assertEquals("Invalid ISBN.", thrown.getMessage());
        verify(mockDatabaseService, never()).getBookByISBN(invalidISBN);
        verify(mockDatabaseService, never()).getUserById(userId);
        verify(mockReviewService, never()).getReviewsForBook(invalidISBN);
        verify(mockReviewService, never()).close();
    }


    @Test
    public void givenNullBook_whenNotifyUserWithBookReviews_thenBookNotFoundException() {
        //Arrange
        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBookNull = null;
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBookNull);

        //Act
        BookNotFoundException thrown = assertThrows(BookNotFoundException.class, () -> {
            library.notifyUserWithBookReviews(ISBN, userId);
        });

        //Assert
        assertEquals("Book not found!", thrown.getMessage());
        verify(mockDatabaseService, never()).getUserById(userId);
        verify(mockReviewService, never()).getReviewsForBook(ISBN);
        verify(mockReviewService, never()).close();
    }

    @Test
    public void givenNullUser_whenNotifyUserWithBookReviews_thenUserNotRegisteredException() {
        //Arrange
        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBook = mock(Book.class);
        User mockUserNull = null;
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUserNull);

        //Act
        UserNotRegisteredException thrown = assertThrows(UserNotRegisteredException.class, () -> {
            library.notifyUserWithBookReviews(ISBN, userId);
        });

        //Assert
        assertEquals("User not found!", thrown.getMessage());
        verify(mockReviewService, never()).getReviewsForBook(ISBN);
        verify(mockReviewService, never()).close();
    }

    @Test
    public void givenNullReviews_whenNotifyUserWithBookReviews_thenNoReviewsFoundException() {
        //Arrange
        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBook = mock(Book.class);
        User mockUser = mock(User.class);
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        when(mockReviewService.getReviewsForBook(ISBN)).thenReturn(null);

        //Act
        NoReviewsFoundException thrown = assertThrows(NoReviewsFoundException.class, () -> {
            library.notifyUserWithBookReviews(ISBN, userId);
        });

        //Assert
        assertEquals("No reviews found!", thrown.getMessage());
        verify(mockReviewService).close();
        verify(mockUser, never()).sendNotification(anyString());

    }

    @Test
    public void givenEmptyReviews_whenNotifyUserWithBookReviews_thenNoReviewsFoundException() {
        //Arrange
        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBook = mock(Book.class);
        User mockUser = mock(User.class);
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        List<String> mockReviews = new ArrayList<>();
        List<String> spyReviewsEmpty = spy(mockReviews);
        when(mockReviewService.getReviewsForBook(ISBN)).thenReturn(spyReviewsEmpty);

        //Act
        NoReviewsFoundException thrown = assertThrows(NoReviewsFoundException.class, () -> {
            library.notifyUserWithBookReviews(ISBN, userId);
        });

        //Assert
        assertEquals("No reviews found!", thrown.getMessage());
        verify(mockReviewService).close();
        verify(mockUser, never()).sendNotification(anyString());
    }


    @Test
    public void givenReviewServiceUnavailable_whenNotifyUserWithBookReviews_thenReviewServiceUnavailableException() {
        //Arrange
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

        //Act
        ReviewServiceUnavailableException thrown = assertThrows(ReviewServiceUnavailableException.class, () -> {
            library.notifyUserWithBookReviews(ISBN, userId);
        });

        //Assert
        assertEquals("Review service unavailable!", thrown.getMessage());
        verify(mockReviewService).close();
        verify(mockUser, never()).sendNotification(anyString());
    }

    @Test
    public void givenSendNotificationsFails5Times_whenNotifyUserWithBookReviews_thenNotificationException() {
        //Arrange
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

        //Act
        NotificationException thrown = assertThrows(NotificationException.class, () -> {
            library.notifyUserWithBookReviews(ISBN, userId);
        });

        //Assert
        assertEquals("Notification failed!", thrown.getMessage());
        verify(mockReviewService).close();
        verify(mockUser, times(5)).sendNotification(notificationMessage);
    }

    @Test
    public void givenValidISBNAndUserID_whenGetBookByISBN_thenBookByISBNFound(){
        //Arrange
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

        //Act
        Book resultBook = library.getBookByISBN(ISBN, userId);

        //Assert
        assertEquals("It ends with us" ,resultBook.getTitle());
    }

    @Test
    public void givenInvalidISBN_whenGetBookByISBN_thenIllegalArgumentException(){
        //Arrange
        String invalidISBN = "978150111036";
        String userId = "102030405060";

        //Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.getBookByISBN(invalidISBN, userId);
        });

        //Assert
        assertEquals("Invalid ISBN.", thrown.getMessage());
        verify(mockDatabaseService, never()).getBookByISBN(invalidISBN);
    }

    @Test
    public void givenNullUserID_whenGetBookByISBN_thenIllegalArgumentException() {
        //Arrange
        String ISBN = "9781501110368";
        String nullUserId = null;
        
        //Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.getBookByISBN(ISBN, nullUserId);
        });

        //Assert
        assertEquals("Invalid user Id.", thrown.getMessage());
        verify(mockDatabaseService, never()).getBookByISBN(ISBN);
    }

    @ParameterizedTest
    @ValueSource(strings = { "10203040506" , "1020304050601" })
    public void givenInvalidUserID_whenGetBookByISBN_thenIllegalArgumentException(String invalidUserId) {
        //Arrange
        String ISBN = "9781501110368";
        
        //Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            library.getBookByISBN(ISBN, invalidUserId);
        });

        //Assert
        assertEquals("Invalid user Id.", thrown.getMessage());
        verify(mockDatabaseService, never()).getBookByISBN(ISBN);
    }


    @Test
    public void givenNullBook_whenGetBookByISBN_thenBookNotFoundException() {
        //Arrange
        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBookNull = null;
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBookNull);

        //Act
        BookNotFoundException thrown = assertThrows(BookNotFoundException.class, () -> {
            library.getBookByISBN(ISBN, userId);
        });

        //Assert
        assertEquals("Book not found!", thrown.getMessage());
    }

    @Test
    public void givenBookAlreadyBorrowed_whenGetBookByISBN_thenBookAlreadyBorrowedException(){
        //Arrange
        String ISBN = "9781501110368";
        String userId = "102030405060";
        Book mockBookBorrowed = mock(Book.class);
        when(mockBookBorrowed.getTitle()).thenReturn("It ends with us");
        User mockUser = mock(User.class);
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBookBorrowed);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        when(mockBookBorrowed.isBorrowed()).thenReturn(true);

        //Act
        BookAlreadyBorrowedException thrown = assertThrows(BookAlreadyBorrowedException.class, () -> {
            library.getBookByISBN(ISBN, userId);
        });

        //Assert
        assertEquals("Book was already borrowed!", thrown.getMessage());
    }


    @Test
    public void givenUserNotificationFailed_whenGetBookByISBN_thenBookByISBNFound(){
        //Arrange
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
        List<String> spyReviewsEmpty = spy(mockReviews);
        when(mockReviewService.getReviewsForBook(ISBN)).thenReturn(spyReviewsEmpty);

        //Act
        Book resultBook = library.getBookByISBN(ISBN, userId);

        //Assert
        assertEquals("It ends with us", resultBook.getTitle());

    }
}
