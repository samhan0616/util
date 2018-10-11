package edu.neu.ccs.util.base.pager;

import edu.neu.ccs.util.base.page.Pager;

import java.util.List;

public class PageUtil<T> {

    public static<T> PageInfo<T> getPageInfo(Pager pager, List<T> rows) {
        PageInfo<T> pageInfo = new PageInfo<T>();
        pageInfo.setCount((long) pager.getRowCount());
        pageInfo.setPageCount((long) pager.getPageCount());
        pageInfo.setPageNo(pager.getPageId());
        pageInfo.setPageSize(pager.getPageSize());
        pageInfo.setList(rows);
        return pageInfo;
    }
}