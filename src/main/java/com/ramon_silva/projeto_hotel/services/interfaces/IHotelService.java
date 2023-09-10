
package com.ramon_silva.projeto_hotel.services.interfaces;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ramon_silva.projeto_hotel.dto.HotelDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;

public interface IHotelService {
    public HotelDto create(HotelDto hotel, List<MultipartFile> files);

    public PageDto<HotelDto> getAll(int pageNumber, int pageSize, String sortBy, String sortOrder);

    public HotelDto getById(Long id);

    public HotelDto updateById(Long id, HotelDto address, List<MultipartFile> files);

    public void deleteById(Long id);
}