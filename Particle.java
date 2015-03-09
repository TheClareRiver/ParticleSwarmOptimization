//Creates every particle
//Updates the particle positions & velocities

public class Particle {

	public int particle;
	private double position[];  
	private double velocity[]; 
	private Solution currentSolution;
	private Solution personalBest;

	public Particle(int particleID) 
	{	
		particle = particleID;

		position = new double[PSO.numberOfDimensions];
		for(int i=0; i<position.length; i++) 
		{
			position[i] = (TestFunctions.INITIAL_MIN_VALUES[PSO.function]+(TestFunctions.INITIAL_RANGES[PSO.function]*PSO.getRandomDouble()));
		}

		//evaluate the position
		double result = TestFunctions.evaluate(position);

		currentSolution = new Solution(particle, result, position, 0);
		
		personalBest = new Solution(particle, result, position, 0);
		
		//check if update to global-best solution is necessary
		if (personalBest.getFunctionValue() < PSO.globalBest.getFunctionValue()) 
		{  
			PSO.globalBest.setFunctionValue(currentSolution.getFunctionValue());
			PSO.globalBest.setPosition(currentSolution.getPosition());
			PSO.globalBest.setIterationCreated(currentSolution.getIterationCreated());
		}

		//start with a small random velocity
		velocity = new double[PSO.numberOfDimensions];
		double minimumSpeed = 0.0;
		double speedRange = 0.0;

		//limit the initial speed to a small amount
		if (TestFunctions.UNIVERSAL_SPEED_RANGE < TestFunctions.SPEED_RANGES[PSO.function]) 
		{
			minimumSpeed = TestFunctions.UNIVERSAL_MIN_INITIAL_SPEED;
			speedRange = TestFunctions.UNIVERSAL_SPEED_RANGE;
		}
		else 
		{
			minimumSpeed = TestFunctions.SPEED_MIN_VALUES[PSO.function];
			speedRange = TestFunctions.SPEED_RANGES[PSO.function];			
		}

		for(int i=0; i<PSO.numberOfDimensions; i++) 
		{
			velocity[i] = (minimumSpeed+(speedRange*PSO.getRandomDouble()));
		}
	}

	public static Particle[] initializeParticles()
	{                                                                  
		Particle particleArray[] = new Particle[PSO.totalNumberOfParticles];     
		for(int i=0; i<PSO.totalNumberOfParticles; i++)
		{
			particleArray[i] = new Particle(i);
		}
		return particleArray;
	}

	//update the position and velocity
	public static Solution update(Particle particleToUpdate) 
	{
		//to start, acceleration = 0.0
		double acceleration[] = new double[PSO.numberOfDimensions];

		//neighborhood component
		double neighborhoodBestPosition[] = returnBestPositonInNeighborhood(particleToUpdate);
		double neighborhoodComponent[] = new double[neighborhoodBestPosition.length];
		double neighborhoodRange = PSO.neighborhoodTheta;
		for (int i=0; i<neighborhoodComponent.length; i++)
		{
			neighborhoodComponent[i] = (neighborhoodBestPosition[i]-particleToUpdate.position[i])*(PSO.getRandomDouble()*neighborhoodRange);
			acceleration[i] = acceleration[i] + neighborhoodComponent[i];
		}

		//particle component
		double personalBestPosition[] = particleToUpdate.personalBest.getPosition();
		double personalComponent[] = new double[personalBestPosition.length];
		double personalRange = PSO.personalTheta;
		for (int i=0; i<personalComponent.length; i++)
		{
			personalComponent[i] = (personalBestPosition[i]-particleToUpdate.position[i])*(PSO.getRandomDouble()*personalRange);
			acceleration[i] = acceleration[i] + personalComponent[i];
		}

		//update the velocity and apply the constriction factor
		for (int i=0; i<personalComponent.length; i++)
		{
			particleToUpdate.velocity[i] = (particleToUpdate.velocity[i] + acceleration[i])*PSO.constrictionFactor;

			//make sure velocity isn't going out of bounds
			if (particleToUpdate.velocity[i] < TestFunctions.SPEED_MIN_VALUES[PSO.function]) //lower than the minimum velocity speed
			{
				particleToUpdate.velocity[i] = TestFunctions.SPEED_MIN_VALUES[PSO.function];	
			}
			else if (particleToUpdate.velocity[i] > TestFunctions.SPEED_MAX_VALUES[PSO.function]) //higher than the maximum velocity speed
			{
				particleToUpdate.velocity[i] = TestFunctions.SPEED_MAX_VALUES[PSO.function];	
			}
			
			//move the particle 	
			particleToUpdate.position[i] = particleToUpdate.position[i] + particleToUpdate.velocity[i]; 
		}
		
		//save old particle value for comparison
		double oldPersonalBestValue = particleToUpdate.personalBest.getFunctionValue();

		//evaluate the new position
		double newParticleValue = TestFunctions.evaluate(particleToUpdate.position);

		//set the current solution
		particleToUpdate.currentSolution.setFunctionValue(newParticleValue);
		particleToUpdate.currentSolution.setPosition(particleToUpdate.position);
		particleToUpdate.currentSolution.setIterationCreated(PSO.currentIterationNumber);

		//update the personal-best (if necessary)
		if (newParticleValue < oldPersonalBestValue) 
		{
			particleToUpdate.personalBest.setFunctionValue(newParticleValue);
			particleToUpdate.personalBest.setPosition(particleToUpdate.position);
			particleToUpdate.personalBest.setIterationCreated(PSO.currentIterationNumber);
			
			//update the global-best (if necessary)
			if (newParticleValue < PSO.globalBest.getFunctionValue()) 
			{  
				PSO.globalBest.setParticleNumber(particleToUpdate.personalBest.getParticleNumber());
				PSO.globalBest.setFunctionValueG(newParticleValue);
				PSO.globalBest.setPosition(particleToUpdate.personalBest.getPosition());
				PSO.globalBest.setIterationCreated(particleToUpdate.personalBest.getIterationCreated());	
			}
		}

		return particleToUpdate.currentSolution;
	}	

	public static double[] returnBestPositonInNeighborhood(Particle particleToUpdate)
	{
		double neighborhoodBestParticleValue = particleToUpdate.personalBest.getFunctionValue(); //best particle value in the neighborhood
		double neighborhoodBestPosition[] = particleToUpdate.personalBest.getPosition(); //position of the particle with the best value in neighborhood

		Particle neighborhoodParticles[] = new Particle[PSO.particlesPerNeighborhood];
		neighborhoodParticles = PSO.neighborhoods[particleToUpdate.particle]; //array of particle's neighbors
		
		for (int i=0; i<PSO.particlesPerNeighborhood; i++) //for each neighbor...
		{
			double neighborValue = neighborhoodParticles[i].personalBest.getFunctionValue(); //get the neighbor's personal best value
			
			if (neighborValue < neighborhoodBestParticleValue) //check against current smallest value in neighborhood, update if smaller 
			{
				neighborhoodBestParticleValue = neighborValue; //new best value in neighborhood
				neighborhoodBestPosition = neighborhoodParticles[i].personalBest.getPosition(); //new best position in neighborhood
			}
		}
		return neighborhoodBestPosition;
	}

}