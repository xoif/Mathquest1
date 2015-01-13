package paetow.seifert.mathquest;

import java.util.Random;

/**
 * Created by Domi on 13.01.15.
 */
public class Tools{

    public static int calculate(int x, ButtonProp b){

        switch(b.operator){
            case 0: x += b.value;break;
            case 1: x -= b.value;break;
            case 2: x *= b.value;break;
            case 3: x /= b.value;break;
            default: break;
        }
        return x;
    }

    public static void aim(){
        Random zufall = new Random();
        for (int i = 0; i < EinsActivity.levelCounter; i++) {
            int dynamik = zufall.nextInt(4);

            if (dynamik == 0) {
               EinsActivity.Goal = calculate(EinsActivity.Goal, EinsActivity.buttonA);
            }
            if (dynamik == 1) {
                EinsActivity.Goal = calculate(EinsActivity.Goal, EinsActivity.buttonB);
            }
            if (dynamik == 2) {
                EinsActivity.Goal = calculate(EinsActivity.Goal, EinsActivity.buttonC);
            }
            if (dynamik == 3) {
                EinsActivity.Goal = calculate(EinsActivity.Goal, EinsActivity.buttonD);
            }
        }
    }
}
