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

import com.ramon_silva.projeto_hotel.dto.HotelImageDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.dto.RoomDto;
import com.ramon_silva.projeto_hotel.dto.RoomImageDto;
import com.ramon_silva.projeto_hotel.infra.errors.GeralException;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;
import com.ramon_silva.projeto_hotel.models.HotelImage;
import com.ramon_silva.projeto_hotel.models.HotelModel;
import com.ramon_silva.projeto_hotel.models.RoomImage;
import com.ramon_silva.projeto_hotel.models.RoomModel;
import com.ramon_silva.projeto_hotel.repositories.HotelRepository;
import com.ramon_silva.projeto_hotel.repositories.RoomImageRepository;
import com.ramon_silva.projeto_hotel.repositories.RoomRepository;
import com.ramon_silva.projeto_hotel.services.interfaces.IRoomService;
import com.ramon_silva.projeto_hotel.util.UploadUtil;

@Service
public class RoomServiceIMP implements IRoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomImageRepository roomImageRepository;
    private final ModelMapper modelMapper;

    private RoomServiceIMP(RoomRepository roomRepository, HotelRepository hotelRepository, ModelMapper modelMapper,
            RoomImageRepository roomImageRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.modelMapper = modelMapper;
        this.roomImageRepository = roomImageRepository;
    }

    @Override
    public RoomDto create(RoomDto room) {
        HotelModel hotel = hotelRepository.findById(room.getHotel().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", room.getHotel().getId()));

        RoomModel roomModel = modelMapper.map(room, RoomModel.class);
        roomModel.setHotel(hotel);
        RoomModel result = roomRepository.save(roomModel);

        return modelMapper.map(result, RoomDto.class);
    }

    @Override
    public PageDto<RoomDto> getAll(int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<RoomModel> page = roomRepository.findAll(pageable);
        List<RoomDto> roomDtos = page.getContent().stream().map((result) -> modelMapper.map(result, RoomDto.class))
                .collect(Collectors.toList());
        PageDto<RoomDto> pageDto = new PageDto<>(roomDtos, page.getNumber(), page.getNumberOfElements(), page.getSize(),
                page.getTotalPages(), page.getTotalElements());
        return pageDto;
    }

    @Override
    public RoomDto getById(Long id) {
        RoomModel roomModel = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quartos", "id", id));

        return modelMapper.map(roomModel, RoomDto.class);
    }

    @Override
    public RoomDto updateById(Long id, RoomDto room) {
        roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Quartos", "id", id));

        RoomModel roomModel = roomRepository.save(modelMapper.map(room, RoomModel.class));

        return modelMapper.map(roomModel, RoomDto.class);
    }

    @Override
    public void deleteById(Long id) {
        roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Quartos", "id", id));
        roomRepository.deleteById(id);
    }
    @Override
    public void addImages(Long id, Map<String, MultipartFile> file) {
        
        RoomModel room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quarto", "id", id));
    
        List<RoomImage> roomImages = roomImageRepository.findAllByRoomId(id);
        List<RoomImage> roomImageModels = new ArrayList<>();
    
        if (file != null && !file.isEmpty()) {
            file.forEach((key, value) -> {
                try {
                    if (value != null) {
                        String originalFilename = value.getOriginalFilename();
                        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
    
                     
                        if (fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("png")) {
                            String imageUrl = room.getHotel().getName() + "-" + room.getNumber_room() + "-" + key + "." + fileExtension;
    
                            
                            if (UploadUtil.uploadLoadImage(value, "room", imageUrl)) {
                                Optional<RoomImage> searchImage = roomImages.stream()
                                        .filter(image -> image.getType().equals(key))
                                        .findFirst();
    
                                RoomImage roomImage = searchImage.orElseGet(RoomImage::new);
                                roomImage.setType(key);
                                roomImage.setImageUrl(imageUrl);
                                roomImage.setRoom(room);
                                roomImageModels.add(roomImage);
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
    
        if (!roomImageModels.isEmpty()) {
            roomImageRepository.saveAll(roomImageModels);
        }
    }
    

    @Override
    public void removeImageById(Long id, Long file) {
        Optional<RoomImage> roomImage = roomImageRepository.findByRoomIdAndId(id, file);

        if (roomImage.isPresent()) {
            if (UploadUtil.removeImage("room", roomImage.get().getImageUrl())) {
                roomImageRepository.deleteById(file);
            } else {
                throw new GeralException("Não foi possivel deletar o registro!");
            }
        } else {
            throw new ResourceNotFoundException("quartoImagem", "id", file);
        }

    }

    @Override
    public PageDto<RoomImageDto> getAllImages(Long id, int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<RoomImage> page = roomImageRepository.findAllByRoomId(id, pageable);

        List<RoomImageDto> roomImageDtos = page.getContent().stream()
                .map((e) -> modelMapper.map(e, RoomImageDto.class)).collect(Collectors.toList());
        PageDto<RoomImageDto> result = new PageDto<>(roomImageDtos,
                page.getNumber(),
                page.getNumberOfElements(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements());

        return result;
    }

}
