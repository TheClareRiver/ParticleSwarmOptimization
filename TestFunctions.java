//Creates 4 test functions: Ackley, Rastrigin, Rosenbrock, Sphere
//Evaluates a particle value for a given test function

public class TestFunctions {

	public static final int NUMBER_OF_FUNCTIONS = 4;
	public static final double UNIVERSAL_MIN_INITIAL_SPEED = -2.0;
	public static final double UNIVERSAL_SPEED_RANGE = 4.0;

	//ACKLEY FUNCTION:
	//minimum is 0.0, which occurs at (0.0,...,0.0)
	public static final int ACKLEY_FUNCTION_NUMBER = 0;

	private static final double ACKLEY_INITIAL_MIN_VALUE = 16.0; 
	private static final double ACKLEY_INITIAL_MAX_VALUE = 32.0;   
	private static final double ACKLEY_INITIAL_RANGE = ACKLEY_INITIAL_MAX_VALUE - ACKLEY_INITIAL_MIN_VALUE;

	private static final double ACKLEY_SPEED_MIN_VALUE = -2.0;
	private static final double ACKLEY_SPEED_MAX_VALUE = 4.0;
	private static final double ACKLEY_SPEED_RANGE = ACKLEY_SPEED_MAX_VALUE - ACKLEY_SPEED_MIN_VALUE;

	public static final double ACKLEY_OPTIMAL_COORDINATE = 0.0; 
	public static final double ACKLEY_SHIFT_RANGE = ACKLEY_INITIAL_MAX_VALUE - ACKLEY_OPTIMAL_COORDINATE;

	//RASTRIGIN FUNCTION:
	//minimum is 0.0, which occurs at (0.0,...,0.0)
	public static final int RASTRIGIN_FUNCTION_NUMBER = 1;

	private static final double RASTRIGIN_INITIAL_MIN_VALUE = 2.56;
	private static final double RASTRIGIN_INITIAL_MAX_VALUE = 5.12;
	private static final double RASTRIGIN_INITIAL_RANGE = RASTRIGIN_INITIAL_MAX_VALUE - RASTRIGIN_INITIAL_MIN_VALUE;

	private static final double RASTRIGIN_SPEED_MIN_VALUE = -2.0;
	private static final double RASTRIGIN_SPEED_MAX_VALUE = 4.0;
	private static final double RASTRIGIN_SPEED_RANGE = RASTRIGIN_SPEED_MAX_VALUE - RASTRIGIN_SPEED_MIN_VALUE;

	public static final double RASTRIGIN_OPTIMAL_COORDINATE = 0.0; 
	public static final double RASTRIGIN_SHIFT_RANGE = RASTRIGIN_INITIAL_MAX_VALUE - RASTRIGIN_OPTIMAL_COORDINATE;

	//ROSENBROCK FUNCTION:
	//minimum is 0.0, which occurs at (1.0,...,1.0)
	public static final int ROSENBROCK_FUNCTION_NUMBER = 2;

	private static final double ROSENBROCK_INITIAL_MIN_VALUE = 15.0;
	private static final double ROSENBROCK_INITIAL_MAX_VALUE = 30.0;
	private static final double ROSENBROCK_INITIAL_RANGE = ROSENBROCK_INITIAL_MAX_VALUE - ROSENBROCK_INITIAL_MIN_VALUE;

	private static final double ROSENBROCK_SPEED_MIN_VALUE = -2.0;
	private static final double ROSENBROCK_SPEED_MAX_VALUE = 2.0;
	private static final double ROSENBROCK_SPEED_RANGE = ROSENBROCK_SPEED_MAX_VALUE - ROSENBROCK_SPEED_MIN_VALUE;

	public static final double ROSENBROCK_OPTIMAL_COORDINATE = 1.0; 
	public static final double ROSENBROCK_SHIFT_RANGE = ROSENBROCK_INITIAL_MAX_VALUE - ROSENBROCK_OPTIMAL_COORDINATE;

	//SPHERE FUNCTION:
	//minimum is 0.0, which occurs at (0.0,...,0.0)
	public static final int SPHERE_FUNCTION_NUMBER = 3;

	private static final double SPHERE_INITIAL_MIN_VALUE = 50.0;
	private static final double SPHERE_INITIAL_MAX_VALUE = 80.0;
	private static final double SPHERE_INITIAL_RANGE = SPHERE_INITIAL_MAX_VALUE - SPHERE_INITIAL_MIN_VALUE;

	private static final double SPHERE_SPEED_MIN_VALUE = UNIVERSAL_MIN_INITIAL_SPEED;
	private static final double SPHERE_SPEED_MAX_VALUE = 4.0;
	private static final double SPHERE_SPEED_RANGE = UNIVERSAL_SPEED_RANGE;

