public class Polynomial {
	private int n;		// n = number of variables
	private int degree; 
	private double [][] coefs;
	
	//constructors
	public Polynomial() {
		this.n = 0;
		this.degree = 0;
	}
	
	//getters
	public int getN() {return this.n;}
	public int getDegree() {return this.degree;}
	public double[][] getCoefs() {return this.coefs;}
	
	//setters
	public void setCoefList (double [][] j) {this.coefs = j;}
	public void setCoef(int j, int d, double a) {this.coefs[j][d] = a;}
	public void setDegree(int j) {this.degree = j;}
	public void setN (int j) {this.n = j;}
	
	//other functions
	public void getPolynomial() {
		int N = Pro4_phamngu6.getInt("Enter number of variables (0 to cancel): ", 0 , 2147483647);
		if(N == 0) {
			System.out.println("\nProcess canceled. No changes made to polynomial function.");
			return;
		}
		this.n = N;
		int degree = Pro4_phamngu6.getInt("Enter polynomial degree (0 to cancel): ", 0 , 2147483647);
		if(degree == 0) {
			System.out.println("\nProcess canceled. No changes made to polynomial function.");
			return;
		}
		this.degree = degree;
			
		this.coefs = new double[this.n][this.degree + 1];
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < this.degree + 1; j++) {
				this.coefs[i][j] = 0;
			}
		}
		for(int i = 0; i < this.n; i++) {
			System.out.format("Enter coefficients for variable x%d: %n",i + 1);
			for(int d = 0; d < this.degree + 1; d++) {
				String prompt = String.format("   Coefficient %d: ", d + 1);
				double coefs = Pro4_phamngu6.getDouble(prompt, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
				this.setCoef(i, d, coefs);
			}
		}
		System.out.println("");
		System.out.println("Polynomial complete!");
	}
	
	public double f(double[] x) {
		double result = 0;
		for(int var = 0 ; var < this.n ; var++) { 
			for(int d = 0; d < this.degree; d++) {
				result += this.coefs[var][d] * Math.pow(x[var], (this.degree - d));
			}
			result += this.coefs[var][this.degree];
		}
		return result;
	}

	// gradient at point x = [x1, x2, etc.]
	public double[] gradient(double[] x) {
		double[] grad = new double[this.n]; 
		for(int var = 0; var < this.n; var++) { 
			double pardiff = 0;
			for(int d = 0; d < this.degree; d++) {
				pardiff += this.coefs[var][d] * (this.degree - d) * Math.pow(x[var], (this.degree - d -1));
			}
			grad[var] = pardiff;
		}
		return grad;
	}
	
	// magnitude of gradient at point x = [x1, x2, etc.]
	public double gradNorm(double[] x) {
		double[] grad = this.gradient(x);
		double total = 0;
		for(int i = 0; i < grad.length; i++) {
			total += grad[i] * grad[i];
		}
		return Math.sqrt(total); 
	}
	
	public void print() {
		System.out.print("f(x) = ");
		for(int i = 0; i < this.n; i++) {
			System.out.print("(");
			for(int j = 0; j < this.degree; j++) {
				System.out.format(" %.2fx%d^%d +", this.coefs[i][j], i + 1, this.degree - j);				
			}
			System.out.format(" %.2f )", coefs[i][this.degree]);
			if(i+1 < n) {
				System.out.print(" + ");
			}
		}
		System.out.println("");
	}
}
