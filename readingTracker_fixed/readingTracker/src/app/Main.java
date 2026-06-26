package app;

import model.AudioBook;
import model.Book;
import model.PhysicalBook;
import model.ReadingStatus;
import service.BookService;
import service.InputHelper;
import service.WishlistService;

import java.util.Scanner;

public class Main {

    private static final BookService bookService    = new BookService();
    private static final WishlistService wishlistService = new WishlistService();
    private static final Scanner sc = new Scanner(System.in);
    private static final InputHelper input = new InputHelper(sc);

    public static void main(String[] args) {
        printBanner();
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = input.readIntInRange("  Enter choice: ", 1, 9);
            System.out.println();
            switch (choice) {
                case 1 -> manageBooks();
                case 2 -> updateProgress();
                case 3 -> logReadingSession();
                case 4 -> searchAndFilter();
                case 5 -> bookService.showStatistics();
                case 6 -> viewWishlist();
                case 7 -> addToWishlist();
                case 8 -> ratingsAndReviews();
                case 9 -> {
                    System.out.println("  Goodbye! Happy Reading! 📖");
                    running = false;
                }
            }
            if (running) pause();
        }
        sc.close();
    }

    // ══════════════════════════════════════════════════════════════════════
    //  1. MANAGE BOOKS
    // ══════════════════════════════════════════════════════════════════════
    private static void manageBooks() {
        System.out.println("  ┌── Manage Books ──────────────────────┐");
        System.out.println("  │  1. Add Book                         │");
        System.out.println("  │  2. View All Books                   │");
        System.out.println("  │  3. View Book Details                │");
        System.out.println("  │  4. Update Book                      │");
        System.out.println("  │  5. Delete Book                      │");
        System.out.println("  │  6. Back                             │");
        System.out.println("  └──────────────────────────────────────┘");
        int choice = input.readIntInRange("  Enter choice: ", 1, 6);
        System.out.println();
        switch (choice) {
            case 1 -> addBook();
            case 2 -> bookService.listAllBooks();
            case 3 -> viewBookDetail();
            case 4 -> editBook();
            case 5 -> deleteBook();
            case 6 -> { /* back */ }
        }
    }

    private static void addBook() {
        System.out.println("  ── Add New Book ──────────────────────");
        try {
            String title  = input.readString ("  Title      : ");
            String author = input.readString ("  Author     : ");
            String genre  = input.readOptional("  Genre (Enter to skip): ");
            char type  = input.readChar("  Enter P for physical book, E for audiobook: ");
            if (Character.toUpperCase(type) == 'P') {
                int pages = input.readPositiveInt("  Total Pages: ");
                bookService.addPhysicalBook(title, author, genre, pages);
            } else if (Character.toUpperCase(type) == 'E') {
                int totalDuration = input.readPositiveInt("  Total Duration (minutes): ");
                bookService.addAudioBook(title, author, genre, totalDuration);
            } else {
                System.out.println("  ⚠  Invalid type. Please enter P or E.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("  ⚠  " + e.getMessage());
        }
    }

    private static void viewBookDetail() {
        bookService.listAllBooks();
        if (bookService.getBookCount() == 0) return;
        int id = input.readPositiveInt("\n  Enter Book ID: ");
        try { bookService.showBookDetail(id); }
        catch (IllegalArgumentException e) { System.out.println("  ⚠  " + e.getMessage()); }
    }

    private static void editBook() {
        bookService.listAllBooks();
        if (bookService.getBookCount() == 0) return;
        int id = input.readPositiveInt("\n  Enter Book ID to update: ");
        Book b = bookService.getBookById(id);
        if (b == null) { System.out.println("  ⚠  Book not found."); return; }
        System.out.println("  Leave blank to keep existing value.");
        try {
            String title  = input.readOptional("  New Title  [" + b.getTitle()  + "]: ");
            String author = input.readOptional("  New Author [" + b.getAuthor() + "]: ");
            String genre  = input.readOptional("  New Genre  [" + b.getGenre()  + "]: ");
            String sizeLabel;
            int currentSize;
            if (b instanceof AudioBook ab) {
                sizeLabel   = "New Total Duration (minutes)";
                currentSize = ab.getTotalDuration();
            } else {
                sizeLabel   = "New Total Pages";
                currentSize = ((model.PhysicalBook) b).getTotalPages();
            }
            String pagesStr = input.readOptional("  " + sizeLabel + " [" + currentSize + "]: ");
            int pages = pagesStr.isEmpty() ? 0 : Integer.parseInt(pagesStr);
            bookService.updateBook(id, title, author, genre, pages);
        } catch (IllegalArgumentException e) {
            System.out.println("  ⚠  " + e.getMessage());
        }
    }

    private static void deleteBook() {
        bookService.listAllBooks();
        if (bookService.getBookCount() == 0) return;
        int id = input.readPositiveInt("\n  Enter Book ID to delete: ");
        if (input.confirm("  Are you sure you want to delete this book?")) {
            try { bookService.deleteBook(id); }
            catch (IllegalArgumentException e) { System.out.println("  ⚠  " + e.getMessage()); }
        } else {
            System.out.println("  Deletion cancelled.");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  2. UPDATE PROGRESS
    // ══════════════════════════════════════════════════════════════════════
    private static void updateProgress() {
        bookService.listAllBooks();
        if (bookService.getBookCount() == 0) return;
        int id = input.readPositiveInt("\n  Enter Book ID: ");
        Book b = bookService.getBookById(id);
        if (b == null) { System.out.println("  ⚠  Book not found."); return; }

        System.out.println("  ── Update Progress ───────────────────");
        System.out.println("  1. Update current page");
        System.out.println("  2. Change reading status");
        int choice = input.readIntInRange("  Choice: ", 1, 2);
        System.out.println();

        if (choice == 1) {
            if (b instanceof AudioBook ab) {
                System.out.printf("  Current duration: %d / %d minutes%n", ab.getCurrentDuration(), ab.getTotalDuration());
                int minutes = input.readNonNegativeInt("  New current duration (minutes): ");
                try { bookService.updateProgress(id, minutes); }
                catch (IllegalArgumentException e) { System.out.println("  ⚠  " + e.getMessage()); }
            } else if (b instanceof PhysicalBook pb) {
                System.out.printf("  Current page: %d / %d%n", pb.getCurrentPage(), pb.getTotalPages());
                int page = input.readNonNegativeInt("  New current page: ");
                try { bookService.updateProgress(id, page); }
                catch (IllegalArgumentException e) { System.out.println("  ⚠  " + e.getMessage()); }
            }
        } else {
            printStatusMenu();
            int s = input.readIntInRange("  Choice: ", 1, 5);
            ReadingStatus[] statuses = ReadingStatus.values();
            try { bookService.updateStatus(id, statuses[s - 1]); }
            catch (IllegalArgumentException e) { System.out.println("  ⚠  " + e.getMessage()); }
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  3. LOG READING SESSION
    // ══════════════════════════════════════════════════════════════════════
    private static void logReadingSession() {
        bookService.listAllBooks();
        if (bookService.getBookCount() == 0) return;
        System.out.println("  ── Log Reading Session ───────────────");
        System.out.println("  1. Add session");
        System.out.println("  2. View sessions for a book");
        int choice = input.readIntInRange("  Choice: ", 1, 2);
        System.out.println();

        int id = input.readPositiveInt("  Enter Book ID: ");
        if (bookService.getBookById(id) == null) { System.out.println("  ⚠  Book not found."); return; }

        if (choice == 1) {
            try {
                Book sessionBook = bookService.getBookById(id);
                String date    = input.readDate("  Date (YYYY-MM-DD or Enter for today): ");
                int    progress = (sessionBook instanceof AudioBook)
                        ? input.readPositiveInt("  Minutes listened this session: ")
                        : input.readPositiveInt("  Pages read this session      : ");
                int    minutes = input.readPositiveInt("  Minutes spent               : ");
                bookService.logSession(id, date, progress, minutes);
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("  ⚠  " + e.getMessage());
            }
        } else {
            bookService.viewSessions(id);
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  4. SEARCH & FILTER
    // ══════════════════════════════════════════════════════════════════════
    private static void searchAndFilter() {
        System.out.println("  ── Search & Filter ───────────────────");
        System.out.println("  1. Search by Title");
        System.out.println("  2. Search by Author");
        System.out.println("  3. Filter by Status");
        System.out.println("  4. Filter by Genre");
        System.out.println("  5. Back");
        int choice = input.readIntInRange("  Choice: ", 1, 5);
        System.out.println();

        switch (choice) {
            case 1 -> {
                String kw = input.readString("  Enter title keyword: ");
                bookService.searchByTitle(kw);
            }
            case 2 -> {
                String kw = input.readString("  Enter author name: ");
                bookService.searchByAuthor(kw);
            }
            case 3 -> {
                printStatusMenu();
                int s = input.readIntInRange("  Choice: ", 1, 5);
                bookService.filterByStatus(ReadingStatus.values()[s - 1]);
            }
            case 4 -> {
                String g = input.readString("  Enter genre: ");
                bookService.filterByGenre(g);
            }
            case 5 -> { /* back */ }
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  6. VIEW WISHLIST
    // ══════════════════════════════════════════════════════════════════════
    private static void viewWishlist() {
        System.out.println("  ── Wishlist ──────────────────────────");
        wishlistService.viewWishlist();
        if (wishlistService.getCount() == 0) return;
        System.out.println("\n  1. Remove an item   2. Back");
        int c = input.readIntInRange("  Choice: ", 1, 2);
        if (c == 1) {
            int id = input.readPositiveInt("  Wishlist item ID to remove: ");
            try { wishlistService.removeItem(id); }
            catch (IllegalArgumentException e) { System.out.println("  ⚠  " + e.getMessage()); }
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  7. ADD TO WISHLIST
    // ══════════════════════════════════════════════════════════════════════
    private static void addToWishlist() {
        System.out.println("  ── Add to Wishlist ───────────────────");
        try {
            String title  = input.readString  ("  Title      : ");
            String author = input.readString  ("  Author     : ");
            String genre  = input.readOptional("  Genre (Enter to skip): ");
            String note   = input.readOptional("  Note  (Enter to skip): ");
            wishlistService.addItem(title, author, genre, note);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("  ⚠  " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  8. RATINGS & REVIEWS
    // ══════════════════════════════════════════════════════════════════════
    private static void ratingsAndReviews() {
        bookService.listAllBooks();
        if (bookService.getBookCount() == 0) return;
        int id = input.readPositiveInt("\n  Enter Book ID: ");
        if (bookService.getBookById(id) == null) { System.out.println("  ⚠  Book not found."); return; }

        System.out.println("\n  1. Rate book (1–5 stars)");
        System.out.println("  2. Write / update review");
        System.out.println("  3. View rating & review");
        int choice = input.readIntInRange("  Choice: ", 1, 3);
        System.out.println();

        switch (choice) {
            case 1 -> {
                int r = input.readIntInRange("  Rating (1–5): ", 1, 5);
                try { bookService.rateBook(id, r); }
                catch (IllegalArgumentException e) { System.out.println("  ⚠  " + e.getMessage()); }
            }
            case 2 -> {
                String rev = input.readString("  Your review: ");
                bookService.reviewBook(id, rev);
            }
            case 3 -> bookService.showBookDetail(id);
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  UI HELPERS
    // ══════════════════════════════════════════════════════════════════════
    private static void printBanner() {
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║         📚  READING  TRACKER  📚         ║");
        System.out.println("  ║     Keep track of every page you read    ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.println();
    }

    private static void printMainMenu() {
        System.out.println();
        System.out.println("  ┌────────────────────────────────────────┐");
        System.out.println("  │            MAIN MENU                   │");
        System.out.println("  ├────────────────────────────────────────┤");
        System.out.println("  │  1. Manage Books                       │");
        System.out.println("  │  2. Update Reading Progress            │");
        System.out.println("  │  3. Log Reading Session                │");
        System.out.println("  │  4. Search / Filter Books              │");
        System.out.println("  │  5. View Statistics                    │");
        System.out.println("  │  6. View Wishlist                      │");
        System.out.println("  │  7. Add to Wishlist                    │");
        System.out.println("  │  8. Ratings & Reviews                  │");
        System.out.println("  │  9. Exit                               │");
        System.out.println("  └────────────────────────────────────────┘");
    }

    private static void printStatusMenu() {
        System.out.println("  1. Not Started   2. Reading   3. Completed");
        System.out.println("  4. On Hold       5. Dropped");
    }

    private static void pause() {
        System.out.println("\n  Press Enter to continue...");
        sc.nextLine();
    }
}
