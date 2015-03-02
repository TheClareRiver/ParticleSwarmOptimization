//NIC-Project2
//Written by Gabby Grandin
//March 1st 2015

/*
 * Still Need: Command line input parser -> set variables using input
 */

import java.util.Random;

public class PSO {

	//the usual PSO parameters
	public static double neighborhoodTheta;
	public static double personalTheta;
	public static double theta;
	public static double constrictionFactor;

	//run variables
	public static int totalNumberOfRuns;
	public static int currentRunNumber; 

	//iteration variables
	public static int totalNumberOfIterations;
	public static int currentIterationNumber; 

	//dimension variable
	public static int numberOfDimensions;

	//particle variables
	public static int totalNumberOfParticles;
	public static int particlesPerNeighborhood;
	private static Particle arrayOfParticles[];
	private static Particle neighborhoods[][];
	public static Solution globalBest;

	//neighborhood topology variables
	public static int GLOBAL = 0;
	public static int RING = 1;
	public static int VONNEUMANN = 2;
	public static int RANDOM = 4;
	public static int topology;
	public static int k = 5;
	public static int numberOfRowsVonNeumann; 
	public static int numberOfColumnsVonNeumann; 

	//test function variables
	public static int ACKLEY = 0;
	public static int RASTRIGIN = 1;
	public static int ROSENBROCK = 2;
	public static int SPHERE = 3;
	public static int function;

	//for random numbers
	private static Random randomNumber = new Random();

