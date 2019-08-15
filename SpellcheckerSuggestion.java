import java.util.*;
import java.io.*;

public class SpellcheckerSuggestion{

    /* ------------------ VARIABLES ------------------ */
    private String fileName;

    public String finalString = "";
    private Boolean modify = false;

    //MAIN DICTIONARY AND SUGGESTIONS ARRAYLISTS
    private ArrayList<String> wholeDictionary = new ArrayList<String>();
    private ArrayList<String> allSuggestions = new ArrayList<String>();

    /* ------------------ CONSTRUCTOR ------------------ */
    private SpellcheckerSuggestion() throws IOException{

        Scanner whatFile = new Scanner(System.in);

        //Construct the main dictionary ArrayList
        constructArrayDictionary();

        //Welcome message
        System.out.println("\n*****************************************************************");
        System.out.println("******************** WELCOME TO SPELLCHECKER ********************");
        System.out.println("*****************************************************************");

        System.out.println("\nEnter path or filename you want to spell check:");

        //MAIN FILE AND SPELL CHECKER - Run the loop till a break is found
        while(true){

            //Expect the user to get write the filename or path
            if(whatFile.hasNextLine()){

                //Store user's input
                String fileToCheck = whatFile.nextLine();

                //Create file object to be scanned
                File theFile = new File(fileToCheck);

                //If the file exists, then scan it
                if(theFile.exists()){

                    //Store the name of the file
                    fileName = theFile.getName();

                    //Scan the file for spell checking
                    Scanner myFileChecker = new Scanner(theFile);

                    //Go through each of the words in the file
                    while(myFileChecker.hasNext()){
                        //Check that each word is in the dictionary
                        String actualWord = myFileChecker.next();
                        findInDictionary(actualWord);
                    }

                    //Leave the while loop
                    break;

                //File doesn't exist, the user should try again to input correct filename or path
                }else{
                    System.out.println("\nThis file does not exist, please enter another filename or path:");
                }

            }

        }

        //REWRITE DICTIONARY
        reWriteDictionary();

        //IF THERE WAS ANY MODIFICATION IN THE SPELL CHECKING
        if(!modify){
            System.out.println("\n*****************************************************************");
            System.out.println("********* CONGRATULATIONS, YOU HAD NO SPELLING MISTAKES *********");
            System.out.println("*****************************************************************");

        //IF THERE WAS NOT MODIFICATIONS IN THE SPELL CHECKING
        }else{
            //CALL METHOD TO EXPORT THE NEW SPELLING
            exportFinalFile(finalString);
            System.out.println("\n*****************************************************************");
            System.out.println("***** YOUR NEW FILE (revised-" + fileName + ") IS NOW READY *****");
            System.out.println("*****************************************************************");

        }

    }

    /* ------------------ METHODS ------------------ */

    //CONSTRUCT THE DICTIONARY ARRAYLIST FROM THE DICTIONARY FILE
    private void constructArrayDictionary() throws IOException{

        //Read the dictionary file
        Scanner scanDict = new Scanner(new File("dictionary.txt"));

        //Go through the whole dictionary, word by word
        while(scanDict.hasNext()){
            //Add each word to the dictionary ArrayList
            wholeDictionary.add(scanDict.next());
        }

        //Close the scanner
        scanDict.close();
    }


