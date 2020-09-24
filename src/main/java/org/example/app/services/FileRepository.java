package org.example.app.services;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.example.app.exeptions.FileDownloadException;
import org.example.app.exeptions.FileUploadException;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FileRepository {

    List<String> files;
    private final Logger logger = Logger.getLogger(FileRepository.class);

    public void uploadFile(MultipartFile file) throws Exception {
        String name = file.getOriginalFilename();
        if (name.isEmpty()) {
            throw new FileUploadException("no file selected");
        }
        byte[] bytes = file.getBytes();
        //create dir
        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "external_uploads");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //create file
        File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
            stream.write(bytes);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        logger.info("new file saved at: " + serverFile.getAbsolutePath());
    }

    public byte[] downloadFile(String fileName) throws FileDownloadException {
        if (fileName == null) {
            throw new FileDownloadException("No file selected for download");
        } else {
            String rootPath = System.getProperty("catalina.home");
            try (InputStream in = new FileInputStream(new File(rootPath + File.separator + "external_uploads" + File.separator + fileName))) {
                return IOUtils.toByteArray(in);
            } catch (IOException ex) {
                logger.info("File download failed: " + ex.getMessage());
                return null;
            }
        }
    }

    public List<String> getAllFilesName(String path) {
        List<String> names = new ArrayList<>();
        File folder = new File(path);
        for (File file : folder.listFiles()) {
            names.add(file.getName());
        }
        return names;

    }
}
