package fr.Jodge.elementalLibrary.area;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Jodge
 *
 */
public class Circle extends Schema
{
	/** Circle List<List<Boolean>>*/
	protected List<List<Boolean>> circle;
	/** Rayon of this Circle */
	protected int rayon;
	/** List of all circle generate at least one time. */
	protected static final List<Circle> PRE_GENERATE_CIRCLE = new ArrayList<Circle>();

	/**
	 *  Basic constructor
	 * @param rayon (int) Rayon of Circle (full circle)
	 * @param gridSize (int - optionnal) size of the grid (if empty size : rayon*2+1). Use to adapt all your object on same grid
	 */
	public Circle(int rayon)
	{
		this(rayon, rayon * 2 + 1);
	}
	public Circle(int rayon, int gridSize)
	{
		circle = adaptOnGrid(generateCircle(rayon), gridSize, rayon * 2 + 1);
		this.rayon = rayon;
	}

	/**
	 * return the tab of boolean of the circle generate
	 * @return (List<List<Boolean>>) 
	 */
	public List<List<Boolean>> getCircle()
	{
		return circle;
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
	 * static function to get circle, whitout generate a Circle var
	 * @param rayon (int) Rayon of Circle (full circle)
	 * @param gridSize (int - optionnal) size of the grid (if empty size : rayon*2+1). Use to adapt all your object on same grid
	 * @return (List<List<Boolean>>) return the circle alone based on a ran
	 */
	public static List<List<Boolean>> getCircle(int rayon)
	{
		return getCircle(rayon, rayon*2+1);
	}
	public static List<List<Boolean>> getCircle(int rayon, int gridSize)
	{
		return createCircle(rayon, gridSize);
	}
	
	/**
	 * Function use to create the final circle (and use a save system to keep value)
	 * @param rayon (int) Rayon of Circle (full circle)
	 * @param gridSize (int - optional) size of the grid (if empty size : rayon*2+1). Use to adapt all your object on same grid
	 * @return (List<List<Boolean>>) return the circle alone based on a ran (not create a Circle variable)
	 */
	protected static List<List<Boolean>> createCircle(int rayon)
	{
		return createCircle(rayon, rayon * 2 + 1);
	}
	protected static List<List<Boolean>> createCircle(int rayon, int gridSize)
	{
		List<List<Boolean>> areaBool = new ArrayList<List<Boolean>>(); // Tab of boolean
		int actualGridSize = rayon * 2 + 1;
		for (int i = 0; i < actualGridSize; i++)
		{
			areaBool.add(new ArrayList<Boolean>());
		}

		for (int i = PRE_GENERATE_CIRCLE.size(); i <= rayon + 1; i++)
		{
			PRE_GENERATE_CIRCLE.add(new Circle(i));
		}

		if(rayon > 0)
			areaBool = PRE_GENERATE_CIRCLE.get(rayon).getCircle();
		else
			areaBool.get(0).add(false);

		return adaptOnGrid(areaBool, gridSize, actualGridSize);
	}

	/**
	 * generate a circle (algorithm) 
	 * @param rayon (int) ran of the circle
	 * @return (List<List<Boolean>>) return the circle  based on a ran
	 */
	protected static List<List<Boolean>> generateCircle(int rayon)
	{
		List<List<Boolean>> areaBool = new ArrayList<List<Boolean>>(); // Tab of boolean

		for (int x = 0; x <= rayon * 2; ++x)
		{
			areaBool.add(new ArrayList<Boolean>());
			for (int z = 0; z <= rayon * 2; ++z)
			{
				if ((Math.pow(x - rayon, 2) + Math.pow(z - rayon, 2)) <= Math.pow(rayon + 0.1F, 2))
				{
					areaBool.get(x).add(true);
				}
				else
				{
					areaBool.get(x).add(false);
				}
			}
		}

		return areaBool;
	}
	
	@Override
	public String toString()
	{
		return "circle :\n" + circle + "\n ran use : " + rayon;
	}

}
