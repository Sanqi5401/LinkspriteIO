package com.linksprite.io.adapter;

public class GroupSort implements Comparable {

    private String mGroupName;
    private int size;

    public GroupSort(String mGroupName, int size) {
        this.mGroupName = mGroupName;
        this.size = size;
    }

    public String getGroupName() {
        return mGroupName;
    }

    public void setGroupName(String mGroupName) {
        this.mGroupName = mGroupName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int compareTo(Object another) {
        GroupSort sort = (GroupSort) another;
        int otherSize = sort.getSize();
        return  otherSize - this.size ;
    }
}