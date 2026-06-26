package model;

public class ReadingSession {
    private int bookId;
    private String date;
    private int pagesRead;
    private int minutesSpent;

    public ReadingSession(int bookId, String date, int pagesRead, int minutesSpent) {
        if (pagesRead <= 0)
            throw new IllegalArgumentException("Pages read must be positive.");
        if (minutesSpent <= 0)
            throw new IllegalArgumentException("Time spent must be positive.");
        this.bookId = bookId;
        this.date = date;
        this.pagesRead = pagesRead;
        this.minutesSpent = minutesSpent;
    }

    public int getBookId() { return bookId; }
    public String getDate() { return date; }
    public int getPagesRead() { return pagesRead; }
    public int  getMinutesSpent() { return minutesSpent; }

    public double getPagesPerHour() {
        return (minutesSpent > 0) ? (pagesRead * 60.0) / minutesSpent : 0;
    }

    @Override
    public String toString() {
        return String.format("  Date: %-12s | Pages: %4d | Time: %3d min | Speed: %.1f pg/hr",
                date, pagesRead, minutesSpent, getPagesPerHour());
    }
}
