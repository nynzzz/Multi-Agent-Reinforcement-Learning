package agents;

public class FixedAgent implements Agent {

	// Works only for two actions ... sue me
	
	double firstactionprob = 0.5;
	
	@Override
	public double actionProb(int i) {
		if (i==0)
			return firstactionprob;
		else
			return (1-firstactionprob);
					
	}

	@Override
	public int selectAction() {
		if (Math.random() < firstactionprob) return 0;
		else return 1;
	}

	@Override
	public void update(int own, int other, double reward) {
		// nothing to see here
	}

	@Override
	public double getQ(int i) {
		// doesn't do Q's ...
		return 0;
	}
	
	
	

}
