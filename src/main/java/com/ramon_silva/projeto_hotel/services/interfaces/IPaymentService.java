package com.ramon_silva.projeto_hotel.services.interfaces;

import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.dto.PaymentDto;
import com.ramon_silva.projeto_hotel.dto.PaymentMethodDto;
import com.ramon_silva.projeto_hotel.enums.PaymentMethodEnum;

public interface IPaymentService {

    public PaymentDto create(Long reservation_id,PaymentMethodEnum entity);

    public PageDto<PaymentDto> getAll(int pageNumber, int pageSize, String sortBy, String sortOrder);

    public PaymentDto getById(Long id);

    public PaymentDto updateById(Long id, PaymentDto entity);

    public void deleteById(Long id);

}
