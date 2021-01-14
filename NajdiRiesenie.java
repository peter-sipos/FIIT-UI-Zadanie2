import java.util.*;

public class NajdiRiesenie {

    private Sachovnica sachovnica;
    int riadky;
    int stlpce;
    int pocetVygenerovanychUzlov;
    int pocetRozvinutychUzlov;
    private Stack<Uzol> stack;
    private List<Uzol> navstivene;



    public NajdiRiesenie(Sachovnica sachovnica, int riadky, int stlpce){
        this.sachovnica = sachovnica;
        this.riadky = riadky;
        this.stlpce = stlpce;
        this.pocetRozvinutychUzlov = 0;
        this.pocetVygenerovanychUzlov = 0;
    }


    public List<Integer> najdiRiesenie(int startRiadok, int startStlpec){ //Hlavna funkcia algoritmu.

        int farbaStartu = zistiFarbuStartu(startRiadok, startStlpec);

        System.out.println("Pociatocna pozicia riadku: " + startRiadok);
        System.out.println("Pociatocna pozicia stlpca: " + startStlpec);
        vypisFarbuSartovaciehoPolicka(farbaStartu);

        //Pre urychlenie hladania moznych rieseni problemu program hladanie ani nezacne ak vie, ze spravne riesenie
        //neexistuje. To je pripad, ked startovacie policko je biele a pocet policok sachovnice je neparny.
        if (1 == farbaStartu && ((riadky*stlpce)%2) == 1){
            System.out.println("Ak je pocet poli sachovnice neparny a pociatocne policko je biele, riesenie problemu nexistuje");
            return null;
        }

        //Nastavenie startovacieho uzla
        Uzol start = new Uzol();
        start.setRiadok(startRiadok);
        start.setStlpec(startStlpec);
        start.setTah(1);
        start.setRodic(null);
        start.setStupen(vypocitajStupen(start));

        int konecnyTah = riadky*stlpce; //Hodnota posledneho tahu konom, respektive hodnota tahu v cielovom stave

        stack = new Stack<>(); //Sluzi na ukladanie generovanych uzlov
        navstivene = new ArrayList<>(); //Sluzi na ukladanie navstivenych uzlov

        stack.add(start);

        Uzol sucasny;

        while (!stack.isEmpty()){
            sucasny = stack.pop();  //Rozvinutie uzla
            pocetRozvinutychUzlov++;


            if (sucasny.getTah() == konecnyTah){ //Ak si dosiel na posledny tah, teda do cieloveho stavu, vrat postupnost operatorov
                sachovnica.nastavHodnotu(sucasny.getTah(), sucasny.getRiadok(), sucasny.getStlpec());
                return ziskajRiesenieZUzla(sucasny);

            } else {
                sucasny.setDeti(vytvorDeti(sucasny)); //Vygeneruj moznych nasledovnikov aktualneho uzla (jeho deti)
                if (sucasny.getDeti().isEmpty()){
                    //Ak uzol nema ziadne deti (teda nie je mozne sa pohnut na ziadne dalsie policko),
                    //tak uzol dalej nerozvijaj a pokracuj dalsim dietatom rodica uzla (nasledujucim uzlom zo zasobnika)
                    sucasny.getRodic().getDeti().remove(sucasny);       //Vymaz uzol zo zoznamu deti jeho rodica
                    while (sucasny.getRodic().getDeti().isEmpty()) {    //Pokial sa nepride na uzol, ktoreho rodic ma este nejake dieta
                        navstivene.remove(sucasny.getRodic());          //Odober uzol zo zoznamu navstivenych policok sachovnice
                        sucasny = sucasny.getRodic();
                        sucasny.getRodic().getDeti().remove(sucasny);   //Vymaz povodneho rodica zo zoznamu deti jeho rodica
                    }
                    continue;
                } else {
                    if (!jeNavstiveny(navstivene, sucasny.getRiadok(), sucasny.getStlpec())) {
                        navstivene.add(sucasny); //Ak dany uzol este nebol navstiveny, pridaj ho do navstivenych (rozvin ho)
                        sachovnica.nastavHodnotu(sucasny.getTah(), sucasny.getRiadok(), sucasny.getStlpec()); //Pridaj na policko sachovnice aktualny tah
                        Collections.sort(sucasny.getDeti()); //Usporiadaj deti od najvacsieho po najmensi podla ich stupna (teda podla poctu moznych platnych tahov kona)
                        stack.addAll(sucasny.getDeti()); //A pridaj ich do zasobnika. Prvy prvok v pridavanom poli ide na najspodnejsie mozno miesto v zasaboniku
                        pocetVygenerovanychUzlov += sucasny.getDeti().size();
                    }
                }

            }
        }
        return null; //Ak sa nepodarilo vratit postupnost vykonanych operatorov (riesenie sa nenaslo), vrat prazdnu mnozinu (null)
    }


