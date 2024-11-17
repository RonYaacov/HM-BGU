public class Main{
    public static void main(String[] args) {
        LibrarySystem library = new LibrarySystem();

        Book book1 = new Book(1, "Software System Analysis for Beginners", "Chen Amir", "Programming", true);
        Book book2 = new Book(2, "Design Patterns", "Gulliver Yaacov", "Programming",true);
        library.addBook(book1);
        library.addBook(book2);

        User user = new User(1, "ron_yaacov", "password123");
        library.registerUser(user);

        User authenticatedUser = library.authenticateUser("ron_yaacov", "password123");
        library.borrowBook(authenticatedUser, book1);
        library.borrowBook(authenticatedUser, book1);
        library.returnBook(authenticatedUser, book1);
    }

}