    //FIND EACH WORD OF THE FILE IN THE DICTIONARY
    private boolean findInDictionary(String x){

        //Clean the word to remove any symbols or characters
        String cleanString = x.replaceAll("[\\.$|,|;|(|)|'|+|-|?|:|!]","");

        //Convert the clean word to lower case as all words in dictionary are in lower case
        cleanString = cleanString.toLowerCase();

        //New scanner to get the user answers
        Scanner answer = new Scanner(System.in);
        Scanner newWord = new Scanner(System.in);

        int usersIntInput;
        String usersInput;
        String theWord;

        int indexSuggestion = -1;
        Boolean add = false;

        //If the word is in the dictionary in any of its possible solutions
        if(wholeDictionary.contains(cleanString)){

            finalString += cleanString + " ";
            return true;

        //IF THE WORD WAS NOT FOUND IN THE DICTIONARY
        }else{

            //Extra to print different message at the end and create a new file
            modify = true;

            //CREATE AND DISPLAY SUGGESTIONS
            displaySuggestions(cleanString);

            //Run the loop till a break is found
            while(true){

                //It expects and integer from the user
                if(answer.hasNextInt()){

                    //Store the user's input
                    usersIntInput = answer.nextInt();

                    //CHECK IF THE KEY ENTERED IS IN THE ARRAYLIST
                    if(usersIntInput < allSuggestions.size() && usersIntInput >= 0){

                      //Store the input in a variable to call this suggestion later and leave the loop
                      indexSuggestion = usersIntInput;
                      break;

                    //IF KEY WAS NOT FOUND, THEN DISPLAY MESSAGE AND KEEP THE LOOP RUNNING
                    }else{

                        //This if statement is just to display a more precise message to the user
                        if(allSuggestions.isEmpty()){
                            System.out.println("Please type A to add '" + cleanString + "' to dictionary or R to enter a replacement)");
                        }else{
                            System.out.println("\nPlease type a key from the table to use a suggestion, A to add '" + cleanString + "' to the dictionary or R to enter a replacement:");
                        }

                    }

                //It expects a letter from the user
                }else if(answer.hasNext()){

                    //Store the user's input
                    usersInput = answer.next().toLowerCase();

                    //IF THE ANSWER IS POSITIVE - Change boolean and break the loop
                    if(usersInput.equals("a") || usersInput.equals("add")){
                        add = true;
                        break;

                    //IF THE ANSWER IS NEGATIVE - Break the loop as the boolean is already false
                    }else if(usersInput.equals("r") || usersInput.equals("replace")){
                        break;

                    //ELSE DON'T MODIFY THE LOOP SO THE USER CAN INPUT AGAIN
                    }else{

                        //This if statement is just to display a more precise message to the user
                        if(allSuggestions.isEmpty()){
                            System.out.println("Please type A to add '" + cleanString + "' to dictionary or R to enter a replacement)");
                        }else{
                            System.out.println("\nPlease type a key from the table to use a suggestion, A to add '" + cleanString + "' to the dictionary or R to enter a replacement:");
                        }

                    }

                }

            }

        }

        // --------------- SECOND PART OF THE METHOD --------------- //

        //IF THE BOOLEAN IS TRUE, THEN ADD WORD TO DICTIONARY
        if(indexSuggestion > -1){

          System.out.println("\nYou chose '" + allSuggestions.get(indexSuggestion) + "' to replace '" + cleanString + "'!");
          finalString += allSuggestions.get(indexSuggestion) + " ";
          return true;

        }else if(add){

            addToDictionaryArray(cleanString);
            System.out.println("\nThe word '" + cleanString + "' was added to dictionary!");

            finalString += cleanString + " ";
            return true;

        }else{

            System.out.println("\nType the new word that will replace '" + cleanString + "':");

            //Run the loop till a break is found
            while(true){

                //Wait till the user enters a word
                if(newWord.hasNext()){

                    //Store the new in a variable
                    theWord = newWord.next();

                    //Recursive to check the new word if exist in dictionary
                    if(findInDictionary(theWord)){
                        //THIS RETURN MAKES THE BREAK AND LEAVES THE METHOD
                        return true;
                    }

                }

            }

        }

    }


    //METHOD TO ADD WORD TO DICTIONARY AND ALPHABETICALLY SORT
    private void addToDictionaryArray(String str){

        //Adds the word to the dictionary ArrayList
        wholeDictionary.add(str);

        //It sorts the ArrayList alphabetically
        Collections.sort(wholeDictionary);

    }


    //METHOD TO REWRITE THE DICTIONARY
    private void reWriteDictionary() throws IOException{

        FileWriter fileWrite = new FileWriter("dictionary.txt");

        for(String x : wholeDictionary){
            fileWrite.write(x + "\n");
        }

        fileWrite.close();

    }


