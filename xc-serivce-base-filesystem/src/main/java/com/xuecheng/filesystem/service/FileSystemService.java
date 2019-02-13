package com.xuecheng.filesystem.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.hibernate.dialect.Oracle10gDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.net.www.MeteredStream;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Map;
import java.util.jar.JarEntry;

/**
 * Date:2019/2/1
 * Author:gyc
 * Desc:
 */
@Service
@Transactional(rollbackOn = Exception.class)
@Slf4j
public class FileSystemService {


    @Value("${xuecheng.fastdfs.tracker_servers}")
    String tracker_servers;
    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
    int connect_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
    int network_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.charset}")
    String charset;


    @Autowired
    private FileSystemRepository fileSystemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemService.class);


    public UploadFileResult upload(MultipartFile file, String filetag, String businesskey, String metadata) {
        String fileId = fileUpload(file);

        if(fileId==null){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }

        String fileName = file.getOriginalFilename();

        FileSystem fileSystem = new FileSystem();
        fileSystem.setFiletag(filetag);
        fileSystem.setBusinesskey(businesskey);
        fileSystem.setFileId(fileId);
        fileSystem.setFilePath(fileId);
        fileSystem.setFileName(fileName);
        fileSystem.setFileType(file.getContentType());
        if(metadata!=null){
            Map map = JSONObject.parseObject(metadata, Map.class);
            fileSystem.setMetadata(map);
        }

        fileSystemRepository.insert(fileSystem);
        return new UploadFileResult(CommonCode.SUCCESS,fileSystem);


    }


    private String  fileUpload(MultipartFile file) {
        if(file == null){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }
        initFdfsConfig();
        TrackerClient trackerClient = new TrackerClient();
        try {
            TrackerServer trackerServer = trackerClient.getConnection();

            StorageServer storeStorageServer = trackerClient.getStoreStorage(trackerServer);

            StorageClient1 storageClient1 = new StorageClient1(trackerServer,storeStorageServer);

            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf(".")+1);

            String fileId = storageClient1.upload_file1(file.getBytes(), ext, null);

            return fileId;

        } catch (Exception e) {
            log.error(this.getClass().toString(),e);
            return null;

        }

    }



    private void initFdfsConfig(){
        try {
            ClientGlobal.initByTrackers(tracker_servers);
            ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
            ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
            ClientGlobal.setG_charset(charset);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }
    }

}
