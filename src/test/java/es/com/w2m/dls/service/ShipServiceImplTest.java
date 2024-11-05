package es.com.w2m.dls.service;

import es.com.w2m.dls.config.error.CustomException;
import es.com.w2m.dls.dao.entity.ShipEntity;
import es.com.w2m.dls.dao.repository.IShipRepository;
import es.com.w2m.dls.mapper.ShipMapper;
import es.com.w2m.dls.model.ShipDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShipServiceImplTest {
    @Mock
    private IShipRepository shipRepository;
    @Mock
    private ShipMapper shipMapper;
    @InjectMocks
    private ShipServiceImpl shipService;

    private ShipEntity shipEntity;
    private ShipDto shipDto;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        shipEntity = new ShipEntity();
        shipEntity.setId(1L);
        shipEntity.setName("ship 1");

        shipDto = new ShipDto();
        shipDto.setId(1L);
        shipDto.setName("ship 1");
    }

    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").descending());
        Page<ShipEntity> page = new PageImpl<>(List.of(new ShipEntity(), new ShipEntity()));
        when(shipRepository.findAll(pageable)).thenReturn(page);
        when(shipMapper.toShipDto(any(ShipEntity.class))).thenReturn(new ShipDto());
        List<ShipDto> result = shipService.findAll(0, 10);
        assertEquals(2, result.size()); verify(shipRepository, times(1)).findAll(pageable);
        verify(shipRepository, times(1)).findAll(pageable);
        verify(shipMapper, times(2)).toShipDto(any(ShipEntity.class));

    }

    @Test
    public void testFindById() {
        when(shipRepository.findById(1L)).thenReturn(Optional.of(shipEntity));
        when(shipMapper.toShipDto(shipEntity)).thenReturn(shipDto);
        ShipDto result = shipService.findById(1L);
        assertNotNull(result);
        assertEquals(shipDto, result);
    }

    @Test
    public void testFindById_NotFound() {
        when(shipRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CustomException.class, () -> shipService.findById(1L));
    }

    @Test
    public void testFindByName() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").descending());
        Page<ShipEntity> page = new PageImpl<>(List.of(new ShipEntity(), new ShipEntity()));
        when(shipRepository.findByNameContainingIgnoreCase("name", pageable)).thenReturn(page);
        when(shipMapper.toShipDto(any(ShipEntity.class))).thenReturn(new ShipDto());
        when(shipMapper.toFullName(any(ShipDto.class))).thenReturn(new ShipDto());
        List<ShipDto> result = shipService.findByName(0, 10, "name");
        assertEquals(2, result.size());
        verify(shipRepository, times(1)).findByNameContainingIgnoreCase("name", pageable);
        verify(shipMapper, times(2)).toShipDto(any(ShipEntity.class));
        verify(shipMapper, times(2)).toFullName(any(ShipDto.class));
    }

    @Test
    public void testSave() {
        when(shipMapper.toShipEntity(shipDto)).thenReturn(shipEntity);
        when(shipRepository.save(shipEntity)).thenReturn(shipEntity);
        when(shipMapper.toShipDto(shipEntity)).thenReturn(shipDto);
        ShipDto result = shipService.save(shipDto);
        assertNotNull(result);
        assertEquals(shipDto, result);
    }

    @Test public void testUpdate() {
        when(shipRepository.findById(1L)).thenReturn(Optional.of(shipEntity));
        when(shipRepository.save(shipEntity)).thenReturn(shipEntity);
        when(shipMapper.toShipDto(shipEntity)).thenReturn(shipDto);
        ShipDto result = shipService.update(1L, shipDto);
        assertNotNull(result);
        assertEquals(shipDto, result);
    }

    @Test
    public void testUpdate_NotFound() {
        when(shipRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CustomException.class, () -> shipService.update(1L, shipDto));
    }

    @Test
    public void testDeleteById() {
        ShipEntity shipEntity = new ShipEntity();
        when(shipRepository.findById(1L)).thenReturn(Optional.of(shipEntity));
        shipService.deleteById(1L);
        verify(shipRepository, times(1)).findById(1L);
        verify(shipRepository, times(0)).deleteById(1L);
    }

}