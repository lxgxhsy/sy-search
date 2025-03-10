package com.sy.sysobackend.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sy.sysobackend.common.BaseResponse;
import com.sy.sysobackend.common.ErrorCode;
import com.sy.sysobackend.common.ResultUtils;
import com.sy.sysobackend.exception.BusinessException;
import com.sy.sysobackend.model.dto.post.PostQueryRequest;
import com.sy.sysobackend.model.dto.search.SearchQueryRequest;
import com.sy.sysobackend.model.dto.user.UserQueryRequest;
import com.sy.sysobackend.model.entity.Picture;
import com.sy.sysobackend.model.enums.SearchTypeBizEnum;
import com.sy.sysobackend.model.vo.PostVO;
import com.sy.sysobackend.model.vo.SearchVO;
import com.sy.sysobackend.model.vo.UserVO;
import com.sy.sysobackend.service.PictureService;
import com.sy.sysobackend.service.PostService;
import com.sy.sysobackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

/**
 * 图片接口
 *
 * @author 诺诺

 */
@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {

    @Resource
    private PictureService pictureService;

    @Resource
    private PostService postService;

    @Resource
    private UserService userService;



    /**
     * 分页获取列表（封装类）
     *
     * @param searchQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchQueryRequest searchQueryRequest, HttpServletRequest request) {
        String type = searchQueryRequest.getType();
        SearchTypeBizEnum searchTypeEnum = SearchTypeBizEnum.getEnumByValue(type);
        if (StrUtil.isBlank(type)) {
          throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String searchText = searchQueryRequest.getSearchText();
        if(searchTypeEnum == null){
            CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(() -> {

                UserQueryRequest userQueryRequest = new UserQueryRequest();
                userQueryRequest.setUserName(searchText);
                Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
                return userVOPage;
            });

            CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(() -> {

                Page<Picture> picturePage = pictureService.searchPicture(searchText, 1, 10);
                return picturePage;
            });
            CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(() -> {

                PostQueryRequest postQueryRequest = new PostQueryRequest();
                postQueryRequest.setSearchText(searchText);
                Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);
                return postVOPage;
            });

            CompletableFuture.allOf(userTask, pictureTask, postTask).join();

            try {
                Page<UserVO> userVOPage = userTask.get();
                Page<PostVO> postVOPage = postTask.get();
                Page<Picture> picturePage = pictureTask.get();
                SearchVO searchVO = new SearchVO();
                searchVO.setUserList(userVOPage.getRecords());
                searchVO.setPostList(postVOPage.getRecords());
                searchVO.setPictureList(picturePage.getRecords());
                return ResultUtils.success(searchVO);
            } catch (Exception e) {
                log.error("searchAll error : {}", e);
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"操作失败");
            }
        }else {
               SearchVO searchVO = new SearchVO();
               switch (searchTypeEnum){
                   case POST:
                       PostQueryRequest postQueryRequest = new PostQueryRequest();
                       postQueryRequest.setSearchText(searchText);
                       Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);
                       searchVO.setPostList(postVOPage.getRecords());
                       break;
                   case USER:
                       UserQueryRequest userQueryRequest = new UserQueryRequest();
                       userQueryRequest.setUserName(searchText);
                       Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
                       searchVO.setUserList(userVOPage.getRecords());
                       break;
                   case PICTURE:
                       Page<Picture> picturePage = pictureService.searchPicture(searchText, 1, 10);
                       searchVO.setPictureList(picturePage.getRecords());
                       break;
                   default:
               }
               return ResultUtils.success(searchVO);
        }
    }

}
