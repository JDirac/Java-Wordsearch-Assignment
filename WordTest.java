import java.util.ArrayList;
import java.util.Arrays;

public class WordTest {

    public static void main(String[] args) {

        System.out.println("CREATE INSTANCE ECHO USING CONSTRUCTOR 2 AND DISPLAY ITS WORD LIST USING display METHOD");
        System.out.println();

        WordSearchPuzzle Echo = new WordSearchPuzzle("WordList.txt", 8, 4, 10);
        Echo.display();

        System.out.println();
        System.out.println("CREATE NEW INSTANCE, FOXTROT, TO TEST THE getWordSearchList METHOD AND CONSTRUCTOR 1");
        System.out.println();

        WordSearchPuzzle Foxtrot = new WordSearchPuzzle(Echo.getWordSearchList());
        Foxtrot.display();

        System.out.println();
        System.out.println("GET PUZZLE GRID USING getPuzzleAsGrid METHOD");
        System.out.println("THEN FILL IT WITH RANDOM CHARACTERS USING THE fillArray METHOD");
        System.out.println();

        Foxtrot.fillArray();
        System.out.println(Arrays.deepToString(Foxtrot.getPuzzleAsGrid()));

        System.out.println();
        System.out.println("CHECK THE getPuzzleAsString METHOD");
        System.out.println();

        String test = Foxtrot.getPuzzleAsString();
        System.out.println(test);

        System.out.println();
        System.out.println("CREATE NEW INSTANCE, CHARLIE, AND GENERATE WORD SEARCH");
        System.out.println("SHOW THE STARTING POSITION AND DIRECTION OF EACH WORD USING showWordSearchPuzzle");
        System.out.println();

        WordSearchPuzzle Charlie = new WordSearchPuzzle(Foxtrot.getWordSearchList());
        Charlie.showWordSearchPuzzle(false);

        System.out.println();
        System.out.println("CREATE NEW INSTANCE, DELTA, USING A CUSTOM WORD LIST");
        System.out.println();

        ArrayList<String> customWords = new ArrayList<>();
        customWords.add("Software");
        customWords.add("Development");
        customWords.add("Java");
        customWords.add("Programming");
        customWords.add("Word");
        customWords.add("Search");
        customWords.add("Puzzle");

        WordSearchPuzzle Delta = new WordSearchPuzzle(customWords);

        System.out.println("NOW TEST showWordSearchPuzzle WITH BOOLEAN hide = true");
        System.out.println();

        Delta.showWordSearchPuzzle(true);

        System.out.println();
        System.out.println("NOW SHOW THE SAME PUZZLE BUT WITH BOOLEAN hide = false");
        System.out.println();

        Delta.showWordSearchPuzzle(false);


    }

}
