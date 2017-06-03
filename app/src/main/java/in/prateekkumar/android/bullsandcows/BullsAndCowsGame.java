package in.prateekkumar.android.bullsandcows;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class BullsAndCowsGame {

    private int[] mSecret;
    private List<int[]> mGuess;
    private boolean isGameOver;

    BullsAndCowsGame() {
        mGuess = new ArrayList<>();
        mSecret = new int[]{-1, -1, -1, -1};
        isGameOver = false;
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
    }

    private static int[] computeBC(int[] n1, int[] n2) throws BullsAndCowsException {
        int bulls = 0, cows = 0;
        if (n1.length != n2.length)
            throw new BullsAndCowsException("Guess should have " + Integer.toString(n2.length) + " integers.");
        for (int i = 0; i < n1.length; i++) {
            for (int j = 0; j < n2.length; j++) {
                if (i == j && n1[i] == n2[j]) bulls++;
                else if (n1[i] == n2[j]) cows++;
            }
        }
        return new int[]{bulls, cows};
    }

    private static int random(int start, int end) {
        return start + (int) (Math.round((Math.random() * 1000000)) % (end - start));
    }

    private static int random() {
        return random(0, 10);
    }

    int[] guess(int... guess) throws BullsAndCowsException {
        if (isGameOver) throw new BullsAndCowsException("Game already Over");
        else if (mGuess.add(guess)) {
            int[] bullsAndCows = computeBC(guess);
            if (bullsAndCows[0] == mSecret.length) isGameOver = true;
            return bullsAndCows;
        } else throw new BullsAndCowsException("Could not add guess.");
    }

    private int[] computeBC(int[] guess) throws BullsAndCowsException {
        return computeBC(guess, mSecret);
    }

    static class BullsAndCowsException extends Exception {
        BullsAndCowsException(String detailMessage) {
            super(new Throwable(detailMessage));
        }
    }

    static class Player {

        private ArrayList<int[]> possibleValues;
        private ArrayList<int[]> guessList;
        private boolean isGameOver;

        Player() {
            isGameOver = false;
            guessList = new ArrayList<>();
            possibleValues = new ArrayList<>();
            int[] guess = {-1, -1, -1, -1, -1, -1};
            for (int i = 0; i < 4; i++) {
                guess[i] = random();
                for (int j = 0; j < i; j++) {
                    if (guess[j] == guess[i]) {
                        i--;
                        break;
                    }
                }
            }
            guessList.add(guess);
            for (int i1 = 0; i1 < 10; i1++) {
                for (int i2 = 0; i2 < 10; i2++) {
                    if (i1 == i2) continue;
                    for (int i3 = 0; i3 < 10; i3++) {
                        if (i1 == i3 || i2 == i3) continue;
                        for (int i4 = 0; i4 < 10; i4++) {
                            if (i1 == i4 || i2 == i4 || i3 == i4) continue;
                            possibleValues.add(new int[]{i1, i2, i3, i4});
                        }
                    }
                }
            }
        }

        String getGuessAt(int i) {
            int[] guess = guessList.get(i);
            return String.valueOf(guess[0]) + String.valueOf(guess[1]) + String.valueOf(guess[2]) + String.valueOf(guess[3]);
        }

        String play(int bulls, int cows) throws BullsAndCowsException {
            if (isGameOver) return "DONE";
            if (bulls >= 0 || bulls <= 4 || cows >= 0 || cows <= 4 || bulls + cows <= 4) {
                int[] guess = guessList.get(guessList.size() - 1);
                guess[4] = bulls;
                guess[5] = cows;
                guessList.set(guessList.size() - 1, guess);
                for (int i = 0; i < possibleValues.size(); i++) {
                    int[] bc = computeBC(Arrays.copyOfRange(guess, 0, 4), Arrays.copyOfRange(possibleValues.get(i), 0, 4));
                    if (bc[0] != bulls || bc[1] != cows) {
                        possibleValues.remove(i);
                        i--;
                    }
                }
                if (possibleValues.size() == 0) return "ERROR";
                guess = possibleValues.get(random(0, possibleValues.size()));
                guessList.add(new int[]{guess[0], guess[1], guess[2], guess[3], -1, -1});
                if (bulls == 4) {
                    isGameOver = true;
                    return "DONE";
                }
                return String.valueOf(guess[0]) + String.valueOf(guess[1]) + String.valueOf(guess[2]) + String.valueOf(guess[3]);
            } else throw new BullsAndCowsException("Inconsistent number of bulls or cows");
        }
    }
}
