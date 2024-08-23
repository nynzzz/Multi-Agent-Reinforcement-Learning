package agents;

public interface Agent {

	public abstract double actionProb(int i);
	
	public abstract int selectAction();
	
	public abstract void update(int own, int other, double reward);

	public abstract double getQ(int i);
	
}
