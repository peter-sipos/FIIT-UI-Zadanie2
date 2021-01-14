import java.util.Arrays;

public class Sachovnica {

    private int[][] sachovnica;
    private int riadky;
    private int stlpce;

    public Sachovnica(int riadky, int stlpce){
        this.sachovnica = new int[riadky][stlpce];
        this.riadky = riadky;
        this.stlpce = stlpce;
    }

    public void nastavHodnotu(int hodnota, int riadok, int stlpec){
        sachovnica[riadok][stlpec] = hodnota;
    }

    public void ukazSachovnicu(){
        for (int i = 0; i<riadky; i++){
            for (int j = 0; j< stlpce; j++){
                System.out.printf("%d ", sachovnica[i][j]);
            }
            System.out.println();
        }
    }
}
