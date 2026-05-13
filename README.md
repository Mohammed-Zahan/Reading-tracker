# 📚 Reading Tracker

A Java console application to manage and track your personal reading library — supports both physical books and audiobooks.

---

## Features

- **Manage your library** — add, view, edit, and delete physical books and audiobooks
- **Track reading progress** — update current page or minutes listened
- **Log reading sessions** — record date, pages read, and time spent per session
- **Search & filter** — find books by title, author, genre, or reading status
- **Ratings & reviews** — rate books 1–5 stars and write personal reviews
- **Wishlist** — keep a list of books you want to read next
- **Reading statistics** — view overall stats across your library

---

## Tech Stack

- **Language:** Java
- **Paradigm:** Object-Oriented Programming (OOP)
- **Data storage:** In-memory arrays (no external database)
- **Interface:** Command-line (CLI)

---

## Project Structure

```
readingTracker/
└── src/
    ├── app/
    │   └── Main.java               # Entry point, menu navigation
    ├── model/
    │   ├── Book.java               # Abstract base class
    │   ├── PhysicalBook.java       # Physical book with page tracking
    │   ├── AudioBook.java          # Audiobook with duration tracking
    │   ├── ReadingSession.java     # Individual reading session record
    │   ├── ReadingStatus.java      # Enum: Not Started, Reading, Completed, On Hold, Dropped
    │   └── WishlistItem.java       # Wishlist entry model
    └── service/
        ├── BookService.java        # Core CRUD and business logic
        ├── WishlistService.java    # Wishlist management
        └── InputHelper.java        # Safe, validated console input
```

---

## How to Run

**Requirements:** Java 17 or higher

### Compile

```bash
cd readingTracker/src
javac app/Main.java model/*.java service/*.java
```

### Run

```bash
java app.Main
```

---

## Usage

When launched, you are presented with a main menu:

```
  ┌────────────────────────────────────────┐
  │            MAIN MENU                   │
  ├────────────────────────────────────────┤
  │  1. Manage Books                       │
  │  2. Update Reading Progress            │
  │  3. Log Reading Session                │
  │  4. Search / Filter Books              │
  │  5. View Statistics                    │
  │  6. View Wishlist                      │
  │  7. Add to Wishlist                    │
  │  8. Ratings & Reviews                  │
  │  9. Exit                               │
  └────────────────────────────────────────┘
```

Enter the number corresponding to the action you want to perform and follow the prompts.

---

## OOP Concepts Used

| Concept | Where Applied |
|---|---|
| Inheritance | `PhysicalBook` and `AudioBook` extend abstract `Book` |
| Abstraction | `Book` is an abstract class with shared fields and behaviour |
| Encapsulation | All fields are private/protected with getters and setters |
| Enums | `ReadingStatus` for type-safe status management |
| Polymorphism | `BookService` handles both book types through the `Book` reference |

---

## Limitations

- Data is stored in memory only — no persistence between runs
- Maximum of 100 books and 500 reading sessions per session

---

## Author

Built as a Java OOP learning project by me and my friend.
