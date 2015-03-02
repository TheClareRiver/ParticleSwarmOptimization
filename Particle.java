//Creates every particle
//Updates the particle positions & velocities

public class Particle {

	private int particle;
	private Vector2D position;  
	private Vector2D velocity; 
	private Solution currentSolution;
	private Solution personalBest;

	public Particle(int particleID) 
	{	
		particle = particleID;

		position = new Vector2D(PSO.numberOfDimensions);
		for(int i=0; i<position.size(); i++) 
		{
			position.setValue(i, (TestFunctions.INITIAL_MIN_VALUES[PSO.function]+(TestFunctions.INITIAL_RANGES[PSO.function]*PSO.getRandomDouble())));
		}

		double results = -1;
		if(PSO.function == PSO.ACKLEY)
		{
			results = TestFunctions.evaluateAckley(position);
		}
		else if(PSO.function == PSO.RASTRIGIN)
		{
			results = TestFunctions.evaluateRastrigin(position);
		}
		else if(PSO.function == PSO.ROSENBROCK)
		{
			results = TestFunctions.evaluateRosenbrock(position);
		}
		else if(PSO.function == PSO.SPHERE)
		{
			results = TestFunctions.evaluateSphere(position);
		}
		else
		{
			System.out.println("Function Evaluation Error");
		}

		//can send position to itself because the Solution constructor makes a copy of the position 
		currentSolution = new Solution(particle, results, position, 0);
		personalBest = currentSolution.getCopy();

		//check if update to global-best solution is necessary
		if (personalBest.getFunctionValue() < PSO.globalBest.getFunctionValue()) 
		{  
			PSO.globalBest.setParticleNumber(personalBest.getParticleNumber());
			PSO.globalBest.setFunctionValue(personalBest.getFunctionValue());
			PSO.globalBest.copyFromPosition(personalBest.getPosition());
			PSO.globalBest.setIterationCreated(0);	
		}

		//start with a small random velocity
		velocity = new Vector2D(PSO.numberOfDimensions);
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
			velocity.setValue(i, (minimumSpeed+(speedRange*PSO.getRandomDouble())));
		}
	}

	public static Particle[] initializeParticles()
	{                                                                  
		Particle[] particleArray = new Particle[PSO.totalNumberOfParticles];     
		for(int i=0; i<PSO.totalNumberOfParticles; i++)
		{
			particleArray[i] = new Particle(i);
		}
		return particleArray;
	}

	//update the position and velocity
	public static Solution update(Particle particleToUpdate, Particle neighborhoods[][]) 
	{
		//to start, acceleration = 0.0
		Vector2D acceleration = new Vector2D(PSO.numberOfDimensions, 0.0);

		//neighborhood component
		Vector2D neighborhoodBestPosition = returnBestPositonInNeighborhood(particleToUpdate, neighborhoods); 
		Vector2D neighborhoodComponent = Vector2D.subtractVectors(neighborhoodBestPosition, particleToUpdate.position);
		neighborhoodComponent.multiplyByRandomNumber(0.0, PSO.neighborhoodTheta);
		acceleration.addVector(neighborhoodComponent);

		//particle component
		Vector2D personalComponent = Vector2D.subtractVectors(particleToUpdate.personalBest.getPosition(), particleToUpdate.position);
		personalComponent.multiplyByRandomNumber(0.0, PSO.personalTheta);
		acceleration.addVector(personalComponent);

		//update the velocity and apply the constriction factor
		particleToUpdate.velocity.addVector(acceleration);
		particleToUpdate.velocity.multiplyByNumber(PSO.constrictionFactor);

		//make sure velocity isn't going out of bounds
		for (int i=0; i<PSO.numberOfDimensions; i++) 
		{
			if (particleToUpdate.velocity.getValue(i) < TestFunctions.SPEED_MIN_VALUES[PSO.function]) //lower than the minimum velocity speed
			{
				particleToUpdate.velocity.setValue(i, TestFunctions.SPEED_MIN_VALUES[PSO.function]);	
			}
			else if (particleToUpdate.velocity.getValue(i) > TestFunctions.SPEED_MAX_VALUES[PSO.function]) //higher than the maximum velocity speed
			{
				particleToUpdate.velocity.setValue(i, TestFunctions.SPEED_MAX_VALUES[PSO.function]);	
			}
		}

		//move the particle 
		particleToUpdate.position.addVector(particleToUpdate.velocity); 

		//evaluate the new position
		double newParticleValue = TestFunctions.evaluate(particleToUpdate.position);

		//set the current solution
		particleToUpdate.currentSolution.setFunctionValue(newParticleValue);
		particleToUpdate.currentSolution.copyFromPosition(particleToUpdate.position);
		particleToUpdate.currentSolution.setIterationCreated(PSO.currentIterationNumber);

		//update the personal-best (if necessary)
		if (newParticleValue < particleToUpdate.personalBest.getFunctionValue()) 
		{
			particleToUpdate.personalBest.setFunctionValue(newParticleValue);
			particleToUpdate.personalBest.copyFromPosition(particleToUpdate.position);
			particleToUpdate.personalBest.setIterationCreated(PSO.currentIterationNumber);
		}

		//update the global-best (if necessary)
		if (particleToUpdate.personalBest.getFunctionValue() < PSO.globalBest.getFunctionValue()) 
		{  
			PSO.globalBest.setParticleNumber(particleToUpdate.personalBest.getParticleNumber());
			PSO.globalBest.setFunctionValue(particleToUpdate.personalBest.getFunctionValue());
			PSO.globalBest.copyFromPosition(particleToUpdate.personalBest.getPosition());
			PSO.globalBest.setIterationCreated(particleToUpdate.personalBest.getIterationCreated());	
		}
		
		return particleToUpdate.currentSolution;
	}	

	public static Vector2D returnBestPositonInNeighborhood(Particle particleToUpdate, Particle neighborhoods[][])
	{
		double neighborhoodBestParticleValue = particleToUpdate.personalBest.getFunctionValue(); //best particle value in the neighborhood
		Vector2D neighborhoodBestPositon = particleToUpdate.personalBest.getPosition(); //position of the particle with the best value in neighborhood

		Particle neighborhoodParticles[] = neighborhoods[particleToUpdate.particle]; //array of particle's neighbors
		for (int i=0; i<PSO.particlesPerNeighborhood; i++) //for each neighbor...
		{
			double neighborValue = neighborhoodParticles[i].personalBest.getFunctionValue(); //get the neighbor's personal best value
			if (neighborValue < neighborhoodBestParticleValue) //check against current smallest value in neighborhood, update if smaller 
			{
				neighborhoodBestParticleValue = neighborValue; //new best value in neighborhood
				neighborhoodBestPositon = neighborhoodParticles[i].personalBest.getPosition(); //new best position in neighborhood
			}
		}
		return neighborhoodBestPositon;
	}

}