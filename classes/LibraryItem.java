package classes;

public class LibraryItem {
    private int id;
    private String title;
    private String publisher;
    private String isbn;
    private String author;
    private int numberOfCopies;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getNumberOfCopies() {
        return numberOfCopies;
    }

    public void setNumberOfCopies(int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    public void updateNumberOfCopies(int newCopies) {
        this.numberOfCopies = newCopies;
    }

    public String getAvailabilityStatus() {
        return this.numberOfCopies > 0 ? "Available" : "Not Available";
    }
}
