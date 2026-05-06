package com.grampanchayat.app.models;
public class Poll {
    public int id, votes1, votes2, votes3;
    public String question, option1, option2, option3;
    public Poll(int id, String question, String option1, String option2, String option3,
                int votes1, int votes2, int votes3) {
        this.id=id; this.question=question; this.option1=option1; this.option2=option2;
        this.option3=option3; this.votes1=votes1; this.votes2=votes2; this.votes3=votes3;
    }
}
