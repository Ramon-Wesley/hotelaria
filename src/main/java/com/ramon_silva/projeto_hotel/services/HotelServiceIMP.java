package com.ramon_silva.projeto_hotel.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ramon_silva.projeto_hotel.dto.HotelDto;
import com.ramon_silva.projeto_hotel.dto.HotelImageDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.infra.errors.GeralException;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;
import com.ramon_silva.projeto_hotel.models.HotelModel;
import com.ramon_silva.projeto_hotel.repositories.HotelRepository;
import com.ramon_silva.projeto_hotel.services.interfaces.IHotelService;
import com.ramon_silva.projeto_hotel.util.UploadUtil;

@Service
public class HotelServiceIMP implements IHotelService {

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private UploadUtil uploadUtil;

    private HotelServiceIMP(HotelRepository hotelRepository, ModelMapper modelMapper) {

        this.hotelRepository = hotelRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public HotelDto create(HotelDto hotel, List<MultipartFile> files) {
        boolean existsCnpj = hotelRepository.existsByCnpj(hotel.getCnpj());

        if (!existsCnpj) {
            if (files.size() > 0) {
                HotelImageDto hotelImageDtos = new HotelImageDto();
            
                files.stream().forEach((e) -> {
                    try {
                        if (uploadUtil.uploadLoadImage(e, "hotel") == true) {
                            hotelImageDtos.setHotel(hotel);
                            hotelImageDtos.setImageUrl(e.getOriginalFilename());
                            hotel.getHotelImageDtos().add(hotelImageDtos);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }

            HotelModel hotelModel = hotelRepository.save(modelMapper.map(hotel, HotelModel.class));
            return modelMapper.map(hotelModel, HotelDto.class);
        }
        throw new GeralException("Cnpj já cadastrado!");
    }

    @Override
    public PageDto<HotelDto> getAll(int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<HotelModel> page = hotelRepository.findAll(pageable);
        List<HotelDto> hotelDtos = page.getContent().stream().map((e) -> modelMapper.map(e, HotelDto.class))
                .collect(Collectors.toList());
        PageDto<HotelDto> result = new PageDto<>(hotelDtos, page.getNumber(), page.getNumberOfElements(),
                page.getSize(),
                page.getTotalPages(), page.getTotalElements());
        return result;
    }

    @Override
    public HotelDto getById(Long id) {
        HotelModel hotelModel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", id));

        return modelMapper.map(hotelModel, HotelDto.class);
    }

    @Override
    public HotelDto updateById(Long id, HotelDto hotel, List<MultipartFile> files) {

        hotelRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Hotel", "id", id));

        boolean existsCnpj = hotelRepository.existsByCnpjAndIdNot(hotel.getCnpj(), id);
        if (!existsCnpj) {
            hotel.setId(id);
            HotelModel result = hotelRepository.save(modelMapper.map(hotel, HotelModel.class));
            return modelMapper.map(result, HotelDto.class);
        }
        throw new GeralException("Cnpj já cadastrado!");
    }

    @Override
    public void deleteById(Long id) {
        hotelRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Hotel", "id", id));
        hotelRepository.deleteById(id);
    }

}
