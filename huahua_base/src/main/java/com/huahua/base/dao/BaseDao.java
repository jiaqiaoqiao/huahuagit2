package com.huahua.base.dao;


import com.huahua.base.pojo.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//标签数据访问接口

//JpaRepository提交了基本的增删查改
//JpaSpecificationExecutor 用于做复杂的查询（条件查询）
public interface BaseDao extends JpaRepository<Label,String>, JpaSpecificationExecutor<Label> {

    //查询推荐标签列表
    List<Label> findAllByRecommendOrderByFansDesc(String recommend);
    //有效标签列表
    List<Label> findAllByStateOrderByFansDesc(String state);

    //@Query是查询语句 nativeQuery是执行sql语句 ？是单个传参 如果是多个参数 则？1开始 一次累加
    @Query(nativeQuery = true,value = "select * from tb_label where id = ?")
    Label queryById(String id);
}
