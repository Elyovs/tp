package bookmarked;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Book {

    private static final int EXTENSION_DAYS = 7;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final LocalDate DEFAULT_RETURNED_DATE = LocalDate.of(1900,1,1);
    protected String description;
    protected boolean isBorrowed;
    protected LocalDate borrowDate;
    protected LocalDate returnDate;
    protected String user;




    public Book(String description) {
        this.description = description;
        this.isBorrowed = false;
        this.borrowDate = null;
        this.returnDate = DEFAULT_RETURNED_DATE;
    }

    public String getName() {
        return this.description;
    }

    public boolean isAvailable() {
        return !this.isBorrowed;
    }

    public void borrowBook(LocalDate borrowDate, Period borrowPeriod) {
        if (isAvailable()) {
            this.isBorrowed = true;
            this.borrowDate = borrowDate;
            this.returnDate = borrowDate.plus(borrowPeriod);
        }
    }

    public void extendDueDate() {
        if (this.isBorrowed) {
            this.returnDate = this.returnDate.plusDays(EXTENSION_DAYS);
        }
    }


    public LocalDate getBorrowDate() {
        return this.borrowDate;
    }

    public LocalDate getReturnDate() {
        return this.returnDate;
    }

    public boolean getIsBorrowed() {
        return this.isBorrowed;
    }

    public void setBorrowed() {
        this.isBorrowed = true;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setReturned() {
        this.isBorrowed = false;
        this.borrowDate = null;
        this.returnDate = DEFAULT_RETURNED_DATE;
    }

    public void setName(String bookName) {
        this.description = bookName;
    }

    public String getFormattedReturnDate() {
        return isBorrowed ? returnDate.format(DATE_FORMATTER) : "Not borrowed";
    }

    public String getBorrowedStatus() {
        return (isBorrowed ? ", borrowed" : "available"); // mark done task with X
    }

    @Override
    public String toString() {
        if (isBorrowed) {
            String formattedBorrowDate =
                    (borrowDate != null) ? borrowDate.format(DATE_FORMATTER) : "Not set";
            String formattedReturnDate = getFormattedReturnDate();
            return String.format(
                    "%s, borrowed on: %s, due on: %s", this.description, formattedBorrowDate,
                    formattedReturnDate);
        } else {
            return this.description + " available";
        }
    }

}





