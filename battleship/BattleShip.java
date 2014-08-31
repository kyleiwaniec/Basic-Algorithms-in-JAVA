import java.util.Random;
import java.util.Scanner;

public class BattleShip {
	public static void main(String[] args) {
		
		int[][] ships = { { -1, -1, -1, -1, -1 }, { -1, -1, -1, -1 }, { -1, -1, -1 },
				{ -1, -1, -1 }, { -1, -1 } };
		// this may be kind of a redundant way of doing things, but for now... so be it:
		int[][] ships_sunk = { { -1, -1, -1, -1, -1 }, { -1, -1, -1, -1 }, { -1, -1, -1 },
				{ -1, -1, -1 }, { -1, -1 } };
		
		int[][] compShips = { { -1, -1, -1, -1, -1 }, { -1, -1, -1, -1 }, { -1, -1, -1 },
				{ -1, -1, -1 }, { -1, -1 } };
		/*
		 * Aircraft carrier = 5 battleship = 4 destroyer = 3 submarine = 3
		 * patrol boat = 2
		 */
		String[] shipNames = { "Aircraft Carrier", "Battleship", "Destroyer",
				"Submarine", "Patrol Boat" };
		String[] directions = { "left", "right", "up", "down" };

		// make a grid 10 by 10 (2 dimensional array)
		Scanner input = new Scanner(System.in);
		char[][] playerBoard = new char[10][10];
		char[][] compBoard = new char[10][10];
		char[][] compBoardView = new char[10][10];

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				playerBoard[i][j] = ' ';
				compBoard[i][j] = ' ';
				compBoardView[i][j] = ' ';
			}
		}

		
		// computer's board: - is a miss, * is a hit,
		// player's board: - is a miss, * is a hit, o is an un-hit ship
		
		// place computer ships: (One day when I'm smarter, I'll use some kind of probability theory or something... but for now...)
				for (int i = 0; i < shipNames.length;) {

					
					Random row = new Random();
					int rowInt = row.nextInt(9);
					int colInt = row.nextInt(9);
					int dir = row.nextInt(3);
					
					if (compBoard[rowInt][colInt] != ' ') {
							continue;
						} 
					
					
					// right
					if (dir == 0) {
						if (colInt + compShips[i].length <= 10) {
							int count = 0;
							for (int k = 0; k < compShips[i].length; k++) {
								if (compBoard[rowInt][colInt + k] == ' ') {
									count++;
								}
							}
							if (count == compShips[i].length) {
								// if everything OK, add ship to array
								for (int j = colInt; j < compShips[i].length + colInt; j++) {
									compBoard[rowInt][j] = 'o';
									
									}
								// put the ships in ships array	
								for(int s = 0; s < compShips[i].length; s++){
									compShips[i][s] = (rowInt * 10) + colInt + s;
								}
							} else {
								continue;
							}
						} else {
							continue;
						}
					}// end of direction right
					
					// left
					if (dir == 1) {

						if (colInt - compShips[i].length >= -1) {
							int count = 0;
							for (int k = 0; k < compShips[i].length; k++) {
								if (compBoard[rowInt][colInt - k] == ' ') {
									count++;
								}
							}
							if (count == compShips[i].length) {
								// if everything OK, add ship to array
								for (int j = colInt; j > colInt - compShips[i].length; j--) {
									compBoard[rowInt][j] = 'o';
								}
								// put the ships in ships array	
								for(int s = 0; s < compShips[i].length; s++){
									compShips[i][s] = (rowInt * 10) + colInt - s;
								}
							} else {
								continue;
							}
						} else {
							continue;
						}
					}// end of direction left

					// up
					if (dir == 2) {

						if (rowInt - compShips[i].length >= -1) {
							int count = 0;
							for (int k = 0; k < compShips[i].length; k++) {
								if (compBoard[rowInt -k][colInt] == ' ') {
									count++;
								}
							}
							if (count == compShips[i].length) {
								// if everything OK, add ship to array
								for (int j = rowInt; j > rowInt - compShips[i].length; j--) {
									compBoard[j][colInt] = 'o';
								}
								// put the ships in ships array	
								for(int s = 0; s < compShips[i].length; s++){
									compShips[i][s] = ((rowInt * 10) - (s * 10)) + colInt;
								}
							} else {
								continue;
							}
						} else {
							continue;
						}
					}// end of direction up
					
					// down
					if (dir == 3) {
						if (rowInt + compShips[i].length <= 10) {
							int count = 0;
							for (int k = 0; k < compShips[i].length; k++) {
								if (compBoard[rowInt + k][colInt] == ' ') {
									count++;
								}
							}
							if (count == compShips[i].length) {
								// if everything OK, add ship to array
								for (int j = rowInt; j < compShips[i].length + rowInt; j++) {
									compBoard[j][colInt] = 'o';
								}
								// put the ships in ships array	
								for(int s = 0; s < compShips[i].length; s++){
									compShips[i][s] = ((rowInt * 10) + (s * 10)) + colInt;
								}
							} else {
								continue;
							}
						} else {
							continue;
						}
					}// end of direction down
					//printIntArr(compShips);
					i++;
				} // end of placing computer ships

		// ask player to place ships
		printBoard(playerBoard);
		for (int i = 0; i < shipNames.length;) {

			System.out.println("To place your " + shipNames[i] + ", enter a Row Letter followed by a Column Number:");
			String coordinate = input.next();
			char row = coordinate.charAt(0);
			char col = coordinate.charAt(1);

			// check validity of row and col
			// col has to be between a and j or A and J
			// row has to be between 0-9

			
			try {

				if (playerBoard[Character.getNumericValue(row) - 10][Character.getNumericValue(col)] != ' ') {
					System.out.println("That spot's already taken!");
					continue;
				} else if (((row >= 'A' && row <= 'J') || (row >= 'a' && row <= 'j'))
						&& (coordinate.length() == 2)) {
					System.out.println("Enter your " + shipNames[i] + "'s direction (left, right, up, down):");
				} else {
					System.out.println("Enter a valid coordinate: Row Letter followed by a Column Number");
					continue;
				}
			} catch (Exception e) {
				System.out.println("Enter a valid coordinate: Row Letter followed by a Column Number");
				continue;
			}
			
			String direction = input.next();	
			// check if direction exists, and remember which one
			boolean go = false;
			while(go == false){
				if (search(directions, direction) == true) {
						// System.out.println("That's a valid direction");
						go = true;
					} else {
						System.out.println("Enter a valid direction: left, right, up, down");
						direction = input.next();
						//continue;
					}
			}

			int rowInt = Character.getNumericValue(row) - 10; // -10 to get 0
			int colInt = Character.getNumericValue(col);

			// if direction.equals("right"): check if in bounds column + 5 has
			// to be less than or equal to 10,
			// and if there are other ships there
			if (direction.equals("right")) {
				if (colInt + ships[i].length <= 10) {
					int count = 0;
					for (int k = 0; k < ships[i].length; k++) {
						if (playerBoard[rowInt][colInt + k] == ' ') {
							count++;
						}
					}
					if (count == ships[i].length) {
						// if everything OK, add ship to array
						for (int j = colInt; j < ships[i].length + colInt; j++) {
							playerBoard[rowInt][j] = 'o';
							
							
							}
						// put the ships in ships array	
						for(int s = 0; s < ships[i].length; s++){
							ships[i][s] = (rowInt * 10) + colInt + s;
							// and also in this one to use later for checking sunk ship coordinates.
							ships_sunk[i][s] = (rowInt * 10) + colInt + s;
						}
					} else {
						System.out.println("You can't put one ship on top of another!, try again:");
						continue;
					}
				} else {
					System.out.println("Off the Board!, try again:");
					continue;
				}
			}// end of direction right

			if (direction.equals("left")) {

				if (colInt - ships[i].length >= -1) {
					int count = 0;
					for (int k = 0; k < ships[i].length; k++) {
						if (playerBoard[rowInt][colInt - k] == ' ') {
							count++;
						}
					}
					if (count == ships[i].length) {
						// if everything OK, add ship to array
						for (int j = colInt; j > colInt - ships[i].length; j--) {
							playerBoard[rowInt][j] = 'o';
						}
						// put the ships in ships array	
						for(int s = 0; s < ships[i].length; s++){
							ships[i][s] = (rowInt * 10) + colInt - s;
							// and also in this one to use later for checking sunk ship coordinates.
							ships_sunk[i][s] = (rowInt * 10) + colInt - s;
						}
					} else {
						System.out.println("You can't put one ship on top of another!, try again:");
						continue;
					}
				} else {
					System.out.println("Off the Board!, try again:");
					continue;
				}
			}// end of direction left

			
			if (direction.equals("up")) {

				if (rowInt - ships[i].length >= -1) {
					int count = 0;
					for (int k = 0; k < ships[i].length; k++) {
						if (playerBoard[rowInt -k][colInt] == ' ') {
							count++;
						}
					}
					if (count == ships[i].length) {
						// if everything OK, add ship to array
						for (int j = rowInt; j > rowInt - ships[i].length; j--) {
							playerBoard[j][colInt] = 'o';
						}
						// put the ships in ships array	
						for(int s = 0; s < ships[i].length; s++){
							ships[i][s] = ((rowInt * 10) - (s * 10)) + colInt;
							// and also in this one to use later for checking sunk ship coordinates.
							ships_sunk[i][s] = ((rowInt * 10) - (s * 10)) + colInt;
						}
					} else {
						System.out.println("You can't put one ship on top of another!, try again:");
						continue;
					}
				} else {
					System.out.println("Off the Board!, try again:");
					continue;
				}
			}// end of direction up
			
			// down
			if (direction.equals("down")) {
				if (rowInt + ships[i].length <= 10) {
					int count = 0;
					for (int k = 0; k < ships[i].length; k++) {
						if (playerBoard[rowInt + k][colInt] == ' ') {
							count++;
						}
					}
					if (count == ships[i].length) {
						// if everything OK, add ship to array
						for (int j = rowInt; j < ships[i].length + rowInt; j++) {
							playerBoard[j][colInt] = 'o';
						}
						// put the ships in ships array	
						for(int s = 0; s < ships[i].length; s++){
							ships[i][s] = ((rowInt * 10) + (s * 10)) + colInt;
							// and also in this one to use later for checking sunk ship coordinates.
							ships_sunk[i][s] = ((rowInt * 10) + (s * 10)) + colInt;
						}
					} else {
						System.out.println("You can't put one ship on top of another!, try again:");
						continue;
					}
				} else {
					System.out.println("Off the Board!, try again:");
					continue;
				}
			}// end of direction down
			
			printBoard(playerBoard);
			//printIntArr(ships);
			i++;
		} // end of placing player ships

		
		//printIntArr(compShips);
		
		System.out.println("All your ships are in place! Now let's start shootin'!");
		// TODO remove:
		// printIntArr(ships);
		
		boolean win = false;
		int player = 1; // 1 is human, 0 is computer
		int[] hits = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}; // 
		int hitsIndex = -1;
		
		// game loop
		gameLoop:
		while(win == false){
			
		if(player == 1){
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++ \n Computer Board:");
			// TODO change to printBoard(compBoardView);
			// printBoard(compBoard);
			printBoard(compBoardView);
			 System.out.println("Enter a coordinate on the computer's board (ex: A0):");
			 String coordinate = input.next();
				char row = coordinate.charAt(0);
				char col = coordinate.charAt(1);
				
				int rowInt = Character.getNumericValue(row) - 10; // -10 to get 0
				int colInt = Character.getNumericValue(col);
				
				
				try {
					
					if (((row >= 'A' || row <= 'J') && (row >= 'a' || row <= 'j')) && coordinate.length() == 2) {
						   
						if (compBoard[rowInt][colInt] == 'o') {
							//System.out.println("You hit a ship at index: "+ hitShipRow+ ", box: "+ hitShipBox);
							System.out.println("Yay! - You got a hit!");
							
						//  store in the computer Boards arrays
							compBoard[rowInt][colInt] = '*';
							compBoardView[rowInt][colInt] = '*';
							
							
							// find the location of the hit ship in the ships array
							int hitShipRow = whichShip(compShips, rowInt, colInt); // returns index of hit ship in ships array
							int hitShipBox = whichShipCol(compShips, rowInt, colInt); // returns index of hit ship box in ship
							
							compShips[hitShipRow][hitShipBox] = -1;
							
							
							// check if hit ship was sunk (all boxes in ship equal -1)
							if(isSunk(compShips, hitShipRow) == true){
								System.out.println("And... you sunk the "+shipNames[hitShipRow] + "!");
							}
							
							// check if ALL ships were sunk. 
							if(allSunk(compShips) == true){
								System.out.println("++++++++++++++++++++++++++++++++++++++++++++++ \n Computer Board:");
								printBoard(compBoardView);
								System.out.println("You win!");
								break;
							}
							
						} else if(compBoard[rowInt][colInt] == '-' || compBoard[rowInt][colInt] == '*'){
							System.out.println("You tried that before, have another go...");
							continue;
						}else if(compBoard[rowInt][colInt] == ' '){
							
							//  store in array
							compBoard[rowInt][colInt] = '-';
							compBoardView[rowInt][colInt] = '-';

							System.out.println("Oh well, you missed...");
						}
					} else{
						 System.out.println("Enter a valid coordinate: Row Letter followed by a Column Number");
						 continue;
					}
				} catch (Exception e) { // if somebody enters number followed by letter
					System.out.println("Enter a valid coordinate: Row Letter followed by a Column Number");
					continue;
				}
				
				
			}// end player 1(human) actions

		// computer actions (AI)
		
		
		if(player == 0){
			
			int rowInt = 0; 
			int colInt = 0;
			
			int row = 0;
			int col = 0;
			
			if(isEmpty(hits, -1) == true){
				// if there are no partially hit ships
				// generate random hit 0-9, 0-9
				
				//TODO smarter board search instead of random, based on binary search principle maybe? Or probability? Or something even smarter I don't know about yet :)
				
				Random hit = new Random();
				int rowRan = hit.nextInt(9);
				int colRan = hit.nextInt(9);
				
				while(playerBoard[rowRan][colRan] == '-'  || playerBoard[rowRan][colRan] == '*'){
					
					rowRan = hit.nextInt(9);
					colRan = hit.nextInt(9);
				}
				rowInt = rowRan;
				colInt = colRan;
				
				
			}else{
				// find a partially hit ship 
				// by finding coordinates in the hits array: 
				row = hits[hitsIndex]/10;
				col = hits[hitsIndex]%10;
				int incrementRow = row;
				int incrementCol = col;
				
				if(isSingle(hits, -1) == true){ // ship was only hit once if hits contains only 1 value other than -1 
					// try all 4 directions
					// TODO randomize order of direction - use a switch instead of if/else ? 
					
					// check if potential hit is in bounds and is a space or o
					if((row - 1 >= 0) && (playerBoard[row - 1][col] == ' ' || playerBoard[row - 1][col] == 'o' )){  // try going up
						rowInt = row - 1;
						colInt = col;
					}else if((row + 1 <= 9) && (playerBoard[row + 1][col] == ' ' || playerBoard[row + 1][col] == 'o')){ // try going down
						rowInt = row + 1;
						colInt = col;
					}else if((col + 1 <= 9) && (playerBoard[row][col + 1] == ' ' || playerBoard[row][col + 1] == 'o')){ // try going right
						rowInt = row;
						colInt = col + 1;
					}else if((col - 1 >= 0) && (playerBoard[row][col - 1] == ' ' || playerBoard[row][col - 1] == 'o')){ // go left
						rowInt = row;
						colInt = col - 1;
					}
					
					
				}else if(isVertical(hits) == true){ // ship is vertical - check hits array

					// try up else down
					// TODO randomize order of direction (up or down first) - or maybe that's not a good idea...
										
					if((row - 1 >= 0) && (playerBoard[row - 1][col] == ' ' || playerBoard[row - 1][col] == 'o')){  // try going up
						rowInt = row - 1;
						colInt = col;
					}else if((row + 1 <= 9) && (playerBoard[row + 1][col] == ' ' || playerBoard[row + 1][col] == 'o')){ // go down
						rowInt = row + 1;
						colInt = col;
					}else if((col + 1 <= 9) && (playerBoard[row][col + 1] == ' ' || playerBoard[row][col + 1] == 'o')){ // try going right
						rowInt = row;
						colInt = col + 1;
					}else if((col - 1 <= 0) && (playerBoard[row][col - 1] == ' ' || playerBoard[row][col - 1] == 'o')){ // go left
						rowInt = row;
						colInt = col - 1;
					}
					
					
				}else{ // ship is horizontal
					// try left else right
					// TODO randomize order of direction (left or right first)
					
					if((col + 1 <= 9) && (playerBoard[row][col + 1] == ' ' || playerBoard[row][col + 1] == 'o')){ // try going right
						rowInt = row;
						colInt = col + 1;
					}else if ((col - 1 >= 0) && (playerBoard[row][col - 1] == ' ' || playerBoard[row][col - 1] == 'o')){ // go left
						rowInt = row;
						colInt = col - 1;
					}else if((row - 1 >= 0) && (playerBoard[row - 1][col] == ' ' || playerBoard[row - 1][col] == 'o')){  // try going up
						rowInt = row - 1;
						colInt = col;
					}else if((row + 1 <= 9) && (playerBoard[row + 1][col] == ' ' || playerBoard[row + 1][col] == 'o')){ // go down
						rowInt = row + 1;
						colInt = col;
					}
					}
				
				
			}
					   
					if (playerBoard[rowInt][colInt] == 'o') {
						//System.out.println("You hit a ship at index: "+ hitShipRow+ ", box: "+ hitShipBox);
						System.out.println("Computer got a hit!" + rowInt+":"+colInt);
						
						//  store in the playerBoard array
						playerBoard[rowInt][colInt] = '*';
						hits[hitsIndex + 1] = rowInt * 10 + colInt;
						
						// TODO remove
						// printNumArr(hits);
						
						hitsIndex++;
						
						
						// find the location of the hit ship in the ships array
						int hitShipRow = whichShip(ships, rowInt, colInt); // returns index of hit ship in ships array
						int hitShipBox = whichShipCol(ships, rowInt, colInt); // returns index of hit ship box in the hit ship
						
						// set it to -1 for later use, to check if ship is sunk
						ships[hitShipRow][hitShipBox] = -1; 
						
						
						// check if hit ship was sunk (all boxes in ship equal -1)
						if(isSunk(ships, hitShipRow) == true){
							System.out.println("And... computer sunk your "+shipNames[hitShipRow] + "!");
							
							// find the coordinates for ships[hitShipRow] and remove them from hits
							for(int i = 0; i < ships_sunk[hitShipRow].length; i++){
								for(int j = 0; j < hits.length; j++){
									if(ships_sunk[hitShipRow][i] == hits[j]){
										hits[j] = -1;
									}
								}
									
							}
							
							// 
							// shift all coordinates to the left, and set the hitsIndex to last actual coordinate
							sortHits(hits);
							
							// we'll be left with just hits but not sunk ships in the hits array.
							hitsIndex = lastIndex(hits);
							
						}
						
						// check if ALL ships were sunk. 
						if(allSunk(ships) == true){
							System.out.println("++++++++++++++++++++++++++++++++++++++++++++++ \n Your Board:");
							printBoard(playerBoard);
							System.out.println("Oh well - Computer won...");
							return;
						}
						
					} else if(playerBoard[rowInt][colInt] == '-' || playerBoard[rowInt][colInt] == '*'){
						hitsIndex = 0; // TODO let's try hitting in a different direction - (needs work)
						continue gameLoop;
					}else if(playerBoard[rowInt][colInt] == ' '){
						
						//  store the miss in playerBoard array
						playerBoard[rowInt][colInt] = '-';
						System.out.println("Computer missed - Yay for you!");
						// TODO remove
						 System.out.println("Computer missed at: " + rowInt+":"+colInt);
						// printNumArr(hits);
					}
				
			
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++ \n Your Board:");
			printBoard(playerBoard);
			
		}
		
		// toggle player...
		if (player == 1) {
			player = 0;
		} else {
			player = 1;
		}
	}// end game loop
		
	}

	public static void printBoard(char[][] array) {
		System.out.print("  ");
		for (int col = 0; col < array.length; col++) {
			System.out.printf("%-1s %-2d", " ", col);
		}

		System.out.println("\n   -----------------------------------------");
		for (int j = 0; j < array.length; j++) {
			System.out.printf("%-2c", 'A' + j);
			for (int i = 0; i < array.length; i++) {

				System.out.printf("%-1s %-2c", "|", array[j][i]);

			}
			System.out.println("|\n   -----------------------------------------");
		}
	}

	public static boolean search(String[] array, String str) {
		boolean found = false;
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(str)) {
				found = true;
				break; // no need to keep searching if str is found
			}
		}
		return found;
	}

	public static int whichShip(int[][] array, int row, int col){
	 //find which ship got hit (find coordinate in ships array, and return the index)
		int result = 0;
		for (int i = 0; i < array.length; i++){
			for (int j = 0; j < array[i].length; j++){
				
				if(array[i][j] == (row * 10) + col){
					result = i;
					break;
				}
				
			 }
		}
		return result;
	}
	public static int whichShipCol(int[][] array, int row, int col){
		 //find where ship got hit (find coordinate in ships array, and return the col index)
			int result = 0;
			for (int i = 0; i < array.length; i++){
				for (int j = 0; j < array[i].length; j++){
					
					if(array[i][j] == (row * 10) + col){
						result = j;
						break;
					}
					
				 }
			}
			return result;
		}
	
	public static boolean isSunk(int[][] array, int hitShip){
		boolean sunk = false;
		int count = 0;
			for(int i = 0; i < array[hitShip].length; i++){
				if(array[hitShip][i] == -1){
					count++;
				}
			}
			if(count == array[hitShip].length){
				sunk = true;
			}
		return sunk;
		
	}
	public static boolean allSunk(int[][] array){
		boolean sunk = false;
		int count = 0; // where value == -1
		int countInners = 0; // the length of an inner array
		
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				if(array[i][j] == -1){
					count++;
				}
			}
			countInners += 	array[i].length;
		}
		if(count == countInners){
			sunk = true;
		}
		return sunk;
	}
	
	
	// sort highest to lowest
	public static void sortHits(int[] array){ 
		int temp = 0;
		for(int i = 0; i < (array.length-1); ++i){
		   for(int j=0; j < (array.length-1); ++j){
		      if(array[j] < array[j+1]){
		             temp = array[j];
		             array[j] = array[j+1];
		             array[j+1] = temp;
		          }
		      }
		    }
		}
	
	// sort lowest to highest
