package Model;

public class Bildirim {

    private String kullaniciid;
    private String text;
    private String gonderiid;
    private boolean ispost; // gonderildi,gonderme durumu için

    public Bildirim() {
    }

    public Bildirim(String kullaniciId, String text, String gonderiId, boolean ispost) {
        this.kullaniciid = kullaniciId;
        this.text = text;
        this.gonderiid = gonderiId;
        this.ispost = ispost;
    }

    public String getKullaniciId() {
        return kullaniciid;
    }

    public void setKullaniciId(String kullaniciId) {
        this.kullaniciid = kullaniciId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getGonderiId() {
        return gonderiid;
    }

    public void setGonderiId(String gonderiId) {
        this.gonderiid = gonderiId;
    }

    public boolean isIspost() {
        return ispost;
    }

    public void setIspost(boolean ispost) {
        this.ispost = ispost;
    }
}
