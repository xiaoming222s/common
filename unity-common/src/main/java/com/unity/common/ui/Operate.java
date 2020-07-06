package com.unity.common.ui;


public enum Operate {

    eq("eq", "等于"), ne("ne", "不等于"), in("in", "包含"), ni("ni", "不包含"),
    bw("bw", "右匹配"), bn("bn", "右不匹配"), ew("ew", "左匹配"), en("en", "左不匹配"),
    cn("cn", "左右匹配"), nc("nc", "不左右匹配"), nu("nu", "空"), nn("nn", "非空"),
    lt("lt", "小于"), le("le", "小于等于"), gt("gt", "大于"), ge("ge", "大于等于"),
    ex("ex", "存在"), nx("nx", "不存在");

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

    Operate(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Operate of(String id) {


        
        if (id.equals(eq.getId())) {
            return eq;
        }

        if (id.equals(ne.getId()))  {
            return ne;
        }

        if (id.equals(in.getId()))  {
            return in;
        }
        if (id.equals(ni.getId()))  {
            return ni;
        }
        
        if (id.equals(bw.getId()))  {
            return bw;
        }

        if (id.equals(bn.getId()))  {
            return bn;
        }

        if (id.equals(ew.getId()))  {
            return ew;
        }
        if (id.equals(en.getId()))  {
            return en;
        }

        if (id.equals(cn.getId()))  {
            return cn;
        }

        if (id.equals(nc.getId()))  {
            return nc;
        }

        if (id.equals(nu.getId()))  {
            return nu;
        }
        if (id.equals(nn.getId()))  {
            return nn;
        }

        if (id.equals(ex.getId()))  {
            return ex;
        }
        if (id.equals(nx.getId()))  {
            return nx;
        }
        
        if (id.equals(lt.getId()))  {
            return lt;
        }
        if (id.equals(le.getId()))  {
            return le;
        }
        if (id.equals(gt.getId()))  {
            return gt;
        }
        if (id.equals(ge.getId()))  {
            return ge;
        }
        
        return null;
    }

}
