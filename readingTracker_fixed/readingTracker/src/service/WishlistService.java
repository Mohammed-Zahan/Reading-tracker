package service;

import model.WishlistItem;

public class WishlistService {

    private static final int MAX_WISHLIST = 100;

    private WishlistItem[] items;
    private int            count;
    private int            nextId;

    public WishlistService() {
        items  = new WishlistItem[MAX_WISHLIST];
        count  = 0;
        nextId = 1;
    }

    public void addItem(String title, String author, String genre, String note) {
        if (count >= MAX_WISHLIST) throw new IllegalStateException("Wishlist is full.");
        for (int i = 0; i < count; i++)
            if (items[i].getTitle().equalsIgnoreCase(title.trim()))
                throw new IllegalArgumentException("\"" + title + "\" is already in your wishlist.");
        items[count++] = new WishlistItem(nextId++, title, author, genre, note);
        System.out.println("✔  Added to wishlist!");
    }

    public void viewWishlist() {
        if (count == 0) { System.out.println("  Your wishlist is empty."); return; }
        System.out.println("  " + "-".repeat(75));
        System.out.printf("  %-4s %-30s | %-20s | %-12s | %s%n", "ID", "Title", "Author", "Genre", "Note");
        System.out.println("  " + "-".repeat(75));
        for (int i = 0; i < count; i++) System.out.println("  " + items[i]);
    }

    public void removeItem(int id) {
        int idx = -1;
        for (int i = 0; i < count; i++) if (items[i].getId() == id) { idx = i; break; }
        if (idx == -1) throw new IllegalArgumentException("Wishlist item ID " + id + " not found.");
        for (int i = idx; i < count - 1; i++) items[i] = items[i + 1];
        items[--count] = null;
        System.out.println("✔  Removed from wishlist.");
    }

    public WishlistItem getById(int id) {
        for (int i = 0; i < count; i++) if (items[i].getId() == id) return items[i];
        return null;
    }

    public int getCount() { return count; }
}
