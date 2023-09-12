package com.ramon_silva.projeto_hotel.services.interfaces;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ramon_silva.projeto_hotel.dto.PageDto;

public interface IImagesServices<T> {
    public void addImage(Long id, List<MultipartFile> file);
    public void removeImage(Long id,List<String> file);
    public PageDto<T> getAll(Long id,int pageNumber, int pageSize, String sortBy, String sortOrder);

}
