package me.boxcubed.main.Objects;

public interface LivingEntity extends Entity {
	public double getHealth();
	public void setHealth(double health);
	public double getMaxHealth();
	public default boolean isAlive(){
		if(getHealth()>0){
			return true;
		}
		return false;
	};
}
  
