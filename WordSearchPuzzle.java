import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WordSearchPuzzle {
    private char[][] puzzle; // will contain the word search
    private List<String> puzzleWords; // the list of words to be used
    private Boolean notGeneratedYet = true; // used in the showWordSearchPuzzle method
    private HashMap<String, String> wordDirections = new HashMap<>(); // these HashMaps will keep track of important information about each word in puzzleWords
    private HashMap<String, Integer> startRow = new HashMap<>(); // like their direction and start position, used in showWordSearchPuzzle method.
    private HashMap<String, Integer> startCol = new HashMap<>();

    /**
     * simple constructor that fills puzzleWords with the words from a user specified list, and prepares a suitable 2D grid to create the puzzle.
     *
     * @param userSpecifiedWords a user supplied list of words to be used in the puzzle
     */
    public WordSearchPuzzle(List<String> userSpecifiedWords) {
        puzzleWords = userSpecifiedWords;
        

        for(int i = 0; i < puzzleWords.size(); i++) {
            puzzleWords.set(i, puzzleWords.get(i).toUpperCase());
        }

        //Next, find the dimensions of the puzzle char array.
        //Get sum of lengths of words in the puzzleWords list.
        int sum = 0;
        int dimensions;

        for (String Word : puzzleWords) {
            sum += Word.length(); // add the length of every word in the list
        }

        dimensions =  (int) Math.ceil(Math.sqrt(sum*3)); // Math.ceil will return the next whole number >= its operand, unless the operand is already a whole number.

        puzzle = new char[dimensions][dimensions]; // initialise our puzzle char array using our new dimensions.
    }

    /**
     *  Word search puzzle constructor.
     *
     * @param wordFile --> name or absolute path of text file containing words to be used
     * @param wordCount --> indicates how many words to store in list for use in puzzle
     * @param shortest --> indicates minimum length of word to be added to the puzzleWord list
     * @param longest --> indicates maximum length of word to be added to the puzzleWord list
     *
     *  Author --> Jonathan Falvey, ID: 19239718
     */
    public WordSearchPuzzle(String wordFile, int wordCount, int shortest, int longest) {
        try {
            File wordList = new File(wordFile);
            Scanner reader = new Scanner(wordList); // scanner will be used to read words from the supplied file.
            puzzleWords = new ArrayList<>(); // the words will be stored in the arrayList puzzleWords
            String word; // declare a String variable ready to store words from the file

            while(reader.hasNextLine()) { // add ALL suitable words from the list to the arrayList. the excess words will be removed at random later.
                word = reader.nextLine().toUpperCase();
                if(word.length() >= shortest && word.length() <= longest) { //check if the selected word meets the criteria specified by shortest and longest.
                    puzzleWords.add(word);
                }
            }
            reader.close(); //close scanner to free up memory in program.
        } catch (FileNotFoundException e) { //in the event that the file doesn't exist or its name was inputted incorrectly, print "File not found"
            System.err.println("File not found");
        }


        Collections.shuffle(puzzleWords); //shuffle the arrayList that now contains all the suitable words.

        try {
            for (int i = puzzleWords.size() - 1; puzzleWords.size() != wordCount; i--) { //remove elements from the shuffled list until we reach the desired capacity
                puzzleWords.remove(i);
            }
        } catch (IndexOutOfBoundsException e) { // accounts for a number of errors that may arise when not enough words are available to meet the word count specified by the user.
            System.err.println("Not enough words to meet your desired word count.");
            System.err.println("Please add more suitable words to your file, change the criteria to allow more words to be used,");
            System.err.println("or lower the word count.");
        }

        //Next, find the dimensions of the puzzle char array.
        //Get sum of lengths of words in the puzzleWords list.
        int sum = 0;
        int dimensions;

        for (String Word : puzzleWords) {
            sum += Word.length(); // add the length of every word in the list
        }

        dimensions =  (int) Math.ceil(Math.sqrt(sum*3)); // Math.ceil will return the next whole number >= its operand, unless the operand is already a whole number.

        puzzle = new char[dimensions][dimensions]; // initialise our puzzle char array using our new dimensions.

    }

    /**
     * Simple method to return the list used in the word search puzzle, in alphabetical order.
     *
     * @return returns puzzleWords, the list containing all words used in the puzzle.
     *
     * Author --> Jonathan Falvey, ID: 19239718
     */
    public List<String> getWordSearchList() {
        Collections.sort(puzzleWords);
        return puzzleWords;
    }


    /**
     * Very simple method to return the puzzle grid of the word search puzzle
     *
     * @return returns puzzle, the two-dimensional grid containing th word search
     */
    public char[][] getPuzzleAsGrid() { return puzzle; }


    /**
     * simple and convenient method for displaying the word search puzzle.
     *
     * @return returns the string, Puzzle, containing the word search grid in a convenient format for viewing
     */
    public String getPuzzleAsString() {
        String Puzzle = ""; //initialise an empty string, Puzzle

        for(int i = 0; i < puzzle.length; i++) {  // adds indexing above crossword
            if(i < 10) {
                Puzzle += i + "  ";
            } else {
                Puzzle += i + " ";
            }
        }

        Puzzle += System.lineSeparator();
        Puzzle += System.lineSeparator();

        for (char[] row : puzzle) { //iterate the 2D array, and print a substring of every row, and replace every comma with a space, then proceed to a new line for the next row
            Puzzle += (Arrays.toString(row).substring(1, Arrays.toString(row).length() - 1)).replace(',', ' ') + System.lineSeparator(); //substring removes "[" and "]".
        }

        return Puzzle;
    }


    /**
     * This method will call for the word search to be generated, and displays the word list used with it.
     * Has the option to display the starting position and direction of each word
     *
     * @param hide if true, will only display the word list
     *             if false, will display the word list and the starting position and direction of each word
     */
    public void showWordSearchPuzzle(boolean hide) {

        if(notGeneratedYet) {
            generateWordSearchPuzzle(); // call for word search to be generated
            notGeneratedYet = false; // this will stop a new puzzle overriding if this method is called again within the same instance
        }
        System.out.println(getPuzzleAsString()); // display the puzzle
        System.out.println(); // leave a space between the puzzle and the word list

        if(hide) {      // if hide is true, only display the word list
            display();
        } else {        // if hide is false, display every word, and obtain the starting position and direction through the three HashMaps
            for( String word : puzzleWords) {
                System.out.println(word + " [" + startRow.get(word) + "] [" + startCol.get(word) + "] " + wordDirections.get(word));
            }
        }
    }


    /**
     * Method Directly responsible for generating the word search inside the 2D array, puzzle.
     * It assigns a direction to each individual word and slots it into the array depending on that direction
     *
     */
    private void generateWordSearchPuzzle() {
        String direction; // Used to indicate each word's direction
        String[] directions = {"UP", "DOWN", "LEFT", "RIGHT"}; // Array containing each of the four possible directions
        int startPos, endPos, counter, diff;
        int colCounter = 0; // keeps track of the current column (or row) that the program is testing for word placement
        ArrayList<String>  freeSpace = new ArrayList<>(); // ArrayList that will be used to determine if there is enough space for the word to be placed


        for(String word : puzzleWords) {
            direction = directions[ (int) (Math.random()*directions.length) ]; // assign a direction to the word at random
            diff = puzzle.length - word.length(); // used for finding the range of available lots the word may start from

            wordDirections.put(word, direction); // store the word and its direction in the HashMap wordDirections

            switch (direction) { // Detect which direction the word has been assigned and place it accordingly



                case "UP":
                    startPos = (int) (Math.random()*(diff)) + word.length(); // chooses a start position randomly from the available range indicated by diff
                    endPos = (startPos - word.length()) + 1; // where the word will end in the same column / row

                    for(int col = 0; col < puzzle.length; col++) { // This nested for loop will search for available positions to place the word

                        freeSpace.clear(); // clear the arrayList on each iteration of the loop
                        counter = 0; // counter is used to iterate through the current word
                        colCounter = col;

                        for(int row = startPos; row >= endPos; row--) {
                            if (puzzle[row][col] == 0 || puzzle[row][col] == word.charAt(counter)) { // by default the array is filled with 0's, so we check if == 0.
                                freeSpace.add("FREE");                                               // alternatively, it also accepts if the letter at that position matches
                            } else {                                                                 // the letter of the word that would be there
                                freeSpace.add("OCCUPIED");
                            }
                            counter++;
                        }

                        if(col == puzzle.length-1 && freeSpace.contains("OCCUPIED")) { // if we've hit the last available column and that too is unable to fit the word
                            col = 0;                                                   // we reset col to keep the restart the loop
                            startPos = (int) (Math.random()*(diff)) + word.length();   // we also choose a new random spot for the word and test it
                            endPos = (startPos - word.length()) + 1;
                        }

                        if(!freeSpace.contains("OCCUPIED")) { // if the current column has available space to fit the word, break out of the for loop
                            break;
                        }

                    }
                    counter = 0; // reset the counter after the for loop

                    startRow.put(word, startPos); // store the row where the first letter of the word is contained
                    startCol.put(word, colCounter); // store the column where the first letter of the word is contained

                    for(int row = startPos; row >= endPos; row --) { // this for loop inserts the word into the space we found previously
                        puzzle[row][colCounter] = word.charAt(counter);
                        counter++;
                }
                break;

                // the following cases follow the same process as the previous case, with only slight alterations as a result of the direction

                case "DOWN":
                    startPos = (int) (Math.random()*diff);
                    endPos = startPos + word.length() - 1;

                    for(int col = 0; col < puzzle.length; col++) {
                        freeSpace.clear();
                        counter = 0;
                        colCounter = col;

                        for(int row = startPos; row <= endPos; row++) {
                            if (puzzle[row][col] == 0 || puzzle[row][col] == word.charAt(counter)) {
                                freeSpace.add("FREE");
                            } else {
                                freeSpace.add("OCCUPIED");
                            }
                            counter++;
                        }

                        if(col == puzzle.length-1 && freeSpace.contains("OCCUPIED") && startPos < diff) {
                            col = 0;
                            startPos = (int) (Math.random()*diff);
                            endPos = startPos + word.length() - 1;
                        }

                        if(!freeSpace.contains("OCCUPIED")) {
                            break;
                        }

                    }
                    counter = 0;

                    startRow.put(word, startPos);
                    startCol.put(word, colCounter);

                    for(int row = startPos; row <= endPos; row++) {
                        puzzle[row][colCounter] = word.charAt(counter);
                        counter++;
                    }
                break;



                case "LEFT":
                    startPos = (int) (Math.random()*diff) + word.length();
                    endPos = (startPos - word.length()) + 1;

                    for(int row = 0; row < puzzle.length; row++) {
                        freeSpace.clear();
                        counter = 0;
                        colCounter = row;

                        for(int col = startPos; col >= endPos; col--) {
                            if (puzzle[row][col] == 0 || puzzle[row][col] == word.charAt(counter)) {
                                freeSpace.add("FREE");
                            } else {
                                freeSpace.add("OCCUPIED");
                            }
                            counter++;
                        }

                        if(row == puzzle.length-1 && freeSpace.contains("OCCUPIED") && startPos > diff) {
                            row = 0;
                            startPos = (int) (Math.random()*diff) + word.length();
                            endPos = (startPos - word.length()) + 1;
                        }

                        if(!freeSpace.contains("OCCUPIED")) {
                            break;
                        }

                    }
                    counter = 0;

                    startRow.put(word, colCounter);
                    startCol.put(word, startPos);

                    for(int col = startPos; col >= endPos; col--) {
                        puzzle[colCounter][col] = word.charAt(counter);
                        counter++;
                    }

                break;



                default:
                    startPos = (int) (Math.random()*diff);
                    endPos = startPos + word.length() - 1;

                    for(int row = 0; row < puzzle.length; row++) {
                        freeSpace.clear();
                        counter = 0;
                        colCounter = row;

                        for(int col = startPos; col <= endPos; col++) {
                            if (puzzle[row][col] == 0 || puzzle[row][col] == word.charAt(counter)) {
                                freeSpace.add("FREE");
                            } else {
                                freeSpace.add("OCCUPIED");
                            }
                            counter++;
                        }

                        if(row == puzzle.length-1 && freeSpace.contains("OCCUPIED") && startPos < diff) {
                            row = 0;
                            startPos = (int) (Math.random()*diff);
                            endPos = startPos + word.length() - 1;
                        }

                        if(!freeSpace.contains("OCCUPIED")) {
                            break;
                        }

                    }
                    counter = 0;

                    startRow.put(word, colCounter);
                    startCol.put(word, startPos);

                    for(int col = startPos; col <= endPos; col++) {
                        puzzle[colCounter][col] = word.charAt(counter);
                        counter++;
                    }


            }
        }
        fillArray(); //Fill puzzle with random characters once the words have been placed.
    }


    /**
     * This is a helper method that prints all the words in the puzzleWords list in alphabetical order
     *
     */
    public void display() { // method created for testing and error checking the constructors and methods.
        Collections.sort(puzzleWords);

        for (String Word : puzzleWords) {
            System.out.println(Word.toUpperCase());
        }
    }

    /**
     * This is a helper method that fills any unused slots in the puzzle with random letters
     *
     */
    public void fillArray() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for(int row = 0; row < puzzle.length; row++) {
            for(int col = 0; col < puzzle[0].length; col++) {
                if(alphabet.indexOf(puzzle[row][col]) == -1) {
                    puzzle[row][col] = alphabet.charAt((int) (Math.random() * alphabet.length()));
                }
            }
        }
    }
}