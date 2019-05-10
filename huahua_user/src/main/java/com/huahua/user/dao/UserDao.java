package com.huahua.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.huahua.user.pojo.User;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface UserDao extends JpaRepository<User,String>,JpaSpecificationExecutor<User>{
    //通过手机号查询用户信息
    User findByMobile(String mobile);
    //登录
    User findByMobileAndPassword(String mobile,String password);

    @Query(nativeQuery = true,value = "select * from tb_user where id = ?1")
    User findUser(String id);

     User findUserByMobile(String mobile);
}
