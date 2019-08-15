import java.util.*;

public class Levenshtein{

    public static int distance(String a, String b){

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

    //MAIN METHOD
    public static void main(String [] args){

      	String a = "moning";
      	String b = "moriing";

        System.out.println("Levenshtein Distance of (" + a + ", " + b + ") = " + distance(a, b));

    }

}
