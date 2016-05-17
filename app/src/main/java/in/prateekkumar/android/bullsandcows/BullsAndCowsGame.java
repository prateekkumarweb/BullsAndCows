package in.prateekkumar.android.bullsandcows;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BullsAndCowsGame {

    private int[] mSecret;
    private List<int[]> mGuess;
    private boolean isGameOver;

    public BullsAndCowsGame(int size) {
        mSecret = new int[size];
        for (int i=0; i<size; i++) {
            mSecret[i] = random();
            for (int j=0; j<i; j++) {
                if(mSecret[i] == mSecret[j] || mSecret[i] >= 10 || mSecret[i] < 0) {
                    i--;
                    break;
                }
            }
        }
        mGuess = new ArrayList<>();
        isGameOver = false;
        for (int i=0; i<size; i++) {
            Log.v("Game : ", String.valueOf(mSecret[i]));
        }
    }

    public class BullsAndCowsExceptions extends Exception {

        public BullsAndCowsExceptions(String detailMessage) {
            super(new Throwable(detailMessage));
        }
    }

    public int[] guess(int ...guess) throws BullsAndCowsExceptions {
        if (isGameOver) throw new BullsAndCowsExceptions("Game already Over");
        else if (mGuess.add(guess)) {
            int[] bullsAndCows = computeBC(guess);
            if (bullsAndCows[0] == mSecret.length) isGameOver = true;
            return bullsAndCows;
        }
        else throw new BullsAndCowsExceptions("Could not add guess.");
    }

    private int[] computeBC(int[] guess) throws BullsAndCowsExceptions {
        int bulls = 0, cows = 0;
        if (guess.length != mSecret.length) throw new BullsAndCowsExceptions("Guess should have " + Integer.toString(mSecret.length) + " integers.");
        for (int i=0; i<guess.length; i++) {
            for (int j=0; j<mSecret.length; j++) {
                if (i == j && guess[i] == mSecret[j]) bulls++;
                else if (guess[i] == mSecret[j]) cows++;
            }
        }
        return new int[] {bulls, cows};
    }

    private int random(int start, int end) {
        return (int) (start + (Math.round((Math.random() * 100000) % (end - start))));
    }

    private int random() {
        return random(0, 10);
    }
}
