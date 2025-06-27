package ar.unrn.tp.mapper;

import ar.unrn.tp.dto.DescuentoCompraDTO;
import ar.unrn.tp.dto.DescuentoProductoDTO;
import ar.unrn.tp.modelo.DescuentoCompra;
import ar.unrn.tp.modelo.DescuentoProducto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-26T15:40:39-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 22.0.2 (Amazon.com Inc.)"
)
@Component
public class DescuentoMapperImpl implements DescuentoMapper {

    @Override
    public DescuentoCompraDTO descuentoCompraToDescuentoCompraDTO(DescuentoCompra descuentoCompra) {
        if ( descuentoCompra == null ) {
            return null;
        }

        DescuentoCompraDTO descuentoCompraDTO = new DescuentoCompraDTO();

        descuentoCompraDTO.setId( descuentoCompra.getId() );
        descuentoCompraDTO.setFechaInicio( descuentoCompra.getFechaInicio() );
        descuentoCompraDTO.setFechaFin( descuentoCompra.getFechaFin() );
        descuentoCompraDTO.setMarcaTarjeta( descuentoCompra.getMarcaTarjeta() );
        descuentoCompraDTO.setPorcentajeDescuento( descuentoCompra.getPorcentajeDescuento() );

        return descuentoCompraDTO;
    }

    @Override
    public DescuentoCompra descuentoCompraDTOToDescuentoCompra(DescuentoCompraDTO descuentoCompraDTO) {
        if ( descuentoCompraDTO == null ) {
            return null;
        }

        DescuentoCompra descuentoCompra = new DescuentoCompra();

        descuentoCompra.setId( descuentoCompraDTO.getId() );
        descuentoCompra.setFechaInicio( descuentoCompraDTO.getFechaInicio() );
        descuentoCompra.setFechaFin( descuentoCompraDTO.getFechaFin() );
        descuentoCompra.setMarcaTarjeta( descuentoCompraDTO.getMarcaTarjeta() );
        descuentoCompra.setPorcentajeDescuento( descuentoCompraDTO.getPorcentajeDescuento() );

        return descuentoCompra;
    }

    @Override
    public DescuentoProductoDTO descuentoProductoToDescuentoProductoDTO(DescuentoProducto descuentoProducto) {
        if ( descuentoProducto == null ) {
            return null;
        }

        DescuentoProductoDTO descuentoProductoDTO = new DescuentoProductoDTO();

        descuentoProductoDTO.setId( descuentoProducto.getId() );
        descuentoProductoDTO.setFechaInicio( descuentoProducto.getFechaInicio() );
        descuentoProductoDTO.setFechaFin( descuentoProducto.getFechaFin() );
        descuentoProductoDTO.setMarcaProducto( descuentoProducto.getMarcaProducto() );
        descuentoProductoDTO.setPorcentajeDescuento( descuentoProducto.getPorcentajeDescuento() );

        return descuentoProductoDTO;
    }

    @Override
    public DescuentoProducto descuentoProductoDTOToDescuentoProducto(DescuentoProductoDTO descuentoProductoDTO) {
        if ( descuentoProductoDTO == null ) {
            return null;
        }

        DescuentoProducto descuentoProducto = new DescuentoProducto();

        descuentoProducto.setId( descuentoProductoDTO.getId() );
        descuentoProducto.setFechaInicio( descuentoProductoDTO.getFechaInicio() );
        descuentoProducto.setFechaFin( descuentoProductoDTO.getFechaFin() );
        descuentoProducto.setMarcaProducto( descuentoProductoDTO.getMarcaProducto() );
        descuentoProducto.setPorcentajeDescuento( descuentoProductoDTO.getPorcentajeDescuento() );

        return descuentoProducto;
    }
}
