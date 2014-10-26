package paetow.seifert.mathquest;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MenuActivity extends Activity {

    private TextView highscore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        highscore = (TextView) findViewById(R.id.menuHighscoreAusgabe);


        if (checkPref()) {
            highscore.setText(readHighscore());
        } else {
            highscore.setText("noch keine 5 Spiele am Stück geschafft");
        }

    }


    public void loslegen(View Buttonclick) {

        Intent in = new Intent(this, EinsActivity.class);
        startActivity(in);


    }

    public boolean checkPref() {
        SharedPreferences pref = getSharedPreferences("GAME", 0);
        return pref.contains("HIGHSCORE");
    }

    public int readHighscore() {
        SharedPreferences pref = getSharedPreferences("GAME", 0);
        return pref.getInt("HIGHSCORE", 0);
    }
}
