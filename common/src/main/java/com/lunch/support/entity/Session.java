package com.lunch.support.entity;

public class Session extends BaseOAuth {
    private static final int TIME_OUT = 7 * 24 * 60 * 60 * 1000;
    //根据时间戳生成的session
    private String session;

    public Session() {

    }

    public Session(String session, long time) {
        super(time);
        this.session = session;
    }

    public Session(String session) {
        this.session = session;
    }

    @Override
    public String getResult() {
        return session;
    }

    public void setResult(String session) {
        this.session = session;
    }

    @Override
    protected int registerTime() {
        return TIME_OUT;
    }
}
