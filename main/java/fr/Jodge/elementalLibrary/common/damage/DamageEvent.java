package fr.Jodge.elementalLibrary.common.damage;

import java.util.List;

import fr.Jodge.elementalLibrary.common.data.DataHelper;
import fr.Jodge.elementalLibrary.common.data.ElementalDataSerializers;
import fr.Jodge.elementalLibrary.common.function.JLog;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.EntityDataManager.DataEntry;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.ReportedException;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DamageEvent 
{

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onAttack(LivingAttackEvent event)
	{
		// we take all available variable
		DamageSource source = event.getSource();
		
		Entity attacker = null;
		if(source instanceof EntityElementalDamageSource)
		{
			event.setCanceled(false);
			return;
		}
		else
		{
			attacker = source.getEntity();
		}
		
/*		if(attacker == null && source instanceof EntityDamageSourceIndirect)
		{
			attacker = source.getSourceOfDamage();
		}*/
			
		EntityLivingBase target = event.getEntityLiving();
		boolean needCanceld = false;

		// check of attacker
		if(attacker == null)
		{
			// if is null then we have a basic source of damage
			
		}
		else
		{
			// else we have an entity which deal damage
			if(attacker instanceof EntityLivingBase)
			{
				try
				{
					// If attacker is an instance of living base, then it's something alive, and source came from EntityDamageSource
					DamageHelper.dealDamage((EntityLivingBase)attacker, target, (EntityDamageSource)source);
					needCanceld = true;
				}
	            catch (Throwable throwable)
	            {
	            	
	            	String text = "A problem occur when " + attacker.getName() + "try to hit " + target.getName() + "\n"
            				+	JLog.getDetails((EntityLivingBase) attacker)
            				+	JLog.getDetails(target)
            				;
	            	
	            	JLog.crashReport(throwable, text);
	            	
	            }
			}
			else
			{
				// else, it's an other kind of entity... (if we have EntityDamageSourceIndirect then source.getEntity() will give use the projective)
				
			}
		}
		
		event.setCanceled(needCanceld);
	}
}
