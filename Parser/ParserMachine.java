import java.util.Scanner;
import java.util.regex.*;
import java.util.ArrayList;
import java.io.*;


public class ParserMachine {
	public static void main(String[] args) throws Exception {

		String[] keywords = { "abstract", "continue", "for", "new", "switch",
				"assert", "default", "goto", "package", "synchronized",
				"boolean", "if", "private", "this", "break", "double", "do",
				"implements", "protected", "throws", "throw", "byte", "else",
				"import", "public", "case", "enum", "instanceof", "return",
				"transient", "catch", "extends", "int", "short", "try", "char",
				"final", "interface", "static", "void", "class", "finally",
				"long", "strictfp", "volatile", "const", "float", "native",
				"super", "while"};

		String[] literals = {"true", "false", "null"};

		String[] separators = {"(", ")", "{", "}", "[", "]"};


		Scanner request = null;
		String filename = "";

		String content = "";
		Scanner scanner = null;

		boolean promptForFile = true;

		boolean blockComm = false;
		boolean lineComm = false;
		boolean quote = false;
		boolean character = false;
		boolean keyword = false;
		int lineNum = 1;
		int index = 0;

		final String lnBr = System.getProperty ( "line.separator" );

		int ls = 0; // literals state
		String tempLS = ""; // temporary literals string

		int ks = 0; // keyword state
		String tempKS = ""; // temporary keyword string

		int ns = 0; // number state
		String tempNums = ""; // temporary numbers string

		int as = 0; // annotation state
		String tempAnnot = ""; // temporary annotation string
		
		int bs = 0; // block comment state
		


		while(promptForFile == true){
			System.out.println("Enter path and file name (without the java extention):\n" +
					"(for example: myComputer/myStuff/myFile)");

			request = new Scanner(System.in);
			filename = request.next();

			File file = new File(filename+".java");

			try {

				scanner = new Scanner(file);

				//scanner = new Scanner(new FileReader(filename+".java")); // not sure what the difference is.

				while ( scanner.hasNextLine() ){

					index = 0;

					String origLine = scanner.nextLine();
					Scanner scannerLine = new Scanner(origLine);


					outer:
						while(scannerLine.hasNextLine()){
							// Read line by line
							String line = scannerLine.nextLine();


							// first convert all the brackets to html
							String gt = "([>])";
							line = Pattern.compile(gt).matcher(line)
									.replaceAll("&gt;");

							String lt = "([<])";
							line = Pattern.compile(lt).matcher(line)
									.replaceAll("&lt;");



							// reset the ArrayList on every line
							ArrayList<String> characters = new ArrayList<String>();

							// In case weird shit happens, don't let it propagate to rest of file:
							if(quote == true){ 
								quote = false;
								characters.add("</span>");
								index++;
							}
							if(lineComm == true){ 
								lineComm = false;
								characters.add("</span>");
								index++;
							}
							// end weird shit reset - for now...

							tempKS = "";
							tempLS = "";

							// read character by character:
							inner:
								for (int i = 0; i < line.length(); i++){

									// Copy the input character by character into the ArrayList
									characters.add(i + index, Character.toString(line.charAt(i)));



									// char:
									if(
											(i > 0 
													&& character == false 
													&& quote == false 
													&& lineComm == false 
													&& blockComm == false  
													&&  line.charAt(i) == '\'' 
													&& line.charAt(i-1) != '\\')
													||	(i == 0 
													&& character == false 
													&& quote == false 
													&& lineComm == false 
													&& blockComm == false  
													&&  line.charAt(i) == '\'')
											){

										characters.add(i+index, "<span class='char'>"); // insert the tag before the single quote
										index++;
										character = true;


									}else if(i > 0 
											&& character == true  
											&& line.charAt(i) == '\'' 
											&& (line.charAt(i-1) != '\\' 
											|| (line.charAt(i-1) == '\\' && line.charAt(i-2) == '\\'))){
										
										ensureSize(characters, line.length()+index+1);
										characters.add(i+index+1, "</span>"); // insert the tag after the single quote
										index++;
										character = false;

									}

									// line comment:
									if(i < line.length()-1 
											&& line.charAt(i) == '/' 
											&& line.charAt(i+1) == '/' 
											&& quote == false 
											&& blockComm == false 
											&& lineComm == false){
										characters.add(i+index, "<span class='lnComm'>");
										index++;
										lineComm = true;
									}

									if(lineComm == true && i == line.length()-1){
										ensureSize(characters, line.length()+index+1);
										characters.add(line.length()+1+index, "</span>");
										index++;
										lineComm = false;
									}

									// block comments:
									if(i < line.length()-1 
											&& line.charAt(i) == '/' 
											&& line.charAt(i+1) == '*' 
											&& lineComm == false 
											&& blockComm == false 
											&& quote == false ){
										characters.add(i+index, "<span class='blComm'>");
										index++;
										blockComm = true;
									}else if(i > 1 
											&& line.charAt(i-1) == '*' 
											&& line.charAt(i) == '/' 
											&& line.charAt(i-2) != '/' 
											&& blockComm == true ){
										characters.add(i+1+index, "</span>");
										index++;
										blockComm = false;
									}

									// quoted strings:
									if(quote == false 
											&& lineComm == false 
											&& blockComm == false 
											&& character == false 
											&&  line.charAt(i) == '"'){
										characters.add(i+index, "<span class='string'>");
										index++;
										quote = true;

									}else if(quote == true  
											&& line.charAt(i) == '"' 
											&& line.charAt(i-1) != '\\'){
										ensureSize(characters, line.length()+index+1);
										characters.add(i+1+index, "</span>");
										index++;
										quote = false;

									}

									// keywords 
									if(	quote == false 
											&& lineComm == false 
											&& blockComm == false 
											&& character == false){

										if(i == 0) {
											ks = 1;
										}else if(ks == 0 // characters before the keyword:
												&& (line.charAt(i) == ' ' 
												|| line.charAt(i) == '\t' 
												|| line.charAt(i) == '(' 
												|| line.charAt(i) == ')' 
												|| line.charAt(i) == '{'
												|| line.charAt(i) == '}'
												|| line.charAt(i) == '['
												|| line.charAt(i) == ']'
												|| line.charAt(i) == '/' 
												|| line.charAt(i) == '.'
												|| line.charAt(i) == ','
												|| line.charAt(i) == ';'))
										{
											ks = 2;	
										}

										if(ks == 1 || ks == 2){
											tempKS += line.charAt(i);
										}
										
										if((// characters after the keyword:
												line.charAt(i) == ' ' 
												|| line.charAt(i) == '\t' 
												|| line.charAt(i) == '\r' 
												|| line.charAt(i) == '\n' 
												|| line.charAt(i) == '(' 
												|| line.charAt(i) == ')' 
												|| line.charAt(i) == '{'
												|| line.charAt(i) == '}'
												|| line.charAt(i) == '['
												|| line.charAt(i) == ']'
												|| line.charAt(i) == '/'
												|| line.charAt(i) == ','
												|| line.charAt(i) == '.'
												|| line.charAt(i) == ';')){
											tempKS = "";
										}
										if(i < line.length()-1){
											for(String key : keywords){
												if(tempKS.equals(key) && 
														(  line.charAt(i+1) == ' ' 
														|| line.charAt(i+1) == '\t' 
														|| line.charAt(i+1) == '/' 
														|| line.charAt(i+1) == '('
														|| line.charAt(i+1) == ')' 
														|| line.charAt(i+1) == '{'
														|| line.charAt(i+1) == '}' 
														|| line.charAt(i+1) == '['
														|| line.charAt(i+1) == ']' 
														|| line.charAt(i+1) == '.' 
														|| line.charAt(i+1) == ','
														|| line.charAt(i+1) == ';')){ 
													ensureSize(characters, line.length()+index+tempKS.length()+2);
													characters.add(i+index-tempKS.length()+1, "<span class='keyword'>");
													index++;
													characters.add(i+index+1, "</span>");
													index++;
													tempKS = "";

													ks = 0;

												}
											}
										}else if(i == line.length()-1){ // end of line
											for(String key : keywords){
												if(tempKS.equals(key)){
													ensureSize(characters, line.length()+index+tempKS.length()+2);
													characters.add(i+index-tempKS.length()+1, "<span class='keyword'>");
													index++;
													characters.add(i+index+1, "</span>");
													index++;
													tempKS = "";

													ks = 0;
												}
											}
										}
									}

									// literals 
									if(	quote == false && lineComm == false && blockComm == false && character == false){

										if(i == 0) {
											ls = 1;
										}else if(ls == 0 && 
												(line.charAt(i) == ' ' 
												|| line.charAt(i) == '\t' 
												|| line.charAt(i) == '(' 
												|| line.charAt(i) == ')' 
												|| line.charAt(i) == '/'))
										{
											ls = 2;	
										}

										if(ls == 1 || ls == 2){
											tempLS += line.charAt(i);
										}


										if((line.charAt(i) == ' ' 
												|| line.charAt(i) == '\t' 
												|| line.charAt(i) == '/' 
												|| line.charAt(i) == '(')){
											tempLS = "";
										}
										if(i < line.length()-1){
											for(String lit : literals){
												if(tempLS.equals(lit) && 
														(line.charAt(i+1) == ' ' 
														|| line.charAt(i+1) == '\t' 
														|| line.charAt(i+1) == '/' 
														|| line.charAt(i+1) == '(' 
														|| line.charAt(i+1) == ')' 
														|| line.charAt(i+1) == ';' 
														|| line.charAt(i+1) == '{' 
														|| line.charAt(i+1) == ',')){

													ensureSize(characters, line.length()+index+tempLS.length());
													characters.add(i+index-tempLS.length()+1, "<span class='literal'>");
													index++;
													characters.add(i+index+1, "</span>");
													index++;
													tempLS = "";

													ls = 0;

												}
											}
										}else if(i == line.length()-1){
											for(String lit : literals){
												if(tempLS.equals(lit)){
													ensureSize(characters, line.length()+index+tempLS.length());
													characters.add(i+index-tempLS.length()+1, "<span class='literal'>");
													index++;
													characters.add(i+index+1, "</span>");
													index++;
													tempLS = "";

													ls = 0;
												}
											}
										}

									}
									// annotations
									if(as == 0 && quote == false && character == false){


										if(i== 0 && line.charAt(i) == '@')
										{

											ensureSize(characters, line.length()+index+tempAnnot.length());
											characters.add(i+index, "<span class='annotation'>");
											index++;
											as = 1;


										}else if(i > 0 && (line.charAt(i-1) == ' ' || line.charAt(i-1) == '\t')
												&& line.charAt(i) == '@') 
										{
											ensureSize(characters, line.length()+index+tempAnnot.length());
											characters.add(i+index, "<span class='annotation'>");
											index++;
											as = 1;
										}



									}
									if(as == 1 
											&& (line.charAt(i) == ' ' 
											|| line.charAt(i) == '\t' 
											|| line.charAt(i) == '\n' 
											|| line.charAt(i) == '(')){
										characters.add(i+index, "</span>");
										index++;
										as = 0;
									}else if(as == 1 && i == line.length()-1){
										characters.add(i+index+1, "</span>");
										index++;
										as = 0;
									}



									// numbers
									if(ns == 0 && quote == false && lineComm == false && blockComm == false && character == false){


										if(i== 0 && (line.charAt(i) == '0' 
												|| line.charAt(i) == '1' 
												|| line.charAt(i) == '2' 
												|| line.charAt(i) == '3' 
												|| line.charAt(i) == '4' 
												|| line.charAt(i) == '5' 
												|| line.charAt(i) == '6' 
												|| line.charAt(i) == '7' 
												|| line.charAt(i) == '8'
												|| line.charAt(i) == '9' 
												|| line.charAt(i) == '\\') 
												)
										{

											ensureSize(characters, line.length()+index+tempNums.length());
											characters.add(i+index, "<span class='number'>");
											index++;
											ns = 1;


										}else if(i > 0 && (
												line.charAt(i-1) == ' ' 
												|| line.charAt(i-1) == '+' 
												|| line.charAt(i-1) == '-' 
												|| line.charAt(i-1) == '(' 
												|| line.charAt(i-1) == '.'
												|| line.charAt(i-1) == ','
												|| line.charAt(i-1) == '[' 
												|| line.charAt(i-1) == '/'
												|| line.charAt(i-1) == '%'
												|| line.charAt(i-1) == '<'
												|| line.charAt(i-1) == '>'
												|| line.charAt(i-1) == '!'
												|| line.charAt(i-1) == '='
												|| line.charAt(i-1) == '^'
												|| line.charAt(i-1) == ':'
												|| line.charAt(i-1) == '~')
												&& (line.charAt(i) == '0' 
												|| line.charAt(i) == '1' 
												|| line.charAt(i) == '2' 
												|| line.charAt(i) == '3' 
												|| line.charAt(i) == '4' 
												|| line.charAt(i) == '5' 
												|| line.charAt(i) == '6' 
												|| line.charAt(i) == '7' 
												|| line.charAt(i) == '8'
												|| line.charAt(i) == '9' 
												|| line.charAt(i) == '\\') 
												){
											ensureSize(characters, line.length()+index+tempNums.length());
											characters.add(i+index, "<span class='number'>");
											index++;
											ns = 1;
										}



									}
									if(ns == 1 && (line.charAt(i) == ' ' 
											|| line.charAt(i) == '\t' 
											|| line.charAt(i) == ';' 
											|| line.charAt(i) == '-' 
											|| line.charAt(i) == '+' 
											|| line.charAt(i) == ')' 
											|| line.charAt(i) == ',' 
											|| line.charAt(i) == '.' 
											|| line.charAt(i) == ']' 
											|| line.charAt(i) == '*'
											|| line.charAt(i) == '%'
											|| line.charAt(i) == '/'
											|| line.charAt(i) == '>'
											|| line.charAt(i) == '<'
											|| line.charAt(i) == '!'
											|| line.charAt(i) == '='
											|| line.charAt(i) == ':'
											|| line.charAt(i) == '^'
											|| line.charAt(i) == '~')
											){
										characters.add(i+index, "</span>");
										index++;
										ns = 0;
									}else if(ns == 1 && i == line.length()-1){
										characters.add(i+index+1, "</span>");
										index++;
										ns = 0;
									}

									// separators
									if(quote == false && lineComm == false && blockComm == false && character == false){
										for(String sep : separators){
											if(Character.toString(line.charAt(i)).equals(sep)){
												characters.add(i+index, "<span class='separator'>");
												index++;
												ensureSize(characters, line.length()+index+1);
												characters.add(i+index+1, "</span>");
												index++;
											}
										}
									}
								} // end char by char loop



							content += "\n";

							// comment out the 2 lines below to disable line numbers:
							content += "<span class='lnNumb'>" + lineNum+":</span>";
							lineNum++;



							// Make one big content string out of the characters ArrayList:

							String str = characters.toString();
							String revStr = str.substring(1, str.length() - 1); // get rid of the square brackets

							String[] temp;

							temp = revStr.split(", ");

							for(int i = 0; i < temp.length ; i++){
								content += temp[i];
							}

						}


				}
				promptForFile = false;

			}catch(IOException ioe){
				System.out.println("An Exception has occured");
				System.out.println(ioe.toString());
				//System.exit(1);
				continue;

			}
		}


		PrintWriter writer = new PrintWriter(filename+".html");

		writer.println("<!DOCTYPE HTML>\n"
				+ "<html lang='en'>\n"
				+ "<head>\n"
				+ "<meta charset='UTF-8'>\n"
				+ "<style>\n"
				+ "pre{color:#444;}\n"
				+ ".lnComm{color:#85bd7f;}\n"
				+ ".string{color:#015de6;}\n"
				+ ".char{color:purple;}\n"
				+ ".blComm{color:#7f9ed2;}\n"
				+ ".lnNumb{color:#b5b5b5; font-size:80%; padding-right:30px; display:inline-block; width: 40px;}\n"
				+ ".number{color:#fa4903;}\n"
				+ ".annotation{color:#9756aa;}\n"
				+ ".keyword{color:hotpink;font-weight:bold;}\n"
				+ ".literal{color:darkblue;font-weight:bold;}\n"
				+ ".separator{color:#b10148;font-weight:bold;}\n"
				+ "</style>\n"
				+ "</head>\n" + "<body>\n" + "<pre>");

		writer.print(content);


		writer.println("</pre> " + "</body>" + "</html>");

		writer.flush(); // flush the output
		writer.close(); // close the file

		System.out.println("A "+filename+".html file was created in the same directory as your java file.");
	}

	public static String emptyString(String s){ // not being used
		s ="";
		return s;
	}
	public static void ensureSize(ArrayList<String> list, int size) {
		list.ensureCapacity(size);
		while (list.size() < size) {
			list.add("");
		}
	}
}