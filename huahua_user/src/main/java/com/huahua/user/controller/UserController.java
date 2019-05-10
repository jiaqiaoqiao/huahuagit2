package com.huahua.user.controller;

import java.util.HashMap;
import java.util.Map;

import huahua.common.PageResult;
import huahua.common.Result;
import huahua.common.StatusCode;

import huahua.common.utils.JwtUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.huahua.user.pojo.User;
import com.huahua.user.service.UserService;

import javax.servlet.http.HttpServletRequest;


/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private HttpServletRequest httpServletRequest;
	@Autowired
	HttpServletRequest request;
	@Autowired
	private JwtUtil jwtUtil;

	public static final String USER_LOGIN = "USER_LOGIN";
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true, StatusCode.OK,"查询成功",userService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",userService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param user
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody User user  ){
		userService.add(user);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param user
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody User user, @PathVariable String id ){
		user.setId(id);
		userService.update(user);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		userService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	//发送短信验证码
	@RequestMapping(value = "/sendsms/{mobile}",method =RequestMethod.POST)
	public Result sendsms(@PathVariable("mobile")String mobile){
		userService.sendsms(mobile);
		return new Result(true,StatusCode.OK,"发送成功");
	}
    //注册
	@PostMapping(value = "/user/register/{code}")
	public Result registerUser(@PathVariable("code")String code,@RequestBody User user){
		userService.registerUser(code,user);
		return new Result(true,StatusCode.OK,"注册成功");
	}

	//登录
	@RequestMapping(method = RequestMethod.POST,value = "/login")
	public Result login(@RequestBody User user){
		if (null==user || StringUtils.isEmpty(user.getMobile())){
			return new Result(false,StatusCode.ERROR,"参数有误");
		}
		if (null==user || StringUtils.isEmpty(user.getPassword())){
			return new Result(false,StatusCode.ERROR,"参数有误");
		}
		User respUser = userService.findByMobile(user);
		if (null!=respUser){
			//		//用户已登录
//		httpServletRequest.getSession().setAttribute(IS_LOGIN,true);
			String token = jwtUtil.createJWT(user.getId(), user.getNickname(), "admin");
			//JMT前端和后端访问的唯一标识符-->都要校验token否则都会让操作的用户返回登录
			//把token数据返回给前端(身份唯一标识)
			HashMap<String, Object> map = new HashMap<>();
			map.put("token",token);
			map.put("telphone",user.getMobile());
			return new Result(true,StatusCode.OK,"登录成功",map);
		}else {
			return new Result(false,StatusCode.LOGINERROR,"登陆失败");
		}

	}

	//查询登陆用户信息
	@RequestMapping(method = RequestMethod.GET,value = "/user/info")
	public Result findUser(){
		User user = (User) httpServletRequest.getSession().getAttribute(USER_LOGIN);
		return new Result(true,StatusCode.OK,"查询成功",userService.findUser(user.getId()));
	}
	//修改当前登陆用户信息
	@RequestMapping(method = RequestMethod.PUT,value = "/user/saveinfo")
	public Result updateUser(@RequestBody User user){
		userService.updateUser(user);
		return new Result(true,StatusCode.OK,"修改成功");
	}

}
