package in.prateekkumar.android.bullsandcows;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity {

    private BullsAndCowsGame bullsAndCowsGame;
    private MainActivity.GuessAdapter guessAdapter;
    private TextView codeView;
    private EditText bullsView, cowsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        bullsAndCowsGame = new BullsAndCowsGame(BullsAndCowsGame.PLAYER_GUESSES);
        ArrayList<String> guesses = new ArrayList<>();
        guesses.add("CodeBC");
        guessAdapter = new MainActivity.GuessAdapter(this, guesses);
        ListView listView = (ListView) findViewById(R.id.guess_list);
        if (listView != null) {
            listView.setAdapter(guessAdapter);
        }
        codeView = (TextView) findViewById(R.id.code);
        bullsView = (EditText) findViewById(R.id.bulls_editText);
        cowsView = (EditText) findViewById(R.id.cows_editText);
        codeView.setText(bullsAndCowsGame.getGuessAt(0));
    }

    public void replay(View view) {
        onDestroy();
        onCreate(null);
    }

    public void play(View view) {
        String bullsString = bullsView.getText().toString();
        String cowsString = cowsView.getText().toString();
        if (bullsString.equals("") || cowsString.equals("")) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }
        int bulls = Integer.parseInt(bullsString);
        int cows = Integer.parseInt(cowsString);
        if (bulls < 0 || bulls > 4 || cows < 0 || cows > 4 || bulls + cows > 4) {
            Toast.makeText(this, "Inconsistent number of bulls or cows", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            String res = bullsAndCowsGame.play(bulls, cows);
            guessAdapter.add(codeView.getText() + bullsString + cowsString);
            codeView.setText(res);
            bullsView.setText("");
            cowsView.setText("");
            bullsView.requestFocus();
        } catch (BullsAndCowsGame.BullsAndCowsExceptions bullsAndCowsExceptions) {
            bullsAndCowsExceptions.printStackTrace();
        }
    }
}
