//Creates a particle matrix based on 1 of the 4 neighborhood topologies: Global, Ring, vonNeumann, and Random
//Establishes each particle's neighborhood

public class NeighborhoodTopologies {

	//the particle matrix variables
	private static int particleMatrix[][];
	private static int size;
	
	//create a particleMatrix of a given size -> initialized to one of the neighborhood topologies
	public NeighborhoodTopologies() 
	{
		size = PSO.totalNumberOfParticles;
		particleMatrix = new int[size][size]; //initialize everything to 0
		
		//GLOBAL
		if (PSO.topology == PSO.GLOBAL) 
		{
			setAllTo(1);
		}

		//RING
		else if (PSO.topology == PSO.RING) 
		{
			for (int i=1; i<size; i++) 
			{
				particleMatrix[i][i] = 1;

				//then add in left and right neighbors...

				if (i == 0) //special start in first row:
				{
					particleMatrix[0][size-1] = 1;
					particleMatrix[0][1] = 1;
				}
				else if (i == size-1) //special end in last row:
				{
					particleMatrix[size-1][size-2] = 1;
					particleMatrix[size-1][0] = 1;
				}
				else
				{
					particleMatrix[i][i-1] = 1;
					particleMatrix[i][i+1] = 1;
				}
			}
		}

		//vonNEUMANN
		else if (PSO.topology == PSO.VONNEUMANN) 
		{
			//to add in the 4 neighbors, must get the particle grid dimensions from PSO
			int numberOfRowsInGrid = PSO.numberOfRowsVonNeumann;
			int numberOfColumnsInGrid = PSO.numberOfColumnsVonNeumann;

			for (int i=0; i<size; i++) //for each row in the matrix:
			{
				particleMatrix[i][i] = 1;	

				//calculate the grid location [row, column] of the particle
				int row = i/numberOfColumnsInGrid;
				int column = (i%numberOfColumnsInGrid)-1;

				//calculate the grid location [row, column] for each neighbor
				int neighborGridLocation;
				//north neighbor:
				int northRow;
				if (row-1 < 0)
				{
					northRow = numberOfRowsInGrid-1;
				}
				else
				{
					northRow = row-1;
				}
				int northColumn = column;
				neighborGridLocation = (northRow*numberOfColumnsInGrid)+northColumn;
				particleMatrix[i][neighborGridLocation] = 1;

				//east neighbor:
				int eastRow = row;
				int eastColumn = (column+1)%numberOfColumnsInGrid;
				neighborGridLocation = (eastRow*numberOfColumnsInGrid)+eastColumn;
				particleMatrix[i][neighborGridLocation] = 1;

				//south neighbor:
				int southRow = (row+1)%numberOfRowsInGrid;
				int southColumn = column;
				neighborGridLocation = (southRow*numberOfColumnsInGrid)+southColumn;
				particleMatrix[i][neighborGridLocation] = 1;

				// west neighbor:
				int westRow = row;
				int westColumn;
				if (column-1 < 0)
				{
					westColumn = numberOfColumnsInGrid-1;
				}
				else
				{
					westColumn = column-1;
				}
				neighborGridLocation  = (westRow*numberOfColumnsInGrid)+westColumn;
				particleMatrix[i][neighborGridLocation ] = 1;
			}
		}

		//RANDOM
		else if (PSO.topology == PSO.RANDOM) 
		{
			for (int i=0; i<size; i++) 
			{
				particleMatrix[i][i] = 1;
				
				for (int j=1; j<PSO.k; ) //then add in k-1 random neighbors...
				{
					int randomParticle = PSO.getRandomInteger(size); //choose a random particle
					if (particleMatrix[i][randomParticle] != 1) //make sure particle isn't already in neighborhood 
					{
						particleMatrix[i][randomParticle] = 1; //add particle
						j++;
					}
				}
			}
		}
		else 
		{
			System.out.println("Neighborhood Formation Error");
		} 
	}

	//generate and return an array of neighborhood particle numbers
	public static int[] getNeighborhood(int particle) 
	{
		int neighbors[] = new int[PSO.particlesPerNeighborhood];
		int first = 0;
		for (int i=0; i<size; i++) 
		{		
			if (particleMatrix[particle][i] == 1) //if a neighbor is found
			{
				neighbors[first] = i; //add it to the array
				first++;
			}
		}
		return neighbors; 
	}

	//set all probabilities in the matrix to an integer 
	public void setAllTo(int number) 
	{
		for (int i=0; i<size; i++) 
		{
			for (int j=0; j<size; j++) 
			{
				particleMatrix[i][j] = number;
			}
		}
	}

}