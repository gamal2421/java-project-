package classes;

import java.util.ArrayList;

public class Librarian extends Staff {
    private String email;
    private String password;
    private ArrayList<LibraryItem> libraryItems = new ArrayList<>();
    private ArrayList<Customer> customers = new ArrayList<>();

    public Librarian(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getter and Setter for email and password
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addBook(LibraryItem book) {
        if (!libraryItems.contains(book)) {
            libraryItems.add(book);
            System.out.println("Book added successfully.");
        } else {
            System.out.println("Book already exists.");
        }
    }

    public void deleteBook(LibraryItem book) {
        if (libraryItems.contains(book)) {
            libraryItems.remove(book);
            System.out.println("Book removed successfully.");
        } else {
            System.out.println("Book not found.");
        }
    }

    public ArrayList<LibraryItem> viewBooks() {
        System.out.println("Viewing all books:");
        for (LibraryItem book : libraryItems) {
            System.out.println("- " + book.getTitle());
        }
        return libraryItems;
    }

    public void addMember(Customer customer) {
        if (!customers.contains(customer)) {
            customers.add(customer);
            System.out.println("Member added successfully.");
        } else {
            System.out.println("Member already exists.");
        }
    }

    // Additional librarian-specific operations can be added here
}
