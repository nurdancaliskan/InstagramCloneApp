package Model;

public class Kullanici {

    private String id;
    private String kullaniciadi;
    private String ad;
    private String resimurl;
    private String soyad;
    private String bio;

    public Kullanici() {
    }

    public Kullanici(String id, String kullaniciadi, String ad, String resimurl, String soyad, String bio) {
        this.id = id;
        this.kullaniciadi = kullaniciadi;
        this.ad = ad;
        this.resimurl = resimurl;
        this.soyad = soyad;
        this.bio = bio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKullaniciadi() {
        return kullaniciadi;
    }

    public void setKullaniciadi(String kullaniciadi) {
        this.kullaniciadi = kullaniciadi;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getResimurl() {
        return resimurl;
    }

    public void setResimurl(String resimurl) {
        this.resimurl = resimurl;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
