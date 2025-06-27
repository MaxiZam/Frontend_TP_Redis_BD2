package ar.unrn.tp.mapper;

import ar.unrn.tp.dto.ClienteDTO;
import ar.unrn.tp.dto.TarjetaCreditoDTO;
import ar.unrn.tp.modelo.Cliente;
import ar.unrn.tp.modelo.TarjetaCredito;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-26T12:06:31-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 22.0.2 (Amazon.com Inc.)"
)
@Component
public class ClienteMapperImpl implements ClienteMapper {

    @Autowired
    private TarjetaCreditoMapper tarjetaCreditoMapper;

    @Override
    public ClienteDTO clienteToClienteDTO(Cliente cliente) {
        if ( cliente == null ) {
            return null;
        }

        ClienteDTO clienteDTO = new ClienteDTO();

        clienteDTO.setTarjetas( tarjetaCreditoListToTarjetaCreditoDTOList( cliente.getTarjetas() ) );
        clienteDTO.setId( cliente.getId() );
        clienteDTO.setNombre( cliente.getNombre() );
        clienteDTO.setApellido( cliente.getApellido() );
        clienteDTO.setDni( cliente.getDni() );
        clienteDTO.setEmail( cliente.getEmail() );

        return clienteDTO;
    }

    @Override
    public Cliente clienteDTOToCliente(ClienteDTO clienteDTO) {
        if ( clienteDTO == null ) {
            return null;
        }

        Cliente cliente = new Cliente();

        cliente.setTarjetas( tarjetaCreditoDTOListToTarjetaCreditoList( clienteDTO.getTarjetas() ) );
        cliente.setId( clienteDTO.getId() );
        cliente.setNombre( clienteDTO.getNombre() );
        cliente.setApellido( clienteDTO.getApellido() );
        cliente.setDni( clienteDTO.getDni() );
        cliente.setEmail( clienteDTO.getEmail() );

        return cliente;
    }

    protected List<TarjetaCreditoDTO> tarjetaCreditoListToTarjetaCreditoDTOList(List<TarjetaCredito> list) {
        if ( list == null ) {
            return null;
        }

        List<TarjetaCreditoDTO> list1 = new ArrayList<TarjetaCreditoDTO>( list.size() );
        for ( TarjetaCredito tarjetaCredito : list ) {
            list1.add( tarjetaCreditoMapper.tarjetaCreditoToTarjetaCreditoDTO( tarjetaCredito ) );
        }

        return list1;
    }

    protected List<TarjetaCredito> tarjetaCreditoDTOListToTarjetaCreditoList(List<TarjetaCreditoDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<TarjetaCredito> list1 = new ArrayList<TarjetaCredito>( list.size() );
        for ( TarjetaCreditoDTO tarjetaCreditoDTO : list ) {
            list1.add( tarjetaCreditoMapper.tarjetaCreditoDTOToTarjetaCredito( tarjetaCreditoDTO ) );
        }

        return list1;
    }
}
