package service;

import model.AudioBook;
import model.Book;
import model.PhysicalBook;
import model.ReadingSession;
import model.ReadingStatus;

import java.time.LocalDate;

public class BookService {

    // ── Fixed-size arrays (syllabus requirement) ───────────────────────────
    private static final int MAX_BOOKS = 100;
    private static final int MAX_SESSIONS = 500;

    private Book[] books;
    private ReadingSession[] sessions;
    private int bookCount;
    private int sessionCount;
    private int nextBookId;

    public BookService() {
        books = new Book[MAX_BOOKS];
        sessions = new ReadingSession[MAX_SESSIONS];
        bookCount = 0;
        sessionCount = 0;
        nextBookId = 1;
    }

    // ── ADD ────────────────────────────────────────────────────────────────
    public void addPhysicalBook(String title, String author, String genre, int totalPages) {
        if (bookCount >= MAX_BOOKS)
            throw new IllegalStateException("Library full. Cannot add more books.");
        if (findBookByTitle(title) != null)
            throw new IllegalArgumentException("A book with that title already exists.");
        String today = LocalDate.now().toString();
        books[bookCount++] = new PhysicalBook(nextBookId++, title, author, genre, totalPages, today);
        System.out.println("Physical book added successfully!");
    }

    public void addAudioBook(String title, String author, String genre, int totalDuration) {
        if (bookCount >= MAX_BOOKS)
            throw new IllegalStateException("Library full. Cannot add more books.");
        if (findBookByTitle(title) != null)
            throw new IllegalArgumentException("A book with that title already exists.");
        String today = LocalDate.now().toString();
        books[bookCount++] = new AudioBook(nextBookId++, title, author, genre, totalDuration, today);
        System.out.println("Audiobook added successfully!");
    }

    // ── VIEW ALL ───────────────────────────────────────────────────────────
    public void listAllBooks() {
        if (bookCount == 0) { System.out.println("  No books in your library yet."); return; }
        printBookHeader();
        for (int i = 0; i < bookCount; i++) System.out.println("  " + books[i]);
    }

    // ── UPDATE ─────────────────────────────────────────────────────────────
    public Book getBookById(int id) {
        for (int i = 0; i < bookCount; i++)
            if (books[i].getId() == id) return books[i];
        return null;
    }

    public void updateBook(int id, String title, String author, String genre, int totalPages) {
        Book b = requireBook(id);
        if (title != null && !title.isEmpty()) b.setTitle(title);
        if (author != null && !author.isEmpty()) b.setAuthor(author);
        if (genre != null && !genre.isEmpty()) b.setGenre(genre);

        // totalPages / totalDuration only applies to the matching subtype
        if (totalPages > 0) {
            if (b instanceof PhysicalBook) {
                ((PhysicalBook) b).setTotalPages(totalPages);
            } else if (b instanceof AudioBook) {
                ((AudioBook) b).setTotalDuration(totalPages); // treated as duration when audiobook
            }
        }
        System.out.println("Book updated.");
    }

    // ── DELETE ─────────────────────────────────────────────────────────────
    public void deleteBook(int id) {
        int idx = -1;
        for (int i = 0; i < bookCount; i++)
            if (books[i].getId() == id) {
                idx = i;
                break;
            }
        if (idx == -1) throw new IllegalArgumentException("Book ID " + id + " not found.");
        // shift left
        for (int i = idx; i < bookCount - 1; i++) books[i] = books[i + 1];
        books[--bookCount] = null;
        // remove sessions for this book
        removeSessionsForBook(id);
        System.out.println("Book deleted.");
    }

