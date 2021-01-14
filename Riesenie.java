import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;

public class Riesenie {

    public void dynamickeRiesenie(){
        Scanner stdin = new Scanner(System.in);

        System.out.println("Zadajte ziadany pocet rieseni problemu");
        int pocetRieseni = stdin.nextInt();

        System.out.println("Zadajte pocet riadkov a stlpcov sachovnice");
        int riadky = stdin.nextInt();
        int stlpce = stdin.nextInt();

        System.out.println("Zadajte casovy limit (v sekundach), do ktoreho musi program najst riesenie problemu");
        int casovyLimit = stdin.nextInt();

        ExecutorService exekutor;

        for(int iter = 1; iter<=pocetRieseni; iter++) {

            //ExecutorService sluzi na casove ohranicenie behu hladania. Automaticky zastavi hladanie riesenia,
            //ak sa nenaslo do 15 sekund a program pokracuje hladanim riesenia pre dalsi pociatocny bod.
            //Na zaver ukonci vykonavanie celeho programu.
            //zdroj: https://stackoverflow.com/questions/4044726/how-to-set-a-timer-in-java
            exekutor = Executors.newSingleThreadExecutor();

            System.out.println("Zacinam riesenie c.: " + iter);
            try {
                Runnable r = new Runnable() {
                    @Override
                    public void run() {

                        Sachovnica sachovnica = new Sachovnica(riadky, stlpce);

                        NajdiRiesenie riesenie = new NajdiRiesenie(sachovnica, riadky, stlpce);

                        //Pri dynamickom rieseni program vzdy zacina hladat postupnost krokov z nahodneho policka na sachovnici.
                        Random nahoda = new Random();
                        List<Integer> postupnostRiesenia = riesenie.najdiRiesenie(nahoda.nextInt(riadky), nahoda.nextInt(stlpce));


                        //Ak sa riesenie problemu z daneho startovacieho policka nenaslo (respektive nenaslo do
                        //casoveho limitu alebo neexistuje vobec), hladanie riesenia vrati null.
                        //Ak sa riesenie naslo, program vypise set pouzitych operatorov, pouzitim ktorych sa dospelu k rieseniu.
                        if (postupnostRiesenia == null) {
                            riesenie.vypisPocetUzlov();
                            System.out.println("Riesenie sa nenaslo\n");
                        } else {
                            riesenie.vypisPocetUzlov();
                            vypisRiesenie(sachovnica, postupnostRiesenia);
                        }
                    }
                };

                Future<?> f = exekutor.submit(r);

                f.get(casovyLimit, TimeUnit.SECONDS); //Nastavenie casoveho limitu
            } catch (final InterruptedException e) {
                e.printStackTrace();
            } catch (final TimeoutException e) {
                System.out.println("Riesenie sa nenaslo v danom case\n");
                if (iter == pocetRieseni){
                    System.exit(0);
                }
                exekutor.shutdown();
                continue; //Ak sa nenaslo riesenie do casoveho limitu, pokracuj hladanim riesenia pre dalsiu startovaciu poziciu.
            } catch (final ExecutionException e) {
                e.printStackTrace();
            } finally {
                if(iter<pocetRieseni){
                    continue;
                }
                exekutor.shutdown();
                System.exit(0);
            }
        }
    }

    public void vzoroveRiesenie(){
        ExecutorService service;
        int pocet_rieseni = 10;
        int riadky = 8;
        int stlpce = 8;

        int startRiadok = 0;
        int startStlpec = 0;

        for(int iter = 1; iter<=pocet_rieseni; iter++) {
            System.out.println("Zacinam riesenie c.: " + iter);

            Sachovnica sachovnica = new Sachovnica(riadky, stlpce);
            NajdiRiesenie riesenie = new NajdiRiesenie(sachovnica, riadky, stlpce);
            List<Integer> postupnostRiesenia = riesenie.najdiRiesenie(startRiadok, startStlpec) ;

            startRiadok++;
            if (iter == 7){
                startRiadok = 0;
                startStlpec++;
            }

            if (postupnostRiesenia == null) {
                riesenie.vypisPocetUzlov();
                System.out.println("Riesenie sa nenaslo\n");
            } else {
                riesenie.vypisPocetUzlov();
                vypisRiesenie(sachovnica, postupnostRiesenia);
            }

        }
    }

    //Funkcia vypise postupnost operatorov pouzitych na dosiahnutie riesenia.
    public void vypisRiesenie(Sachovnica sachovnica, List<Integer> postupnostRiesenia){
        System.out.println("Najdene riesenie:");
        sachovnica.ukazSachovnicu();
        System.out.println();
        System.out.println("Postuponost operatorov v poradi 'posun riadku' a 'posun stlpca':");
        System.out.println(postupnostRiesenia + "\n\n");
    }


}
