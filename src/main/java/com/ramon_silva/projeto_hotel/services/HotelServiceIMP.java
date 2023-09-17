package com.ramon_silva.projeto_hotel.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import com.ramon_silva.projeto_hotel.models.HotelImage;
import com.ramon_silva.projeto_hotel.models.HotelModel;
import com.ramon_silva.projeto_hotel.models.RoomImage;
import com.ramon_silva.projeto_hotel.models.RoomModel;
import com.ramon_silva.projeto_hotel.repositories.HotelImageRepository;
import com.ramon_silva.projeto_hotel.repositories.HotelRepository;
import com.ramon_silva.projeto_hotel.services.interfaces.IHotelService;
import com.ramon_silva.projeto_hotel.util.UploadUtil;

@Service
public class HotelServiceIMP implements IHotelService {

    private final HotelRepository hotelRepository;
    private final HotelImageRepository hotelImageRepository;
    private final ModelMapper modelMapper;

    private HotelServiceIMP(HotelRepository hotelRepository, ModelMapper modelMapper,
            HotelImageRepository hotelImageRepository) {
        this.hotelRepository = hotelRepository;
        this.modelMapper = modelMapper;
        this.hotelImageRepository = hotelImageRepository;
    }

    @Override
    public HotelDto create(HotelDto hotel) {
        boolean existsCnpj = hotelRepository.existsByCnpj(hotel.getCnpj());

        if (!existsCnpj) {
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
        List<HotelDto> hotelDtos = page.getContent().stream()
                .map((e) -> modelMapper.map(e, HotelDto.class))
                .collect(Collectors.toList());
        PageDto<HotelDto> result = new PageDto<>(
                hotelDtos,
                page.getNumber(),
                page.getNumberOfElements(),
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
    public HotelDto updateById(Long id, HotelDto hotel) {

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

       @Override
    public void addImages(Long id, Map<String, MultipartFile> file) {
        
        HotelModel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("hotel", "id", id));
    
        List<HotelImage> hotelImages = hotelImageRepository.findAllByHotelId(id);
        List<HotelImage> hotelImageModels = new ArrayList<>();
    
        if (file != null && !file.isEmpty()) {
            file.forEach((key, value) -> {
                try {
                    if (value != null) {
                        String originalFilename = value.getOriginalFilename();
                        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
    
                     
                        if (fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("png")) {
                            String imageUrl = hotel.getName() + "-" + key + "." + fileExtension;
    
                            
                            if (UploadUtil.uploadLoadImage(value, "hotel", imageUrl)) {
                                Optional<HotelImage> searchImage = hotelImages.stream()
                                        .filter(image -> image.getType().equals(key))
                                        .findFirst();
    
                                HotelImage hotelImage = searchImage.orElseGet(HotelImage::new);
                                hotelImage.setType(key);
                                hotelImage.setImageUrl(imageUrl);
                                hotelImage.setHotel(hotel);
                                hotelImageModels.add(hotelImage);
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
    
        if (!hotelImageModels.isEmpty()) {
            hotelImageRepository.saveAll(hotelImageModels);
        }
    }

    @Override
    public void removeImageById(Long id, Long file) {
        Optional<HotelImage> hotelImage = hotelImageRepository.findByHotelIdAndId(id, file);

        if (hotelImage.isPresent()) {
            if (UploadUtil.removeImage("hotel", hotelImage.get().getImageUrl())) {
                hotelImageRepository.deleteById(file);
            } else {
                throw new GeralException("Não foi possivel deletar o registro!");
            }
        } else {
            throw new ResourceNotFoundException("hotelImagem", "id", file);
        }

    }

    @Override
    public PageDto<HotelImageDto> getAllImages(Long id, int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<HotelImage> page = hotelImageRepository.findAllByHotelId(id, pageable);

        List<HotelImageDto> hotelImageDtos = page.getContent().stream()
                .map((e) -> modelMapper.map(e, HotelImageDto.class)).collect(Collectors.toList());
        PageDto<HotelImageDto> result = new PageDto<>(hotelImageDtos,
                page.getNumber(),
                page.getNumberOfElements(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements());

        return result;
    }

}
