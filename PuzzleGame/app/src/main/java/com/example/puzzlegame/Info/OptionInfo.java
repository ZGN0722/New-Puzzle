package com.example.puzzlegame.Info;

public class OptionInfo {
    private long _id;
    private String value;
    private String content;
    private long option_id;

    public long get_id() {
        return _id;
    }

    public long getOption_id() {
        return option_id;
    }

    public String getValue() {
        return value;
    }

    public String getContent() {
        return content;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setOption_id(long option_id) {
        this.option_id = option_id;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
