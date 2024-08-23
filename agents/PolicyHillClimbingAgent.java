package agents;

public class PolicyHillClimbingAgent implements Agent {

    private double[] policy;
    private double[] valueEstimates;
    private double learningRate;
    private double explorationRate;
    private int numberOfActions;

    public PolicyHillClimbingAgent(int numberOfActions) {
        this.numberOfActions = numberOfActions;
        policy = new double[numberOfActions];
        valueEstimates = new double[numberOfActions];

        // Initialize policy to be uniform
        for (int i = 0; i < numberOfActions; i++) {
            policy[i] = 1.0 / numberOfActions;
            valueEstimates[i] = 0.0; // Initial estimates
        }

        learningRate = 0.01; // Learning rate for value updates
        explorationRate = 0.1; // Exploration rate for policy improvement
    }

    @Override
    public double actionProb(int i) {
        return policy[i];
    }

    @Override
    public int selectAction() {
        // Select action based on policy probabilities
        double r = Math.random();
        double cumulativeProbability = 0.0;
        for (int i = 0; i < numberOfActions; i++) {
            cumulativeProbability += policy[i];
            if (r <= cumulativeProbability) {
                return i;
            }
        }
        return numberOfActions - 1; // Fallback
    }

    @Override
    public void update(int own, int other, double reward) {
        // Update value estimates based on reward
        valueEstimates[own] += learningRate * (reward - valueEstimates[own]);

        // Policy improvement step (explore new policies by adjusting probabilities slightly)
        for (int i = 0; i < numberOfActions; i++) {
            if (i == own) {
                policy[i] = Math.min(policy[i] + explorationRate, 1.0);
            } else {
                policy[i] = Math.max(policy[i] - explorationRate / (numberOfActions - 1), 0);
            }
        }

        // Normalize policy to ensure it's a valid probability distribution
        normalizePolicy();
    }

    private void normalizePolicy() {
        double sum = 0;
        for (double p : policy) {
            sum += p;
        }
        for (int i = 0; i < policy.length; i++) {
            policy[i] /= sum;
        }
    }

    @Override
    public double getQ(int i) {
        // Return the value estimate for action i
        return valueEstimates[i];
    }
}
