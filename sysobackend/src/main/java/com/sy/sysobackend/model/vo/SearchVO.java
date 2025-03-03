package com.sy.sysobackend.model.vo;

import com.sy.sysobackend.model.entity.Picture;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: shiyong
 * @CreateTime: 2025-02-21
 * @Description: 聚合返回所有
 * @Version: 1.0
 */

@Data
public class SearchVO implements Serializable {
	private List<UserVO> userList;

	private List<PostVO> postList;

	private List<Picture> pictureList;




	private static final long serialVersionUID = 1L;
}