/*		public static void sortHits(int[] array){ 
			int temp = 0;
			for(int i = 0; i < (array.length-1); ++i){
			   for(int j=0; j < (array.length-1); ++j){
			      if(array[j] > array[j+1]){
			             temp = array[j];
			             array[j] = array[j+1];
			             array[j+1] = temp;
			          }
			      }
			    }
			}*/
	
	
	public static boolean isVertical(int[] array){
		boolean vertical = false;
			if(array[0] % 10 == array[1] % 10){
				vertical = true;
			}
			
		return vertical;
	}
	

	// get lastIndex by counting the number of values in hits that are not -1
	// lastIndex(hits);
	public static int lastIndex(int[] array){
		
		int count = 0;
		for(int i = 0; i < array.length; i++){
			if(array[i] != -1){
				count++;
			}
		}
		return count - 1;
		
		
	}
	
	
	public static void printArr(String[] array) {
		for (String x : array) {
			System.out.print(x + ", ");
		}

	}
	public static void printNumArr(int[] array) {
		for (int x : array) {
			System.out.print(x + ", ");
		}

	}
	public static void printIntArr(int[][] array) {
		System.out.println("-------------");
		for (int j = 0; j < array.length; j++) {
			for (int i = 0; i < array[j].length; i++) {
				System.out.printf("%-1s %-2d", "|", array[j][i]);
				
			}
			System.out.println("|\n-------------");
		}
	}
	
	public static boolean isSingle(int[] array, int value){
		boolean single = false;
		int count = 0;
		for(int i = 0; i < array.length; i++){
			if(array[i] != value){
				count++;
			}
		}
		if(count == 1){
			single = true;
		}
		
		return single;
	}
	public static boolean isEmpty(int[] array, int value){
		boolean empty = false;
		int count = 0;
		for(int i = 0; i < array.length; i++){
			if(array[i] != value){
				count++;
			}
		}
		if(count == 0){
			empty = true;
		}
		
		return empty;
	}

}
