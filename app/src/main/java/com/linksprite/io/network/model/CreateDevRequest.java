package com.linksprite.io.network.model;

/**
 * Created by Administrator on 2016/8/8.
 */
public class CreateDevRequest {


    /**
     * name : Switch
     * group : LINKNODE
     * type : 01
     */

    private String name;
    private String group;
    private String type;

    public CreateDevRequest(String name, String group, String type) {
        this.name = name;
        this.group = group;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
