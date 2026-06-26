package model;

public class WishlistItem {
    private int id;
    private String title;
    private String author;
    private String genre;
    private String note;

    public WishlistItem(int id, String title, String author, String genre, String note) {
        if (title == null || title.trim().isEmpty())
            throw new IllegalArgumentException("Title cannot be empty.");
        if (author == null || author.trim().isEmpty())
            throw new IllegalArgumentException("Author cannot be empty.");
        this.id = id;
        this.title = title.trim();
        this.author = author.trim();
        this.genre = (genre == null || genre.trim().isEmpty()) ? "General" : genre.trim();
        this.note = (note  == null) ? "" : note.trim();
    }

    public int    getId()     { return id; }
    public String getTitle()  { return title; }
    public String getAuthor() { return author; }
    public String getGenre()  { return genre; }
    public String getNote()   { return note; }

    public void setNote(String note) {
        this.note = (note == null) ? "" : note.trim();
    }

    @Override
    public String toString() {
        return String.format("[%d] %-30s | %-20s | %-12s%s",
                id, title, author, genre, note.isEmpty() ? "" : " | Note: " + note);
    }
}
