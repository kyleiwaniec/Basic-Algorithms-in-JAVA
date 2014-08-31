// loosely based on the concept of RC4 and one-time-pad. Of course totally unreliable - give me couple of years :)

import java.util.Scanner;
import java.util.Random;

public class GenericEncryptionHandler {
   public static void main(String[] args){
      
      Scanner input = new Scanner(System.in);
      
      System.out.println("enter a phrase:");
      
      String phrase = input.nextLine();
      
      int keyLength = phrase.length();
      // generate keystream based on number of characters entered.
      Random n = new Random();
      int[] keystream = new int[keyLength];
      for(int i = 0; i < keyLength; i++){
    	  keystream[i] = n.nextInt(26) + 65;
    	  
      }

      
      String encryptedMessage = encryptString(phrase, new PseudoOneTimePad(keystream));
      //System.out.println(phrase);
      System.out.print("Key: " );
      printNumArr(keystream);
      System.out.println();
      System.out.println("encryptedMessage: " + encryptedMessage);
      System.out.println("decryptedMessage: " + decryptString(encryptedMessage, new PseudoOneTimePad(keystream)));
   }
   
   public static String encryptString(String s, Encryptable e){
      
      return e.encrypt(s);
   }
   
   public static String decryptString(String s, Decryptable d){
      
      return d.decrypt(s);
   }
   
   public static void printNumArr(int[] array) {
		for (int x : array) {
			System.out.print(x + ", ");
		}

	}
}
