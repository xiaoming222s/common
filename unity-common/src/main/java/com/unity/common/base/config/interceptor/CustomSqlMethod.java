package com.unity.common.base.config.interceptor;

public enum CustomSqlMethod {
    SELECT_COUNT2("selectCount2", "查询满足条件总记录数", "<script>\nSELECT COUNT(1) FROM %s %s\n</script>"),
    SELECT_COUNT3("selectCount3", "查询满足条件总记录数", "<script>\nSELECT COUNT(1) FROM %s %s\n</script>"),

    /**
     * 物理删除
     */
    PHYSICAL_DELETE_BY_ID("physicalDeleteById", "根据ID 删除一条数据", "<script>\nDELETE FROM %s WHERE %s=#{%s}\n</script>"),
    PHYSICAL_DELETE_BY_MAP("physicalDeleteByMap", "根据columnMap 条件删除记录", "<script>\nDELETE FROM %s %s\n</script>"),
    PHYSICAL_DELETE("physicalDelete", "根据 entity 条件删除记录", "<script>\nDELETE FROM %s %s\n</script>"),
    PHYSICAL_DELETE_BATCH_BY_IDS("physicalDeleteBatchIds", "根据ID集合，批量删除数据", "<script>\nDELETE FROM %s WHERE %s IN (%s)\n</script>");


    private final String method;
    private final String desc;
    private final String sql;

    CustomSqlMethod(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }

    public String getMethod() {
        return method;
    }

    public String getDesc() {
        return desc;
    }

    public String getSql() {
        return sql;
    }
}
