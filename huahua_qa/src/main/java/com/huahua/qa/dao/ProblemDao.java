package com.huahua.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.huahua.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{
    Problem findOneById(String id);

    /**
     * 最新问答
     * @param labelid
     * @return
     */
    @Query(nativeQuery = true,value = "select * from tb_problem,tb_pl where 1=1 and id =problemid and labelid=? order by createtime desc ")
    public Page<Problem> newlist(String labelid, Pageable pageable);

    /**
     * 热门问答
     * @return
     */
    @Query(nativeQuery = true,value = "select * from tb_problem,tb_pl where 1=1 and id =problemid and labelid=? order by visits desc ")
    public Page<Problem> hotlist(String labelid, Pageable pageable);

    /**
     * 等待问答
     */
    @Query(nativeQuery = true,value = "select * from tb_problem,tb_pl where 1=1 and id =problemid and reply=0 and labelid=? order by createtime desc ")
    public Page<Problem> waitlist(String labelid,Pageable pageable);

}
