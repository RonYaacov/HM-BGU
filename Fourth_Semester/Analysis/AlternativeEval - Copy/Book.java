public class Book {
    private int bookID;
    private String title;
    private String author;
    private String category;
    private boolean availability;

    public Book(int bookID, String title, String author, String category, boolean availability) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.category = category;
        this.availability = availability;
    }

    public void updateAvailbility() {
        this.availability = !this.availability;
    }

    public String getCategory() {
        return category;
    }

    public String getDetails() {
        return title + " by " + author + " (ID: " + bookID + ")";
    }

    public boolean isAvalible() {
        return availability;
    }
    
}
