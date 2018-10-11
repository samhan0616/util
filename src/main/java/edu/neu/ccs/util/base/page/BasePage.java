package edu.neu.ccs.util.base.page;

public class BasePage {
    private Integer page = Integer.valueOf(1);
    private Integer pageSize = Integer.valueOf(20);
    private Pager pager = new Pager();

    public BasePage() {
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



}
