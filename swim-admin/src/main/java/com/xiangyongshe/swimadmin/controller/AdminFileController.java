package com.xiangyongshe.swimadmin.controller;

import com.xiangyongshe.swimadmin.common.ApiResponse;
import com.xiangyongshe.swimadmin.entity.FileResource;
import com.xiangyongshe.swimadmin.entity.SysUser;
import com.xiangyongshe.swimadmin.service.FileResourceService;
import com.xiangyongshe.swimadmin.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/files")
public class AdminFileController {

    private final FileResourceService fileResourceService;
    private final SecurityUtil securityUtil;

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    @Value("${app.upload-url-prefix:/uploads/}")
    private String uploadUrlPrefix;

    public AdminFileController(FileResourceService fileResourceService, SecurityUtil securityUtil) {
        this.fileResourceService = fileResourceService;
        this.securityUtil = securityUtil;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload")
    public ApiResponse<FileResource> upload(@RequestParam("file") MultipartFile file,
                                            @RequestParam(required = false) String bizType,
                                            @RequestParam(required = false) Long bizId) throws IOException {
        if (file.isEmpty()) {
            return ApiResponse.error(400, "文件为空");
        }
        String originalName = StringUtils.cleanPath(file.getOriginalFilename() == null ? "file" : file.getOriginalFilename());
        String ext = "";
        int dot = originalName.lastIndexOf('.');
        if (dot >= 0) {
            ext = originalName.substring(dot);
        }
        String datePath = LocalDate.now().toString();
        String filename = UUID.randomUUID().toString().replace("-", "") + ext;
        Path dir = Paths.get(uploadDir, datePath);
        Files.createDirectories(dir);
        Path target = dir.resolve(filename);
        file.transferTo(target.toFile());

        SysUser user = securityUtil.currentUser();
        String url = uploadUrlPrefix + datePath + "/" + filename;

        FileResource resource = new FileResource();
        resource.setBizType(bizType);
        resource.setBizId(bizId);
        resource.setFileName(originalName);
        resource.setFilePath(target.toString());
        resource.setFileUrl(url);
        resource.setContentType(file.getContentType());
        resource.setFileSize(file.getSize());
        resource.setUploaderId(user == null ? null : user.getId());
        resource.setDeleted(0);
        fileResourceService.save(resource);
        return ApiResponse.success(resource);
    }
}
