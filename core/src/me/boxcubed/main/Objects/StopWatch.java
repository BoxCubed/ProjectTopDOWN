package me.boxcubed.main.Objects;

public class StopWatch {

	private long startTime = 0;
	private long stopTime = 0;
	private boolean running = false;
	private int counter=0;

	public void start() {
		if(counter<=0){
			running = true;
			System.out.println("Timer Started");
			this.startTime = System.currentTimeMillis();
			this.running = true;
			counter++;
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void stop() {
		this.stopTime = System.currentTimeMillis();
		this.running = false;
	}

	public void reset() {
		stop();
		stopTime=0;
		startTime=0;
		counter=0;
	}

	// elaspsed time in milliseconds
	public long getElapsedTime() {
		long elapsed;
		if (running) {
			elapsed = (System.currentTimeMillis() - startTime);
		} else {
			elapsed = (stopTime - startTime);
		}
		return elapsed;
	}

	// elaspsed time in seconds
	public long getElapsedTimeSecs() {
		long elapsed;
		if (running) {
			elapsed = ((System.currentTimeMillis() - startTime) / 1000);
		} else {
			elapsed = ((stopTime - startTime) / 1000);
		}
		return elapsed;
	}
}