    // ── PROGRESS ──────────────────────────────────────────────────────────
    public void updateProgress(int id, int progress) {
        Book b = requireBook(id);
        if (b instanceof PhysicalBook pb) {
            pb.setCurrentPage(progress);
            System.out.printf("✔  Progress updated: %d / %d pages (%.1f%%)%n",
                    progress, pb.getTotalPages(), pb.getProgressPercent());
        } else if (b instanceof AudioBook ab) {
            ab.setCurrentDuration(progress);
            System.out.printf("✔  Progress updated: %d / %d minutes (%.1f%%)%n",
                    progress, ab.getTotalDuration(), ab.getProgressPercent());
        }
        if (b.getStatus() == ReadingStatus.COMPLETED)
            System.out.println("Congratulations! You've completed this book!");
    }

    public void updateStatus(int id, ReadingStatus status) {
        requireBook(id).setStatus(status);
        System.out.println("✔  Status updated to: " + status);
    }

    // ── SESSIONS ──────────────────────────────────────────────────────────
    public void logSession(int bookId, String date, int pages, int minutes) {
        requireBook(bookId);
        if (sessionCount >= MAX_SESSIONS)
            throw new IllegalStateException("Session log is full.");
        sessions[sessionCount++] = new ReadingSession(bookId, date, pages, minutes);
        System.out.println("✔  Reading session logged.");
    }

    public void viewSessions(int bookId) {
        Book b = requireBook(bookId);
        System.out.println("\n  Sessions for: " + b.getTitle());
        boolean found = false;
        for (int i = 0; i < sessionCount; i++) {
            if (sessions[i].getBookId() == bookId) {
                System.out.println(sessions[i]);
                found = true;
            }
        }
        if (!found)
            System.out.println("  No sessions logged for this book.");
    }

