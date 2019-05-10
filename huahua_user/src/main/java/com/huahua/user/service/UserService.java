package com.huahua.user.service;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import cn.hutool.core.util.RandomUtil;
import huahua.common.utils.IdWorker;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



import com.huahua.user.dao.UserDao;
import com.huahua.user.pojo.User;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private BCryptPasswordEncoder encoder;
	/**
	 * 查询全部列表
	 * @return
	 */
	public List<User> findAll() {
		return userDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<User> findSearch(Map whereMap, int page, int size) {
		Specification<User> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return userDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<User> findSearch(Map whereMap) {
		Specification<User> specification = createSpecification(whereMap);
		return userDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public User findById(String id) {
		return userDao.findById(id).get();
	}

	/**
	 * 增加
	 * @param user
	 */
	public void add(User user) {
		user.setId( idWorker.nextId()+"" );
		userDao.save(user);
	}

	/**
	 * 修改
	 * @param user
	 */
	public void update(User user) {
		userDao.save(user);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		userDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<User> createSpecification(Map searchMap) {

		return new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 手机号码
                if (searchMap.get("mobile")!=null && !"".equals(searchMap.get("mobile"))) {
                	predicateList.add(cb.like(root.get("mobile").as(String.class), "%"+(String)searchMap.get("mobile")+"%"));
                }
                // 密码
                if (searchMap.get("password")!=null && !"".equals(searchMap.get("password"))) {
                	predicateList.add(cb.like(root.get("password").as(String.class), "%"+(String)searchMap.get("password")+"%"));
                }
                // 昵称
                if (searchMap.get("nickname")!=null && !"".equals(searchMap.get("nickname"))) {
                	predicateList.add(cb.like(root.get("nickname").as(String.class), "%"+(String)searchMap.get("nickname")+"%"));
                }
                // 性别
                if (searchMap.get("sex")!=null && !"".equals(searchMap.get("sex"))) {
                	predicateList.add(cb.like(root.get("sex").as(String.class), "%"+(String)searchMap.get("sex")+"%"));
                }
                // 头像
                if (searchMap.get("avatar")!=null && !"".equals(searchMap.get("avatar"))) {
                	predicateList.add(cb.like(root.get("avatar").as(String.class), "%"+(String)searchMap.get("avatar")+"%"));
                }
                // E-Mail
                if (searchMap.get("email")!=null && !"".equals(searchMap.get("email"))) {
                	predicateList.add(cb.like(root.get("email").as(String.class), "%"+(String)searchMap.get("email")+"%"));
                }
                // 兴趣
                if (searchMap.get("interest")!=null && !"".equals(searchMap.get("interest"))) {
                	predicateList.add(cb.like(root.get("interest").as(String.class), "%"+(String)searchMap.get("interest")+"%"));
                }
                // 个性
                if (searchMap.get("personality")!=null && !"".equals(searchMap.get("personality"))) {
                	predicateList.add(cb.like(root.get("personality").as(String.class), "%"+(String)searchMap.get("personality")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}
    //用户注册
	public void registerUser(String code, User user) {
		if (null == user){
			throw new RuntimeException("用户信息不能为空");
		}
		//用户手机号
		String mobile = user.getMobile();
		//从缓存中获取手机号验证码
		String redisCode = stringRedisTemplate.opsForValue().get("code_" + mobile);
		//校验用户的手机验证码
		if (!StringUtils.equals(code,redisCode)){
			throw new RuntimeException("验证码错误，请重新发送短信验证码");
		}
		//密码加密
		user.setId(String.valueOf(idWorker.nextId()));
		user.setFollowcount(0);//关注数
		user.setFanscount(0);//粉丝数
		user.setOnline(0L);//在线时长
		user.setRegdate(new Date());//注册日期
		user.setUpdatedate(new Date());//更新日期
		user.setLastdate(new Date());//最后登录日期
		userDao.save(user);
	}
	//密码加密
	public String encodePwd(String password){
		return encoder.encode(password);
	}

	//发送短信验证码
	public void sendsms(String mobile) {
        //校验手机号是否存在
		User byMobile = userDao.findByMobile(mobile);
		if (null!=byMobile){//如果用户信息不为空，则表示手机已经被注册过了
			throw new RuntimeException("手机号已存在，请登录");
		}
		//生成验证码
		String mobileCode = RandomUtil.randomNumbers(6);
		//往redis存放验证码，同时设置验证码的超时时间 不能超过5分钟
		stringRedisTemplate.opsForValue().set("smsCode_"+mobile,mobileCode,5, TimeUnit.MINUTES);
		//通知rabmq发送短信 同时将手机号和验证码发送过去--> 消息队列
		Map<String,String> map = new HashMap<>();
        map.put("mobile",mobile);
        map.put("code",mobileCode);
        //创建消息队列
        rabbitTemplate.convertAndSend("sms",map);

	}
	public static void main(String[] args) {
		System.out.println(RandomUtil.randomNumbers(6));
	}
	public User login(String mobile, String password) {
		return userDao.findByMobileAndPassword(mobile,password);
	}
//查询用户登录信息
	public User findUser(String id) {
		return userDao.findUser(id);
	}
//修改当前用户登录信息
	public void updateUser(User user) {
		userDao.save(user);
	}

    //用户登录
	public User findByMobile(User user) {
		User byMobile = userDao.findByMobile(user.getMobile());
		if (null !=byMobile && encoder.matches(user.getPassword(),byMobile.getPassword())){
			return byMobile;
		}

		return null;
	}
}
