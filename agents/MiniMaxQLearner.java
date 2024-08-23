package agents;

public class MiniMaxQLearner implements Agent {

    private double[] Q; // Q-values for actions
    private double V; // Value for the state, since it's a stateless game we only need one value
    private double pi[]; // Mixed strategy (probability distribution over actions)
    private double alpha; // Learning rate
    private double alphadecay; // Learning rate decay
    private int numberOfActions; // Number of actions in the game
    
    public MiniMaxQLearner(int numberOfActions) {
        this.numberOfActions = numberOfActions;
        Q = new double[numberOfActions];
        pi = new double[numberOfActions];
        
        // Initialize Q and pi here, pi comes from the uniform distribution initially
        // Q is initialized to small random values like in QLearnerBoltzmann
        for (int i = 0; i < numberOfActions; i++) {
            Q[i] = -0.1 + Math.random() * 0.2;
            pi[i] = 1.0 / numberOfActions; // Uniform distribution
        }
        
        alpha = 0.01;
        alphadecay = 1.0;
    }


    @Override
    public double actionProb(int i) {
        return pi[i];
    }

    @Override
    public int selectAction() {
        // Select an action based on the mixed strategy (pi)
        double r = Math.random();
        double sum = 0;
        for (int i = 0; i < numberOfActions; i++) {
            sum += pi[i];
            if (r <= sum) {
                return i;
            }
        }
        return numberOfActions - 1;
    }

    @Override
    public void update(int own, int other, double reward) {
        // Calculating the utility of the action taken given the mixed strategy
        double utility = 0;
        for (int a = 0; a < numberOfActions; a++) {
            utility += pi[a] * Q[a];
        }
        
        // Update the Q-value for the action taken
        // Since this is a stateless game, we consider immediate reward only
        Q[own] = Q[own] + alpha * (reward - utility);
    
        // Now compute the best response for the opponent
        // For stateless game, this is just the maximum Q-value for other agent's actions
        double opponentBestResponseValue = Double.NEGATIVE_INFINITY;
        for (double qValue : Q) {
            if (qValue > opponentBestResponseValue) {
                opponentBestResponseValue = qValue;
            }
        }
        
        V = opponentBestResponseValue;
        
        // Update the mixed strategy based on the new Q-values
        computeMiniMaxStrategy();
        
        // Decay the learning rate
        alpha *= alphadecay;
    }
    

    @Override
    public double getQ(int i) {
        return Q[i];
    }
    
    public void computeMiniMaxStrategy() {
        // Linear program to maximize over pi

        // The linear program is:
        // maximize sum_i pi[i] * Q[i]
        // subject to sum_i pi[i] = 1
        //            pi[i] >= 0 for all i

        // We didn't have time to implement the linear program, but we found this algorithm pretty cool!
        

    }
    
        
}
