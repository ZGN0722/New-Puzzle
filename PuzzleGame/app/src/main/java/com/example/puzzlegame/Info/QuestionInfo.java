package com.example.puzzlegame.Info;

public class QuestionInfo {
    private long _id;
    private String question;
    private String type;
    private long paper_id;

    public long get_id() {
        return _id;
    }

    public long getPaper_id() {
        return paper_id;
    }

    public String getQuestion() {
        return question;
    }

    public String getType() {
        return type;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public void setPaper_id(long paper_id) {
        this.paper_id = paper_id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setType(String type) {
        this.type = type;
    }
}
