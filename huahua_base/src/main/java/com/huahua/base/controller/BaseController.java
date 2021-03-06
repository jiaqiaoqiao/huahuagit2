package com.huahua.base.controller;

import com.huahua.base.pojo.Label;
import com.huahua.base.service.BaseService;

import huahua.common.PageResult;
import huahua.common.Result;
import huahua.common.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/label")
@RestController
public class BaseController {

    @Autowired
    private BaseService baseService;

    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Label label){
        baseService.add(label);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){

        return new Result(true,StatusCode.OK,"查询成功",baseService.findAll());
    }

    @RequestMapping(method = RequestMethod.GET,value = "/toplist")
    public Result toplist(){

        return new Result(true,StatusCode.OK,"查询成功",baseService.toplist("1"));
    }

    @RequestMapping(method = RequestMethod.GET,value = "/list")
    public Result queryByStateAllList(){

        return new Result(true,StatusCode.OK,"查询成功",baseService.queryByStateAllList("1"));
    }

    //获取地址栏上的值
    @RequestMapping(method = RequestMethod.GET,value = "/{labelId}")
    public Result queryById(@PathVariable("labelId") String id){
        return new Result(true,StatusCode.OK,"查询成功",baseService.queryById(id));
    }

    //修改
    @RequestMapping(method = RequestMethod.PUT,value = "/{labelId}")
    public Result update(@RequestBody Label label,@PathVariable String labelId){
        //label =null;
        label.setId(labelId);
        //JPA中 add ==add或update 当id存在时--修改，当id不存在--添加；（优先去数据库看一下）
        baseService.add(label);
        return new Result(true,StatusCode.OK,"更新成功");
    }

    //删除方法
    @RequestMapping(method = RequestMethod.DELETE,value = "/{labelId}")
    public Result delete(@PathVariable("labelId") String id){
        baseService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    @RequestMapping(value = "/search/{page}/{size}",method=RequestMethod.POST)
    public Result search(@RequestBody Label label,@PathVariable("page") Integer page,@PathVariable("size")Integer size){
        Page<Label> search = baseService.findSearch(label, page, size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(search.getTotalElements(),search.getContent()));
    }



}
