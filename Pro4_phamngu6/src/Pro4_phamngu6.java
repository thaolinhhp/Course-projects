import java.io.*;
import java.util.ArrayList;


public class Pro4_phamngu6 {
	public static BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
	public static ArrayList<Polynomial> polylist = new ArrayList<Polynomial>();
	//main function
	public static void main(String[] args) throws IOException {
	SteepestDescent sp = new SteepestDescent();
		
	boolean checkEnd = false;
	do { 
		displayMenu();
		String choice = cin.readLine();
		System.out.println(""); 
		
		if(choice.equals("L") || choice.equals("l")){
			loadPolynomialFile();
		}
		
		else if(choice.equals("F") || choice.equals("f")) {
			if(!checkPolySet()) {
		        System.out.println("ERROR: No polynomial functions are loaded!");
			}else {
				printPolynomial();
			}
		}
		else if(choice.equals("C") || choice.equals("c")) {
			polylist.clear();
			sp = new SteepestDescent();
			System.out.println();
	        System.out.println("All polynomials cleared.");
	        System.out.println();
		}
		
		else if(choice.equals("S") || choice.equals("s")) {
				sp.getParamsUser();
			}
			
		
		else if(choice.equals("P") || choice.equals("p")) {
				sp.print(); 
		}
		else if(choice.equals("R") || choice.equals("r")) { 
				if(!checkPolySet()) {
					System.out.println("ERROR: No polynomial functions are loaded!");
				}else {
					sp.run(polylist);
				}
		}
		else if(choice.equals("D") || choice.equals("d")) {
				if(!sp.hasResult()) {
					System.out.println("ERROR: No results exist!"); 
				}else { 
					sp.printAll();
					sp.printStats();
				}
		}
		else if(choice.equals("Q") || choice.equals("q")) {
			checkEnd = true;
			System.out.println("Fin.");
			System.exit(0);
		}
		else {
			System.out.println("ERROR: Invalid menu choice!");
		}
		System.out.println("");
		
	}while(!checkEnd);
}
	
	//other functions
	public static void displayMenu() {
		System.out.println("   JAVA POLYNOMIAL MINIMIZER (STEEPEST DESCENT)");
		System.out.println("L - Load polynomials from file");
		System.out.println("F - View polynomial function");
		System.out.println("C - Clear polynomial functions");
		System.out.println("S - Set steepest descent parameters");
		System.out.println("P - View steepest descent parameters");
		System.out.println("R - Run steepest descent algorithm");
		System.out.println("D - Display algorithm performance");
		System.out.println("Q - Quit\n");
		System.out.print("Enter choice: ");
	}
	
	public static void printPolynomial() {
		System.out.println("---------------------------------------------------------");
        System.out.println("Poly No.  Degree   # vars   Function");
        System.out.println("---------------------------------------------------------");
        
        for (int i=0; i < polylist.size(); i++) {
        	System.out.format("%8d%8d%9d    ",(i+1), polylist.get(i).getDegree(), polylist.get(i).getN());
        	polylist.get(i).print();
        }
        System.out.print("\n");
	}
	
