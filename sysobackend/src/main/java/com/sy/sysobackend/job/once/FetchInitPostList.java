package com.sy.sysobackend.job.once;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sy.sysobackend.model.entity.Post;
import com.sy.sysobackend.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: sy
 * @CreateTime: 2025-02-16
 * @Description: 初始化帖子列表
 * @Version: 1.0
 */

// 取消注释后，每次启动 springboot 项目时会执行一次 run 方法
//@Component
@Slf4j
public class FetchInitPostList implements CommandLineRunner {

	@Resource
	private PostService postService;


	@Override
	public void run(String... args) {
		// 获取数据
		// 获取数据
		String json = "{\"pageSize\":12,\"sortOrder\":\"descend\",\"sortField\":\"createTime\",\"tags\":[],\"current\":1,\"reviewStatus\":1,\"category\":\"文章\",\"hiddenContent\":true}";
		String url = "https://api.codefather.cn/api/course/list/page/vo";
		String result = HttpRequest.post(url)
				.body(json)
				.execute().body();
		// json转对象
		Map<String,Object> map = JSONUtil.toBean(result, Map.class);
		JSONObject data = (JSONObject) map.get("data");
		JSONArray records = (JSONArray) data.get("records");
		List<Post> postList = new ArrayList<>();
		for(Object record : records){
			JSONObject item  = (JSONObject) record;
			if(ObjectUtil.isEmpty(item)){
				break;
			}
			Post post = new Post();
			post.setTitle(item.getStr("title"));
			post.setContent(item.getStr("content"));
			JSONArray tags = (JSONArray) item.get("tags");
			List<String> tagList = tags.toList(String.class);
			post.setTags(JSONUtil.toJsonStr(tagList));
			post.setUserId(1L);
			postList.add(post);
		}
		// 数据入库
		boolean b = postService.saveBatch(postList);
		if (b) {
			log.info("获取初始化帖子列表成功，条数 = {}", postList.size());
		} else {
			log.error("获取初始化帖子列表失败");
		}
	}
}

