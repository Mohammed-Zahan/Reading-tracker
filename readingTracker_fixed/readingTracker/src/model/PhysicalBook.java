package model;

public class PhysicalBook extends Book{
    private int totalPages;
    private int currentPage;

    public PhysicalBook (int id, String title, String author, String genre, int totalPages, String dateAdded) {
        super(id, title, author, genre, dateAdded);
        if (totalPages <= 0)
            throw new IllegalArgumentException("Total pages must be positive.");

        this.totalPages = totalPages;
        this.currentPage = 0;
    }

    public int getTotalPages() { return totalPages; }
    public int getCurrentPage() { return currentPage; }

    public void setTotalPages(int p)    {
        if (p <= 0) throw new IllegalArgumentException("Total pages must be positive.");
        this.totalPages = p;
    }
    public void setCurrentPage(int p)   {
        if (p < 0 || p > totalPages) throw new IllegalArgumentException("Page must be between 0 and " + totalPages + ".");
        this.currentPage = p;
        if (p == totalPages) this.status = ReadingStatus.COMPLETED;
        else if (p > 0)      this.status = ReadingStatus.READING;
    }

    public double getProgressPercent() {
        if (totalPages == 0) return 0;
        return (currentPage * 100.0) / totalPages;
    }

    @Override
    public String toString() {
        return String.format("[%d] %-30s | %-20s | %-12s | %s | %5.1f%%",
                id, title, author, genre, status, getProgressPercent());
    }
}