	public static void  loadPolynomialFile () throws IOException {
		System.out.print("Enter file name (0 to cancel): ");
		String filename = cin.readLine ();
		
		if (filename.equals("0")) {

			System.out.println("\nFile loading process canceled.");
					}
		else {
			File filenew = new File (filename) ;
			
			if (filenew.exists()==false) {
				System.out.println();
	            System.out.println("ERROR: File not found!");
			}
	        else {
		       BufferedReader fin = new BufferedReader(new FileReader(filename));
		       String line;
		       double [] coeflist = new double[100];
		       int coefadded = 0;
		       int newPolyadded = 0;
		       
		       Polynomial newPoly = new Polynomial();
		       // current polynomial values
		       int currentdegree = 0, countvar = 0,count = 0;
		       boolean currentValid = true;
		       
	    	   do {
	    		   line = fin.readLine(); 
	    		   if( line == null || line.isBlank() || line.equals("*")) {
	    			// TODO: If * is at beginning of file
	    			   if (currentValid)
	    			   {
	    				   newPoly.setDegree(currentdegree);
		    			   newPoly.setN(countvar);
		    			   double [][] templist = new double[countvar][currentdegree + 1];
		    			   int varadded = 0, on = 0;
		    			   // Add coefs to 2D templist from 1D coeflist
		    			   while (varadded < countvar) {
		    				   for (int i = 0; i < currentdegree + 1; i++) {
		    					   templist[varadded][i] = coeflist[on];
		    					   on++;
		    				   }
		    				   varadded++;
		    			   }
		    			   
		    			   newPoly.setCoefList(templist);
		    			   coeflist = new double[100];
		    			   coefadded = 0;
		    			   polylist.add(newPoly);
		    			   newPolyadded++;
	    			   }
	    			   else {
	    				   System.out.println("\nError: Inconsistent dimensions in polynomial "+ (count+1) +"!");
	    			   }
	    			   currentdegree = 0;
	    			   countvar = 0;
	    			   currentValid = true;
	    			   newPoly = new Polynomial();
	    			   
	    			   count++;	// Index of current polynomial in input file
	    		   }
	    		   else if (currentValid){
	    			   String tl[];
    			       tl = line.split(",");
	    	   
	    			   if (currentdegree == 0)
	    			   {
	    				   currentdegree = tl.length-1;
	    				   for (int i=0; i < tl.length; i++) {
	    					   Double coefficient= Double.parseDouble(tl[i]);
	    					   coeflist[coefadded] = coefficient;
	    					   coefadded++;
	    				   }
	    				   countvar++;
	    			   }
	    			   else if (tl.length-1 == currentdegree) {
	    				   for (int i = 0; i < tl.length; i++) {
	    					   Double coefficient= Double.parseDouble(tl[i]);
	    					   coeflist[coefadded] = coefficient;
	    					   coefadded++;
	    				   }
	    				   countvar++;
	    			   }
	    			   else {
	    				   currentValid = false;
	    				   coefadded = coefadded - currentdegree - 1;
	    				   countvar--;
	    			   }
    			   }
//	    		   line = fin.readLine(); 
		       } while (line != null); // end do while loop
	    	   fin.close();
    		   System.out.println("\n" + newPolyadded + " polynomials loaded!");
	        }
		}
	}

	public static boolean checkPolySet() {
		return (polylist.size() != 0);
	}
	
	// get Double
	public static double getDouble(String prompt, double lower, double upper){
		boolean valid = true;
		double num = 0; 
		
		String lowerstring, upperstring, error;
		if (lower == Double.NEGATIVE_INFINITY) {
			lowerstring = "-infinity";
		}else {
			lowerstring = String.format("%.2f", lower);
		}
		if (upper == Double.POSITIVE_INFINITY) {
			upperstring = "infinity";
		}else {
			upperstring = String.format("%.2f", upper);
		}
		error = "ERROR: Input must be a real number in [" + lowerstring + ", " + upperstring + "]!\n";

		do {
			valid=true;
			System.out.print(prompt);
			try {
				num = Double.parseDouble(cin.readLine());
			}catch(IOException e) {
				System.out.println(error);
				valid=false;
			}catch(NumberFormatException e) {
				System.out.println(error);
				valid=false;
			}
			if(!(num >= lower && num <= upper)) {
				System.out.println(error);
				valid=false;
			}
		} while(!valid);
		return num;
	}
	
	// get Integer
	public static int getInt(String prompt, int lower, int upper) {
		boolean valid = true;
		int num = 0;
		
		String lowerstring, upperstring, error;
		if (lower == -2147483647) {
			lowerstring = "-infinity";
		}else {
			lowerstring = String.format("%d", lower);
		}
		if (upper == 2147483647) {
			upperstring = "infinity";
		}else {
			upperstring = String.format("%d", upper);
		}
		error = "ERROR: Input must be an integer in [" + lowerstring + ", " + upperstring + "]!\n";
	
		do {
			valid=true;
			System.out.print(prompt);
			try {
				num = Integer.parseInt(cin.readLine());
			}catch(IOException e) { 
				System.out.println(error);
				valid=false;
			}catch(NumberFormatException e) {
				System.out.println(error);
				valid=false;
			}
			if(!(num >= lower && num <= upper)) {
				System.out.println(error);
				valid=false;
			}
		} while(!valid);
		return num;
	}
}
