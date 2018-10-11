package edu.neu.ccs.util.base.dao;


import edu.neu.ccs.util.base.page.BasePage;

import java.util.List;

public interface BaseDao<T> {
    int insert(T var1);

    int insertSelective(T var1);

    int updateByPrimaryKey(T var1);

    int updateByPrimaryKeySelective(T var1);

    T selectByPrimaryKey(Object var1);

    int deleteByPrimaryKey(Object var1);

    List<T> queryByList(BasePage var1);

    int queryByCount(BasePage var1);

    List<T> queryByPage(BasePage var1);

    T selectByPrimaryKeyAuth(Object var1);

    List<T> queryByListAuth(BasePage var1);

    int queryByCountAuth(BasePage var1);

    List<T> queryByPageAuth(BasePage var1);

    int deleteByPrimaryKeyAuth(BasePage var1);

    int updateByPrimaryKeySelectiveAuth(BasePage var1);

    int updateByPrimaryKeyAuth(BasePage var1);


}
