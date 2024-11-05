package es.com.w2m.dls.service;

import es.com.w2m.dls.model.ShipDto;

import java.util.List;

public interface IShipService {
    List<ShipDto> findAll(Integer page, Integer size);
    ShipDto findById(Long id);
    List<ShipDto> findByName(Integer page, Integer size, String name);
    ShipDto save(ShipDto shipDto);
    ShipDto update(Long id, ShipDto shipDto);
    void deleteById(Long id);
}
