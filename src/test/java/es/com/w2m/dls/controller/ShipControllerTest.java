package es.com.w2m.dls.controller;

import es.com.w2m.dls.model.ShipDto;
import es.com.w2m.dls.service.IShipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ShipController.class)
public class ShipControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IShipService shipService;

    private ShipDto ship;
    @BeforeEach
    void setUp() {
        ship = new ShipDto();
        ship.setId(1L);
        ship.setName("Nave Espacial");
    }

    @Test
    public void testFindAll() throws Exception {
        List<ShipDto> ships = Collections.singletonList(ship);
        when(shipService.findAll(any(Integer.class), any(Integer.class))).thenReturn(ships);
        mockMvc.perform(get("/ships") .param("page", "1") .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(ship.getId()))
                .andExpect(jsonPath("$[0].name").value(ship.getName()));
    }

    @Test
    public void testFindById() throws Exception {
        when(shipService.findById(1L)).thenReturn(ship);
        mockMvc.perform(get("/ships/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ship.getId()))
                .andExpect(jsonPath("$.name").value(ship.getName()));
    }

    @Test
    public void testFindByName() throws Exception {
        List<ShipDto> ships = Collections.singletonList(ship);
        when(shipService.findByName(any(Integer.class), any(Integer.class), any(String.class))).thenReturn(ships);
        mockMvc.perform(get("/ships/name")
                .param("page", "1")
                .param("size", "10")
                .param("name", "Nave")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(ship.getId()))
                .andExpect(jsonPath("$[0].name").value(ship.getName()));
    }

    @Test
    public void testSave() throws Exception {
        when(shipService.save(any(ShipDto.class))).thenReturn(ship);
        mockMvc.perform(post("/ships")
                .content("{\"name\":\"Nave Espacial\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ship.getId()))
                .andExpect(jsonPath("$.name").value(ship.getName()));
    }

    @Test
    public void testUpdate() throws Exception {
        when(shipService.update(any(Long.class), any(ShipDto.class))).thenReturn(ship);
        mockMvc.perform(put("/ships/{id}", 1L)
                .content("{\"name\":\"Nave Espacial Actualizada\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ship.getId()))
                .andExpect(jsonPath("$.name").value(ship.getName()));
    }

    @Test
    public void testDeleteById() throws Exception {
        mockMvc.perform(delete("/ships/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}