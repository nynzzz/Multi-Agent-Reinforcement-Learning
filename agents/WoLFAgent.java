package agents;

public class WoLFAgent implements Agent {

    private double[] currentPolicy, averagePolicy, valueEstimates;
    private double learningRateWinning, learningRateLosing, deltaWinning, deltaLosing;
    private int numberOfActions;
    private double totalGames;

    public WoLFAgent(int numberOfActions) {
        this.numberOfActions = numberOfActions;
        currentPolicy = new double[numberOfActions];
        averagePolicy = new double[numberOfActions];
        valueEstimates = new double[numberOfActions];

        // Initialize policies to be uniform
        for (int i = 0; i < numberOfActions; i++) {
            currentPolicy[i] = 1.0 / numberOfActions;
            averagePolicy[i] = 1.0 / numberOfActions;
            valueEstimates[i] = 0.0; // Initial value estimates
        }

        learningRateWinning = 0.01; // Learning rate when winning
        learningRateLosing = 0.04; // Learning rate when losing
        deltaWinning = 0.001; // Adjustment when winning
        deltaLosing = 0.002; // Adjustment when losing
        totalGames = 0;
    }

    @Override
    public double actionProb(int i) {
        return currentPolicy[i];
    }

    @Override
    public int selectAction() {
        // Select action based on current policy probabilities
        double r = Math.random();
        double cumulativeProbability = 0.0;
        for (int i = 0; i < numberOfActions; i++) {
            cumulativeProbability += currentPolicy[i];
            if (r <= cumulativeProbability) {
                return i;
            }
        }
        return numberOfActions - 1; // Fallback
    }

    @Override
    public void update(int own, int other, double reward) {
        totalGames++;

        // Update value estimates
        valueEstimates[own] += learningRateWinning * (reward - valueEstimates[own]);

        // Determine if winning or losing
        boolean isWinning = isWinning();

        // Adjust learning rate based on win/lose status
        double delta = isWinning ? deltaWinning : deltaLosing;
        double learningRate = isWinning ? learningRateWinning : learningRateLosing;

        // Policy improvement
        for (int i = 0; i < numberOfActions; i++) {
            if (i == own) {
                currentPolicy[i] += isWinning ? -delta : delta;
            } else {
                currentPolicy[i] += isWinning ? delta / (numberOfActions - 1) : -delta / (numberOfActions - 1);
            }
        }

        // Normalize current policy
        normalizePolicy(currentPolicy);

        // Update average policy
        for (int i = 0; i < numberOfActions; i++) {
            averagePolicy[i] = (averagePolicy[i] * (totalGames - 1) + currentPolicy[i]) / totalGames;
        }
    }

    private boolean isWinning() {
        // Compare current performance with expected performance to determine winning status
        // This is a simplified version; you'll need a more sophisticated method for a real implementation
        double currentPerformance = 0;
        double expectedPerformance = 0;
        for (int i = 0; i < numberOfActions; i++) {
            currentPerformance += currentPolicy[i] * valueEstimates[i];
            expectedPerformance += averagePolicy[i] * valueEstimates[i];
        }
        return currentPerformance > expectedPerformance;
    }

    private void normalizePolicy(double[] policy) {
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
