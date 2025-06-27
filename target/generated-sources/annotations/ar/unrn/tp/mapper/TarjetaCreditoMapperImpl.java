package ar.unrn.tp.mapper;

import ar.unrn.tp.dto.TarjetaCreditoDTO;
import ar.unrn.tp.modelo.Cliente;
import ar.unrn.tp.modelo.TarjetaCredito;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-26T12:06:31-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 22.0.2 (Amazon.com Inc.)"
)
@Component
public class TarjetaCreditoMapperImpl implements TarjetaCreditoMapper {

    @Override
    public TarjetaCreditoDTO tarjetaCreditoToTarjetaCreditoDTO(TarjetaCredito tarjetaCredito) {
        if ( tarjetaCredito == null ) {
            return null;
        }

        TarjetaCreditoDTO tarjetaCreditoDTO = new TarjetaCreditoDTO();

        tarjetaCreditoDTO.setClienteId( tarjetaCreditoClienteId( tarjetaCredito ) );
        tarjetaCreditoDTO.setId( tarjetaCredito.getId() );
        tarjetaCreditoDTO.setNumero( tarjetaCredito.getNumero() );
        tarjetaCreditoDTO.setMarca( tarjetaCredito.getMarca() );
        tarjetaCreditoDTO.setFechaVencimiento( tarjetaCredito.getFechaVencimiento() );

        return tarjetaCreditoDTO;
    }

    @Override
    public TarjetaCredito tarjetaCreditoDTOToTarjetaCredito(TarjetaCreditoDTO tarjetaCreditoDTO) {
        if ( tarjetaCreditoDTO == null ) {
            return null;
        }

        TarjetaCredito tarjetaCredito = new TarjetaCredito();

        tarjetaCredito.setCliente( map( tarjetaCreditoDTO.getClienteId() ) );
        tarjetaCredito.setId( tarjetaCreditoDTO.getId() );
        tarjetaCredito.setNumero( tarjetaCreditoDTO.getNumero() );
        tarjetaCredito.setMarca( tarjetaCreditoDTO.getMarca() );
        tarjetaCredito.setFechaVencimiento( tarjetaCreditoDTO.getFechaVencimiento() );

        return tarjetaCredito;
    }

    private Long tarjetaCreditoClienteId(TarjetaCredito tarjetaCredito) {
        if ( tarjetaCredito == null ) {
            return null;
        }
        Cliente cliente = tarjetaCredito.getCliente();
        if ( cliente == null ) {
            return null;
        }
        Long id = cliente.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
