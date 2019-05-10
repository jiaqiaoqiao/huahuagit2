package com.huahua.article.controller;
import java.util.List;
import java.util.Map;

import huahua.common.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.huahua.article.pojo.Article;
import com.huahua.article.service.ArticleService;

import huahua.common.PageResult;
import huahua.common.Result;
import huahua.common.StatusCode;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private JwtUtil jwtUtil;
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",articleService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",articleService.findById(id));
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
		Page<Article> pageList = articleService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Article>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",articleService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param article
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Article article  ){
		Claims claims = (Claims) request.getAttribute("user_claims");
		if (null==claims){
			throw new RuntimeException("权限不足");
		}
		article.setUserid(claims.getId());
		articleService.add(article);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param article
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Article article, @PathVariable String id ){
		article.setId(id);
		articleService.update(article);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		Claims claims = (Claims) request.getAttribute("admin_claims");
		//只有admin的权限
		if (null==claims){
			return new Result(false,StatusCode.AUTOROLES,"权限不足");
		}
		articleService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
    //审核通过
	@RequestMapping(value = "/examine/{articleId}",method = RequestMethod.PUT)
	public Result updateArticleStateByArticleId(@PathVariable String articleId){
		return new Result(true,StatusCode.OK,"审批通过");
	}

    /**
     * 点赞
     */
    @RequestMapping(value = "/thumbup/{articleId}",method = RequestMethod.PUT)
    public Result updateArticleThumbup(@PathVariable String articleId){
        articleService.updateArticleStateByArticleId(articleId);
        return new Result(true,StatusCode.OK,"点赞成功");
    }

	/**
	 * 通过频道id 查询所属文章
	 */
	@RequestMapping(value = "/channel/{channelId}/{page}/{size}",method = RequestMethod.POST)
	public Result findAllByChannelidOrderByCreatetimeDesc(@PathVariable String channelId,@PathVariable Integer page,@PathVariable Integer size){
		Page<Article> articlePage = articleService.findAllByChannelidOrderByCreatetimeDesc(channelId, page, size);
		return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(articlePage.getTotalElements(),articlePage.getContent()));
	}

	/**
	 * 专栏id 查询所属文章
	 */
	@RequestMapping(value = "/column/{columnId}/{page}/{size}",method = RequestMethod.POST)
	public Result findAllByColumnidOrderByCreatetime(@PathVariable String columnId,@PathVariable Integer page,@PathVariable Integer size){
		Page<Article> articlePage = articleService.findAllByColumnidOrderByCreatetime(columnId, page, size);
		return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(articlePage.getTotalElements(),articlePage.getContent()));
	}

	/**
	 * 头条列表
	 * @return
	 */
	@RequestMapping(value = "/top",method = RequestMethod.GET)
	public Result findAllByIstop(){

		return new Result(true,StatusCode.OK,"查询成功",articleService.findAllByIstop());
	}
}
