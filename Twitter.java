
/*
Created by: GREG WOO
Program: Simulate Twitter algorithms
*/

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;


public class Twitter {

  // Attributes

  private ArrayList<Tweet> tweets;

  // Constructor

  public Twitter() {
    ArrayList<Tweet> temp = new ArrayList<Tweet>();
    temp.clear();
    this.tweets = temp;

  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////

  // loadDB

  public void loadDB(String fileName) {

    try {

      // First we are going to read each line of the file

      FileReader fr = new FileReader(fileName);
      BufferedReader br = new BufferedReader(fr);

      String tweetLine = br.readLine();

      while(!(tweetLine == null)) {

        String[] stringArr = new String[4];
        stringArr = tweetLine.split("\t");

        //Then we create a tweet for each line

        Tweet newTweet = new Tweet(stringArr[0], stringArr[1], stringArr[2], stringArr[3]);

        // We check if it's valid

        if (newTweet.checkMessage() == true) {

          // If it's valid, we add it to the ArrayList of tweets

          this.tweets.add(newTweet);

        }
        tweetLine = br.readLine();

      }

      br.close();
      fr.close();

      // Then after all valid tweets are stored, we call sortTwitter to sort the list

      sortTwitter();

      // We use try/catch block in case of an error

    } catch (IOException ioe) {
      System.out.println("ERROR CAUGHT: There is a problem with the file");

    } catch (NullPointerException npe) {
      System.out.println("Error checking the stopWords database: The file of stopWords has not been loaded yet");

    }
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////

  // sortTwitter

  public void sortTwitter() {

    ArrayList<Tweet> temp = new ArrayList<Tweet>();
    temp = this.tweets;
    int counter;

    int size = this.tweets.size();
    int i = 0;

    // We use the isBefore method to sort our twitter
    // The sorting algorithm is as follows:
    // We start with the first tweet (index i = 0)
    // We check if this tweet isBefore the other tweets
    // If it is, we add 1 to the counter
    // When the counter indicates that the first tweet isBefore ALL the other tweets,
    // we move on to the next tweet (index i = 1)
    // Here is an example with 1 representing a tweet posted before the tweet 2 etc...
    // 13245 i = 0
    // 13245 i = 1
    // 12453 i = 1
    // 12453 i = 2
    // 12534 i = 2
    // 12345 etc...

    while(i < size - 1) {

      counter = 0;

      for(int j = 0; j < size; j++) {

        if(this.tweets.get(i).isBefore(this.tweets.get(j))) {
          counter++;

        } if(counter == (size - 1 - i)) {
          i++;

        } else if ((j == size - 1) && (counter != (size - 1 -i))) {
          temp.add(this.tweets.get(i));
          temp.remove(i);

        }
      }
    }
    this.tweets = temp;

  }


  /////////////////////////////////////////////////////////////////////////////////////////////////////

  // Get Methods

  // getSizeTwitter

  public int getSizeTwitter() {
    return this.tweets.size();

  }

  // getTweet

  public Tweet getTweet(int index) {
    return this.tweets.get(index);

  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////

  //printDB

  public String printDB() {

    String s = "";
    for(Tweet t : this.tweets) {
      s = s + t.toString();

    }
    return s;

  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////

  //rangeTweets

  public ArrayList<Tweet> rangeTweets(Tweet tweet1, Tweet tweet2) {

    ArrayList<Tweet> range = new ArrayList<Tweet>();

    for( Tweet t: this.tweets) {
      range.add(t);

    }

    // First we check which one of the two tweets was posted first
    if(tweet1.isBefore(tweet2)) {

      for(int i = 0; i < this.tweets.size(); i++) {

        // Then we remove the tweets that are not in the range

        if(i < this.tweets.indexOf(tweet1)) {
          range.remove(this.tweets.get(i));

        } else if ( i > this.tweets.indexOf(tweet2)) {
          range.remove(this.tweets.get(i));

        }
      }

      // Here we do the same thing except that the other tweet was posted first

    } else {

      for(int i = 0; i < this.tweets.size(); i++) {
        if(i < this.tweets.indexOf(tweet2)) {
          range.remove(this.tweets.get(i));

        } else if ( i > this.tweets.indexOf(tweet1)) {
          range.remove(this.tweets.get(i));

        }
      }
    }
    return range;

  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////

  // saveDB

  public void saveDB(String fileName) {

    try {

      // Here we write the list of tweet in the file given

      FileWriter fw = new FileWriter(fileName);
      BufferedWriter bw = new BufferedWriter(fw);

      String whatToWrite = printDB();
      bw.write(whatToWrite);

      bw.close();
      fw.close();

      // Try/catch block in case there's an error

    } catch (IOException ioe) {
      System.out.println(" ERROR CAUGHT: Something is wrong with the file");

    }
  }

  //////////////////////////////////////////////////////////////////////////////////

  // trendingTopic

  public String trendingTopic() {

    HashMap<String, Integer> mapWords = new HashMap<String, Integer>();

    HashSet<String> allWordsOfTwitter = new HashSet<String>();

    // First we make a set with all the words of Twitter of all the messages

    for( Tweet t : this.tweets) {

      String[] allTheWords = t.getMessage().split(" ");

      for( String wordsOfMessage : allTheWords) {
        allWordsOfTwitter.add(wordsOfMessage);

      }
    }

    // Here we put all the words in a hashmap.
    // If the words end with a punctuation mark, the mark is removed and the word is stored normally

    int counter = 0;

    for( String s : allWordsOfTwitter) {
      String noPunctuation = punctuationRemoverTwitter(s);
      mapWords.put(noPunctuation, counter);

    }

    // Here we are going to count in how many tweets each word appears
    // If they appear in the tweet, the hashmap is updated

    for(Tweet t : this.tweets) {
      mapWords = wordInTweetChecker(mapWords, t);

    }

    // Finally, we see which of the words appears the most

    int counterSecond = 0;
    int biggestCounter = 0;
    String trendingWord = "";

    for ( String words : mapWords.keySet() ){

      counterSecond = mapWords.get(words);

      if ( counterSecond > biggestCounter) {

        // We then check if the word isn't a stopWord

        if(!Tweet.isStopWord(words)) {
          biggestCounter = counterSecond;
          trendingWord = words;

        }
      }
    }
    //System.out.println(mapWords.get(trendingWord));
    return trendingWord;

  }


  // Helper Method to check if a word appears in the tweet

  private static HashMap<String, Integer> wordInTweetChecker(HashMap<String, Integer> map, Tweet t) {

    HashMap<String, Integer> newMap = map;

    // We create a Hashset for the words of each tweet

    HashSet<String> wordsOfThisTweet = getMessageWords(t);


    for ( String word : wordsOfThisTweet) {

      // We remove the punctuation in the word

      String wordNoPunctuation = punctuationRemoverTwitter(word);

      if ( (newMap.containsKey(wordNoPunctuation) == true) ) {
        Integer myValue = newMap.get(wordNoPunctuation) + 1;
        newMap.put(wordNoPunctuation, myValue);

      }
    }
    return newMap;

  }

  // Helper Method to the remove the punctuation of a word

  private static String punctuationRemoverTwitter(String word) {

    HashSet<Character> punctuation = new HashSet<Character>();

    punctuation.add('.');
    punctuation.add(';');
    punctuation.add(',');
    punctuation.add(':');

    for ( Character c : punctuation) {

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

// Helper Method to create a Hashset for the words of a given tweet

  private static HashSet<String> getMessageWords(Tweet tw) {

    HashSet<String> wordsOfTweet = new HashSet<String>();
    String[] allTheWordsOfThisTweet = tw.getMessage().split(" ");

    for( String s : allTheWordsOfThisTweet) {
      String noPunctuation = punctuationRemoverTwitter(s);
      wordsOfTweet.add(noPunctuation);

    }

    return wordsOfTweet;

  }


  ////////////////////////////////////////////////////////////////////////////////////////////////////

  public static void main(String[] args) {

    // Ex 1 CHECK
    /*
     Twitter example = new Twitter();
     example.loadDB("tweets.txt");
     */

    // Ex 2 CHECK
    /*
     Twitter example = new Twitter();
     Tweet.loadStopWords("stopWords.txt");
     example.loadDB("tweets.txt");
     System.out.println("The number of tweets is: " + example.getSizeTwitter());
     */

    //Ex 3 CHECK
    /*
     Twitter example = new Twitter();
     Tweet.loadStopWords("stopWords.txt");
     example.loadDB("tweets.txt");
     System.out.println(example.printDB());
     */

    //Ex 4 CHECK
    /*
     Twitter example = new Twitter();
     Tweet.loadStopWords("stopWords.txt");
     example.loadDB("tweets.txt");
     System.out.println(example.rangeTweets(example.getTweet(4), example.getTweet(2)));
     */



    // Ex 5 CHECK
    /*
     Twitter example = new Twitter();
     Tweet.loadStopWords("stopWords.txt");
     example.loadDB("tweets.txt");
     System.out.println(example.trendingTopic());
      */


  }




}
