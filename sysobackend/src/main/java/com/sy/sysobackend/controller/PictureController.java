package com.sy.sysobackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sy.sysobackend.common.BaseResponse;
import com.sy.sysobackend.common.ErrorCode;
import com.sy.sysobackend.common.ResultUtils;
import com.sy.sysobackend.exception.ThrowUtils;
import com.sy.sysobackend.model.dto.picture.PictureQueryRequest;
import com.sy.sysobackend.model.entity.Picture;
import com.sy.sysobackend.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 图片接口
 *
 * @author 诺诺

 */
@RestController
@RequestMapping("/picture")
@Slf4j
public class PictureController {

    @Resource
    private PictureService pictureService;



    /**
     * 分页获取列表（封装类）
     *
     * @param pictureQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<Picture>> listPictureByPage(@RequestBody PictureQueryRequest pictureQueryRequest,
                                                         HttpServletRequest request) {
        long current = pictureQueryRequest.getCurrent();
        long size = pictureQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        String searchText = pictureQueryRequest.getSearchText();

        Page<Picture> postPage = pictureService.searchPicture(searchText, current, size);
        return ResultUtils.success(postPage);
    }

}
