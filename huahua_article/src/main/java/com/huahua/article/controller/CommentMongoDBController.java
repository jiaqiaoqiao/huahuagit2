package com.huahua.article.controller;


import com.huahua.article.pojo.CommentMongoDB;
import com.huahua.article.service.CommentMongoDBService;
import huahua.common.Result;
import huahua.common.StatusCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@CrossOrigin
public class CommentMongoDBController {

    @Autowired
    private CommentMongoDBService commentMongoDBService;

    /**
     * 添加
     * @param commentMongoDB
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody CommentMongoDB commentMongoDB){
       if(StringUtils.isEmpty(commentMongoDB.getArticleid())){

           return new Result(true, StatusCode.OK,"参数有误!");
       }
         commentMongoDBService.add(commentMongoDB);

        return new Result(true, StatusCode.OK,"添加成功!");
    }

    /**
     * 根据文章的id查询评论的列表
     * @param articleId
     * @return
     */
    @RequestMapping(value = "/article/{articleId}",method = RequestMethod.POST)
    public Result queryArticleId(@PathVariable String articleId){

        List<CommentMongoDB> list=commentMongoDBService.queryArticleId(articleId);
        return new Result(true, StatusCode.OK,"查询成功!",list);
    }

    /**
     * 删除评论
     * @param ids
     * @return
     */
    @RequestMapping(value = "/{ids}",method = RequestMethod.DELETE)
    public  Result delete(@PathVariable String ids){
        commentMongoDBService.delete(ids);
        return new Result(true, StatusCode.OK,"删除成功!");
    }
}