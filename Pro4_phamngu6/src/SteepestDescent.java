import java.util.ArrayList;
import java.util.Arrays;

public class SteepestDescent {
	private double eps ; // tolerance
	private int maxIter ; // maximum number of iterations
	private double stepSize ; // step size alpha
	private double x0 ; // starting point
	
	private ArrayList < double[] > bestPoint = new ArrayList <double[]>() ; // best point found for all polynomials
	private double [] bestObjVal ; // best obj fn value found for all polynomials (last point)
	private double [] bestGradNorm ; // best gradient norm found for all polynomials
	private long[] compTime ; // computation time needed for all polynomials
	private int [] nIter ; // no. of iterations needed for all polynomials
	
	private boolean resultsExist = false; // whether or not results exist
	
	// constructors
	public SteepestDescent () {
		this.eps = 0.001;
		this.maxIter = 100;
		this.stepSize = 0.05;
		this.x0 = 1.00;
		this.resultsExist = false;
	}
	
	// getters
	public boolean hasResult() { return this.resultsExist;}
	public double getEps() { return this.eps;}
	public int getMaxIter() {return this.maxIter;}
	public double getStepSize() {return this.stepSize;}
	public double[] getBestObjVal() {return this.bestObjVal;}
	public double[] getBestGradNorm() {return this.bestGradNorm;}
	public double getX0() {return this.x0;}
	public double[] getBestPoint(int i) {return this.bestPoint.get(i);}
	public int[] getNIter() {return this.nIter;}
	public long[] getCompTime() {return this.compTime;}
	
	// setters
	public void setHasResults(boolean a) {this.resultsExist = a;}
	public void setEps(double a) {this.eps = a;}
	public void setMaxIter(int a) {this.maxIter = a;}
	public void setStepSize(double a) {this.stepSize = a;}
	public void setBestObjVal(int i, double a) {this.bestObjVal[i] = a;}
	public void setBestGradNorm ( int i , double a ) {this.bestGradNorm[i] = a;}
	public void setX0(double a) {this.x0 = a;}
	public void setNInter(int i, int a) {this.nIter[i] = a;}
	public void setCompTime(int i, long a) {this.compTime[i] = a;}

	// other methods
	
	public void run (ArrayList<Polynomial> polylist) { // run the steepest descent algorithm
		int size = polylist.size();
		
		this.bestObjVal = new double [size]; // best obj fn value found for all polynomials (last point found)
		this.bestGradNorm = new double [size]; // best gradient norm found for all polynomials
		this.compTime = new long [size]; // computation time needed for all polynomials
		this.nIter = new int [size]; // no. of iterations needed for all polynomials
		
		// For each polynomials in polylist - j is index of current polynomial
		for (int j = 0; j < size; j++) {
			Polynomial P = polylist.get(j);
			long starttime = System.currentTimeMillis();
			
			int N = P.getN();
			this.nIter[j] = 0;
			
			double [] startingPoint = new double [N];
			for (int i = 0; i < N; i++) { startingPoint[i] = this.x0;}
			this.bestPoint.add(startingPoint);
			
			boolean stop = false; 
			while(!stop) {
				this.bestGradNorm[j] = P.gradNorm(this.bestPoint.get(j));
				this.bestObjVal[j] = P.f(this.bestPoint.get(j));
				
				//Stopping criteria
				if(this.bestGradNorm[j] <= this.eps || this.nIter[j] >= this.maxIter || (Double.isNaN(this.bestGradNorm[j]) && Double.isNaN(this.bestObjVal[j]))) {
					stop = true;
					this.compTime[j] = System.currentTimeMillis() - starttime;
					break;
				}
				this.bestPoint.set(j, this.nextPoint(P, this.bestPoint.get(j)));
				this.nIter[j]++;
			}
		}
		this.resultsExist = true;
		
		for (int i = 0; i < size; i++) {
			System.out.println("Polynomial " + (i+1) + " done in " + this.compTime[i] + " ms.");
			
			
		}
		System.out.println("\nAll polynomials done.");
	}
	
