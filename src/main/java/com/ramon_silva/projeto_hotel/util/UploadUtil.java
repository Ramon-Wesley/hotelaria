package com.ramon_silva.projeto_hotel.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.springframework.web.multipart.MultipartFile;

import com.ramon_silva.projeto_hotel.infra.errors.GeralException;

public class UploadUtil {

    private UploadUtil() {
    }

    public static boolean uploadLoadImage(MultipartFile image, String folderName,String name) {
        boolean sucess = false;

        if (!image.isEmpty()) {
            try {
                String folderUploadLocation = "./src/main/resources/images/img-upload-"
                        + folderName;

                File file = new File(folderUploadLocation);
                if (!file.exists()) {
                    file.mkdirs();
                }

                File serverFile = new File(file.getAbsolutePath() + File.separator + name);

                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));

                stream.write(image.getBytes());
                stream.close();
                sucess = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return sucess;
    }

    public static boolean removeImage(String folderName, String pathName) {
        boolean success = false;

        try {
            String folderUploadLocation = "./src/main/resources/images/img-upload-"+folderName+"/"+pathName;
            File file = new File(folderUploadLocation);

            if (file.exists()) {
                if (file.delete()) {
                    success = true;
                } else {
                    throw new GeralException("Falha ao excluir o arquivo " + pathName);
                }
            } else {
                throw new GeralException("Arquivo " + folderUploadLocation + " não encontrado");
            }
        } catch (Exception e) {
            throw new GeralException("Erro ao excluir o arquivo " + pathName + ": " + e.getMessage());
        }

        return success;
    }

}
