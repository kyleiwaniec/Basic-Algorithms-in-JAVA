public class BinaryException extends Exception{
	private String description;
	public BinaryException(){
	}
	public BinaryException(String description){
		super(description);
		this.description = description;
	}
	public BinaryException(String description, Throwable t){
		super(description, t);
		this.description = description;
	}
	public String getDescription(){
		return this.description;
	}
}