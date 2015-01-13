package paetow.seifert.mathquest;

import android.widget.Button;

import java.util.Random;

/**
 * Created by Domi on 13.01.15.
 */
public class ButtonProp {

    int value;
    int operator;
    Random zufall = new Random();

    public ButtonProp(){

        value = zufall.nextInt(8+1);
        operator = zufall.nextInt(3);
    }

    public void newvalue(){
        value = zufall.nextInt(8+1);
        operator = zufall.nextInt(3);
    }
}
