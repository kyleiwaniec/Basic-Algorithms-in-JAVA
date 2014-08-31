import java.util.Scanner;
public class TestBinaryToDecimal {

       public static void main(String[] args) {
              Scanner input = new Scanner(System.in);
              
              BinaryToDecimal d = new BinaryToDecimal();
              
              boolean numIsBad = true;
              while(numIsBad == true){
            	  System.out.println("Enter a binary number:");
            	  String numInput = input.next();
              try{
            	  d.toDecimal(numInput);
            	  numIsBad = false;
              }catch(BinaryException be){
            	  System.out.println("An Exception has occurred!");
            	  //System.out.println(be.getDescription());
            	  System.out.println(be.toString());
            	  numIsBad = true;
            	  continue;
            	  }
              
              
              }
              
              System.out.println(d.toDecimaltoString());
       }

}
