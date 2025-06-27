package ar.unrn.tp.web;

import ar.unrn.tp.api.DescuentoService;
import ar.unrn.tp.dto.CategoriaDTO;
import ar.unrn.tp.dto.DescuentoCompraDTO;
import ar.unrn.tp.dto.DescuentoProductoDTO;
import ar.unrn.tp.mapper.ClienteMapper;
import ar.unrn.tp.mapper.DescuentoMapper;
import ar.unrn.tp.modelo.DescuentoCompra;
import ar.unrn.tp.modelo.DescuentoProducto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/descuentos")
@AllArgsConstructor
public class DescuentoController {

    private DescuentoMapper descuentoMapper;
    private DescuentoService descuentoService;

    @PostMapping("/compra")
    public ResponseEntity<String> crearDescuentoSobreTotal(@RequestBody DescuentoCompraDTO descuentoCompraDTO) {
        try {
            descuentoService.crearDescuentoSobreTotal(
                    descuentoCompraDTO.getMarcaTarjeta(),
                    descuentoCompraDTO.getFechaInicio(),
                    descuentoCompraDTO.getFechaFin(),
                    descuentoCompraDTO.getPorcentajeDescuento()
            );
            return ResponseEntity.ok("Descuento sobre total creado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/producto")
    public ResponseEntity<String> crearDescuentoSobreProducto(@RequestBody DescuentoProductoDTO descuentoProductoDTO) {
        try {
            descuentoService.crearDescuentoSobreProducto(
                    descuentoProductoDTO.getMarcaProducto(),
                    descuentoProductoDTO.getFechaInicio(),
                    descuentoProductoDTO.getFechaFin(),
                    descuentoProductoDTO.getPorcentajeDescuento()
            );
            return ResponseEntity.ok("Descuento sobre producto creado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/listar-descuentos-compra")
    public ResponseEntity<List<DescuentoCompraDTO>> listarDescuentosCompra(){
        List <DescuentoCompra> descuentos = descuentoService.listarDescuentosCompra();
        return ResponseEntity.ok(descuentos.stream()
                .map(descuentoMapper::descuentoCompraToDescuentoCompraDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/listar-descuentos-producto")
    public ResponseEntity<List<DescuentoProductoDTO>> listarDescuentosProductos(){
        List <DescuentoProducto> descuentos = descuentoService.listarDescuentosProducto();
        return ResponseEntity.ok(descuentos.stream()
                .map(descuentoMapper::descuentoProductoToDescuentoProductoDTO)
                .collect(Collectors.toList()));
    }

}


