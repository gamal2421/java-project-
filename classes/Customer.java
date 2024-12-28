package classes;

import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class Customer extends Person {
    private Date membershipDate;
    private Date expirationDate;
    private boolean isActive;
    private List<String> borrowedBooks;

    public Customer(Date membershipDate, Date expirationDate) {
        this.membershipDate = membershipDate;
        this.expirationDate = expirationDate;
        this.isActive = expirationDate.after(new Date(System.currentTimeMillis()));
        this.borrowedBooks = new ArrayList<>();
    }

    public Date getMembershipDate() {
        return membershipDate;
    }

    public void setMembershipDate(Date membershipDate) {
        this.membershipDate = membershipDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
        this.isActive = expirationDate.after(new Date(System.currentTimeMillis()));
    }

    public boolean isActive() {
        return isActive;
    }

    public void borrowBook(String bookTitle) {
        if (isActive) {
            if (!borrowedBooks.contains(bookTitle)) {
                borrowedBooks.add(bookTitle);
                System.out.println("Borrowed book: " + bookTitle);
            } else {
                System.out.println("Book already borrowed: " + bookTitle);
            }
        } else {
            System.out.println("Membership expired. Cannot borrow books.");
        }
    }

    public void returnBook(String bookTitle) {
        if (borrowedBooks.remove(bookTitle)) {
            System.out.println("Returned book: " + bookTitle);
        } else {
            System.out.println("Book not found in borrowed list: " + bookTitle);
        }
    }

    public void viewBorrowedBooks() {
        if (borrowedBooks.isEmpty()) {
            System.out.println("No books borrowed.");
        } else {
            System.out.println("Borrowed Books:");
            for (String book : borrowedBooks) {
                System.out.println(" ---> " + book);
            }
        }
    }

    @Override
    public String toString() {
        return "Customer{" +
                "membershipDate=" + membershipDate +
                ", expirationDate=" + expirationDate +
                ", isActive=" + isActive +
                ", borrowedBooks=" + borrowedBooks +
                '}';
    }
}
