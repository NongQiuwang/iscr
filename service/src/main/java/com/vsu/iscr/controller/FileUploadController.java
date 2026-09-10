package com.vsu.iscr.controller;

import com.vsu.iscr.core.vo.ResultVo;
import com.vsu.iscr.utils.ResultVoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 文件上传控制器
 * 
 * @author iscr
 */
@Slf4j
@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FileUploadController {

    @Value("${vsu.profile:D:/iscr-images}")
    private String uploadPath;

    @Value("${vsu.prefix:/iscr-images}")
    private String imagePrefix;

    /**
     * 上传汽车图片
     */
    @PostMapping("/car-image")
    public ResultVo uploadCarImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResultVoUtil.error("上传文件不能为空");
        }

        try {
            // 获取原始文件名
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                return ResultVoUtil.error("文件名不能为空");
            }

            // 验证文件类型
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            if (!isImageFile(fileExtension)) {
                return ResultVoUtil.error("只能上传图片文件（jpg、jpeg、png、gif）");
            }

            // 创建上传目录（按日期分组）
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String dateDir = sdf.format(new Date());
            String uploadDir = uploadPath + File.separator + "cars" + File.separator + dateDir;
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 生成新文件名（UUID + 原扩展名）
            String newFileName = UUID.randomUUID().toString() + fileExtension;
            Path filePath = Paths.get(uploadDir, newFileName);

            // 保存文件
            file.transferTo(filePath.toFile());

            // 返回相对路径
            String relativePath = imagePrefix + "/cars/" + dateDir + "/" + newFileName;

            log.info("文件上传成功：{}", relativePath);
            return ResultVoUtil.success(relativePath);

        } catch (IOException e) {
            log.error("文件上传失败", e);
            return ResultVoUtil.error("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 验证是否为图片文件
     */
    private boolean isImageFile(String extension) {
        String[] allowedExtensions = { ".jpg", ".jpeg", ".png", ".gif", ".JPG", ".JPEG", ".PNG", ".GIF" };
        for (String ext : allowedExtensions) {
            if (ext.equals(extension)) {
                return true;
            }
        }
        return false;
    }
}
