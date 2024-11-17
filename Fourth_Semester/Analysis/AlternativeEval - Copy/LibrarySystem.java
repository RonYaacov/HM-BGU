import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LibrarySystem {
     private List<Book> books;
    private List<User> users;

    public LibrarySystem() {
        books = new ArrayList<>();
        users = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public void registerUser(User user) {
        users.add(user);
    }

    public User authenticateUser(String username, String password) {
        for (User user : users) {
            if (user.login(username, password)) {
                return user;
            }
        }
        return null;
    }

    public List<Book> searchBooks(String query) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getDetails().toLowerCase().contains(query.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    public void borrowBook(User user, Book book) {
        if (user != null && book.isAvalible()) {
            user.borrowBook(book);
            System.out.println("The book has been successfully borrowed.");
        } else {
            System.out.println("The book is currently not available.\nWould you like to be added to the waiting list? (Y/N)");
            try{
                Scanner scanner = new Scanner(System.in);
                String response = scanner.nextLine();
                if (response.equalsIgnoreCase("Y")) {
                    user.addToWaitingList(book);
                    System.out.println("You have been added to the waiting list.");
                }
                scanner.close(); 
            }
            catch(Exception e){
                System.out.println("An error occurred.");
            }
        }
    }

    public void returnBook(User user, Book book) {
        Loan loan = user.returnBook(book);
        if (loan != null) {
            System.out.println("The book has been successfully returned.");
        } else {
            System.out.println("The book was not found in the list of borrowed books.");
        }
        for(User otherUser : users){
            if(otherUser.getWaitingList().contains(book)){
                borrowBook(otherUser, book);
                otherUser.getWaitingList().remove(book);
                return;
            }
        }
    }

}
