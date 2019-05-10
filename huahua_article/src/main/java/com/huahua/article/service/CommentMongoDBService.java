package com.huahua.article.service;

import com.huahua.article.dao.CommentMongoDBDao;
import com.huahua.article.pojo.CommentMongoDB;
import huahua.common.utils.IdWorker;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CommentMongoDBService {

   @Autowired
    private CommentMongoDBDao commentMongoDBDao;
   @Autowired
    IdWorker idWorker;

    /**
     * 添加评论
     */

    public void add(CommentMongoDB commentMongoDB){

        commentMongoDB.set_id(idWorker.nextId()+"");
        commentMongoDB.setPublishdate(new Date());
        commentMongoDBDao.save(commentMongoDB);

    }

    /**
     * 根据文章的id查询评论的列表
     * @param articleId
     * @return
     */
    public List<CommentMongoDB> queryArticleId(String articleId) {

        return commentMongoDBDao.findAllByArticleidOrderByPublishdateDesc(articleId);
    }

    /**
     * 删除评论
     * @param ids
     */
    public void delete(String ids) {
        if (StringUtils .isNotEmpty(ids)){
            String[] split=ids.split(",");
            for (String s:split){
                commentMongoDBDao.deleteById(ids);
            }
        }
    }
}