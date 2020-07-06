
package com.unity.common.ui;

public enum GroupOp {
    or("OR", "或者"), and("AND", "并且");

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    GroupOp(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static GroupOp of(String id) {


        
        if (id.equals(or.getId()))  {
            return or;
        }

        if (id.equals(and.getId()))  {
            return and;
        }


        return null;
    }

}
