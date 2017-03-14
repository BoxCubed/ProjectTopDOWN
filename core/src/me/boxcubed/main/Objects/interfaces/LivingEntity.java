package me.boxcubed.main.Objects.interfaces;

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
	@Override
	default boolean isDisposable() {
		return !isAlive();
	}
	@Override
	default void setDisposable(boolean disposable) {
		if(disposable)
			setHealth(0);
		
	}
}
  
