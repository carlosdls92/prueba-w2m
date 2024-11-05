package es.com.w2m.dls.mapper;

import es.com.w2m.dls.dao.entity.ShipEntity;
import es.com.w2m.dls.model.ShipDto;
import org.mapstruct.*;

import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class ShipMapper {

    @Mapping(source = "name", target = "name")
    public abstract ShipDto toShipDto(ShipEntity shipEntity);

    @InheritInverseConfiguration
    public abstract ShipEntity toShipEntity(ShipDto shipDto);

    @AfterMapping
    public ShipDto toFullName(@MappingTarget ShipDto shipDTO) {
        Optional.of(shipDTO)
                .map(ShipDto::getName)
                .map(n->"x-"+n)
                .ifPresent(shipDTO::setName);
        return shipDTO;
    }
}
