package classes;

import java.util.ArrayList;

public class Librarian extends staff {
    private ArrayList<LibraryItem> libraryItems = new ArrayList<>();
    private ArrayList<customar> customers = new ArrayList<>();

    public void addBooks(LibraryItem book) {
        if (!libraryItems.contains(book)) {
            libraryItems.add(book);
            System.out.println("Book added successfully.");
        } else {
            System.out.println("Book already exists.");
        }
    }

    public void deleteBooks(LibraryItem book) {
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

    public void addMember(customar Customer) {
        if (!customers.contains(Customer)) {
            customers.add(Customer);
            System.out.println("Member added successfully.");
        } else {
            System.out.println("Member already exists.");
        }
    }
}