package com.sy.sysobackend.model.dto.picture;

import com.sy.sysobackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author: shiyong
 * @CreateTime: 2025-02-17
 * @Description: 查询请求
 * @Version: 1.0
 */


@EqualsAndHashCode(callSuper = true)
@Data
public class PictureQueryRequest extends PageRequest implements Serializable {

	/**
	 * 搜索词
	 */
	private String searchText;

	private static final long serialVersionUID = 1L;
}