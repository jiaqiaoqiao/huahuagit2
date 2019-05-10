package com.huahua.spit.controller;

import com.huahua.spit.entity.Spit;
import com.huahua.spit.service.SpitService;
import huahua.common.PageResult;
import huahua.common.Result;
import huahua.common.StatusCode;
import huahua.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;




@RestController
@RequestMapping("spit")
@CrossOrigin
public class SpitController {
    @Autowired
    private SpitService spitService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtUtil jwtUtil;

    //查询spit全部列表
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){

        return new Result(true, StatusCode.OK,"查询成功",spitService.findAll());
    }
   //通过id查吐槽
    @RequestMapping(method = RequestMethod.GET,value = "/{id}")
    public Result findById(String id){

        return new Result(true, StatusCode.OK,"查询成功",spitService.findById(id));
    }
   //添加吐槽
   @RequestMapping(method = RequestMethod.POST)
    public Result add( @RequestBody Spit spit){
       Claims claims = (Claims) request.getAttribute("user_claims");
       if (null==claims){
           throw new RuntimeException("权限不足");
       }
        spit.setUserid(claims.getId());
        spit.setPublishtime(new Date());
        spitService.add(spit);
        return new Result(true, StatusCode.OK,"增加成功");
    }
  //修改
    @RequestMapping(method = RequestMethod.PUT,value = "/{id}")
    public Result update(@RequestBody Spit spit,@PathVariable String id){
        spit.set_id(id);

        spitService.update(spit);
        return new Result(true, StatusCode.OK,"修改成功");
    }

    //删除
    @RequestMapping(method = RequestMethod.DELETE,value = "/{id}")
    public Result delete(@PathVariable String id){
        Claims claims = (Claims) request.getAttribute("admin_claims");
        //只有admin的权限
        if (null==claims){
            return new Result(false,StatusCode.AUTOROLES,"权限不足");
        }
        spitService.delete(id);
        return new Result(true, StatusCode.OK,"删除成功");
    }
    //根据上级id查询吐槽数据(分页)
    @RequestMapping(method = RequestMethod.GET,value = "comment/{parentid}/{page}/{size}")
    public Result findByPidList(@PathVariable("parentid") String parentid,@PathVariable("page") Integer page,@PathVariable("size") Integer size){
        Page<Spit> spitList = spitService.findByPidList(parentid,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(spitList.getTotalElements(),spitList.getContent()));
    }
    /**
     * 功能描述：吐槽点赞
     * @Author LiHuaMing
     * @Date 19:58 2019/4/17
     * @Param
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT,value = "/thumbup/{spitId}")
    public Result updateThumbup(@PathVariable("spitId")String spitId){
        String userid="xxx";
        Integer obj = (Integer) redisTemplate.opsForValue().get("thumbup_" + userid + "id_" + spitId);
        if(null == obj){
            spitService.updateThumbup(spitId);
            redisTemplate.opsForValue().set("thumbup_" + userid + "id_" + spitId,1);
            return new Result(true,StatusCode.OK,"点赞成功");
        }
        return new Result(true,StatusCode.OK,"你已经赞过了");
    }
    /**
     * spit分页
     * @return
     */
    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.POST)
    public Result search(@PathVariable("page") Integer page,@PathVariable("size") Integer size){
        Page<Spit> pagel=spitService.findBysearch(page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(pagel.getTotalElements(),pagel.getContent()));
    }
}