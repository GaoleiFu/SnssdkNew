package com.devdroid.snssdknew.eventbus;

public class OnUpdateProgressBackup {
    private int progressNum;
    private int typeProgress;
    public OnUpdateProgressBackup(int progressNum, int typeProgress) {
        this.progressNum = progressNum;
        this.typeProgress = typeProgress;
    }

    public int getTypeProgress() {
        return typeProgress;
    }

    public int getProgressNum() {
        return progressNum;
    }
}
