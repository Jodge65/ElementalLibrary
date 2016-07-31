package fr.Jodge.elementalLibrary.event;

import java.util.List;

import fr.Jodge.elementalLibrary.damage.DamageHelper;
import fr.Jodge.elementalLibrary.damage.EntityElementalDamageSource;
import fr.Jodge.elementalLibrary.data.DataHelper;
import fr.Jodge.elementalLibrary.data.ElementalDataSerializers;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalDamageSource;
import fr.Jodge.elementalLibrary.data.matrix.FinalMatrix;
import fr.Jodge.elementalLibrary.function.JLog;
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
		if(source instanceof IElementalDamageSource)
		{
			// if event is an instance of IElementalDamageSource, then we already pass trough this place and don't need it anymore
			event.setCanceled(false);
			return;
		}
		else
		{
			attacker = source.getEntity();
		}
			
		EntityLivingBase target = event.getEntityLiving();
		boolean needCanceld = false;

		// check of attacker
		if(attacker == null)
		{
			try
			{
				DamageHelper.ElementalizeDamageSource(target, source, event.getAmount());
				needCanceld = true;
			}
            catch (Throwable throwable)
            {
            	String text = "A problem occur when " + target.getName() + " is hit by " + source.getDamageType() + "\n"
        				+	JLog.getDetails((EntityLivingBase) attacker)
        				+	JLog.getDetails(target)
        				;
            	
            	JLog.crashReport(throwable, text);
            }
			
			// if is null then we have a basic source of damage
			//JLog.write("### SOURCE SANS ENTITE : " + source.getDamageType()); 
			// TODO Damage Source external (lava, thunder...)
		}
		else
		{
			// else we have an entity which deal damage
			if(attacker instanceof EntityLivingBase)
			{
				try
				{
					// If attacker is an instance of living base, then it's something alive, and source came from EntityDamageSource
					FinalMatrix damageMatrix = DamageHelper.calculDamage((EntityLivingBase)attacker, target, event.getAmount());
					DamageHelper.dealDamage((EntityLivingBase)attacker, target, (EntityDamageSource)source, damageMatrix);
					needCanceld = true;
				}
	            catch (Throwable throwable)
	            {
	            	String text = "A problem occur when " + attacker.getName() + " try to hit " + target.getName() + "\n"
            				+	JLog.getDetails((EntityLivingBase) attacker)
            				+	JLog.getDetails(target)
            				;
	            	
	            	JLog.crashReport(throwable, text);
	            }
			}
			else
			{
				// else, it's an other kind of entity... 
				JLog.write("### SOURCE heuuu... : " + source + ", Entité: " + attacker); 
				// TODO DamageSource without entity

			}
		}
		
		event.setCanceled(needCanceld);
	}
}