package com.gsnotes.services;

import org.springframework.web.multipart.MultipartFile;

public interface INoteDeliberationService {
    public void save(MultipartFile file);
    public void processExcelFile();
}
