package ar.unrn.tp.mapper;

import ar.unrn.tp.modelo.Producto;
import org.mapstruct.Mapper;
import ar.unrn.tp.modelo.Venta;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;
import ar.unrn.tp.dto.ProductoVentaDTO;
import ar.unrn.tp.dto.VentaDTO; // Importar el DTO renombrado

@Mapper(componentModel = "spring")
public interface VentaMapper {

    VentaMapper INSTANCE = Mappers.getMapper(VentaMapper.class);

    @Mapping(source = "cliente.nombre", target = "nombreCliente")
    @Mapping(source = "cliente.apellido", target = "apellidoCliente")
    VentaDTO toVentaDTO(Venta venta); // Renombrado el método

    List<VentaDTO> toVentaDTOList(List<Venta> ventas); // Renombrado el método

    @Mapping(source = "categoria.nombre", target = "nombreCategoria")
    ProductoVentaDTO toProductoVentaDTO(Producto producto);

    List<ProductoVentaDTO> toProductoVentaDTOList(List<Producto> productos);
}






