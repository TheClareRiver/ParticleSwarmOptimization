//Stores each particle's relevant solution variables
//Each particle has a current solution and a personal-best solution
//The best of all particle solutions becomes the single global-best solution

public class Solution {

	private int particleNumber;
	private double value;
	private double position[];
	private int iterationCreated;

	public Solution() 
	{
		particleNumber = 0;
		value = Double.MAX_VALUE;
		position = null;
		iterationCreated = 0;
	}

	public Solution(int newParticleNumber, double newValue, double newPosition[], int newIterationCreated) 
	{
		particleNumber = newParticleNumber;
		value = newValue;
		position = newPosition;
		iterationCreated = newIterationCreated;
	}

	public int getParticleNumber() 
	{
		return particleNumber;
	}
	
	public void setParticleNumber(int newParticleNumber) 
	{
		particleNumber = newParticleNumber;
	}

	public double getFunctionValue() 
	{
		return value;
	}
	
	public void setFunctionValueG(double newGBest)
	{
		
		value = newGBest;
	}

	public void setFunctionValue(double newValue) 
	{
		value = newValue;
	}

	public double[] getPosition() 
	{
		return position;
	}
	
	public void setPosition(double newPosition[]) 
	{
		position = newPosition;
	}

	public int getIterationCreated() 
	{
		return iterationCreated;
	}
	
	public void setIterationCreated(int newIterationCreated) 
	{
		iterationCreated = newIterationCreated;
	}

}
