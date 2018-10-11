
/*
Created by: GREG WOO
Program: Simulate Twitter algorithms
*/

import java.util.ArrayList;
import java.util.HashSet;
import java.io.*;

public class Tweet{

  // Attributes

  private String userAccount;
  private String date;
  private String time;
  private String message;

  private static HashSet<String> stopWords;

  // Constructor

  public Tweet( String userAccount, String date, String time, String message) {

    this.userAccount = userAccount;
    this.date = date;
    this.time = time;
    this.message = message;

  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////

  // checkMessage method

  public boolean checkMessage() {

    // First we check if the HashSet stopWords is equal to null

    if (stopWords == null) {

      throw new NullPointerException ("The HashSet has not been initialized");

    } else {

      // Then we add the words of the message in an ArrayList

      ArrayList<String> wordList = new ArrayList<String>();

      String[] words = message.split(" ");

      for(int i = 0; i < words.length; i++) {

        // Then we remove the potential stopWords used

        if(isStopWord(words[i]) == false) {
          wordList.add(words[i]);

        }
      }

      // Then we check if the number of words used in the message is betwwen 0 and 16

      int numberOfWords = wordList.size();

      if ((numberOfWords > 0) && (numberOfWords < 16)) {
        return true;

      } else {
        return false;

      }
    }
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////

  // Helper method to check if a String contains a stopWord or not

  public static boolean isStopWord(String word) {

    //First we remove the punctuation

    String noPunctuation = punctuationRemover(word);
    for(String s : stopWords) {

      // Then we check for equality and return true if the word is a stopWord, false otherwise

      if(noPunctuation.equals(s)) {
        return true;

      } else if (noPunctuation.equalsIgnoreCase(s)){
        return true;

      }
    }
    return false;

  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////

  // Helper Method to remove the punctuation of a word


  private static String punctuationRemover(String word) {

    // First we create a HashSet containing the punctuation marks

    HashSet<Character> punctuation = new HashSet<Character>();

    punctuation.add('.');
    punctuation.add(';');
    punctuation.add(',');
    punctuation.add(':');

    for ( Character c : punctuation) {

      // If the word ends with a punctuation mark, we simply remove it from the word

      if (word.charAt(word.length() - 1) == c) {

        String newWord = "";
        for (int i = 0; i < word.length() - 1; i++) {
          newWord = newWord + word.charAt(i);

        }
        return newWord;

      }
    }
    return word;

  }


  /////////////////////////////////////////////////////////////////////////////////////////////////////

  // Get Methods

  public String getDate() {
    return this.date;

  }

  public String getTime() {
    return this.time;

  }

  public String getMessage() {
    return this.message;

  }

  public String getUserAccount() {
    return this.userAccount;

  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////

  // toString

  public String toString() {
    String s =  "" + this.userAccount + "\t" + this.date + "\t" + this.time + "\t" + this.message + "\n" ;
    return s;

  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////

  // isBefore

  public boolean isBefore(Tweet t) {

    String tweetInputDate = t.getDate();
    String tweetInputTime = t.getTime();

    // First we compare the date of the two tweets (which corresponds to the first 10 characters)

    for (int i = 0 ; i < 10; i++) {
      if(tweetInputDate.charAt(i) > this.date.charAt(i)) {
        return true;

      } else if (tweetInputDate.charAt(i) < this.date.charAt(i)) {
        return false;

      }
    }

    // If the code reaches this line, then the dates are identical
    // We therefore compare the times of the two tweets (which corresponds to the 8 characters)

    for (int j = 0 ; j < 8; j++) {
      if(tweetInputTime.charAt(j) > this.time.charAt(j)) {
        return true;

      } else if (tweetInputTime.charAt(j) < this.time.charAt(j)) {
        return false;

      }
    }

    // If we reach this code, the two tweets were posted at the same time

    return false;

  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////

  // loadStopWords

  public static void loadStopWords(String nameFile) {

    try {

      // Here we are going to read the file line by line and add the stop words as we go

      HashSet<String> temp = new HashSet<String>();

      FileReader fr = new FileReader(nameFile);
      BufferedReader br = new BufferedReader(fr);

      String stopWord = br.readLine();

      while(!(stopWord == null)) {
        temp.add(stopWord);
        stopWord = br.readLine();

      }

      br.close();
      fr.close();

      stopWords = temp;

      // The try catch block is here in case there is a problem with the file --> an IOException

    } catch (IOException ioe) {
      System.out.println("ERROR CAUGHT: Invalid file");

    }
  }


  /////////////////////////////////////////////////////////////////////////////////////////////////////

}
