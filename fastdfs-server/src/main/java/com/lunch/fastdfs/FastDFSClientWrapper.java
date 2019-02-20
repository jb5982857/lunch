package com.lunch.fastdfs;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.lunch.support.tool.LogNewUtils;
import com.lunch.support.tool.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class FastDFSClientWrapper {
    private static final String GROUP_NAME = "lunch";

    @Autowired
    private FastFileStorageClient storageClient;

    public String uploadFile(MultipartFile file) throws IOException {
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()), null);
        LogNewUtils.info("Fast Client full path " + storePath.getFullPath());
        return storePath.getFullPath();
    }

    public void deleteFile(String url) {
        if (StringUtils.isEmpty(url)) {
            LogNewUtils.warn("delete file ,but url is null");
            return;
        }
        try {
            storageClient.deleteFile(url);
        } catch (FdfsUnsupportStorePathException e) {
            LogNewUtils.printException("fdfs delete file error!", e);
        }
    }
}
