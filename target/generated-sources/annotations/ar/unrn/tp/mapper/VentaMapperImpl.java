package ar.unrn.tp.mapper;

import ar.unrn.tp.dto.ProductoVentaDTO;
import ar.unrn.tp.dto.VentaDTO;
import ar.unrn.tp.modelo.Categoria;
import ar.unrn.tp.modelo.Cliente;
import ar.unrn.tp.modelo.Producto;
import ar.unrn.tp.modelo.Venta;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-26T12:06:31-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 22.0.2 (Amazon.com Inc.)"
)
@Component
public class VentaMapperImpl implements VentaMapper {

    @Override
    public VentaDTO toVentaDTO(Venta venta) {
        if ( venta == null ) {
            return null;
        }

        VentaDTO ventaDTO = new VentaDTO();

        ventaDTO.setNombreCliente( ventaClienteNombre( venta ) );
        ventaDTO.setApellidoCliente( ventaClienteApellido( venta ) );
        ventaDTO.setId( venta.getId() );
        ventaDTO.setFecha( venta.getFecha() );
        ventaDTO.setNumeroVenta( venta.getNumeroVenta() );
        ventaDTO.setMontoTotal( venta.getMontoTotal() );
        ventaDTO.setProductos( toProductoVentaDTOList( venta.getProductos() ) );

        return ventaDTO;
    }

    @Override
    public List<VentaDTO> toVentaDTOList(List<Venta> ventas) {
        if ( ventas == null ) {
            return null;
        }

        List<VentaDTO> list = new ArrayList<VentaDTO>( ventas.size() );
        for ( Venta venta : ventas ) {
            list.add( toVentaDTO( venta ) );
        }

        return list;
    }

    @Override
    public ProductoVentaDTO toProductoVentaDTO(Producto producto) {
        if ( producto == null ) {
            return null;
        }

        ProductoVentaDTO productoVentaDTO = new ProductoVentaDTO();

        productoVentaDTO.setNombreCategoria( productoCategoriaNombre( producto ) );
        productoVentaDTO.setId( producto.getId() );
        productoVentaDTO.setNombre( producto.getNombre() );
        productoVentaDTO.setCodigo( producto.getCodigo() );
        productoVentaDTO.setMarca( producto.getMarca() );
        productoVentaDTO.setPrecio( producto.getPrecio() );

        return productoVentaDTO;
    }

    @Override
    public List<ProductoVentaDTO> toProductoVentaDTOList(List<Producto> productos) {
        if ( productos == null ) {
            return null;
        }

        List<ProductoVentaDTO> list = new ArrayList<ProductoVentaDTO>( productos.size() );
        for ( Producto producto : productos ) {
            list.add( toProductoVentaDTO( producto ) );
        }

        return list;
    }

    private String ventaClienteNombre(Venta venta) {
        if ( venta == null ) {
            return null;
        }
        Cliente cliente = venta.getCliente();
        if ( cliente == null ) {
            return null;
        }
        String nombre = cliente.getNombre();
        if ( nombre == null ) {
            return null;
        }
        return nombre;
    }

    private String ventaClienteApellido(Venta venta) {
        if ( venta == null ) {
            return null;
        }
        Cliente cliente = venta.getCliente();
        if ( cliente == null ) {
            return null;
        }
        String apellido = cliente.getApellido();
        if ( apellido == null ) {
            return null;
        }
        return apellido;
    }

    private String productoCategoriaNombre(Producto producto) {
        if ( producto == null ) {
            return null;
        }
        Categoria categoria = producto.getCategoria();
        if ( categoria == null ) {
            return null;
        }
        String nombre = categoria.getNombre();
        if ( nombre == null ) {
            return null;
        }
        return nombre;
    }
}
