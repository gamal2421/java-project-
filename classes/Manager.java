package classes;

import java.util.ArrayList;

public class Manager extends Staff {
    private ArrayList<Librarian> librarians = new ArrayList<>();

    public void addLibrarian(Librarian librarian) {
        librarians.add(librarian);
    }

    public void deleteLibrarian(Librarian librarian) {
        librarians.remove(librarian);
    }

    public ArrayList<Librarian> getLibrarians() {
        return librarians;
    }
}
