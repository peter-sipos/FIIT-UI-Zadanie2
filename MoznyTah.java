import java.util.List;

public class MoznyTah {

    private int posunRiadok;
    private int posunStlpec;
    private List<MoznyTah> mozneTahy;

    public MoznyTah(int posunRiadok, int posunStlpec) {
        this.posunRiadok = posunRiadok;
        this.posunStlpec = posunStlpec;
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

    public List<MoznyTah> getMozneTahy() {
        return mozneTahy;
    }

    public void setMozneTahy(List<MoznyTah> mozneTahy) {
        this.mozneTahy = mozneTahy;
    }


}
