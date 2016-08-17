package fr.Jodge.elementalLibrary;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class StaffOfDebuging extends Item
{
	public StaffOfDebuging()
	{
		String name = "staff_of_debuging";
        maxStackSize = 1;
		setUnlocalizedName(name);
		setCreativeTab(CreativeTabs.REDSTONE);
		setRegistryName (name);
		GameRegistry.register(this);
		Main.constante.registerTexture(this);
	}
	
    public boolean isDebugMode()
    {
        return ElementalConfiguration.WINDOW_DEBUG;
    }
    

    /**
     * Returns true if the item can be used on the given entity, e.g. shears on sheep.
     */
    @Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
    {
    	if(ElementalConfiguration.WINDOW_DEBUG)
    		Main.DEBUG_WINDOWS.displayEntity(target);
        return false;
    }
    


}
