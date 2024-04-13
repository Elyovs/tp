package bookmarked.command;

import bookmarked.Book;
import bookmarked.user.User;
import bookmarked.exceptions.EmptyListException;
import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.ui.Ui;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

public class FindCommand extends Command {
    public static final int FIND_KEYWORD_START_INDEX = 14;
    private static Logger logger = Logger.getLogger("Find Command Logger");
    private static int numberOfBookFound = 0;
    private String newItem;
    private ArrayList<Book> listOfBooks;
    private String[] splitCommand;
    private ArrayList<User> listOfUsers;
    private String userName;

    /**
     * finds an item in the list
     *
     * @param newItem     the command
     * @param listOfBooks the current list of books
     */

    public FindCommand(String newItem, ArrayList<Book> listOfBooks, ArrayList<User> listOfUsers) {
        this.newItem = newItem;
        this.listOfUsers = listOfUsers;
        this.listOfBooks = listOfBooks;
    }

    /**
     * handles the command find and finds the books which matches the description
     */

    @Override
    public void handleCommand() {
        try {
            parseCommand();
        } catch (EmptyListException e) {
            Ui.printEmptyListMessage();
        } catch (EmptyArgumentsException | IndexOutOfBoundsException e) {
            Ui.printEmptyArgumentsMessage();
        }
    }

    public void parseCommand() throws EmptyListException, EmptyArgumentsException {
        this.splitCommand = newItem.split(" /by");
        String splitCommandCommand = splitCommand[1].trim();
        String[] findItem = splitCommandCommand.split(" ");
        switch (findItem[0].trim()) {
            case ("book"):
                bookCommand();
                break;
            case ("user"):
                userCommand(splitCommandCommand);
                break;
            default:
                Ui.printEmptyArgumentsMessage();
        }
    }

    /**
     * userCommand handles the case where find/ by user is called
     * splits command by user to extract user name, then calls findusercommand to find matching user
     * @param splitCommandCommand the command after "/by"
     */
    public void userCommand(String splitCommandCommand) {
        String[] userName = splitCommandCommand.split("user");
        if (userName.length >1) {
            FindUserCommand findUserCommand = new FindUserCommand(listOfUsers, userName[1].trim());
            findUserCommand.handleCommand();
        } else {
            Ui.incorrectFindArgument();
        }
    }

    /**
     *bookCommand handles the case where find /by book is called
     *
     */
    public void bookCommand () {
        assert listOfBooks != null : "list of books should not be empty";
        String keyword;
        logger.log(Level.INFO, "going to start processing find command");
        keyword = getKeyword();
        if (keyword == null) {
            return;
        }
        try {
            processFind(keyword);
        } catch (EmptyListException e) {
            Ui.printEmptyListMessage();
        }
    }


        /**
         * ensures the book to be found is not an empty description
         * @return book to be found
         */

    private String getKeyword() {
        String keyword;
        try {
            keyword = this.newItem.substring(FIND_KEYWORD_START_INDEX);
            if (keyword.isBlank()) {
                throw new StringIndexOutOfBoundsException();
            }
        } catch (StringIndexOutOfBoundsException e) {
            logger.log(Level.WARNING, "processing error for empty keyword");
            System.out.println("Find keyword cannot be empty!");
            return null;
        }
        return keyword;
    }

    /**
     * iterates through the list of books to find the matching description
     * returns the book with the matching description
     * if not found, returns no matching book
     * @param keyword the book to be found
     * @throws EmptyListException if the list of books is empty
     */

    private void processFind(String keyword) throws EmptyListException {
        numberOfBookFound = 0;


        if (this.listOfBooks.isEmpty()) {
            throw new EmptyListException();
        }

        logger.log(Level.INFO, "processing find books based on keyword");
        assert keyword != null : "keyword should not be empty";

        ArrayList<Book> bookFound = new ArrayList<>();

        // filter books based on keyword
        filterBooks(keyword, bookFound);

        numberOfBookFound = bookFound.size();
        if (numberOfBookFound == 0) {
            logger.log(Level.INFO, "giving no matching book found warning");
            System.out.println("Sorry, no book with matching keyword: " + keyword);
            return;
        }

        logger.log(Level.INFO, "processing print of matching book lists");
        System.out.println("Here's the list of matching books in your library:");
        for (int i = 0; i < numberOfBookFound; i += 1) {
            String currentBookTitle = bookFound.get(i).getName();
            System.out.println(" " + (i + 1) + ". " + currentBookTitle);
        }
        logger.log(Level.INFO, "end processing");
    }

    /**
     * adds multiple books which contains the matching name to the list
     * prints the entire list of books
     * @param keyword the book name
     * @param bookFound the list of books with the book name
     */

    private void filterBooks(String keyword, ArrayList<Book> bookFound) {
        for (Book currentBook : this.listOfBooks) {
            if (!(currentBook.getName().contains(keyword))) {
                continue;
            }
            assert currentBook.getName().contains(keyword) : "current book should contain the keyword";
            bookFound.add(currentBook);
        }
    }

    public int getNumberOfBookFound() {
        return numberOfBookFound;
    }
}
