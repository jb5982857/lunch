package com.lunch.fastdfs;

import com.lunch.support.tool.LogNewUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;

@RestController
@RequestMapping("/fastdfs")
public class FastDFSController {
    @Autowired
    private FastDFSClientWrapper dfsClient;

    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(@RequestParam("file") MultipartFile file) throws Exception {
        LogNewUtils.info("fastdfs upload:" + file.getOriginalFilename());

        return dfsClient.uploadFile(file);
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE, RequestMethod.POST})
    public void delete(@RequestParam("url") String url) {
        LogNewUtils.info("fastdfs delete :" + url);
        dfsClient.deleteFile(url);
    }

    @GetMapping(value = "/hello")
    public String hello() {
        return "Hello FastDFS";
    }
}
