package fr.Jodge.elementalLibrary.integration;

import java.util.ArrayList;
import java.util.List;

import fr.Jodge.elementalLibrary.log.ElementalCrashReport;

public class ElementalIntegrationHelper
{
	protected static List<Class<? extends IElementalIntegration>> modIntegrationList = new ArrayList<Class<? extends IElementalIntegration>>();
	
	public static boolean registerClass(Class<? extends IElementalIntegration> clazz)
	{
		if(!modIntegrationList.contains(clazz))
		{
			modIntegrationList.add(clazz);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static void initializeClass()
	{
		for(Class<? extends IElementalIntegration> clazz : modIntegrationList)
		{
			IElementalIntegration obj;
			try
			{
				obj = clazz.newInstance();
				obj.initElement();
			}
			catch (Throwable throwable) 
			{
				String text = "Can't instatiate " + clazz + ". We are unable to register value.";
				ElementalCrashReport.crashReport(throwable, text);
			}
			
		}
	}
}
