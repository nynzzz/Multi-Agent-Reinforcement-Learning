package agents;

public class QLearnerBoltzmann implements Agent {

	private double Q[], alpha, alphadecay, temp, tempdecay;
	
	public QLearnerBoltzmann(int numberOfActions) {
		Q = new double[numberOfActions];
		for (int i=0; i<numberOfActions; i++)
			Q[i] = -0.1+Math.random()*0.2;
		temp = 0.1;
		tempdecay = 1.0;
		alpha = 0.01;
		alphadecay = 1.0;
	}
	
	public double actionProb(int index) {
		double sum = 0.0;
		for (double a : Q) 
			sum += Math.exp(a/temp);
		return Math.exp(Q[index]/temp)/sum;
	}
	
	public int selectAction() {
		temp *= tempdecay;
		double target = Math.random();
		double collected = actionProb(0);
		int index = 1;
		while (collected < target && index<Q.length) 
			collected += actionProb(index++);
		return index-1;
	}
	
	public void update(int own, int other, double reward) {
		update(own,reward);
	}
	
	private void update(int index, double reward) {
		Q[index] = Q[index] + alpha * (reward-Q[index]);
		alpha*=alphadecay;
	}

	@Override
	public double getQ(int i) {
		return Q[i];
	}
	
}
