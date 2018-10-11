package edu.neu.ccs.util.base.service;

import edu.neu.ccs.util.base.dao.BaseDao;
import edu.neu.ccs.util.base.page.BasePage;
import edu.neu.ccs.util.base.page.Pager;
import edu.neu.ccs.util.base.pager.PageInfo;
import edu.neu.ccs.util.base.vo.BaseVO;
import edu.neu.ccs.util.common.BeanMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class BaseService<T, K> {

    @Autowired
    private BeanMapper beanMapper;

    public BaseService() {
    }

    public abstract BaseDao<T> getDao();

    public int insert(T t) throws Exception {
        return this.getDao().insert(t);
    }

    public int insertSelective(T t) throws Exception {
        return this.getDao().insertSelective(t);
    }

    public int updateByPrimaryKey(T t) throws Exception {
        return this.getDao().updateByPrimaryKey(t);
    }

    public int updateByPrimaryKeySelective(T t) throws Exception {
        return this.getDao().updateByPrimaryKeySelective(t);
    }

    public T selectByPrimaryKey(K value) throws Exception {
        return this.getDao().selectByPrimaryKey(value);
    }

    public void deleteByPrimaryKey(K value) throws Exception {
        this.getDao().deleteByPrimaryKey(value);
    }

    public List<T> queryByList(BasePage page) throws Exception {
        return this.getDao().queryByList(page);
    }

    public int queryByCount(BasePage page) throws Exception {
        return this.getDao().queryByCount(page);
    }

    public List<T> queryByPage(BasePage page) throws Exception {
        Integer rowCount = Integer.valueOf(this.queryByCount(page));
        page.getPager().setRowCount(rowCount.intValue());
        return this.getDao().queryByPage(page);
    }

    public T queryBySingle(BasePage page) throws Exception {
        page.setPageSize(Integer.valueOf(1));
        List results = this.getDao().queryByList(page);
        return null != results && results.size() != 0? (T) results.get(0):null;
    }

    /******************************************************************************************************/
    public List<T> queryByListAuth(BaseVO baseVO, BasePage page) throws Exception {
        page = beanMapper.map(baseVO, page.getClass());
        return this.getDao().queryByListAuth(page);
    }

    public int queryByCountAuth(BaseVO baseVO, BasePage page) throws Exception {
        page = beanMapper.map(baseVO, page.getClass());
        return this.getDao().queryByCountAuth(page);
    }

    public PageInfo<T> queryByPageAuth(BaseVO baseVO, BasePage page) throws Exception {
        page = beanMapper.map(baseVO, page.getClass());
        Integer rowCount = Integer.valueOf(this.queryByCountAuth(baseVO, page));
        Pager pager = new Pager();
        pager.setRowCount(rowCount.intValue());
        PageInfo<T> pageInfo = new PageInfo();
        pageInfo.setList(this.getDao().queryByPageAuth(page));
        pageInfo.setCount(Long.valueOf((long)pager.getRowCount()));
        pageInfo.setPageSize(Integer.valueOf(pager.getPageSize()));
        pageInfo.setPageCount(Long.valueOf((long)pager.getPageCount()));
        pageInfo.setPageNo(Integer.valueOf(pager.getPageId()));
        return pageInfo;
    }

    public int updateByPrimaryKeyAuth(BaseVO baseVO, BasePage page) throws Exception {
        page = beanMapper.map(baseVO, page.getClass());
        return this.getDao().updateByPrimaryKeyAuth(page);
    }

    public int updateByPrimaryKeySelectiveAuth(BaseVO baseVO, BasePage page) throws Exception {
        page = beanMapper.map(baseVO, page.getClass());
        return this.getDao().updateByPrimaryKeySelectiveAuth(page);
    }


    public void deleteByPrimaryKeyAuth(BaseVO baseVO, BasePage page) throws Exception {
        page = beanMapper.map(baseVO, page.getClass());
        this.getDao().deleteByPrimaryKeyAuth(page);
    }

    public T selectByPrimaryKeyAuth(BaseVO baseVO, BasePage page) throws Exception {
        page = beanMapper.map(baseVO, page.getClass());
        return this.getDao().selectByPrimaryKeyAuth(page);
    }

}
