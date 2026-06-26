package model;

public abstract class Book {
    protected int id;
    protected String title;
    protected String author;
    protected String genre;
    protected ReadingStatus status;
    protected int rating;          // 0 = not rated, out of 5
    protected String review;
    protected String dateAdded;

    public Book(int id, String title, String author, String genre, String dateAdded) {
        if (title == null || title.trim().isEmpty())
            throw new IllegalArgumentException("Title cannot be empty.");
        if (author == null || author.trim().isEmpty())
            throw new IllegalArgumentException("Author cannot be empty.");


        this.id = id;
        this.title = title.trim();
        this.author = author.trim();
        this.genre = (genre == null || genre.trim().isEmpty()) ? "General" : genre.trim();
        this.status = ReadingStatus.NOT_STARTED;
        this.rating = 0;
        this.review = "";
        this.dateAdded = dateAdded;
    }

    // ── Getters ──────────────────────────────────────────────────────────────
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public ReadingStatus getStatus() { return status; }
    public int getRating() { return rating; }
    public String getReview() { return review; }
    public String getDateAdded() { return dateAdded; }

    // ── Setters ──────────────────────────────────────────────────────────────
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) throw new IllegalArgumentException("Title cannot be empty.");
        this.title = title.trim();
    }
    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty()) throw new IllegalArgumentException("Author cannot be empty.");
        this.author = author.trim();
    }
    public void setGenre(String genre)  {
        this.genre  = (genre == null || genre.trim().isEmpty()) ? "General" : genre.trim();
    }

    public void setStatus(ReadingStatus s) {
        this.status = s;
    }
    public void setRating(int r) {
        if (r < 1 || r > 5) throw new IllegalArgumentException("Rating must be between 1 and 5.");
        this.rating = r;
    }
    public void setReview(String review) {
        this.review = (review == null) ? "" : review.trim();
    }

    // ── Others ───────────────────────────────────────────────────────────────

    public String getStars() {
        if (rating == 0) return "Not rated";
        return "★".repeat(rating) + "☆".repeat(5 - rating);
    }


}
