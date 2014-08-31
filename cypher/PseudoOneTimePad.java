public class PseudoOneTimePad implements Encryptable, Decryptable {
	

   private int[] keystream;

   
   public PseudoOneTimePad(){
      this(null); 
   }
   
   public PseudoOneTimePad(int[] keystream){
      this.keystream = keystream;
   }
   
   
   
   public String encrypt(String s){ 
      char[] letters = s.toCharArray();
      
      
      for (int i = 0; i < letters.length; i++){
         
    	  letters[i] = (char) ((int)letters[i] + keystream[i] - 65); // the 65 is not necessary but makes for a prettier string
      }
      
      String encrypted = new String(letters);
      return encrypted;
   }
   
   public String decrypt(String s){
      char[] encryptedLetters = s.toCharArray();
      
      for (int i = 0; i < encryptedLetters.length; i++){
    	  encryptedLetters[i] = (char) (encryptedLetters[i] - keystream[i] + 65);
    	  
      }
      
      String decrypted = new String(encryptedLetters);
      return decrypted;
   }


}
