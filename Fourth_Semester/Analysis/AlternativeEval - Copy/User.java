import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {
    private int userID;
    private String username;
    private String password;
    private List<Book> borrowedBooks;
    private List<Loan> activeLoans;
    private List<Book> waitingList;

    public User(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.borrowedBooks = new ArrayList<>();
        this.activeLoans = new ArrayList<>();
        this.waitingList = new ArrayList<>();
    }

    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public int getUserID() {
        return userID;
    }

    public Loan borrowBook(Book book) {
        if (book.isAvalible()) {
            borrowedBooks.add(book);
            Loan loan = new Loan(this, book, LocalDate.now(), null);
            activeLoans.add(loan);
            loan.open();
            return loan;
        }
        return null;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public Loan returnBook(Book book) {
        for (Loan loan : activeLoans) {
            if (loan.getBook().equals(book)) {
                loan.close();
                activeLoans.remove(loan);
                return loan;
            }
        }
        return null;
    }

    public void addToWaitingList(Book book) {
        waitingList.add(book);
    }

    public List<Book> getWaitingList() {
        return waitingList;
    }
}
