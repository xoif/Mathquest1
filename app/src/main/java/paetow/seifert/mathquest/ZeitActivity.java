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
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;


public class ZeitActivity extends Activity implements View.OnClickListener{

    //Pausedialoge

    private Chronometer chronometer;


    private Dialog dialog;
    private Button dialogReset, dialogNextLevel;

    private EditText startZahl, zielZahl;

    private TextView Ausgabe, bubbleText, finalMessage, currentHighScoreAnzeige, resetScoreAnzeige;

    private Button Plusbutton, Minusbutton, Malbutton, Teilbutton, Resetbutton;

    private int levelCounter, ans, Start, Goal, zugCounter, resetCounter;
    private LinearLayout highscoreAnzeige;


    //Animation des Fortschrittsbalken
    private ImageView fortschrittsBalken;
    private Handler pHandler;
    private ClipDrawable fortschrittsFuellung;
    private int fuellZustand;


    int plusZahl, minusZahl, malZahl, teilZahl;

    int buttonA, buttonB, buttonC, buttonD;
    private Boolean gameEnded, inARow;

    Rechenoperation anton, berta, chris, doofie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zeit);

        inARow = true;

        //Dialogfenster und deren Buttons initialisieren
        //Dialogfenster und deren Buttons initialisieren
        dialog = new Dialog(this, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);

        dialog.hide();

        finalMessage = (TextView) dialog.findViewById(R.id.finalMessage);
        highscoreAnzeige = (LinearLayout) dialog.findViewById(R.id.testContainer);  //Lineares Layout enthälte die Anzeige des Highscore
        resetScoreAnzeige = (TextView) dialog.findViewById(R.id.resetsUsed);
        currentHighScoreAnzeige = (TextView) dialog.findViewById(R.id.Highscore);
        dialogNextLevel = (Button) dialog.findViewById(R.id.dialogNextLevel);
        dialogNextLevel.setOnClickListener(this);
        dialogReset = (Button) dialog.findViewById(R.id.dialogReset);
        dialogReset.setOnClickListener(this);

        //Fortschrittsbalken, dessen Fuellung und Handler initialisieren
        fortschrittsBalken = (ImageView) findViewById(R.id.progress);
        fortschrittsFuellung = (ClipDrawable) fortschrittsBalken.getDrawable();
        pHandler = new Handler();

        fortschrittsFuellung.setLevel(0);    //Setzt Fuellung auf Anfang


        //Buttons und Felder initialisieren
        startZahl = (EditText) findViewById(R.id.Startzahl);
        zielZahl = (EditText) findViewById(R.id.Goal);
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

        chronometer = (Chronometer) findViewById(R.id.chronometer);

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
                dialog.dismiss();
                return true;
            case R.id.menueins:
                levelEins_starten();
                return true;
            case R.id.menuzwei:
                levelZwei_starten();
                return true;
            case R.id.menudrei:
                levelDrei_starten();
                return true;
            case R.id.menuvier:
                levelVier_starten();
                return true;

            case R.id.menufuenf:
                levelFuenf_starten();
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
                    levelEins_starten();
                    finalMessage.setVisibility(View.GONE);
                    highscoreAnzeige.setVisibility(View.GONE);
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
                addieren();
                break;
            case R.id.subtrahieren:
                subtrahieren();
                break;
            case R.id.multiplizieren:
                multiplizieren();
                break;
            case R.id.dividieren:
                dividieren();
                break;

        }
    }


    public void loadLevel()    //Generieren der Spielvariablen und laden des Interface abhaengig vom Level
    {
        // Zufahlszahlen zuweisen
        Random Zufall = new Random();
        Start = Zufall.nextInt(20);
        Goal = Start;
        Zufall.setSeed(System.currentTimeMillis());

        plusZahl = Zufall.nextInt(8) + 1;
        minusZahl = Zufall.nextInt(8) + 1;
        malZahl = Zufall.nextInt(8) + 1;
        teilZahl = Zufall.nextInt(14) + 1;
        ;


        // Button Enums
        buttonA = Zufall.nextInt(4);
        buttonB = Zufall.nextInt(4);
        buttonC = Zufall.nextInt(4);
        buttonD = Zufall.nextInt(4);

        anton = Rechenoperation.getEnumByValue(buttonA);
        berta = Rechenoperation.getEnumByValue(buttonB);
        chris = Rechenoperation.getEnumByValue(buttonC);
        doofie = Rechenoperation.getEnumByValue(buttonD);


        //Zielzahl berechnen
        do {
            Goal = zielen();
        }
        while (Goal == 0);


        // Textfelder zuweisen

        ans = Start;
        String Startzahl = String.valueOf(Start);
        String zuErreichen = String.valueOf(Goal);
        String zwischenErgebnis = String.valueOf(ans);

        String plus = String.valueOf(plusZahl);
        String minus = String.valueOf(minusZahl);
        String mal = String.valueOf(malZahl);
        String teil = String.valueOf(teilZahl);


        //Buttons das jeweilige Drawable zuordnen
        if (anton.toString() == "PLUS") Plusbutton.setBackgroundResource(R.drawable.plus);
        if (anton.toString() == "MINUS") Plusbutton.setBackgroundResource(R.drawable.minus);
        if (anton.toString() == "MAL") Plusbutton.setBackgroundResource(R.drawable.mal);
        if (anton.toString() == "TEIL") Plusbutton.setBackgroundResource(R.drawable.geteilt);

        if (berta.toString() == "PLUS") Minusbutton.setBackgroundResource(R.drawable.plus);
        if (berta.toString() == "MINUS") Minusbutton.setBackgroundResource(R.drawable.minus);
        if (berta.toString() == "MAL") Minusbutton.setBackgroundResource(R.drawable.mal);
        if (berta.toString() == "TEIL") Minusbutton.setBackgroundResource(R.drawable.geteilt);

        if (chris.toString() == "PLUS") Malbutton.setBackgroundResource(R.drawable.plus);
        if (chris.toString() == "MINUS") Malbutton.setBackgroundResource(R.drawable.minus);
        if (chris.toString() == "MAL") Malbutton.setBackgroundResource(R.drawable.mal);
        if (chris.toString() == "TEIL") Malbutton.setBackgroundResource(R.drawable.geteilt);

        if (doofie.toString() == "PLUS") Teilbutton.setBackgroundResource(R.drawable.plus);
        if (doofie.toString() == "MINUS") Teilbutton.setBackgroundResource(R.drawable.minus);
        if (doofie.toString() == "MAL") Teilbutton.setBackgroundResource(R.drawable.mal);
        if (doofie.toString() == "TEIL") Teilbutton.setBackgroundResource(R.drawable.geteilt);


        //Texte in Grafik laden
        startZahl.setText(Startzahl);
        zielZahl.setText(zuErreichen);
        Ausgabe.setText(zwischenErgebnis);
        Plusbutton.setText(plus);
        Minusbutton.setText(minus);
        Malbutton.setText(mal);
        Teilbutton.setText(teil);
        setBubbleText();


        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        /*chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h   = (int)(time /3600000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000- m*60000)/1000 ;
                String hh = h < 10 ? "0"+h: h+"";
                String mm = m < 10 ? "0"+m: m+"";
                String ss = s < 10 ? "0"+s: s+"";
                cArg.setText(hh+":"+mm+":"+ss);
            }
        });*/
       /* chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer c) {
                int cTextSize = c.getText().length();
                if (cTextSize == 5) {
                    chronometer.setText("00:"+c.getText().toString());
                } else if (cTextSize == 7) {
                    chronometer.setText("0"+c.getText().toString());
                }
            }
        });*/


    }


    public void nextLevel() {

        levelCounter++;
        gameEnded = false;
        zugCounter = 0;
        step(true);
        chronometer.stop();
        loadLevel();

    }


    public void addieren() {

        if (gameEnded == true && levelCounter == 5) {
            Intent in = new Intent(this, MenuActivity.class);
            startActivity(in);
        } else if (gameEnded == true && ans != Goal) {
        } else {
            if (anton.toString() == "PLUS") ans = ans + plusZahl;
            if (anton.toString() == "MINUS") ans = ans - plusZahl;
            if (anton.toString() == "MAL") ans = ans * plusZahl;
            if (anton.toString() == "TEIL") ans = ans / plusZahl;

            String zwischenErgebnis = String.valueOf(ans);
            Ausgabe.setText(zwischenErgebnis);
            ziehen();
        }
    }

    public void subtrahieren() {

        if (gameEnded == true && levelCounter == 5) {
            Intent in = new Intent(this, MenuActivity.class);
            startActivity(in);
        } else if (gameEnded == true && ans != Goal) {
        } else {
            if (berta.toString() == "PLUS") ans = ans + minusZahl;
            if (berta.toString() == "MINUS") ans = ans - minusZahl;
            if (berta.toString() == "MAL") ans = ans * minusZahl;
            if (berta.toString() == "TEIL") ans = ans / minusZahl;

            String zwischenErgebnis = String.valueOf(ans);
            Ausgabe.setText(zwischenErgebnis);
            ziehen();
        }
    }

    public void multiplizieren() {

        if (gameEnded == true && levelCounter == 5) {
            Intent in = new Intent(this, MenuActivity.class);
            startActivity(in);
        } else if (gameEnded == true && ans != Goal) {
        } else {
            if (chris.toString() == "PLUS") ans = ans + malZahl;
            if (chris.toString() == "MINUS") ans = ans - malZahl;
            if (chris.toString() == "MAL") ans = ans * malZahl;
            if (chris.toString() == "TEIL") ans = ans / malZahl;

            String zwischenErgebnis = String.valueOf(ans);
            Ausgabe.setText(zwischenErgebnis);
            ziehen();
        }
    }

    public void dividieren() {

        if (gameEnded == true && levelCounter == 5) {
            Intent in = new Intent(this, MenuActivity.class);
            startActivity(in);
        } else if (gameEnded == true && ans != Goal) {
        } else {
            if (doofie.toString() == "PLUS") ans = ans + teilZahl;
            if (doofie.toString() == "MINUS") ans = ans - teilZahl;
            if (doofie.toString() == "MAL") ans = ans * teilZahl;
            if (doofie.toString() == "TEIL") ans = ans / teilZahl;

            String zwischenErgebnis = String.valueOf(ans);
            Ausgabe.setText(zwischenErgebnis);
            ziehen();
        }
    }


    private void ziehen() {

        zugCounter++;
        step(false);
        setBubbleText();

        zugCounter++;
        step(false);
        setBubbleText();

        if (zugCounter == levelCounter && ans == Goal) {
            Ausgabe.setText("Gewonnen!");

            if (levelCounter == 5) {


                finalMessage.setVisibility(View.VISIBLE);

                if (inARow) {
                    finalMessage.setText(R.string.finalMessage);
                    resetScoreAnzeige.setText(resetCounter + "");
                    if (readHighscore() > resetCounter) {
                        writeHighscore(resetCounter);
                    }
                    currentHighScoreAnzeige.setText(readHighscore() + "");
                    highscoreAnzeige.setVisibility(View.VISIBLE);
                } else {
                    String test = getResources().getString(R.string.finalMessage2);
                    finalMessage.setText(test);

                }
            }
            TextView headLine = (TextView) dialog.findViewById(R.id.headline);
            headLine.setText("gewonnen");

            dialogNextLevel.setVisibility(View.VISIBLE);    //nextLevel Button anzeigen
            dialogReset.setVisibility(View.GONE);           //Reset Button ausblenden

            dialog.show();
        }



        if (zugCounter == levelCounter && ans != Goal) {
            Ausgabe.setText("Verloren!");
            gameEnded = true;
            Log.i("dialog", "funktioniert");
           /*     try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }*/


            chronometer.stop();
            chronometer.setBase(SystemClock.elapsedRealtime());
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
        /*chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());*/
        chronometer.start();


    }

    public enum Rechenoperation {
        PLUS,
        MINUS,
        MAL,
        TEIL;

        public static Rechenoperation getEnumByValue(int value) {
            switch (value) {
                case 0:
                    return PLUS;
                case 1:
                    return MINUS;
                case 2:
                    return MAL;
                case 3:
                    return TEIL;
                default:
                    return null;
            }
        }

        public String getRechenzeichen() {
            switch (this) {
                case PLUS:
                    return "+";
                case MINUS:
                    return "-";
                case MAL:
                    return "x";
                case TEIL:
                    return ":";
                default:
                    return null;
                //domi
            }
        }
    }


    //Zufallszahlen berechnen
    //
    //
    public int zielen() {

        Random Zufall = new Random();

        for (int i = 0; i < levelCounter; i++) {
            int dynamik = Zufall.nextInt(4);

            if (dynamik == 0) {
                plus();
            }
            if (dynamik == 1) {
                minus();
            }
            if (dynamik == 2) {
                malen();
            }
            if (dynamik == 3) {
                teilen();
            }
        }
        return Goal;
    }

    public void plus() {

        if (anton.toString() == "PLUS") Goal = Goal + plusZahl;
        if (anton.toString() == "MINUS") Goal = Goal - plusZahl;
        if (anton.toString() == "MAL") Goal = Goal * plusZahl;
        if (anton.toString() == "TEIL") Goal = Goal / plusZahl;

    }

    public void minus() {

        if (berta.toString() == "PLUS") Goal = Goal + minusZahl;
        if (berta.toString() == "MINUS") Goal = Goal - minusZahl;
        if (berta.toString() == "MAL") Goal = Goal * minusZahl;
        if (berta.toString() == "TEIL") Goal = Goal / minusZahl;

    }

    public void malen() {

        if (chris.toString() == "PLUS") Goal = Goal + malZahl;
        if (chris.toString() == "MINUS") Goal = Goal - malZahl;
        if (chris.toString() == "MAL") Goal = Goal * malZahl;
        if (chris.toString() == "TEIL") Goal = Goal / malZahl;

    }

    public void teilen() {

        if (doofie.toString() == "PLUS") Goal = Goal + teilZahl;
        if (doofie.toString() == "MINUS") Goal = Goal - teilZahl;
        if (doofie.toString() == "MAL") Goal = Goal * teilZahl;
        if (doofie.toString() == "TEIL") Goal = Goal / teilZahl;

    }
    //
    //
    // Ende Zufallszahlen berechnen


    public void levelEins_starten() {
        levelCounter = 1;
        inARow = true;
        resetCounter = 0;
        loadLevel();
        reset();
    }

    public void levelZwei_starten() {
        levelCounter = 2;
        inARow = false;
        loadLevel();
        reset();
    }

    public void levelDrei_starten() {
        levelCounter = 3;
        inARow = false;
        loadLevel();
        reset();
    }

    public void levelVier_starten() {
        levelCounter = 4;
        inARow = false;
        loadLevel();
        reset();

    }

    public void levelFuenf_starten() {
        levelCounter = 5;
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
                        ZeitActivity.super.onBackPressed();
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
        SharedPreferences pref = getSharedPreferences("GAME", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("HIGHSCORE", highscore);
        editor.commit();
    }

    public int readHighscore() {
        SharedPreferences pref = getSharedPreferences("GAME", 0);
        return pref.getInt("HIGHSCORE", 0);
    }


}