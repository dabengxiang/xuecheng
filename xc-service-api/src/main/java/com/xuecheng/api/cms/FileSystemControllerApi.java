package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * Date:2019/2/1
 * Author:gyc
 * Desc:
 */
public interface FileSystemControllerApi {

    public UploadFileResult upload(MultipartFile file,
                                   String filetag,
                                   String businesskey,
                                   String metadata);
}
