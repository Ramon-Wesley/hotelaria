package com.ramon_silva.projeto_hotel.services.interfaces;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.ramon_silva.projeto_hotel.dto.PageDto;

public interface IGenericImages<T> {

    public void addImages(Long id, Map<String,MultipartFile> file);

    public void removeImageById(Long id, Long image_id);

    public PageDto<T> getAllImages(Long id, int pageNumber, int pageSize, String sortBy, String sortOrder);
}