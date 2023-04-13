package cn.bhshare.meitu.controller;

import cn.bhshare.meitu.service.IIndexService;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

// 线上环境，静态资源static要把document文件夹去掉
@RequestMapping("")
// 本地环境
//@RequestMapping("/document")
@Controller
public class IndexController {

    @Resource
    private IIndexService indexService;

    @RequestMapping(value = "/images", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImage2(@Param("path") String path) throws IOException {
        File file = new File("D:/MEITU/" + path);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;
    }

    @RequestMapping("")
    public String index() {
        return "redirect:/index";
    }

    @RequestMapping("/")
    public String ind2ex() {
        return "redirect:/index";
    }

    @RequestMapping("/index")
    public String index(HttpServletRequest request, @PathParam("source") String source, @PathParam("search") String search, @PathParam("page") Integer page,
                        @PathParam("size") Integer size) {
        indexService.index(request, source, search, page, size);
        return "/index";
    }

    @RequestMapping("/models")
    public String models(HttpServletRequest request, @PathParam("page") Integer page, @PathParam("size") Integer size) {
        indexService.models(request, page, size);
        return "/models";
    }

    @RequestMapping("/detail/{id}")
    public String detail(HttpServletRequest request, @PathVariable("id") Integer id) {
        indexService.getPhoto(request, id);
        return "/detail";
    }
//
//    @RequestMapping("/model/{id}")
//    public String model(HttpServletRequest request, @PathVariable("id") Integer id) {
//        indexService.getModelById(request, id);
//        return "/model";
//    }

    @RequestMapping("/star")
    public String star() {
        return "/star";
    }

    @RequestMapping("/error/404")
    public String error404() {
        return "/404";
    }

    @RequestMapping("/error/403")
    public String error403() {
        return "/403";
    }

    @RequestMapping("/error/400")
    public String error400() {
        return "/500";
    }

    @RequestMapping("/error/500")
    public String error500() {
        return "/500";
    }

    @RequestMapping("/error/501")
    public String error501() {
        return "/501";
    }

}
