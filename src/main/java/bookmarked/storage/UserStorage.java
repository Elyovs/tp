package bookmarked.storage;

import bookmarked.Book;
import bookmarked.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class UserStorage {
    /**
     * Creates a file at the specified path if it does not already exist.
     *
     * @param userDataPath The path where the file is to be created.
     * @return The file object.
     */
    public static File createFile(String userDataPath) {
        File bookDataFile = new File(userDataPath);
        try {
            boolean fileCreated = bookDataFile.createNewFile();
            if (fileCreated) {
                System.out.println("New file created: " + bookDataFile.getName());
            }
        } catch (IOException e) {
            System.out.println("Sorry, something's wrong, file is not created");
        }

        return bookDataFile;
    }

    /**
     * Reads books from a storage file and returns them as a list.
     *
     * @param userDataFile The file from which to read Book data.
     * @return A list of books read from the file.
     */
    public static ArrayList<User> readFileStorage(File userDataFile) {
        ArrayList<User> listOfUser = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(userDataFile))) {
            fileReader.lines().forEach(line ->
                    processLine(line, listOfUser));
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("Access to file is interrupted");
        }

        return listOfUser;
    }

    /**
     * Process each line read in txt file, by parsing and adding the users and the list of
     * books they borrowed into listOfUsers for user in the program.
     *
     * @param line A string representing a line of text from the storage file.
     * @param listOfUsers The list to which users who borrowed books are kept tracked.
     */
    private static void processLine(String line, ArrayList<User> listOfUsers) {
        String[] userAttributes = line.split(" \\| ");
        User currentUser = new User(userAttributes[0]);

        for (int i = 1; i < userAttributes.length; i += 1) {
            currentUser.borrowedBook(new Book(userAttributes[i]));
        }

        listOfUsers.add(currentUser);
    }
}