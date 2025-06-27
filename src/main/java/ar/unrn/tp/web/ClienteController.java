package ar.unrn.tp.web;

import ar.unrn.tp.api.ClienteService;
import ar.unrn.tp.dto.ClienteDTO;
import ar.unrn.tp.dto.LoginRequestDTO;
import ar.unrn.tp.dto.TarjetaCreditoDTO;
import ar.unrn.tp.mapper.ClienteMapper;
import ar.unrn.tp.mapper.TarjetaCreditoMapper;
import ar.unrn.tp.modelo.Cliente;
import ar.unrn.tp.modelo.TarjetaCredito;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
@AllArgsConstructor
public class ClienteController {

    private ClienteService clienteService;
    private ClienteMapper clienteMapper;
    private TarjetaCreditoMapper tarjetaCreditoMapper;

    @PostMapping
    public ResponseEntity<String> crearCliente(@RequestBody ClienteDTO clienteDTO) {
        try {
            clienteService.crearCliente(
                    clienteDTO.getNombre(),
                    clienteDTO.getApellido(),
                    clienteDTO.getDni(),
                    clienteDTO.getEmail()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body("Cliente creado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> modificarCliente(@PathVariable Long id, @RequestBody ClienteDTO clienteDTO) {
        try {
            clienteService.modificarCliente(id, clienteDTO.getNombre(), clienteDTO.getApellido(), clienteDTO.getDni(), clienteDTO.getEmail());
            return ResponseEntity.ok("Cliente modificado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/agregar-tarjeta/{idCliente}")
    public ResponseEntity<String> agregarTarjeta(
            @PathVariable Long idCliente,
            @RequestBody TarjetaCreditoDTO tarjetaCreditoDTO) {
        try {
            // Llama al servicio pasando el número y la marca desde el DTO
            clienteService.agregarTarjeta(idCliente, tarjetaCreditoDTO.getNumero(), tarjetaCreditoDTO.getMarca());
            return ResponseEntity.ok("Tarjeta agregada exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody LoginRequestDTO loginRequest) {
        String email = loginRequest.getEmail();
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        Cliente cliente = clienteService.buscarClientePorEmail(email);
        ClienteDTO clienteDTO = clienteMapper.clienteToClienteDTO(cliente);

        if (clienteDTO != null) {
            return ResponseEntity.ok(clienteDTO);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> obtenerTodosLosClientes() {
        try {
            List<Cliente> clientes = clienteService.obtenerClientes();
            return ResponseEntity.ok(clientes.stream()
                    .map(clienteMapper::clienteToClienteDTO)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/listar-tarjetas/{clienteId}")
    public ResponseEntity<List<TarjetaCreditoDTO>> obtenerTarjetasCredito(@PathVariable Long clienteId) {
        List<TarjetaCredito> tarjetas = clienteService.listarTarjetas(clienteId);
        return ResponseEntity.ok(tarjetas.stream()
                .map(tarjetaCreditoMapper::tarjetaCreditoToTarjetaCreditoDTO)
                .collect(Collectors.toList()));
    }
}


