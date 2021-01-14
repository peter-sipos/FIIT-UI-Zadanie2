import java.util.List;

public class Uzol implements Comparable<Uzol>{

    private Uzol rodic;
    private List<Uzol> deti;
    private int riadok;
    private int stlpec;
    private int tah;
    private int stupen;
    private int posunRiadok;
    private int posunStlpec;


    public Uzol getRodic() {
        return rodic;
    }

    public void setRodic(Uzol rodic) {
        this.rodic = rodic;
    }

    public List<Uzol> getDeti() {
        return deti;
    }

    public void setDeti(List<Uzol> deti) {
        this.deti = deti;
    }

    public int getRiadok() {
        return riadok;
    }

    public void setRiadok(int riadok) {
        this.riadok = riadok;
    }

    public int getStlpec() {
        return stlpec;
    }

    public void setStlpec(int stlpec) {
        this.stlpec = stlpec;
    }

    public int getTah() {
        return tah;
    }

    public void setTah(int tah) {
        this.tah = tah;
    }

    public int getStupen() {
        return stupen;
    }

    public void setStupen(int stupen) {
        this.stupen = stupen;
    }

    @Override
    public int compareTo(Uzol porovnavany) {
        int porovnajStupen = ((Uzol)porovnavany).getStupen();
        return porovnajStupen - this.stupen;
    }

    public int getPosunRiadok() {
        return posunRiadok;
    }

    public void setPosunRiadok(int posunRiadok) {
        this.posunRiadok = posunRiadok;
    }

    public int getPosunStlpec() {
        return posunStlpec;
    }

    public void setPosunStlpec(int posunStlpec) {
        this.posunStlpec = posunStlpec;
    }
}
