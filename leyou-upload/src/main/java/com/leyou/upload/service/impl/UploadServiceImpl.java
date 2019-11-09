package com.leyou.upload.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.upload.service.IUploadService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传业务层实现类
 *
 * @author MuYang
 */
@Service
public class UploadServiceImpl implements IUploadService {

    //允许上传的图片类型集合
    private static final List<String> CONTENT_TYPES = Arrays.asList("image/gif", "image/jpeg", "image/jpeg");

    private static final Logger LOGGER = LoggerFactory.getLogger(IUploadService.class);

    @Autowired
    private FastFileStorageClient storageClient;

    //获取配置文件中的文件上传路径
    @Value("${uploadurl.url}")
    private String uploadUrl;

    /**
     * 上传图片
     *
     * @param file 上传的图片
     * @return
     */
    @Override
    public String uploadImage(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        //检验文件类型
        String contentType = file.getContentType();
        if (!CONTENT_TYPES.contains(contentType)) {
            // 文件类型不合法，直接返回null
            LOGGER.info("文件类型不合法：{}", originalFilename);
            return null;
        }

        try {
            //校验文件内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                LOGGER.info("文件内容不合法：{}", originalFilename);
                return null;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
            //获取文件后缀
            String sufix = StringUtils.substringAfterLast(originalFilename, ".");
            //获取uuid
            String uuid = String.valueOf(UUID.randomUUID());
            //上传后的文件名
            String fileName = uuid + sufix;
            //校验文件路径是否存在，不存在则创建
            File parFile = new File(uploadUrl);
            if (!parFile.exists()) {
                parFile.mkdir();
            }
            //保存文件到服务器
            //file.transferTo(new File(parFile + "/" + fileName));
            //获取文件后缀名
            StorePath storePath = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), sufix, null);
            //返回url，回显图片
            //return parFile + "/" + fileName;
            return parFile + "/" + storePath.getFullPath();

        } catch (IOException e) {
            LOGGER.info("服务器内部错误：{}", originalFilename);
            e.printStackTrace();
        }
        return null;
    }

}
