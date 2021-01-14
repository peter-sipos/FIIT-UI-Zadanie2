import java.util.Scanner;
import java.util.concurrent.*;

public class Main  {



    public static void main(String[] args) throws TimeoutException, ExecutionException {

        System.out.println("Ak chcete zobrazit vzorove riesenie (10 rieseni pre sachovnicu 8x8) zadajte 'vzorove'");
        System.out.println("Ak chcete skusit Vami dany pocet rieseni pre Vami danu sachovnicu zadajte 'dynamicke'");
        Scanner input = new Scanner(System.in);
        String sposobRiesenia = input.next();
        Riesenie riesenie = new Riesenie();
        if (sposobRiesenia.equals("vzorove")){
           riesenie.vzoroveRiesenie();
        }
        else if (sposobRiesenia.equals("dynamicke")){
            riesenie.dynamickeRiesenie();
        } else {
            System.out.println("Zadali ste nevalidny input");
        }

    }



}
