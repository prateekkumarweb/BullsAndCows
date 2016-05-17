package in.prateekkumar.android.bullsandcows;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BullsAndCowsGame bullsAndCowsGame;
    private boolean isGameOver;
    private GuessAdapter guessAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }*/

        bullsAndCowsGame = new BullsAndCowsGame(4);
        isGameOver = false;
        ArrayList<String> guesses = new ArrayList<>();
        guesses.add("CodeBC");
        guessAdapter = new GuessAdapter(this, guesses);
        ListView listView = (ListView) findViewById(R.id.guess_list);
        if (listView != null) {
            listView.setAdapter(guessAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void play(View view) {
        if (isGameOver) return;
        try {
            EditText[] editTexts = {
                    (EditText) findViewById(R.id.guess_1),
                    (EditText) findViewById(R.id.guess_2),
                    (EditText) findViewById(R.id.guess_3),
                    (EditText) findViewById(R.id.guess_4)
            };
            int[] guess = new int[] {0, 0, 0, 0};
            for (int i=0; i<4; i++) {
                if (editTexts[i] != null && !editTexts[i].getText().toString().equals("")) {
                    guess[i] = Integer.parseInt(editTexts[i].getText().toString());
                }
                else {
                    Toast.makeText(this, "All fields are compulsory", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int j=0; j<i; j++) {
                    if (guess[i] == guess[j]) {
                        Toast.makeText(this, "All digits should be different", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
            int[] bullsAndCows = bullsAndCowsGame.guess(guess);
            guessAdapter.add(String.valueOf(guess[0]) +
            String.valueOf(guess[1]) +
            String.valueOf(guess[2]) +
            String.valueOf(guess[3]) +
            String.valueOf(bullsAndCows[0]) +
            String.valueOf(bullsAndCows[1]));
            if (bullsAndCows[0] == 4) {
                isGameOver = true;
                Toast.makeText(this, "You guessed it right", Toast.LENGTH_SHORT).show();
            }
            for (int i=0; i<4; i++) {
                editTexts[i].setText("");
                if (isGameOver) editTexts[i].setFocusable(false);
            }
        } catch (BullsAndCowsGame.BullsAndCowsExceptions bullsAndCowsExceptions) {
            bullsAndCowsExceptions.printStackTrace();
        }
    }

    public void reset(View view) {
        onDestroy();
        onCreate(null);
    }

    private class GuessAdapter extends ArrayAdapter<String> {

        public GuessAdapter(Context context, ArrayList<String> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String s = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.guess_list_item, parent, false);
            }
            ((TextView) convertView.findViewById(R.id.g1)).setText(String.valueOf(s.charAt(0)));
            ((TextView) convertView.findViewById(R.id.g2)).setText(String.valueOf(s.charAt(1)));
            ((TextView) convertView.findViewById(R.id.g3)).setText(String.valueOf(s.charAt(2)));
            ((TextView) convertView.findViewById(R.id.g4)).setText(String.valueOf(s.charAt(3)));
            ((TextView) convertView.findViewById(R.id.b)).setText(String.valueOf(s.charAt(4)));
            ((TextView) convertView.findViewById(R.id.c)).setText(String.valueOf(s.charAt(5)));
            return convertView;
        }
    }
}
