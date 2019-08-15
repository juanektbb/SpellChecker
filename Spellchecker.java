import java.util.*;
import java.io.*;

public class Spellchecker{

    /* ------------------ VARIABLES ------------------ */
    private String fileName;

    public String finalString = "";
    private Boolean modify = false;

    //MAIN DICTIONARY ARRAYLIST
    private ArrayList<String> wholeDictionary = new ArrayList<String>();

    /* ------------------ CONSTRUCTOR ------------------ */
    private Spellchecker() throws IOException{

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
        String cleanString = x.replaceAll("[\\.$|,|(|)|;|'|+|-|?|:|!]","");

        //Convert the clean word to lower case as all words in dictionary are in lower case
        cleanString = cleanString.toLowerCase();

        //New scanner to get the user answers
        Scanner answer = new Scanner(System.in);
        Scanner newWord = new Scanner(System.in);

        String usersInput;
        String theWord;

        Boolean add = false;

        //If the word is in the dictionary in any of its possible solutions
        if(wholeDictionary.contains(cleanString)){

            finalString += cleanString + " ";
            return true;

        //IF THE WORD WAS NOT FOUND IN THE DICTIONARY
        }else{

            //Extra to print different message at the end and create a new file
            modify = true;

            //Ask the user to action
            System.out.println("\nThe word '" + cleanString + "' is not in dictionary, type A to add to dictionary or R to enter a replacement");

            //Run the loop till a break is found
            while(true){

                //Wait till the user enters a word
                if(answer.hasNext()){

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
                        System.out.println("\nPlease use A or R to add '" + cleanString + "' to the dictionary or enter a replacement:");
                    }

                }

            }

        }

        // --------------- SECOND PART OF THE METHOD --------------- //

        //IF THE BOOLEAN IS TRUE, THEN ADD WORD TO DICTIONARY
        if(add){

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

    /* ------------------ END METHODS ------------------ */

    /* ------------------ MAIN METHOD ------------------ */
    public static void main(String [] args) throws IOException{

        //Creates the main object
        Spellchecker myChecker = new Spellchecker();

    }

}
