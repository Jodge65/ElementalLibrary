package fr.Jodge.elementalLibrary.integration.base;

import baubles.common.Config;
import fr.Jodge.elementalLibrary.data.ItemHelper;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.integration.IElementalIntegration;
import fr.Jodge.elementalLibrary.log.JLog;

public class BaubleHelper implements IElementalIntegration
{
	@Override
	public void initElement()
	{
		JLog.info("Initialization of Bauble Value");
		
		// not needed in this case :)
		/** Default Normal Element */
		//Element normal = Element.addOrGet("normal");
		/** Default Fire Element */
		//Element fire = Element.addOrGet("fire");
		/** Default Water Element */
		//Element water = Element.addOrGet("water");
		/** Default Wind Element */
		//Element wind = Element.addOrGet("wind");
		/** Default Dirt Element */
		//Element dirt = Element.addOrGet("dirt");
		/** Default Wood Element */
		//Element wood = Element.addOrGet("wood");
		/** Default Thunder Element */
		//Element thunder = Element.addOrGet("thunder");
		/** Default Holy Element */
		//Element holy = Element.addOrGet("holy");
		/** Default Dark Element */
		//Element dark = Element.addOrGet("dark");
		/** Default Poison Element */
		//Element poison = Element.addOrGet("poison");
		/** Default Hunger Element */
		//Element hunger = Element.addOrGet("hunger");
		
		for(Element e : Element.getAllElement())
		{
			e.setDefaultValue(shi, ItemHelper.getUnlocalizedName(Config.itemRing), 0.98F); // 2% free def, yay !
		}
		

	}
}