	public double [] nextPoint (Polynomial P, double [] curr) { // find the next point.
		double[] gradient = P.gradient(curr);
	    double[] next = new double[curr.length];
	    for(int i=0; i< curr.length; i++){
	        next[i] = (curr[i] + this.getStepSize() * gradient[i] * -1);
	    }
	    return next; 
	}
	
	
	public void getParamsUser () { // get parameters from user
		double eps = Pro4_phamngu6.getDouble("Enter tolerance epsilon (0 to cancel): ", 0, Double.POSITIVE_INFINITY);
		if(eps == 0) {
			System.out.println("\nProcess canceled. No changes made to algorithm parameters.");
			return;
		}
		else {
			this.setEps(eps);
		}
		
		int maxIter = Pro4_phamngu6.getInt("Enter maximum number of iterations (0 to cancel): ", 0, 10000);
		if(maxIter == 0) {
			System.out.println("\nProcess canceled. No changes made to algorithm parameters.");
			return;
		}
		else {
			this.setMaxIter(maxIter);
		}
		
		double stepSize = Pro4_phamngu6.getDouble("Enter step size alpha (0 to cancel): ", 0, Double.POSITIVE_INFINITY);
		if(stepSize == 0) {
			System.out.println("\nProcess canceled. No changes made to algorithm parameters.");
			return;
		}else {
			this.setStepSize(stepSize);
		}
		
		double startpoint = Pro4_phamngu6.getDouble("Enter value for starting point (0 to cancel): ", Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		if(startpoint == 0) {
			System.out.println("\nProcess canceled. No changes made to algorithm parameters.");
			return;
		}else {
			this.setX0(startpoint);
		}
		
		System.out.println("");
		System.out.println("Algorithm parameters set!");
		this.resultsExist = false;
	}
	
	public void print () { // print algorithm parameters
		System.out.println("Tolerance (epsilon): " + this.eps);
		System.out.println("Maximum iterations: " + this.maxIter);
		System.out.println("Step size (alpha): " + this.stepSize);
		System.out.println("Starting point (x0): " + this.x0);
		System.out.println();
	}

	
	public void printStats () { // print statistical summary of results
		System.out.println();
		System.out.println("Statistical summary:");
	    System.out.println("---------------------------------------------------");
		System.out.println("          norm(grad)       # iter    Comp time (ms)");
		System.out.println("---------------------------------------------------");
		System.out.print("Average");
		System.out.printf("%13.3f%13.3f%18.3f",Average(bestGradNorm), Average(nIter), Average(compTime));
		System.out.println();
		System.out.print("St Dev");
		System.out.printf("%14.3f%13.3f%18.3f",SD(bestGradNorm), SD(nIter), SD(compTime));
		System.out.println();
		System.out.print("Min");
		System.out.printf("%17.3f%13.0f%18.0f", Min(bestGradNorm), Min(nIter), Min(compTime));
		System.out.println();
		System.out.print("Max");
		System.out.printf("%17.3f%13.0f%18.0f",Max(bestGradNorm), Max(nIter),Max(compTime));
		System.out.println();
		System.out.println();
	}
	
	
	public void printAll () {// print final results for all polynomials
		System.out.println();
		System.out.println("Detailed results:");
	    System.out.println("-------------------------------------------------------------------------");
		System.out.println("Poly no.         f(x)   norm(grad)   # iter   Comp time (ms)   Best point");
		System.out.println("-------------------------------------------------------------------------");
		for(int i = 0; i < bestPoint.size();i++) 
		{
			
			System.out.format("%8d%13.6f%13.6f%9d%17d   ",(i+1),this.bestObjVal[i],this.bestGradNorm[i],this.nIter[i],this.compTime[i]);
			for(int j = 0;j < this.getBestPoint(i).length;j++)
            {
				if(j == 0) 
                {
                	System.out.format("%4.4f",this.getBestPoint(i)[j]);
                }
				else {
					System.out.format("%.4f",this.getBestPoint(i)[j]);
				}
				
				if(j<(this.getBestPoint(i).length-1)) {
					System.out.print(", ");
				}
            }
			System.out.println();
				
		}	
	}
	
	
	public double Average (double [] a) 
	{
		double sum = 0;
		for(int i=0;i< a.length;i++) 
		{
			sum += a[i];
		}
		return sum / a.length;
	}
	
	public double Max(double [] a) 
	{
		double max = a[0];  
        
        for (int i = 0; i < a.length; i++) 
        {  
              
           if(a[i] > max) 
           {
        	   max = a[i];
           }
                 
        }  
        return max;
	}
	
	public double Min(double [] a) 
	{
        double min = a[0];  
        
        for (int i = 0; i < a.length; i++) 
        {  
              
           if(a[i] < min) 
           {
        	   min = a[i];
           }         
        }  
        return min;
	}
	
	public static double SD(double [] a) {

	    // get the sum of array
	    double sum = 0.0;
	    for (double i : a) {
	        sum += i;
	    }

	    // get the mean of array
	    int length = a.length;
	    double mean = sum / length;

	    // calculate the standard deviation
	    double standardDeviation = 0.0;
	    for (double num : a) {
	        standardDeviation += Math.pow(num - mean, 2);
	    }

	    return Math.sqrt(standardDeviation / (length-1));
	}
	
	public double Average (int [] a) 
	{
		double sum = 0;
		for(int i=0;i< a.length;i++) 
		{
			sum += a[i];
		}
		return sum / a.length;
	}
	
	public double Max(int [] a) 
	{
		double max = a[0];  
        
        for (int i = 0; i < a.length; i++) 
        {  
              
           if(a[i] > max) 
           {
        	   max = a[i];
           }
                 
        }  
        return max;
	}
	
	public double Min(int [] a) 
	{
        double min = a[0];  
        
        for (int i = 0; i < a.length; i++) 
        {  
              
           if(a[i] < min) 
           {
        	   min = a[i];
           }         
        }  
        return min;
	}
	
	public static double SD(int [] a) {

	    // get the sum of array
	    double sum = 0.0;
	    for (double i : a) {
	        sum += i;
	    }

	    // get the mean of array
	    int length = a.length;
	    double mean = sum / length;

	    // calculate the standard deviation
	    double standardDeviation = 0.0;
	    for (double num : a) {
	        standardDeviation += Math.pow(num - mean, 2);
	    }

	    return Math.sqrt(standardDeviation / (length-1));
	}	
	
	public double Average (long [] a) 
	{
		double sum = 0;
		for(int i=0;i< a.length;i++) 
		{
			sum += a[i];
		}
		return sum / a.length;
	}
	
	public double Max(long [] a) 
	{
		double max = a[0];  
        
        for (int i = 0; i < a.length; i++) 
        {  
              
           if(a[i] > max) 
           {
        	   max = a[i];
           }
                 
        }  
        return max;
	}
	
	public double Min(long [] a) 
	{
        double min = a[0];  
        
        for (int i = 0; i < a.length; i++) 
        {  
              
           if(a[i] < min) 
           {
        	   min = a[i];
           }         
        }  
        return min;
	}
	
	public static double SD(long [] a) {

	    // get the sum of array
	    double sum = 0.0;
	    for (double i : a) {
	        sum += i;
	    }

	    // get the mean of array
	    int length = a.length;
	    double mean = sum / length;

	    // calculate the standard deviation
	    double standardDeviation = 0.0;
	    for (double num : a) {
	        standardDeviation += Math.pow(num - mean, 2);
	    }

	    return Math.sqrt(standardDeviation / (length-1));
	}
	//public void printSingleResult ( int i , boolean rowOnly ) {} // print final result for one polynomial , column header optional
 }

