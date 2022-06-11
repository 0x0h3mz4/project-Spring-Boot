package com.gsnotes.web.controllers;

import com.gsnotes.services.INoteDeliberationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/prof")
public class ImportNoteDelibController {
    @Autowired
    INoteDeliberationService fileStorageService;
    @PostMapping("/isUploaded")
    public String uploadDelib(Model model) {
        //fileStorageService.save(file);
        //System.out.println(Paths.get("upload/").resolve(file.getOriginalFilename()).toString());
        fileStorageService.processExcelFile();
        model.addAttribute("message","book1 is uploaded successfuly");
        model.addAttribute("status", "success");
        return "/prof/noteDelib";
    }
}
