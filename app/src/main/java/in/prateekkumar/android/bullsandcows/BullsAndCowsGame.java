package in.prateekkumar.android.bullsandcows;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BullsAndCowsGame {

    public static final int COMPUTER_GUESSES = 0;
    public static final int PLAYER_GUESSES = 1;

    private int gameType;
    private int[] mNumbers;
    private int[] mSecret;
    private List<int[]> mGuess;
    private boolean isGameOver;

    public BullsAndCowsGame(int type) {
        gameType = type;
        mGuess = new ArrayList<>();
        mNumbers = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        isGameOver = false;
        if (type == COMPUTER_GUESSES) {
            mSecret = new int[4];
            for (int i = 0; i < 4; i++) {
                mSecret[i] = random();
                for (int j = 0; j < i; j++) {
                    if (mSecret[i] == mSecret[j] || mSecret[i] >= 10 || mSecret[i] < 0) {
                        i--;
                        break;
                    }
                }
            }
            for (int i = 0; i < 4; i++) {
                Log.v("Game", String.valueOf(mSecret[i]));
            }
        } else if (type == PLAYER_GUESSES) {
            int[] initGuess = {0, 0, 0, 0, -1, -1};
            for (int i = 0; i < 4; i++) {
                initGuess[i] = random();
                for (int j = 0; j < i; j++) {
                    if (initGuess[i] == initGuess[j]) {
                        i--;
                        break;
                    }
                }
            }
            mGuess.add(initGuess);
        }
    }

    public BullsAndCowsGame() {
        this(COMPUTER_GUESSES);
    }

    public int[] guess(int ...guess) throws BullsAndCowsExceptions {
        if (gameType == COMPUTER_GUESSES) {
            if (isGameOver) throw new BullsAndCowsExceptions("Game already Over");
            else if (mGuess.add(guess)) {
                int[] bullsAndCows = computeBC(guess);
                if (bullsAndCows[0] == mSecret.length) isGameOver = true;
                return bullsAndCows;
            } else throw new BullsAndCowsExceptions("Could not add guess.");
        } else throw new BullsAndCowsExceptions("This method cannot be executed.");
    }

    private int[] computeBC(int[] guess) throws BullsAndCowsExceptions {
        int bulls = 0, cows = 0;
        if (guess.length != mSecret.length)
            throw new BullsAndCowsExceptions("Guess should have " + Integer.toString(mSecret.length) + " integers.");
        for (int i = 0; i < guess.length; i++) {
            for (int j = 0; j < mSecret.length; j++) {
                if (i == j && guess[i] == mSecret[j]) bulls++;
                else if (guess[i] == mSecret[j]) cows++;
            }
        }
        return new int[]{bulls, cows};
    }

    public String getGuessAt(int n) {
        if (n < mGuess.size()) {
            return String.valueOf(mGuess.get(n)[0]) + String.valueOf(mGuess.get(n)[1]) + String.valueOf(mGuess.get(n)[2]) + String.valueOf(mGuess.get(n)[3]);
        } else return null;
    }

    public String play(int bulls, int cows) throws BullsAndCowsExceptions {
        if (bulls < 0 || bulls > 4 || cows < 0 || cows > 4 || bulls + cows > 4) {
            throw new BullsAndCowsExceptions("Inconsistant values of bulls and cows");
        }
        if (gameType == PLAYER_GUESSES) {
            // TODO : Make the play function
            int[] lastGuess = mGuess.get(mGuess.size() - 1);
            lastGuess[4] = bulls;
            lastGuess[5] = cows;
            mGuess.set(mGuess.size() - 1, lastGuess);
            return "Code";
        } else throw new BullsAndCowsExceptions("This method cannot be executed.");
    }

    private int random(int start, int end) {
        return start + (int) (Math.round((Math.random() * 10000)) % (end - start));
    }

    private int random() {
        return random(0, 10);
    }

    public class BullsAndCowsExceptions extends Exception {
        public BullsAndCowsExceptions(String detailMessage) {
            super(new Throwable(detailMessage));
        }
    }
}