    //METHOD TO CREATE A NEW FILE WITHOUT SPELLING MISTAKES
    private void exportFinalFile(String text) throws IOException{

        FileWriter finalFile = new FileWriter("revised-" + fileName);

        finalFile.write(text);
        finalFile.close();

    }


    //METHOD TO CREATE SUGGESTIONS
    private Boolean createSuggestion(String actual){

        //Clear the suggestions to store new ones
        allSuggestions.clear();

        int numberP = 1;

        //While loop to provide extra functionality to find more words
        while(numberP <= 2){

            //Go through the whole dictionary (ArrayList)
            for(String y : wholeDictionary){
                //If the algorithm returns a number below numberP then add as suggestion
                if(levenshteinDistance(y, actual) <= numberP){
                    allSuggestions.add(y);
                }
            }

            // If there are not suggestions, the number for precision will increase
            // to find other words, else will leave the loop is there were suggestions
            if(allSuggestions.isEmpty() && numberP != 2) numberP = 2;
            else break;
        }

        //RETURN TRUE if there were suggestions, otherwise return false
        if(allSuggestions.isEmpty()) return false;
        else return true;

    }


    //METHOD TO DISPLAY SUGGESTIONS IN THE TERMINAL
    private void displaySuggestions(String cleanString){

        //IF THERE ARE SUGGESTIONS
        if(createSuggestion(cleanString)){

            System.out.println("\nThe word '" + cleanString + "' is not in dictionary, but here are some suggestions:");

            //FORMAT FOR THE TABLE
            String leftAlignFormat = "| %-3d | %-13s |%n";

            System.out.format("+-----+---------------+%n");
            System.out.format("| Key | Suggestion    |%n");
            System.out.format("+-----+---------------+%n");

            //Go though each of the suggestions
            for(String suggest : allSuggestions){
                //Store index (key) in a variable and print them all
                int actualIndex = allSuggestions.indexOf(suggest);
                System.out.format(leftAlignFormat, actualIndex, suggest);
            }

            System.out.format("+-----+---------------+%n");

            System.out.println("\n(Type a key to use the suggested word or alternatively type A to add to dictionary or R to enter a replacement)");

        //IF THERE WERE NO SUGGESTIONS FOUND
        }else{
            System.out.println("\nThe word '" + cleanString + "' is not in dictionary and no relevant suggestions were found.");
            System.out.println("(Type A to add to dictionary or R to enter a replacement)");
        }

    }


    //LEVENSHTEIN DISTANCE ALGORITHM
    private int levenshteinDistance(String a, String b){

        /* RESOURCES USED TO MAKE ALGORITHM
        https://www.youtube.com/watch?v=We3YDTzNXEk
        https://rosettacode.org/wiki/Levenshtein_distance */

        //Create the array of values
        int [] values = new int [b.length() + 1];

        //Initialise maxtrix with numbers 0 to length
        for(int j = 0; j < values.length; j++){
            values[j] = j;
        }

        //Go through the length of first word
        for(int i = 1; i <= a.length(); i++){

            //Save current iteration in first element of array of values - Variable of previos iteration
            values[0] = i;
            int prev = i - 1;

            //Go through each letter of second word
            for(int j = 1; j <= b.length(); j++){

                //Find out the smallest bewteen this and previos iteration
                int minimunValues = Math.min(values[j], values[j - 1]);

                //If letters at same position are not the same, increase variable prev
                if(a.charAt(i - 1) != b.charAt(j - 1)){
                    prev++;
                }

                //Save the smallest of the number of changes needed (No very sure why 1 + is needed - Need to find out )
                int cj = Math.min(1 + minimunValues, prev);

                //Store the iteration in and update the array of values
                prev = values[j];
                values[j] = cj;
            }

        }

        //Return the last element of array of values (the actual string distance)
        return values[b.length()];
    }

    /* ------------------ END METHODS ------------------ */

    /* ------------------ MAIN METHOD ------------------ */
    public static void main(String [] args) throws IOException{

        //Creates the main object
        SpellcheckerSuggestion myChecker = new SpellcheckerSuggestion();

    }

}
