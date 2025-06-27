package ar.unrn.tp.mapper;

import ar.unrn.tp.dto.DescuentoCompraDTO;
import ar.unrn.tp.dto.DescuentoProductoDTO;
import ar.unrn.tp.modelo.DescuentoCompra;
import ar.unrn.tp.modelo.DescuentoProducto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DescuentoMapper {

    DescuentoMapper INSTANCE = Mappers.getMapper(DescuentoMapper.class);

    @Mapping(target = "id", source = "id")
    DescuentoCompraDTO descuentoCompraToDescuentoCompraDTO(DescuentoCompra descuentoCompra);
    DescuentoCompra descuentoCompraDTOToDescuentoCompra(DescuentoCompraDTO descuentoCompraDTO);

    @Mapping(target = "id", source = "id")
    DescuentoProductoDTO descuentoProductoToDescuentoProductoDTO(DescuentoProducto descuentoProducto);
    DescuentoProducto descuentoProductoDTOToDescuentoProducto(DescuentoProductoDTO descuentoProductoDTO);
}
