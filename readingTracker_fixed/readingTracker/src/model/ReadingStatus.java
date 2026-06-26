package model;

public enum ReadingStatus {
    NOT_STARTED,
    READING,
    COMPLETED,
    ON_HOLD,
    DROPPED;

    @Override
    public String toString() {
        return switch (this) {
            case NOT_STARTED -> "Not Started";
            case READING     -> "Reading";
            case COMPLETED   -> "Completed";
            case ON_HOLD     -> "On Hold";
            case DROPPED     -> "Dropped";
        };
    }
}
