package fr.Jodge.elementalLibrary.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import fr.Jodge.elementalLibrary.common.function.JLog;

public class Element
{
	
	public final static int NORMAL = 0;
	public final static int FIRE = 1;
	public final static int WATER = 2;
	public final static int WIND = 3;
	public final static int DIRT = 4;
	public final static int WOOD = 5;
	public final static int THUNDER = 6;
	public final static int HOLY = 7;
	public final static int DARK = 8;
	
	private static List<String> keyName = null;

	static
	{
		// we init some var
		keyName = new ArrayList<String>();
		keyName.add(NORMAL, "normal");
		keyName.add(FIRE, "fire");
		keyName.add(WATER ,"water");
		keyName.add(WIND ,"wind");
		keyName.add(DIRT ,"dirt");
		keyName.add(WOOD, "wood");
		keyName.add(THUNDER, "thunder");
		keyName.add(HOLY, "holy");
		keyName.add(DARK, "dark");
	}
	
	
	public static int getNumberOfElement() {return keyName.size();}
		
	public static String getKey(int keyWord)
	{
		String key;
		if(keyWord < keyName.size())
			return keyName.get(keyWord);
		else
			return null;
	}
	
	public static int getIndex(String keyWord)
	{
		int index = 0;
		if(keyName.contains(keyWord))
			while(!keyName.get(index).equalsIgnoreCase(keyWord))
				index++;
		else
			index = -1;
		return index;
	}
	

	


}
