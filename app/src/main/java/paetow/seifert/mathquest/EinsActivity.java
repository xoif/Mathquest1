package paetow.seifert.mathquest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class EinsActivity extends Activity implements OnClickListener {

    //Pausedialoge

    private Dialog dialog;
    private Button dialogReset, dialogNextLevel, Plusbutton, Minusbutton, Malbutton, Teilbutton, Resetbutton;
    private TextView Ausgabe, bubbleText, finalMessage, resetScoreAnzeige,startZahl, zielZahl;
    String newHighscoreName;

    public static ButtonProp buttonA, buttonB, buttonC, buttonD;

    public static int ans, Start, Goal, levelCounter;
    private int zugCounter, resetCounter;
    private int[] highscores;
    private LinearLayout highscoreAnzeige;



    //Animation des Fortschrittsbalken
    private ImageView fortschrittsBalken;
    private Handler pHandler;
    private ClipDrawable fortschrittsFuellung;
    private int fuellZustand;

    private Boolean gameEnded, inARow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zeit);

        inARow = true;

        //Dialogfenster und deren Buttons initialisieren
        dialog = new Dialog(this, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);
        dialog.hide();


        //Zeitmessung ausblenden
        TextView zeit = (TextView) findViewById(R.id.Zeit);
        zeit.setVisibility(View.INVISIBLE);
        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setVisibility(View.INVISIBLE);


        finalMessage = (TextView) dialog.findViewById(R.id.finalMessage);
        highscoreAnzeige = (LinearLayout) dialog.findViewById(R.id.stats);  //Lineares Layout enthälte die Anzeige des Highscore
        resetScoreAnzeige = (TextView) dialog.findViewById(R.id.pointsMade);
        dialogNextLevel = (Button) dialog.findViewById(R.id.dialogNextLevel);
        dialogNextLevel.setOnClickListener(this);
        dialogReset = (Button) dialog.findViewById(R.id.dialogReset);
        dialogReset.setOnClickListener(this);


        levelCounter = 1;              //Beim Starten der Aktivity wird mit Level 1 gestartet
        zugCounter = 0;                          //Zugzaehler auf Null setzen

        fuellZustand = 0;                      //Fortschrittsbalken auf Null setzen
        gameEnded = false;

        Resetbutton = (Button) findViewById(R.id.reset);
        Resetbutton.setOnClickListener(this);

        //Fortschrittsbalken, dessen Fuellung und Handler initialisieren
        fortschrittsBalken = (ImageView) findViewById(R.id.progress);
        fortschrittsFuellung = (ClipDrawable) fortschrittsBalken.getDrawable();
        pHandler = new Handler();

        fortschrittsFuellung.setLevel(0);    //Setzt Fuellung auf Anfang


        //Buttons und Felder initialisieren
        startZahl = (TextView) findViewById(R.id.Startzahl);
        zielZahl = (TextView) findViewById(R.id.Goal);
        Ausgabe = (TextView) findViewById(R.id.Ergebnisanzeige);
        Plusbutton = (Button) findViewById(R.id.addieren);
        Plusbutton.setOnClickListener(this);
        Minusbutton = (Button) findViewById(R.id.subtrahieren);
        Minusbutton.setOnClickListener(this);
        Malbutton = (Button) findViewById(R.id.multiplizieren);
        Malbutton.setOnClickListener(this);
        Teilbutton = (Button) findViewById(R.id.dividieren);
        Teilbutton.setOnClickListener(this);
        bubbleText = (TextView) findViewById(R.id.bubble);

        resetCounter = 0; //Reset Zähler auf 0 setzten
        loadLevel();    //generiert das Interface abhaengig vom Spiellevel

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.eins, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case R.id.hauptMenu:
                Intent in = new Intent(this, MenuActivity.class);
                startActivity(in);
                dialog.dismiss();
                return true;
            case R.id.menueins:
                levelEins_starten();
                return true;
            case R.id.menuzwei:
                startLvl(2);
                return true;
            case R.id.menudrei:
                startLvl(3);
                return true;
            case R.id.menuvier:
                startLvl(4);
                return true;
            case R.id.menufuenf:
                startLvl(5);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialogNextLevel:
                dialog.hide();

                if (levelCounter == 5) {
                    EditText newName = (EditText) dialog.findViewById(R.id.newName);
                    newHighscoreName = newName.getText().toString();   //diesen Wert in Highscore speichern
                    if (newHighscoreName == "gib deinen Name ein"){newHighscoreName = "noName";}
                    writeHighscore(resetCounter);
                    finalMessage.setVisibility(View.GONE);
                    highscoreAnzeige.setVisibility(View.GONE);

                    Intent in = new Intent(this, MenuActivity.class);   //geht wieder ins Hauptmenü
                    startActivity(in);
                    dialog.dismiss();
                    break;
                } else {
                    nextLevel();
                    break;
                }
            case R.id.dialogReset:
                dialog.hide();
                reset();
                break;
            case R.id.reset:
                reset();
                break;
            case R.id.addieren:
                calculateCheck(buttonA);
                break;
            case R.id.subtrahieren:
                calculateCheck(buttonB);
                break;
            case R.id.multiplizieren:
                calculateCheck(buttonC);
                break;
            case R.id.dividieren:
                calculateCheck(buttonD);
                break;

        }
    }


    public void loadLevel()    //Generieren der Spielvariablen und laden des Interface abhaengig vom Level
    {
        // Zufahlszahlen zuweisen
        Random Zufall = new Random();
        Start = Zufall.nextInt(20);
        Goal = Start;


        // Button Propertys
        buttonA = new ButtonProp();
        buttonB = new ButtonProp();
        buttonC = new ButtonProp();
        buttonD = new ButtonProp();


        //Zielzahl berechnen
        do {
            Tools.aim1();
        }
        while (Goal == 0);


        // Textfelder zuweisen

        ans = Start;
        String Startzahl = String.valueOf(Start);
        String zuErreichen = String.valueOf(Goal);
        String zwischenErgebnis = String.valueOf(ans);

        String plus = String.valueOf(buttonA.value);
        String minus = String.valueOf(buttonB.value);
        String mal = String.valueOf(buttonC.value);
        String teil = String.valueOf(buttonD.value);


        //Buttons das jeweilige Drawable zuordnen
        switch(buttonA.operator){
            case 0: Plusbutton.setBackgroundResource(R.drawable.plus);break;
            case 1: Plusbutton.setBackgroundResource(R.drawable.minus);break;
            case 2: Plusbutton.setBackgroundResource(R.drawable.mal);break;
            case 3: Plusbutton.setBackgroundResource(R.drawable.geteilt);break;
            default: break;
        }

        switch(buttonB.operator){
            case 0: Minusbutton.setBackgroundResource(R.drawable.plus);break;
            case 1: Minusbutton.setBackgroundResource(R.drawable.minus);break;
            case 2: Minusbutton.setBackgroundResource(R.drawable.mal);break;
            case 3: Minusbutton.setBackgroundResource(R.drawable.geteilt);break;
            default: break;
        }

        switch(buttonC.operator){
            case 0: Malbutton.setBackgroundResource(R.drawable.plus);break;
            case 1: Malbutton.setBackgroundResource(R.drawable.minus);break;
            case 2: Malbutton.setBackgroundResource(R.drawable.mal);break;
            case 3: Malbutton.setBackgroundResource(R.drawable.geteilt);break;
            default: break;
        }

        switch(buttonD.operator){
            case 0: Teilbutton.setBackgroundResource(R.drawable.plus);break;
            case 1: Teilbutton.setBackgroundResource(R.drawable.minus);break;
            case 2: Teilbutton.setBackgroundResource(R.drawable.mal);break;
            case 3: Teilbutton.setBackgroundResource(R.drawable.geteilt);break;
            default: break;
        }

        //Texte in Grafik laden
        startZahl.setText(Startzahl);
        zielZahl.setText(zuErreichen);
        Ausgabe.setText(zwischenErgebnis);
        Plusbutton.setText(plus);
        Minusbutton.setText(minus);
        Malbutton.setText(mal);
        Teilbutton.setText(teil);
        setBubbleText();

    }


    public void nextLevel() {

        levelCounter++;
        gameEnded = false;
        zugCounter = 0;
        step(true);
        loadLevel();

    }

    public void calculateCheck(ButtonProp b){

        if (gameEnded == true && levelCounter == 5) {
            Intent in = new Intent(this, MenuActivity.class);
            startActivity(in);
        } else if (gameEnded == true && ans != Goal) {
        } else {
            ans=Tools.calculate(ans, b);

            String zwischenErgebnis = String.valueOf(ans);
            Ausgabe.setText(zwischenErgebnis);
            ziehen();
        }
    }

    private void ziehen() {

        zugCounter++;
        step(false);
        setBubbleText();

        if (zugCounter == levelCounter && ans == Goal) {

            if (levelCounter == 5) {


                finalMessage.setVisibility(View.VISIBLE);

                if (inARow) {
                    finalMessage.setText(R.string.finalMessage);
                    resetScoreAnzeige.setText(resetCounter + "");

                    highscoreAnzeige.setVisibility(View.VISIBLE);

                    LinearLayout highscoreTextEdit = (LinearLayout) dialog.findViewById(R.id.newHighscore);
                    highscoreTextEdit.setVisibility(View.VISIBLE);
                } else {
                    String test = getResources().getString(R.string.finalMessage2);
                    finalMessage.setText(test);
                }

                dialogNextLevel.setText("zurück zum Hauptmenü");
            }
            TextView headLine = (TextView) dialog.findViewById(R.id.headline);
            headLine.setText("gewonnen");


            dialogNextLevel.setVisibility(View.VISIBLE);    //nextLevel Button anzeigen
            dialogReset.setVisibility(View.GONE);           //Reset Button ausblenden

            dialog.show();
        }

        if (zugCounter == levelCounter && ans != Goal) {
                     gameEnded = true;
            Log.i("verloren", "funktioniert");

            TextView headLine = (TextView) dialog.findViewById(R.id.headline);
            headLine.setText("verloren");

            dialogNextLevel.setVisibility(View.GONE);    //nextLevel Button ausblenden
            dialogReset.setVisibility(View.VISIBLE);           //Reset Button anzeigen

            dialog.show();
        }

    }

    public void reset() {

        gameEnded = false;
        resetCounter++;
        zugCounter = 0;
        ans = Start;
        String zwischenErgebnis = String.valueOf(ans);
        Ausgabe.setText(zwischenErgebnis);
        step(true);
        setBubbleText(); //sezt den Fortschritsbalken auf Ausgangsposition zurueck


    }


    public void levelEins_starten() {
        levelCounter = 1;
        inARow = true;
        resetCounter = 0;
        loadLevel();
        reset();
    }

    public void startLvl(int x){
        levelCounter = x;
        inARow = false;
        loadLevel();
        reset();
    }


    private Runnable animateImage = new Runnable() {

        @Override
        public void run() {
            doTheAnimation();
        }
    };

    private void doTheAnimation() {

        fortschrittsFuellung.setLevel(fuellZustand);
        if (fuellZustand <= 10000) {
            pHandler.postDelayed(animateImage, 50);
        } else {
            pHandler.removeCallbacks(animateImage);
        }
    }

    private void step(boolean resetter) {
        if (resetter == true) {
            fuellZustand = 0;
        } else {
            fuellZustand += (10000 / levelCounter);
        }
        pHandler.post(animateImage);

    }


    private void setBubbleText() {
        String bubble = zugCounter + " / " + levelCounter;
        bubbleText.setText(bubble);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Wirklich schließen?")
                .setMessage("Sicher, dass Sie genug Spaß hatten?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        EinsActivity.super.onBackPressed();
                    }
                }).create().show();
    }


    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
    }


    public void writeHighscore(int highscore) {
        SharedPreferences pref = getSharedPreferences("POINTS", 0);
        SharedPreferences pref2 = getSharedPreferences("NAMES_SCHRITTE", 0);

        int i = 1;
        while(highscore>=pref.getInt("HIGHSCORE"+i, 9999)){
            i++;
        }
        if (i<=10){
        SharedPreferences.Editor editor = pref.edit();
        SharedPreferences.Editor editor2 = pref2.edit();

        int temp = pref.getInt("HIGHSCORE"+i, 9999);
        String tempS = pref2.getString("NAME"+i, "Blondie");

        editor.putInt("HIGHSCORE"+i, highscore);
        editor2.putString("NAME"+i, newHighscoreName);
        while (i<10){
            i++;
            int temp2 = pref.getInt("HIGHSCORE"+i, 9999);
            String tempS2 = pref2.getString("NAME"+i, "Blondie");
            editor.putInt("HIGHSCORE"+i, temp);
            editor2.putString("NAME"+i, tempS);
            temp = temp2;
            tempS = tempS2;
        }
        editor.commit();
        editor2.commit();
        }
    }


    public void cheat (View v)
    {
        zugCounter = levelCounter - 1;
        ans = Goal;
        ziehen();
    }

}
