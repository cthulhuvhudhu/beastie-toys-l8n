package beastie.toys.l8n;

public class Content {
    public static String[] affirmativeOptions = new String[]{"y", "yes", "ya", "affirmative", "you got it",
            "indeed", "you bet", "correct", "exactly", "yeah", "yep", "sure", "right", "you said it"};
    public static String animalFacts = "Facts about the %s\n";
    public static String animalList = "Here are the animals I know:\n";
    public static String animalNoFacts = "No facts about the %s\n";
    public static String animalPrompt = "Which animal do you like most?\n";
    public static String[] clarifyOptions = new String[]{
            "I'm not sure I caught you: was it yes or no?\n",
            "Funny, I still don't understand, is it yes or no?\n",
            "Oh, it's too complicated for me: just tell me yes or no.\n",
            "Could you please simply say yes or no?\n",
            "Oh, no, don't try to confuse me: say yes or no.\n"
    };
    public static String confirmNo = "You answered: No\n";
    public static String confirmYes = "You answered: Yes\n";
    public static String[] farewellOptions = new String[]{"Bye!\n", "See you later\n", "See you soon\n", "Take it easy\n"};
    public static String greetingMorning = "Good morning\n";
    public static String greetingAfternoon = "Good afternoon\n";
    public static String greetingEvening = "Good evening\n";
    public static String guessCorrect = "I guessed correctly! I win.\n";
    public static String guessFormat = "Is it %s?\n";
    public static String guessIncorrectThenPrompt = "I give up. What animal do you have in mind?\n";
    public static String instructions = "You think of an animal, and I guess it.\nPress enter when you're ready.\n";
    public static String learningConfirm = "Is the statement correct for %s?\n";
    public static String learningDone = "Nice! I've learned so much about animals!\n";
    public static String learningQuestion = "I can distinguish these animals by asking the question:\n - %s\n";
    public static String learningSummary = "I have learned the following facts about animals:\n - The %s %s %s.\n - The %s %s %s.\n";
    public static String menu = """
            What do you want to do:
            
            1. Play the guessing game
            2. list of all animals
            3. Search for an animal
            4. Calculate statistics
            5. Print the Knowledge Tree
            0. Exit
            """;
    public static String[] negativeOptions = new String[]{"n", "no", "no way", "nah", "nope", "negative",
            "i don't think so", "yeah no"};
    public static String playAgain = "Would you like to play again?\n\n";
    public static String requestAnimal = "Enter the animal:\n";
    public static String requestFactError = """
            The examples of a statement:
             - It can fly
             - It has horn
             - It is a mammal
            """;
    public static String requestFactFormat =  """
            Specify a fact that distinguishes %s from %s.
            The sentence should be of the format: 'It can/has/is ...'
            """;
    public static String startGame = "Let's play a game!\n";
    public static String statsFormat = """
            The Knowledge Tree stats
            - root node                    %s
            - total number of nodes        %d
            - total number of animals      %d
            - total number of statements   %d
            - height of the tree           %d
            - minimum animal's depth       %d
            - average animal's depth       %.1f
            """;
    public static String welcomeBeginner = "I want to learn about animals.\n";
    public static String welcomeExpert = "Welcome to the animal expert system!\n";




}