    //Funkcia ktora z uzla vrati postupnost pouzitych operatorov na dostanie sa donho zo zaciatocneho uzla
    public List<Integer> ziskajRiesenieZUzla(Uzol koniec){
        List<Integer> riesenie = new ArrayList<>();
        Uzol aktualny = koniec;

        while (aktualny.getRodic() != null){
            riesenie.add(0, aktualny.getPosunStlpec());
            riesenie.add(0, aktualny.getPosunRiadok());
            aktualny = aktualny.getRodic();
        }
        return riesenie;
    }

    //Funkcia ktora vytvori nasledovnikov daneho uzla
    public List<Uzol> vytvorDeti(Uzol aktualny){
        List<Uzol> deti = new ArrayList<>();

        //Iteruj cez mozne tahy kona
        List<MoznyTah> mozneTahy = vytvorTahy();
        for (MoznyTah tah : mozneTahy) {

            //Ak sa mozes z aktualnej pozicie pohnut na validnu poziciu, tak vytvor novy uzol s danymi parametrami a pridaj ho do deti
            if (validnyKrok(aktualny.getRiadok()+tah.getPosunRiadok(), aktualny.getStlpec()+tah.getPosunStlpec())) {
                Uzol novy = new Uzol();
                novy.setRiadok(aktualny.getRiadok()+tah.getPosunRiadok());
                novy.setStlpec(aktualny.getStlpec()+tah.getPosunStlpec());
                novy.setRodic(aktualny);
                novy.setTah(aktualny.getTah()+1);
                novy.setStupen(vypocitajStupen(novy));
                novy.setPosunRiadok(tah.getPosunRiadok());
                novy.setPosunStlpec(tah.getPosunStlpec());
                deti.add(novy);
            }
        }
        return deti;
    }

    //Funkcia sluziaca na vypocet stupna uzla, teda na kolko policok sa moze kon z aktualneho policka pohnut
    public int vypocitajStupen(Uzol uzol) {

        int stupen = 0;
        List<MoznyTah> mozneTahy = vytvorTahy();
        for (MoznyTah tah : mozneTahy){
            if(validnyKrok(uzol.getRiadok()+tah.getPosunRiadok(), uzol.getStlpec()+tah.getPosunStlpec())){
                stupen++;
            }
        }
        return stupen;
    }

    //Zisti, ci je nejaky navrhovany krok validny
    public boolean validnyKrok(int riadok, int stlpec){
        //Krok bude validny, ak nebude dalsia pozicia mimo hracej plochu
        if (riadok >= riadky || riadok < 0){
            return false;
        }
        if (stlpec >= stlpce || stlpec < 0){
            return false;
        }

        //A ak uz policko na ktore vedie nie je zabrate --> neexistuje navstiveny (rozvity) uzol s danym riadkom a stlpcom (poziciou)
        if (jeNavstiveny(navstivene, riadok, stlpec)){
            return false;
        }

        return true;
    }

    //Jednoducha funkcia ktora zisti, ci sa dany uzol nachadza v navstivenych (teda ci uz bol niekedy rozvity)
    public boolean jeNavstiveny(List<Uzol> navstivene, int riadok, int stlpec){
        if (navstivene == null || navstivene.isEmpty()){
            return false;
        }

        for (Uzol uzol : navstivene){
            if(uzol.getRiadok() == riadok && uzol.getStlpec() == stlpec){
                return true;
            }
        }
        return false;
    }

    //Vrati zoznam moznych tahov
    public List<MoznyTah> vytvorTahy(){
        List<MoznyTah> mozneTahy = new ArrayList<>();
        mozneTahy.add(new MoznyTah(1,2));
        mozneTahy.add(new MoznyTah(1,-2));
        mozneTahy.add(new MoznyTah(2,1));
        mozneTahy.add(new MoznyTah(2,-1));
        mozneTahy.add(new MoznyTah(-1,2));
        mozneTahy.add(new MoznyTah(-1,-2));
        mozneTahy.add(new MoznyTah(-2,1));
        mozneTahy.add(new MoznyTah(-2,-1));
        return mozneTahy;
    }

    //Vypise farbu startovacieho policka
    public void vypisFarbuSartovaciehoPolicka(int farbaStartu){
        if (farbaStartu == 0){
            System.out.println("Startovacie policko je cierne");
        } else {
            System.out.println("Startovacie policko je biele");
        }
    }

    //Zisti farbu startovacieho policka. 0 je cierne policko, 1 je biele
    public int zistiFarbuStartu(int riadok, int stlpec){
        if ((riadok+stlpec)%2 == 0){
            return 0;
        } else {
            return 1;
        }
    }

    public void vypisPocetUzlov(){
        System.out.println("Pocet vygenerovanych uzlov: " + pocetVygenerovanychUzlov);
        System.out.println("Pocet rozvinutych uzlov: " + pocetRozvinutychUzlov);
    }
}
