package es.com.w2m.dls.service;

import es.com.w2m.dls.config.error.CustomException;
import es.com.w2m.dls.dao.entity.ShipEntity;
import es.com.w2m.dls.dao.repository.IShipRepository;
import es.com.w2m.dls.mapper.ShipMapper;
import es.com.w2m.dls.model.ShipDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipServiceImpl implements IShipService{

    private final IShipRepository shipRepository;
    private final ShipMapper shipMapper;

    @Override
    public List<ShipDto> findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").descending());
        return shipRepository.findAll(pageable).getContent()
                .stream()
                .map(shipMapper::toShipDto)
                .collect(Collectors.toList());
    }

    @Override
    public ShipDto findById(Long id) {
        return shipRepository.findById(id)
                .map(shipMapper::toShipDto)
                .orElseThrow(CustomException::notFound);
    }

    @Cacheable("ships")
    @Override
    public List<ShipDto> findByName(Integer page, Integer size, String name) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").descending());
        return shipRepository.findByNameContainingIgnoreCase(name, pageable).getContent()
                .stream()
                .map(shipMapper::toShipDto)
                .map(shipMapper::toFullName)
                .collect(Collectors.toList());
    }

    @Override
    public ShipDto save(ShipDto shipDto) {
        return Optional.of(shipDto)
                .map(shipMapper::toShipEntity)
                .map(shipRepository::save)
                .map(shipMapper::toShipDto)
                .orElseThrow(CustomException::notFound);
    }

    @Override
    public ShipDto update(Long id, ShipDto shipDto) {
        return Optional.of(id)
                .flatMap(shipRepository::findById)
                .map(e->{
                    e.setName(shipDto.getName());
                   return e;
                })
                .map(shipRepository::save)
                .map(shipMapper::toShipDto)
                .orElseThrow(CustomException::notFound);
    }

    @CacheEvict("users")
    @Override
    public void deleteById(Long id) {
        Optional.of(id)
                .flatMap(shipRepository::findById)
                .map(ShipEntity::getId)
                .ifPresentOrElse(shipRepository::deleteById, CustomException::notFound);
    }
}
