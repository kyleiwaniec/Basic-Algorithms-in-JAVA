
public class BinaryToDecimal {


	private String binaryString;
	private int dec;

	//private static char[] letters = binaryString.toCharArray();

	public BinaryToDecimal(){
		// this("0");
	}

	public BinaryToDecimal(String binaryString) throws BinaryException
	{

		for(int i = 0; i < binaryString.length(); i++){
			int b = Integer.parseInt(Character.toString(binaryString.charAt(i)));

			if (b != 0 && b != 1){
				throw new BinaryException("This is not a binary number: " + binaryString);
			}
		}

		this.binaryString = binaryString;
	}

	public int toDecimal(String binaryString) throws BinaryException{
		for(int i = 0; i < binaryString.length(); i++){
			

			if (binaryString.charAt(i) != '0' && binaryString.charAt(i) != '1'){
				throw new BinaryException("This is not a binary number: " + binaryString);
			}
			
		}
		for(int i = 0; i < binaryString.length(); i++){

			dec += Integer.parseInt(Character.toString(binaryString.charAt(i))) * (Math.pow(2, (binaryString.length() - 1 - i)));
		}

		return dec;
	}

	public String toDecimaltoString(){
		return "The decimal equivalent is: "+ dec;
	}
}
