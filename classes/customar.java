package classes;

import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class customar extends person {
    Date membe_ship_date;
    Date Exp_DAte;
    boolean isActive;
    List<String> borrowedBooks = new ArrayList<>();

    public customar(Date membe_ship_date, Date exp_DAte) {
        this.membe_ship_date = membe_ship_date;
        this.Exp_DAte = exp_DAte;
        this.isActive = exp_DAte.after(new Date(System.currentTimeMillis()));
    }

    public void setMembe_ship_date(Date membe_ship_date) {
        this.membe_ship_date = membe_ship_date;
    }

    public Date getMembe_ship_date() {
        return membe_ship_date;
    }

    public void setExp_DAte(Date exp_DAte) {
        Exp_DAte = exp_DAte;
    }

    public Date getExp_DAte() {
        return Exp_DAte;
    }
    public void borrowd_book(String book_title){
        if(isActive){
            borrowedBooks.add(book_title);
            System.out.println("borrowd book"+book_title);
        }
        else {
            System.out.println("not found");
        }

    }
    public void return_book(String title_book) {
        if (borrowedBooks.contains(title_book)) {
            borrowedBooks.remove(title_book);
            System.out.println("the book return " + title_book);
        } else {
            System.out.println("book not found");
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
    }



