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
     high = new TextView[]{highscore1, highscore2, highscore3, highscore4, highscore5, highscore6, highscore7, highscore8, highscore9, highscore10};


    }



    public int[] readHighscore1() {

        SharedPreferences pref = getSharedPreferences("POINTS", 0);
        int[] highscores = new int[9];
        for(int i = 1; i<=highscores.length; i++){
            highscores[i-1] = pref.getInt("HIGHSCORE"+i, 0);
        }
        return highscores;

    }



    public void zeitRanking(View v) {

        SharedPreferences pref = getSharedPreferences("TIME", 0);

        for (int i = 1; i <= high.length; i++) {
            high[i - 1].setText( pref.getInt("HIGHSCORE" + i, 0));

        }
    }

    public void schrittRanking(View v)
    {
        SharedPreferences pref = getSharedPreferences("POINTS", 0);

        for(int i = 1; i<=high.length; i++){
            high[i-1].setText(pref.getInt("HIGHSCORE"+i, 0));
        }

    }

    public void goback (View v)
    {
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
    }


}