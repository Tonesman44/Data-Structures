import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CheatersHangman {

    private static List<String> dictionary = new ArrayList<>(); // List to store words from the dictionary
    private static Set<String> possibleWords = new HashSet<>(); // Set to store possible words based on user-defined length

    public static void main(String[] args) {
        //Note for TA - Will have to change this line depending on where your file is saved
        readDictionary("C:/Users/anton/IdeaProjects/Data Structures/lab08/dictionary.txt"); // Step 1: Read the dictionary

        Scanner scanner = new Scanner(System.in);
        int wordLength;
        do {
            System.out.print("Enter the size of the hidden word: ");
            wordLength = scanner.nextInt();
            possibleWords.clear();
            filterWordsByLength(wordLength); // Step 2: Filter words by length based on user input
            if (possibleWords.isEmpty()) {
                System.out.println("No words of that length found in the dictionary.");
            }
        } while (possibleWords.isEmpty());

        System.out.print("Enter the number of wrong guesses allowed: ");
        int maxWrongGuesses = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        List<String> hiddenWords = new ArrayList<>(possibleWords); // Step 4: Create initial list of hidden words

        playHangman(hiddenWords, maxWrongGuesses, scanner); // Step 5 and 6: Play hangman

        System.out.print("Do you want to play again? (yes/no): ");
        String playAgain = scanner.nextLine().trim();
        if (playAgain.equalsIgnoreCase("yes")) {
            main(args); // Restart the game
        } else {
            System.out.println("Thanks for playing!");
        }

        scanner.close();
    }

    // Method to read words from the dictionary file
    private static void readDictionary(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                dictionary.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading dictionary file: " + e.getMessage());
        }
    }

    // Method to filter words by length
    private static void filterWordsByLength(int length) {
        for (String word : dictionary) {
            if (word.length() == length) {
                possibleWords.add(word);
            }
        }
    }

    // Method to play hangman game
    private static void playHangman(List<String> hiddenWords, int maxWrongGuesses, Scanner scanner) {
        Set<Character> guessedLetters = new HashSet<>();
        int wrongGuesses = 0;
        String wordToGuess = hiddenWords.get(new Random().nextInt(hiddenWords.size()));

        StringBuilder revealedWord = new StringBuilder(wordToGuess.replaceAll(".", "_"));
        System.out.println("Word to guess: " + revealedWord);

        while (wrongGuesses < maxWrongGuesses && revealedWord.toString().contains("_")) {
            System.out.println("Guessed letters: " + guessedLetters);
            System.out.println("Wrong guesses left: " + (maxWrongGuesses - wrongGuesses));

            System.out.print("Enter your guess: ");
            char guess = scanner.nextLine().toLowerCase().charAt(0);
            if (guessedLetters.contains(guess)) {
                System.out.println("You already guessed that letter. Try again.");
                continue;
            }
            guessedLetters.add(guess);

            Set<String> wordFamilies = new HashSet<>();
            for (String word : hiddenWords) {
                StringBuilder family = new StringBuilder();
                for (char letter : word.toCharArray()) {
                    if (guessedLetters.contains(letter)) {
                        family.append(letter);
                    } else {
                        family.append("_");
                    }
                }
                wordFamilies.add(family.toString());
            }

            Map<String, List<String>> familyMap = new HashMap<>();
            for (String family : wordFamilies) {
                List<String> wordsInFamily = new ArrayList<>();
                for (String word : hiddenWords) {
                    if (matchesFamily(word, family)) {
                        wordsInFamily.add(word);
                    }
                }
                familyMap.put(family, wordsInFamily);
            }

            String chosenFamily = Collections.max(familyMap.keySet(), Comparator.comparingInt(k -> familyMap.get(k).size()));
            if (wordToGuess.indexOf(guess) >= 0) {
                for (int i = 0; i < wordToGuess.length(); i++) {
                    if (wordToGuess.charAt(i) == guess) {
                        revealedWord.setCharAt(i, guess);
                    }
                }
                System.out.println("Correct guess!");
            } else {
                wrongGuesses++;
                System.out.println("Incorrect guess!");
            }
            System.out.println("Word to guess: " + revealedWord);
        }

        if (!revealedWord.toString().contains("_")) {
            System.out.println("Congratulations! You guessed the word: " + wordToGuess);
        } else {
            System.out.println("Sorry, you're out of guesses. The word was: " + wordToGuess);
        }
    }

    // Method to check if word matches family pattern
    private static boolean matchesFamily(String word, String family) {
        for (int i = 0; i < word.length(); i++) {
            if (family.charAt(i) != '_' && family.charAt(i) != word.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}