	public static final double SPHERE_OPTIMAL_COORDINATE = 0.0; 
	public static final double SPHERE_SHIFT_RANGE = SPHERE_INITIAL_MAX_VALUE - SPHERE_OPTIMAL_COORDINATE;

	//FUNCTION ARRAYS:
	public static final double[] INITIAL_MIN_VALUES = {ACKLEY_INITIAL_MIN_VALUE, RASTRIGIN_INITIAL_MIN_VALUE, ROSENBROCK_INITIAL_MIN_VALUE, SPHERE_INITIAL_MIN_VALUE};
	public static final double[] INITIAL_MAX_VALUES = {ACKLEY_INITIAL_MAX_VALUE, RASTRIGIN_INITIAL_MAX_VALUE, ROSENBROCK_INITIAL_MAX_VALUE, SPHERE_INITIAL_MAX_VALUE};
	public static final double[] INITIAL_RANGES = {ACKLEY_INITIAL_RANGE, RASTRIGIN_INITIAL_RANGE, ROSENBROCK_INITIAL_RANGE, SPHERE_INITIAL_RANGE};

	public static final double[] SPEED_MIN_VALUES = {ACKLEY_SPEED_MIN_VALUE, RASTRIGIN_SPEED_MIN_VALUE, ROSENBROCK_SPEED_MIN_VALUE, SPHERE_SPEED_MIN_VALUE};
	public static final double[] SPEED_MAX_VALUES = {ACKLEY_SPEED_MAX_VALUE, RASTRIGIN_SPEED_MAX_VALUE, ROSENBROCK_SPEED_MAX_VALUE, SPHERE_SPEED_MAX_VALUE};
	public static final double[] SPEED_RANGES = {ACKLEY_SPEED_RANGE, RASTRIGIN_SPEED_RANGE, ROSENBROCK_SPEED_RANGE, SPHERE_SPEED_RANGE};

	public static final double[] OPTIMAL_COORDINATES = {ACKLEY_OPTIMAL_COORDINATE, RASTRIGIN_OPTIMAL_COORDINATE, ROSENBROCK_OPTIMAL_COORDINATE, SPHERE_OPTIMAL_COORDINATE};
	public static final double[] SHIFT_RANGE = {ACKLEY_SHIFT_RANGE, RASTRIGIN_SHIFT_RANGE, ROSENBROCK_SHIFT_RANGE, SPHERE_SHIFT_RANGE};

	/*
	 * METHODS:
	 */

	public static double evaluate(double position[])
	{
		double results = -1;
		if(PSO.function == PSO.ACKLEY)
		{
			results = evaluateAckley(position);
		}
		else if(PSO.function == PSO.RASTRIGIN)
		{
			results = evaluateRastrigin(position);
		}
		else if(PSO.function == PSO.ROSENBROCK)
		{
			results = evaluateRosenbrock(position);
		}
		else if(PSO.function == PSO.SPHERE)
		{
			results = evaluateSphere(position);
		}
		return results;
	}

	//Ackley Function:
	//minimum is 0.0, which occurs at (0.0,...,0.0)
	public static double evaluateAckley(double z[]) 
	{
		double Sum1 = 0.0;
		double Sum2 = 0.0;
		for (int i=0; i<z.length; i++) 
		{
			double xi = z[i];
			Sum1 += xi * xi;
			Sum2 += Math.cos(2.0*Math.PI*xi);

		}
		return -20.0*Math.exp(-0.2*Math.sqrt(Sum1/z.length))-Math.exp(Sum2/z.length)+20.0+Math.E;   
	}	

	//Rastrigin Function:
	//minimum is 0.0, which occurs at (0.0,...,0.0)
	public static double evaluateRastrigin(double z[]) 
	{
		double returnValue = 0;
		for (int i=0; i<z.length; i++) 
		{
			double xi = z[i];
			returnValue += xi*xi-10.0*Math.cos(2.0*Math.PI*xi);
		}
		return returnValue+10.0;
	}

	//Rosenbrock Function:
	//minimum is 0.0, which occurs at (1.0,...,1.0)
	public static double evaluateRosenbrock(double z[]) 
	{
		double returnValue = 0;
		for (int i=0; i<z.length-1; i++) 
		{
			double xi = z[i];
			double xiPlusOne = z[i+1];
			returnValue += 100.0*Math.pow(xiPlusOne-xi*xi, 2.0)+Math.pow(xi-1.0, 2.0);
		}	
		return returnValue;
	}

	//Sphere function:
	//minimum is 0.0, which occurs at (0.0,...,0.0)
	public static double evaluateSphere(double z[]) 
	{
		double sumOfSquares = 0.0;
		for (int i=0; i<z.length; i++) 
		{
			double xi = z[i];
			sumOfSquares = sumOfSquares+(xi*xi);
		}
		return sumOfSquares;  
	}	

}