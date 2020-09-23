package org.example.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

@Service
public class FileService {

    private FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public boolean uploadFileToServer(MultipartFile file) throws Exception {
        fileRepository.uploadFile(file);
        return true;
    }

    public byte[] downloadFileFromServer(String fileName) {
        return fileRepository.downloadFile(fileName);
    }

    public List<String> getAllFilesNameFromServer() {
        String rootPath = System.getProperty("catalina.home");
        return fileRepository.getAllFilesName(rootPath + File.separator + "external_uploads");
    }
}
