package es.com.w2m.dls.controller;

import es.com.w2m.dls.model.ShipDto;
import es.com.w2m.dls.service.IShipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ships")
@Slf4j
@RequiredArgsConstructor
public class ShipController {

    private final IShipService shipService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ShipDto> findAll(
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {
        return shipService.findAll(page, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ShipDto findById(@PathVariable Long id) {
        return shipService.findById(id);
    }

    @GetMapping("/name")
    @ResponseStatus(HttpStatus.OK)
    public List<ShipDto> findByName(
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size,
            @RequestParam(value = "name", required = true) String name) {
        return shipService.findByName(page, size, name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShipDto save(@RequestBody ShipDto ship) {
        return shipService.save(ship);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ShipDto save(@PathVariable Long id, @RequestBody ShipDto ship) {
        return shipService.update(id, ship);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable Long id) {
        shipService.deleteById(id);
    }

}
