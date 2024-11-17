import java.time.LocalDate;

public class Loan {
    private int loanID;
    private User user;
    private Book book;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private static int loanCounter = 0;

    public Loan(User user, Book book, LocalDate loanDate, LocalDate returnDate) {
        this.loanID = loanCounter++;
        this.user = user;
        this.book = book;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }

    public void open() {
        book.updateAvailbility();
    }

    public Book getBook() {
        return book;
    }

    public void close() {
        book.updateAvailbility();
        returnDate = LocalDate.now();
        user.getBorrowedBooks().remove(book);
    }
}
