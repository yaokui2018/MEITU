package cn.bhshare.meitu.service.impl;

import cn.bhshare.meitu.dao.*;
import cn.bhshare.meitu.model.*;
import cn.bhshare.meitu.service.IIndexService;
import cn.bhshare.meitu.util.textrank.TextRankKeyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cn.bhshare.meitu.util.Xss.xssEncode;

@Service
public class IndexServiceImpl implements IIndexService {

    @Autowired
    PhotoMapper photoMapper;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    NewMapper newMapper;


    SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd");


    /**
     * 根据id获取模特信息
     */
    @Override
    public void getModelById(HttpServletRequest request, Integer id) {
        Model model = modelMapper.selectByPrimaryKey(id);
        String birthday = df3.format(model.getBirthday());
        request.setAttribute("model", model);
        request.setAttribute("birthday", birthday);
    }

    /**
     * 根据id获取photo详情
     *
     * @param request req
     * @param id      id
     */
    @Override
    public void getPhoto(HttpServletRequest request, Integer id) {
        Photo photo = photoMapper.selectByPrimaryKey(id);
        if (null == photo) {
            return;
        }
        List<String> tags = new ArrayList<>();
        String keywords = photo.getTags();
        if (null != keywords) {
            tags = Arrays.asList(keywords.substring(1, keywords.length() - 1).split("/"));
        }

        String date = df3.format(photo.getDate());
        String updateTime = df2.format(photo.getUpdateTime());

        getModelById(request, photo.getModel());

        Photo prePhoto = photoMapper.selectByPrimaryKey(photo.getId() - 1);
        Photo nextPhoto = photoMapper.selectByPrimaryKey(photo.getId() + 1);

        request.setAttribute("photo", photo);
        request.setAttribute("prePhoto", prePhoto);
        request.setAttribute("nextPhoto", nextPhoto);
        request.setAttribute("bgImg", "/images?path=" + photo.getFolder() + "/0.jpg");
        request.setAttribute("source", photo.getSource());
        request.setAttribute("tags", tags);
        request.setAttribute("date", date);
        request.setAttribute("updateTime", updateTime);
    }


    /**
     * 获取数据列表
     *
     * @param request req
     * @param source  秀人网....
     * @param search  搜索关键字
     * @param page    页码
     * @param size    页面显示数
     */
    @Override
    public void index(HttpServletRequest request, String source, String search, Integer page, Integer size) {
        if (search == null) {
            search = "";
        } else {
            search = xssEncode(search);
        }
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size < 1) {
            size = 40;
        }
        if (source == null) {
            source = "";
        } else {
            source = xssEncode(source);
        }
        String search_show = search;
        int searchFlag = 0;// 普通搜索

        String where = "(`title` like '%" + search + "%' or `description` like '%" + search + "%') ";

        if (search.startsWith("fix@")) { // 直接搜索
            // 普通搜索
            search_show = search.replace("fix@", "");
            where = "(`title` like '%" + search_show + "%' or `description` like '%" + search_show + "%' or `tags` like '%/" + search_show + "/%') ";
        } else if (search.startsWith("tag@")) {// 关键词标签
            searchFlag = 1;// 标签搜索
            search_show = search.replace("tag@", "");
            where = "(`tags` like '%/" + search_show + "/%') ";
        } else if (search.startsWith("model@")) {// 模特
            searchFlag = 3;// 模特搜索
            search_show = search.replace("model@", "");
            where = "(`model`=" + search_show + ") ";
            getModelById(request, Integer.valueOf(search_show)); // request添加模特参数
        } else if (search.length() > 20) { // 提取关键词
            searchFlag = 2;// 关键词搜索
            // 提取关键词，根据关键词查找
            List<String> keywords = new TextRankKeyword().getKeyword("", search);
            if (keywords.size() == 0) {
                keywords.add(search);
            }
            search_show = ""; // 原关键词和新提取关键词中间加 分隔符==
            where = "(";
            for (String keyword : keywords) {
                where += "and (`title` like '%" + keyword + "%' or `description` like '%" + keyword + "%' or `tags` like '%/" + keyword + "/%')";
                search_show += keyword + " + ";
            }
            where = where.replace("(and", "(");
            search_show += "***";
            search_show = search_show.replace(" + ***", "");
            where += ") ";
        }
        if (source.length() > 0) {
            where += "and (`source` like '%" + source + "%')";
        }

        where += " order by `date` desc ";

        List<Photo> photoList = newMapper.getPhotoList((page - 1) * size, size, where);
        long count = newMapper.getCount(where);
        int total = Math.toIntExact(count / size);
        if (count % size != 0) {
            ++total;
        }

        if (photoList.size() > 0) {
            request.setAttribute("bgImg", "/images?path=" + photoList.get(0).getFolder() + "/1.jpg");
        }

        String url = "source=" + source + "&search=" + search;
        String url2 = "page=" + 1 + "&search=" + search + "&size=" + size;
        String url3 = "page=1&source=" + source + "&search=" + search + "&size=" + size;
        String url4 = "page=1&source=" + source + "&size=" + size;
        request.setAttribute("photoList", photoList);
        request.setAttribute("count", count);
        request.setAttribute("page", page);
        request.setAttribute("size", size);
        request.setAttribute("source", source);
        request.setAttribute("search", search_show);
        request.setAttribute("search_original", search); // 仅关键词提取searchFlag=2时有用
        request.setAttribute("searchFlag", searchFlag);
        request.setAttribute("total", total);
        request.setAttribute("url", url);
        request.setAttribute("url2", url2);
        request.setAttribute("url3", url3);
        request.setAttribute("url4", url4);

    }

    /**
     * 获取model列表
     *
     * @param request req
     * @param page    页码
     * @param size    页面显示数
     */
    @Override
    public void models(HttpServletRequest request, Integer page, Integer size) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size < 1) {
            size = 40;
        }
        List<Model> modelList = newMapper.getModelList((page - 1) * size, size);
        List<Photo> photoList = new ArrayList<Photo>();
        for (Model model : modelList) {
            PhotoExample photoExample = new PhotoExample();
            photoExample.createCriteria().andModelEqualTo(model.getId());
            photoList.add(photoMapper.selectByExample(photoExample).get(0));
        }
        if (photoList.size() > 0) {
            request.setAttribute("bgImg", "/images?path=" + photoList.get(0).getFolder() + "/1.jpg");
        }
        long count = modelMapper.countByExample(new ModelExample());
        int total = Math.toIntExact(count / size);
        if (count % size != 0) {
            ++total;
        }
        request.setAttribute("modelList", modelList);
        request.setAttribute("photoList", photoList);
        request.setAttribute("count", count);
        request.setAttribute("page", page);
        request.setAttribute("size", size);
        request.setAttribute("total", total);
    }

}
