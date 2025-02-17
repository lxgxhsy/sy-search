package com.sy.sysobackend.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sy.sysobackend.common.ErrorCode;
import com.sy.sysobackend.exception.BusinessException;
import com.sy.sysobackend.model.entity.Picture;
import com.sy.sysobackend.service.PictureService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 帖子服务实现
 *
 * @author 诺诺

 */
@Service
public  class PictureServiceImpl implements PictureService {


    @Override
    public Page<Picture> searchPicture(String searchText, long pageNum, long pageSize)  {
        // 计算当前页码
        long current = (pageNum - 1) * pageSize;
        // 构造请求地址
        String url = String.format("https://cn.bing.com/images/search?q=%s&first=%s", searchText, current);
        Document doc = null;
        try {
            // 发送请求获取页面内容
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            // 发生异常时抛出自定义异常
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据获取异常");
        }
        // 获取图片元素
        Elements elements = doc.select(".iuscp.isv");
        List<Picture> pictures = new ArrayList<>();
        for (Element element : elements) {
            // 取图片地址（murl）
            String m = element.select(".iusc").get(0).attr("m");
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
            String prefixToExclude = "https://gd-hbimg.huaban.com";
            if(murl.contains(prefixToExclude)){
                continue;
            }
//            System.out.println(murl);
            // 取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");
//            System.out.println(title);
            Picture picture = new Picture();
            picture.setTitle(title);
            picture.setUrl(murl);
            pictures.add(picture);
            // 如果图片数量达到每页数量，则跳出循环
            if (pictures.size() >= pageSize) {
                break;
            }
        }
        // 构造分页对象
        Page<Picture> picturePage = new Page<>(pageNum, pageSize);
        picturePage.setRecords(pictures);
        return picturePage;
    }


}




