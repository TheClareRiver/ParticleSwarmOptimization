//Creates a 2D Vector for storing multidimensional positions and velocities

public class Vector2D {

	private double vector[];  

	public Vector2D(int size) 
	{
		vector = new double[size];
	}

	public Vector2D(int size, double initialValue) 
	{
		vector = new double[size];
		for(int i=0; i<size; i++)
		{
			vector[i] = initialValue;
		}
	}

	public double getValue(int i) 
	{
		return vector[i];
	}

	public void setValue(int i, double value) 
	{	
		vector[i] = value;
	}

	public int size() 
	{
		return vector.length;
	}

	public void copy(Vector2D v) 
	{
		for(int i=0; i<vector.length; i++)
		{
			vector[i] = v.getValue(i);
		}
	}

	public Vector2D returnCopy() 
	{
		Vector2D v = new Vector2D(vector.length);
		for(int i=0; i<vector.length; i++)
		{
			v.setValue(i, vector[i]);
		}
		return v;
	}
	
	public void addVector(Vector2D v) 
	{
		for (int i=0; i<vector.length; i++)
		{
			vector[i] = vector[i]+v.getValue(i);	
		}
	}

	//subtract two vectors and return a third vector that is the result
	public static Vector2D subtractVectors(Vector2D v1, Vector2D v2) 
	{
		Vector2D v = new Vector2D(v1.size());
		for (int i=0; i<v.size(); i++)
		{
			v.setValue(i, v1.getValue(i) - v2.getValue(i));
		}
		return v;
	}
	
	public void multiplyByNumber(double number) 
	{
		for (int i=0; i<vector.length; i++)
		{
			vector[i] = vector[i]*number;
		}
	}

	//multiply each element of the vector by a value in [lowValue, highValue]
	public void multiplyByRandomNumber(double lowValue, double highValue) 
	{
		double range = highValue - lowValue;
		for (int i=0; i<vector.length; i++)
		{
			vector[i] = vector[i]*(lowValue+(PSO.getRandomDouble()*range));
		}
	}

}