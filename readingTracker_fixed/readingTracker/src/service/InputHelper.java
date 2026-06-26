package service;

import java.util.Scanner;

public class InputHelper {

    private final Scanner sc;

    public InputHelper(Scanner sc) { this.sc = sc; }

    /** Reads a non-empty string */
    public String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    public char readChar(String prompt)
    {
        while (true) {
            System.out.print(prompt);
            String inputLine = sc.nextLine().trim();
            if (inputLine.isEmpty())
            {
                System.out.println("Input cannot be empty. Please try again.");
                continue;
            }
            return inputLine.charAt(0);
        }
    }

    /** Reads an optional string, empty is allowed */
    public String readOptional(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    /** Reads a positive integer */
    public int readPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String raw = sc.nextLine().trim();
            try {
                int n = Integer.parseInt(raw);
                if (n > 0) return n;
                System.out.println("Please enter a positive number.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    /** Reads a non-negative integer */
    public int readNonNegativeInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String raw = sc.nextLine().trim();
            try {
                int n = Integer.parseInt(raw);
                if (n >= 0) return n;
                System.out.println("Please enter 0 or a positive number.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    /** Reads an int within [min, max] */
    public int readIntInRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String raw = sc.nextLine().trim();
            try {
                int n = Integer.parseInt(raw);
                if (n >= min && n <= max) return n;
                System.out.printf("Enter a number between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    /** Reads a date string in YYYY-MM-DD format, or today's date if blank */
    public String readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String raw = sc.nextLine().trim();
            if (raw.isEmpty()) return java.time.LocalDate.now().toString();
            if (raw.matches("\\d{4}-\\d{2}-\\d{2}")) return raw;
            System.out.println("Use YYYY-MM-DD format or press Enter for today.");
        }
    }

    /** Prompts y/n and returns true for 'y' */
    public boolean confirm(String prompt) {
        System.out.print(prompt + " (y/n): ");
        return sc.nextLine().trim().equalsIgnoreCase("y");
    }
}
