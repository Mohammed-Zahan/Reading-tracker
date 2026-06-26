package model;

public class AudioBook extends Book{
    private int totalDuration;
    private int currentDuration;

    public AudioBook (int id, String title, String author, String genre, int totalDuration, String dateAdded) {
        super(id, title, author, genre, dateAdded);
        if (totalDuration <= 0)
            throw new IllegalArgumentException("Total pages must be positive.");

        this.totalDuration = totalDuration;
        this.currentDuration = 0;
    }

    public int getTotalDuration() { return totalDuration; }
    public int getCurrentDuration() { return currentDuration; }

    public void setTotalDuration(int p)    {
        if (p <= 0) throw new IllegalArgumentException("Total pages must be positive.");
        this.totalDuration = p;
    }
    public void setCurrentDuration(int p)   {
        if (p < 0 || p > totalDuration) throw new IllegalArgumentException("Page must be between 0 and " + totalDuration + ".");
        this.currentDuration = p;
        if (p == totalDuration) this.status = ReadingStatus.COMPLETED;
        else if (p > 0)      this.status = ReadingStatus.READING;
    }

    public double getProgressPercent() {
        if (totalDuration == 0) return 0;
        return (currentDuration * 100.0) / totalDuration;
    }

    @Override
    public String toString() {
        return String.format("[%d] %-30s | %-20s | %-12s | %s | %5.1f%%",
                id, title, author, genre, status, getProgressPercent());
    }
}
