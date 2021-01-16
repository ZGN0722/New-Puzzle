package com.example.puzzlegame.Info;

public class AnswerInfo {
    private long _id;
    private long paper_id;
    private long question_id;
    private String answer_value;

    public long get_id() {
        return _id;
    }

    public long getPaper_id() {
        return paper_id;
    }

    public long getQuestion_id() {
        return question_id;
    }

    public String getAnswer_value() {
        return answer_value;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public void setPaper_id(long paper_id) {
        this.paper_id = paper_id;
    }

    public void setAnswer_value(String answer_value) {
        this.answer_value = answer_value;
    }

    public void setQuestion_id(long question_id) {
        this.question_id = question_id;
    }
}
