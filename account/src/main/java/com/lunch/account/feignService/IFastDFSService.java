package com.lunch.account.feignService;

import com.lunch.account.config.MultipartSupportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@FeignClient(value = "service-fastdfs", configuration = MultipartSupportConfig.class)
public interface IFastDFSService {
    //文件传输要用RequestPart
    //并且需要consumes = MULTIPART_FORM_DATA_VALUE不然不知道你传的是文件
    @RequestMapping(value = "/fastdfs/upload", method = RequestMethod.POST, consumes = MULTIPART_FORM_DATA_VALUE)
    String upload(@RequestPart("file") MultipartFile file);

    @RequestMapping(value = "/fastdfs/hello", method = RequestMethod.GET)
    String hello();
}
