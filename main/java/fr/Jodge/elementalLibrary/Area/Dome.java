package fr.Jodge.elementalLibrary.area;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Jodge
 *
 */
public class Dome extends Schema
{
	/** dome List<List<List<Boolean>>> (3dimensions, Yeay !) */
	public List<List<List<Boolean>>> dome;
	/** Ran of dome */
	public int rayon;

	/**
	 * basic constructor
	 * @param rayon (int) ran of dome
	 * @param gridSize (int) grid where adapt the doom (for complex adaptive structure, have a same grid can be helpful)
	 */
	public Dome(int rayon)
	{
		this(rayon, rayon * 2 + 1);
	}
	public Dome(int rayon, int gridSize)
	{
		dome = createDome(rayon, gridSize);
		this.rayon = rayon;
	}

	/**
	 * set the Dome as an extrude dome
	 * @param wallLenght (int - optional) number of block for the "wall". If empty = 1
	 * @return (Dome)
	 */
	public Dome setExtrudeDome()
	{
		return setExtrudeDome(1);
	}
	public Dome setExtrudeDome(int wallLenght)
	{
		dome = extrudeDome(dome, rayon, wallLenght);;
		return this;
	}
	
	/**
	 * return the tab of boolean of the dome generate
	 * @return (List<List<List<Boolean>>>) 
	 */
	public List<List<List<Boolean>>> getDome()
	{
		return dome;
	}
	
	/**
	 * return the rayon of circle
	 * @return (in)
	 */
	public int getRayon()
	{
		return rayon;
	}
	
	/**
	 * 
	 * @param domeReceive (List<List<List<Boolean>>>) dome to use (/!\ not a JDom var !!!)
	 * @param rayon (int) ran of dome (it's a static methode)
	 * @param wallLenght (int - optional) number of block for the "wall". If empty = 1
	 * @return (List<List<List<Boolean>>>)
	 */
	public static List<List<List<Boolean>>> extrudeDome(List<List<List<Boolean>>> domeReceive, int rayon)
	{
		return extrudeDome(domeReceive, rayon, 1);
	}
	public static List<List<List<Boolean>>> extrudeDome(List<List<List<Boolean>>> domeReceive, int rayon, int wallLenght)
	{
		List<List<List<Boolean>>> intDome = createDome( (rayon - wallLenght) , rayon*2+1);
	
		int limY = domeReceive.size() - 1;
		
		int limX = domeReceive.get(0).get(0).size();
		int limZ = limX;

		boolean dV;
		boolean eDV;
		
		List<List<List<Boolean>>> finalDome = new ArrayList<List<List<Boolean>>>();


		for(int y = 0; y < limY; y++)
		{
			finalDome.add(new ArrayList<List<Boolean>>());
			for(int x = 0; x < limX; x++)
			{
				finalDome.get(y).add(new ArrayList<Boolean>());
				for(int z = 0; z < limZ; z++)
				{
					dV = domeReceive.get(y).get(x).get(z);
					eDV = intDome.get(y).get(x).get(z);
					finalDome.get(y).get(x).add(!(dV == eDV) );
					
					//JLog.write("#BOOL# x=" + x + ", z=" + z + ", y=" + y + "dV : " + dV + ", eDV : " + eDV + ", !(dV == eDV) : " + !(dV == eDV) + ", finaly : " + domeReceive.get(y).get(x).get(z));

				}
			}
		}
		
		return finalDome;
	}
	
	/**
	 * 
	 * @param rayon (int) Rayon of Dome (full dome)
	 * @param grid (int - optional) size of the grid (if empty size : rayon*2+1, height : rayon*2). Use to adapt all your object on same grid
	 * @return (List<List<List<Boolean>>>)
	 */
	public static List<List<List<Boolean>>> createDome(int rayon)
	{
		return createDome(rayon, ((rayon*2)+1));
	}
	public static List<List<List<Boolean>>> createDome(int rayon, int grid)
	{

		List<List<List<Boolean>>> areaBool = new ArrayList<List<List<Boolean>>>(); // Tab of boolean

		int heigth = (grid - 1) / 2 + 1;
		//int domeSize = rayon * 2 + 1;
		
		for (int y = 0; y <= heigth; y++)
		{
			areaBool.add(new ArrayList<List<Boolean>>());
		}
		
		int actualRayon;
		int oldRayon = -1;
		List<List<Boolean>> domeFace = new ArrayList<List<Boolean>>();
		Circle myCircle = new Circle(rayon, grid);
		domeFace.addAll(myCircle.getCircle());
		
		List<List<Boolean>> domeStep = new ArrayList<List<Boolean>>();

		//int numberOfAdd = heigth - (rayon + 1);
		
		for(int y = heigth; y > 0; y--)
		{
			actualRayon = 0;

			if( y != heigth)
			{
				for(int x = 0; x < grid; x++)
				{
					if(domeFace.get(y-1).get(x))
					{
						actualRayon++;
					}
				}
				actualRayon--;
				actualRayon /= 2;
				if(oldRayon != actualRayon)
				{
					domeStep = Circle.createCircle(actualRayon, grid);
				}
			}
			else
			{
				domeStep = domeFace;
			}

			areaBool.get(heigth - y).addAll(domeStep);

			oldRayon = actualRayon;

		} // for y = rayon
		
		return areaBool;
	} 
	
}