    // ── SEARCH ────────────────────────────────────────────────────────────
    public void searchByTitle(String keyword) {
        keyword = keyword.toLowerCase();
        boolean found = false;
        printBookHeader();
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getTitle().toLowerCase().contains(keyword)) {
                System.out.println("  " + books[i]);
                found = true;
            }
        }
        if (!found) System.out.println("  No books found matching \"" + keyword + "\".");
    }

    public void searchByAuthor(String keyword) {
        keyword = keyword.toLowerCase();
        boolean found = false;
        printBookHeader();
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getAuthor().toLowerCase().contains(keyword)) {
                System.out.println("  " + books[i]);
                found = true;
            }
        }
        if (!found)
            System.out.println("  No books found by that author.");
    }

    public void filterByStatus(ReadingStatus status) {
        boolean found = false;
        printBookHeader();
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getStatus() == status) {
                System.out.println("  " + books[i]);
                found = true;
            }
        }
        if (!found)
            System.out.println("  No books with status: " + status);
    }

    public void filterByGenre(String genre) {
        genre = genre.toLowerCase();
        boolean found = false;
        printBookHeader();
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getGenre().toLowerCase().contains(genre)) {
                System.out.println("  " + books[i]);
                found = true;
            }
        }
        if (!found)
            System.out.println("  No books found in genre: " + genre);
    }

    // ── STATISTICS ────────────────────────────────────────────────────────
    public void showStatistics() {
        int completed = 0, reading = 0, notStarted = 0, onHold = 0, dropped = 0;
        int totalPagesRead = 0;

        for (int i = 0; i < bookCount; i++) {
            Book b = books[i];
            // Sum current progress across both subtypes
            if (b instanceof PhysicalBook pb) {
                totalPagesRead += pb.getCurrentPage();
            } else if (b instanceof AudioBook ab) {
                totalPagesRead += ab.getCurrentDuration();
            }
            switch (b.getStatus()) {
                case COMPLETED   -> completed++;
                case READING     -> reading++;
                case NOT_STARTED -> notStarted++;
                case ON_HOLD     -> onHold++;
                case DROPPED     -> dropped++;
            }
        }

        int totalMinutes = 0;
        int totalSessionPages = 0;
        for (int i = 0; i < sessionCount; i++) {
            totalMinutes      += sessions[i].getMinutesSpent();
            totalSessionPages += sessions[i].getPagesRead();
        }
        double avgSpeed = (totalMinutes > 0) ? (totalSessionPages * 60.0 / totalMinutes) : 0;

        System.out.println("\n  ┌─────────────────────────────────────────┐");
        System.out.println("  │            READING STATISTICS           │");
        System.out.println("  ├─────────────────────────────────────────┤");
        System.out.printf( "  │  Total Books in Library  : %3d          │%n", bookCount);
        System.out.printf( "  │  Completed               : %3d          │%n", completed);
        System.out.printf( "  │  Currently Reading       : %3d          │%n", reading);
        System.out.printf( "  │  Not Started             : %3d          │%n", notStarted);
        System.out.printf( "  │  On Hold                 : %3d          │%n", onHold);
        System.out.printf( "  │  Dropped                 : %3d          │%n", dropped);
        System.out.println("  ├─────────────────────────────────────────┤");
        System.out.printf( "  │  Total Progress Units    : %5d        │%n", totalPagesRead);
        System.out.printf( "  │  Total Sessions Logged   : %3d          │%n", sessionCount);
        System.out.printf( "  │  Total Reading Time      : %4d min     │%n", totalMinutes);
        System.out.printf( "  │  Avg Reading Speed       : %6.1f pg/hr│%n", avgSpeed);
        System.out.println("  └─────────────────────────────────────────┘");
    }

    // ── RATINGS & REVIEWS ─────────────────────────────────────────────────
    public void rateBook(int id, int rating) {
        requireBook(id).setRating(rating);
        System.out.println("✔  Rating saved.");
    }

    public void reviewBook(int id, String review) {
        requireBook(id).setReview(review);
        System.out.println("✔  Review saved.");
    }

    public void showBookDetail(int id) {
        Book b = requireBook(id);
        System.out.println("\n  ══════════════════════════════════════════");
        System.out.println("  Title   : " + b.getTitle());
        System.out.println("  Author  : " + b.getAuthor());
        System.out.println("  Genre   : " + b.getGenre());
        System.out.println("  Type    : " + (b instanceof AudioBook ? "Audiobook" : "Physical Book"));

        if (b instanceof PhysicalBook pb) {
            System.out.printf("  Pages   : %d / %d  (%.1f%%)%n",
                    pb.getCurrentPage(), pb.getTotalPages(), pb.getProgressPercent());
        } else if (b instanceof AudioBook ab) {
            System.out.printf("  Duration: %d / %d min  (%.1f%%)%n",
                    ab.getCurrentDuration(), ab.getTotalDuration(), ab.getProgressPercent());
        }

        System.out.println("  Status  : " + b.getStatus());
        System.out.println("  Rating  : " + b.getStars());
        System.out.println("  Added   : " + b.getDateAdded());
        if (!b.getReview().isEmpty())
            System.out.println("  Review  : " + b.getReview());
        System.out.println("  ══════════════════════════════════════════");
    }

    // ── HELPERS ───────────────────────────────────────────────────────────
    public int getBookCount() { return bookCount; }

    private Book requireBook(int id) {
        Book b = getBookById(id);
        if (b == null)
            throw new IllegalArgumentException("Book ID " + id + " not found.");
        return b;
    }

    private Book findBookByTitle(String title) {
        for (int i = 0; i < bookCount; i++)
            if (books[i].getTitle().equalsIgnoreCase(title.trim())) return books[i];
        return null;
    }

    private void removeSessionsForBook(int bookId) {
        int newCount = 0;
        ReadingSession[] temp = new ReadingSession[MAX_SESSIONS];
        for (int i = 0; i < sessionCount; i++)
            if (sessions[i].getBookId() != bookId) temp[newCount++] = sessions[i];
        sessions     = temp;
        sessionCount = newCount;
    }

    private void printBookHeader() {
        System.out.println("  " + "-".repeat(80));
        System.out.printf("  %-4s %-30s | %-20s | %-12s | %-11s | %s%n",
                "ID", "Title", "Author", "Genre", "Status", "Progress");
        System.out.println("  " + "-".repeat(80));
    }
}