package paetow.seifert.mathquest;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class ranking extends Activity {


    TextView highscore1 ;
    TextView highscore2 ;
    TextView highscore3 ;
    TextView highscore4 ;
    TextView highscore5 ;
    TextView highscore6 ;
    TextView highscore7;
    TextView highscore8 ;
    TextView highscore9 ;
    TextView highscore10;
    TextView[] high;

    TextView name1;
    TextView name2;
    TextView name3;
    TextView name4;
    TextView name5;
    TextView name6;
    TextView name7;
    TextView name8;
    TextView name9;
    TextView name10;

    TextView[] names;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        highscore1 = (TextView) findViewById(R.id.Highscore1);
        highscore2 = (TextView) findViewById(R.id.Highscore2);
        highscore3 = (TextView) findViewById(R.id.Highscore3);
        highscore4 = (TextView) findViewById(R.id.Highscore4);
        highscore5 = (TextView) findViewById(R.id.Highscore5);
        highscore6 = (TextView) findViewById(R.id.Highscore6);
        highscore7 = (TextView) findViewById(R.id.Highscore7);
        highscore8 = (TextView) findViewById(R.id.Highscore8);
        highscore9 = (TextView) findViewById(R.id.Highscore9);
        highscore10 = (TextView) findViewById(R.id.Highscore10);

        name1 = (TextView) findViewById(R.id.Name1);
        name2 = (TextView) findViewById(R.id.Name2);
        name3 = (TextView) findViewById(R.id.Name3);
        name4 = (TextView) findViewById(R.id.Name4);
        name5 = (TextView) findViewById(R.id.Name5);
        name6 = (TextView) findViewById(R.id.Name6);
        name7 = (TextView) findViewById(R.id.Name7);
        name8 = (TextView) findViewById(R.id.Name8);
        name9 = (TextView) findViewById(R.id.Name9);
        name10 = (TextView) findViewById(R.id.Name10);
        high = new TextView[]{highscore1, highscore2, highscore3, highscore4, highscore5, highscore6, highscore7, highscore8, highscore9, highscore10};
        names = new TextView[]{name1, name2, name3, name4, name5, name6, name7, name8, name9, name10};

    }


    public void readHighscoreZeit(View v) {

        SharedPreferences pref = getSharedPreferences("TIME", 0);
        SharedPreferences pref2 = getSharedPreferences("NAMES_ZEIT", 0);


        for(int i = 1; i<=high.length; i++){
            high[i-1].setText(""+pref.getInt("HIGHSCORE"+i, 9999));
            names[i-1].setText(pref2.getString("NAME"+i, "Blondie"));
        }
    }

    public void readHighscoreSchritte(View v)
    {
        SharedPreferences pref = getSharedPreferences("POINTS", 0);
        SharedPreferences pref2 = getSharedPreferences("NAMES_SCHRITTE", 0);

        for(int i = 1; i<=high.length; i++){
            high[i-1].setText(""+pref.getInt("HIGHSCORE"+i, 9999));
            names[i-1].setText(pref2.getString("NAME"+i, "Blondie"));
        }
    }

    public void goback (View v)
    {
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
    }


}