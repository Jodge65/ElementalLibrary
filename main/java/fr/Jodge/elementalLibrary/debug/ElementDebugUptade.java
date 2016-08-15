package fr.Jodge.elementalLibrary.debug;

import fr.Jodge.elementalLibrary.data.element.Element;

public class ElementDebugUptade extends Thread
{
	protected static int actualSize = -1;
	
	@Override
	public void run()
	{
		while(true)
		{
			int size = Element.getAllActiveElement().size();
			if(actualSize != size)
			{
				actualSize = size;
				update();
			}
			try{sleep(ElementalLibraryDebug.debugSpeed);}
			catch (InterruptedException e){}
		}
	}
	
	public static void update()
	{
		//caseElementID, caseElementName, caseElementActive;
		String listOfElementId = "  ID  \n";
		String listOfElementName = "  Name  \n";
		String listOfElementActive = "  isActive  \n";
		for(Element element : Element.getAllActiveElement())
		{
			listOfElementId += " " + element.getId() + "  \n";
			listOfElementName += " " + element.getName() + "  \n";
			listOfElementActive += " " + element.isActive() + "  \n";
		}
		ElementalLibraryDebug.caseElementID.setText(listOfElementId);
		ElementalLibraryDebug.caseElementName.setText(listOfElementName);
		ElementalLibraryDebug.caseElementActive.setText(listOfElementActive);

	}
}