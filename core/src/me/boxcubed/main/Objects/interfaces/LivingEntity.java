package me.boxcubed.main.Objects.interfaces;

public interface LivingEntity extends Entity {
    double getHealth();

    void setHealth(double health);

    double getMaxHealth();

    boolean isAlive();


    boolean isDisposable();

    void setDisposable(boolean disposable);
}
  
