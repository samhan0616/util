package edu.neu.ccs.util.base.vo;

import edu.neu.ccs.util.base.page.Pager;

public class BaseVO{

    //private T t;

    //private String traceSql;
    private String whereClause;


    public String getWhereClause() {
        return whereClause;
    }
    private Integer page = Integer.valueOf(1);
    private Integer pageSize = Integer.valueOf(20);
    private Pager pager = new Pager();

    public BaseVO() {
    }



    public Pager getPager() {
        this.pager.setPageId(this.getPage().intValue());
        this.pager.setPageSize(this.getPageSize().intValue());
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public Integer getPage() {
        return this.page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setWhereClause(String whereClause) {
        this.whereClause = whereClause;
    }

//    public String getTraceSql() {
//        return traceSql;
//    }
//
//    public void setTraceSql(String traceSql) {
//        this.traceSql = traceSql;
//    }

}