	public static void main(String[] args) 
	{
		//take in the input from the user
		totalNumberOfParticles = Integer.parseInt(args[1]);
		totalNumberOfIterations = Integer.parseInt(args[2]);
		numberOfDimensions = Integer.parseInt(args[4]);

		System.out.println("\n\nA COMPARISON OF NEIGHBORHOOD TOPOLOGIES IN PARTICLE SWARM OPTIMIZATION");
		System.out.println("Testing the impact of neighborhood toplogys on the performace of the Particle Swarm Optimzation (PSO) algorithm on a st of three common benchmark functions.");
		System.out.println("---------------------");
		System.out.println("By Gabby Grandin, Clarence Johnson, III, Ryan Kulesza, & Andrew Miller-Smith");
		System.out.println("---------------------");


		//standard PSO parameters
		neighborhoodTheta = 2.05;		
		personalTheta = 2.05;
		theta = neighborhoodTheta+personalTheta;
		constrictionFactor = 2.0/(theta-2.0+Math.sqrt(theta*theta-4.0*theta)); //0.7298

		//how many runs?
		totalNumberOfRuns = 50; 

		//how many iterations?
		totalNumberOfIterations = 50000;

		//how many dimensions?
		numberOfDimensions = 30; 

		//how many particles?
		totalNumberOfParticles = 50;

		//which neighborhood topology?
		if(args[0].contains("gl"))
		{
			topology = GLOBAL;
			particlesPerNeighborhood = totalNumberOfParticles;
		}
		else if(args[0].contains("ri"))
		{
			topology = RING;
			particlesPerNeighborhood = 3;
		}
		else if(args[0].contains("vn"))
		{
			topology = VONNEUMANN;
			particlesPerNeighborhood = 5;

			//specify the number of rows and columns for the grid of particles in vonNeumann neighborhood
			int possibleFactors[] = new int[totalNumberOfParticles];
			int numberOfFactors = 0;

			for (int i=1; i<=totalNumberOfParticles; i++) //want uniform grid => find factors of the total number of particles
			{
				if (totalNumberOfParticles%i == 0) //found a factor
				{
					possibleFactors[numberOfFactors] = i;
					numberOfFactors++;
				} 
			}

			int factors[] = new int[numberOfFactors];
			for (int i=0; i<numberOfFactors; i++)
			{
				factors[i] = possibleFactors[i]; //condense the array
			}

			int middleIndex = numberOfFactors/2; 
			int middleFactor = factors[middleIndex]; //find the middle factor (will produce the squarest grid) 

			numberOfRowsVonNeumann = middleFactor;
			numberOfColumnsVonNeumann = totalNumberOfParticles/middleFactor;
		}
		else if (args[0].contains("ra"))
		{
			topology = RANDOM;
			particlesPerNeighborhood = k;
		}
		else
		{
			System.out.println("Topology Selection Error");
		}

		//which test function? 
		if (args[3].contains("ack"))
		{
			function = ACKLEY;
		}
		else if (args[3].contains("ras"))
		{
			function = RASTRIGIN;
		}
		else if (args[3].contains("rok"))
		{
			function = ROSENBROCK;
		}
		else if (args[3].contains("sph"))
		{
			function = SPHERE;
		}
		else
		{
			System.out.println("Function Selection Error");
		}

		System.out.println("\nNEIGHBORHOOD TOPOLOGY: " + args[0] + "\nSWARM SIZE: " + args[1] + "\nITERATIONS: " + args[2]);
		System.out.println("FUNCTION TO OPTIMIZE: " + args[3] + "\nDIMENSIONALITY: " + args [4]);
		/*
		 * Done initializing!
		 * Lets start running tests... 
		 */

		System.out.println();
		System.out.println("Returning the global-best value every 10 iterations, and at the end of each run.");
		System.out.println();

		long startTimeAllRuns = System.currentTimeMillis(); //measure time to complete all runs

		//NUMBER OF RUNS LOOP
		for (int i=0; i<totalNumberOfRuns; i++) //tracking number of runs
		{
			currentRunNumber = i+1;
			System.out.println("Run " +currentRunNumber+ ":");

			//randomly shift the location of the optimum in the function's search space
			double shiftVectorAmount = TestFunctions.SHIFT_RANGE[function]*randomNumber.nextDouble();
			if (randomNumber.nextDouble() < 0.5) 
			{
				shiftVectorAmount = shiftVectorAmount*-1.0;
			}

			//set the function parameters now that the shift for this run is known
			TestFunctions.setShiftVector(new Vector2D(numberOfDimensions, shiftVectorAmount));

			//initialize global-best value to a very large number  
			globalBest = new Solution();

			//initialize the particles, and store them in a particle array
			Particle arrayOfParticles[] = Particle.initializeParticles();  

			//initialize the particle matrix and neighborhoods
			initializeTopology();

			//NUMBER OF ITERATIONS LOOP
			for (int j=0; j<totalNumberOfIterations; j++) //tracking iterations
			{
				currentIterationNumber = j+1; 

				if (topology == RANDOM && j > 0) //in a random topology the particleMatrix and neighborhoods change every iteration 
				{
					initializeTopology(); //randomly reconfigure the particleMatrix and neighborhoods
				}

				//NUMBER OF PARTICLES LOOP
				for (int k=0; k<totalNumberOfParticles; k++) //tracking particles
				{
					Particle.update(arrayOfParticles[k], neighborhoods); //update the particles
				}
				//END PARTICLES LOOP

				if (currentIterationNumber%10 == 0)
				{
					System.out.println(globalBest); //printing global-best value every 10 iterations

					if (currentIterationNumber%10000 == 0)
					{
						System.out.println(); //separate printing global-best values into intervals of 10,000 iterations 
					}
				}
			
			}
			//END ITERATIONS LOOP

			//printing global-best value
			System.out.println("Final Value, Run " +currentRunNumber+ ": " +globalBest);
			System.out.println();
		
		}
		//END RUNS LOOP
		
		long endTimeAllRuns = System.currentTimeMillis(); //end timer
		double secondsPerRun = ((endTimeAllRuns - startTimeAllRuns)*1000.0)/totalNumberOfRuns; //average time per run
		
		System.out.println("DONE");
		System.out.println();
		System.out.println("Average time per run = " +secondsPerRun+ " seconds.");
		System.out.println();
		
	}

	public static void initializeTopology()
	{
		//initialize the particleMatrix given the neighborhood topology
		new NeighborhoodTopologies(); 

		//initialize the neighborhoods, and store them in an array of neighborhoods
		int particalNeighbors[] = new int[particlesPerNeighborhood];   
		for (int p=0; p<totalNumberOfParticles; p++) //for each particle...
		{
			particalNeighbors = NeighborhoodTopologies.getNeighborhood(p); //find the neighbor particle numbers
			for (int q=0; q<particlesPerNeighborhood; q++) //for each neighbor...
			{
				int particleNumber = particalNeighbors[q]; //get the particle number and use it to...
				neighborhoods[p][q] = arrayOfParticles[particleNumber]; //get the actual particle using the particle array, then store the particles in a neighborhood array
			}
		}	
	}

	public static int getRandomInteger(int x)
	{
		return randomNumber.nextInt(x);
	}

	public static double getRandomDouble()
	{
		return randomNumber.nextDouble();
	}

}