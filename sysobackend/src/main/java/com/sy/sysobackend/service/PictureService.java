package com.sy.sysobackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sy.sysobackend.model.entity.Picture;

/**
 * 帖子服务
 *
 * @author 诺诺

 */
public interface PictureService {
    Page<Picture> searchPicture(String searchText, long pageNum, long pageSize);
}
