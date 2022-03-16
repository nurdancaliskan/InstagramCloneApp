package com.nurdancaliskan.instagramclone;

public class Gonderi {

    private String gonderiId;
    private String gonderiResmi;
    private String gonderiHakkinda;
    private String gonderen;

    public Gonderi(){
    }

    public Gonderi(String gonderiId) {
        this.gonderiId = gonderiId;
    }

    public Gonderi(String gonderiId, String gonderiResmi, String gonderiHakkinda, String gonderen) {
        this.gonderiId = gonderiId;
        this.gonderiResmi = gonderiResmi;
        this.gonderiHakkinda = gonderiHakkinda;
        this.gonderen = gonderen;
    }

    public String getGonderiId() {
        return gonderiId;
    }

    public void setGonderiId(String gonderiId) {
        this.gonderiId = gonderiId;
    }

    public String getGonderiResmi() {
        return gonderiResmi;
    }

    public void setGonderiResmi(String gonderiResmi) {
        this.gonderiResmi = gonderiResmi;
    }

    public String getGonderiHakkinda() {
        return gonderiHakkinda;
    }

    public void setGonderiHakkinda(String gonderiHakkinda) {
        this.gonderiHakkinda = gonderiHakkinda;
    }

    public String getGonderen() {
        return gonderen;
    }

    public void setGonderen(String gonderen) {
        this.gonderen = gonderen;
    }
}
