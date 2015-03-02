//Stores each particle's relevant solution variables
//Each particle has a current solution and a personal-best solution
//The best of all particle solutions becomes the single global-best solution

public class Solution {

	private int particleNumber;
	private double value;
	private Vector2D position;
	private int iterationCreated;

	public Solution() 
	{
		particleNumber = 0;
		value = Double.MAX_VALUE;
		position = null;
		iterationCreated = 0;
	}

	public Solution(int newParticleNumber, double newValue, Vector2D newPosition, int newIterationCreated) 
	{
		particleNumber = newParticleNumber;
		value = newValue;
		position = newPosition.returnCopy();
		iterationCreated = newIterationCreated;
	}

	public Solution getCopy() 
	{
		return  new Solution(particleNumber, value, position, iterationCreated);
	}

	public void copyFrom(Solution s) 
	{
		particleNumber = s.getParticleNumber();
		value = s.getFunctionValue();
		position = s.getPositionCopy();
		iterationCreated = s.getIterationCreated();
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

	public void setFunctionValue(double newValue) 
	{
		value = newValue;
	}

	// return the actual position
	public Vector2D getPosition() 
	{
		return position;
	}

	// return a copy of the position
	public Vector2D getPositionCopy() 
	{
		return position.returnCopy();
	}

	// copy from a given position to this position
	public void copyFromPosition(Vector2D inPosition) 
	{
		position.copy(inPosition);
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
