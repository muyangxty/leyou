package com.leyou.upload.service;


import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传业务层接口
 *
 * @author MuYang
 */
public interface IUploadService {

    /**
     * 上传图片
     *
     * @param file 上传的图片
     * @return 返回文件路径
     */
    String uploadImage(MultipartFile file);
}
