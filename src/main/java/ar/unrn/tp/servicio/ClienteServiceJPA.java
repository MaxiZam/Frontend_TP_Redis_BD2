package ar.unrn.tp.servicio;

import ar.unrn.tp.api.ClienteService;
import ar.unrn.tp.mapper.ClienteMapper;
import ar.unrn.tp.modelo.Cliente;
import ar.unrn.tp.modelo.TarjetaCredito;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ClienteServiceJPA implements ClienteService {

    @PersistenceContext
    private EntityManager em;

    private ClienteMapper clienteMapper;

    @Override
    public List<Cliente> obtenerClientes() {
        return em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
    }

    @Override
    public void crearCliente(String nombre, String apellido, String dni, String email) {
        if (em.createQuery("SELECT c FROM Cliente c WHERE c.dni = :dni")
                .setParameter("dni", dni)
                .getResultList()
                .isEmpty()) {
            Cliente cliente = new Cliente();
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setDni(dni);
            cliente.setEmail(email);

            em.persist(cliente);
            em.flush();
        } else {
            throw new IllegalArgumentException("Ya existe un cliente con ese DNI");
        }
    }

    @Override
    public void modificarCliente(Long idCliente, String nombre, String apellido, String dni, String email) {
        Cliente cliente = em.find(Cliente.class, idCliente);
        if (cliente == null) {
            throw new IllegalArgumentException("No se encontró un cliente con el ID proporcionado");
        }

        if (!cliente.getDni().equals(dni)) {
            if (!em.createQuery("SELECT c FROM Cliente c WHERE c.dni = :dni AND c.id != :id")
                    .setParameter("dni", dni)
                    .setParameter("id", idCliente)
                    .getResultList()
                    .isEmpty()) {
                throw new IllegalArgumentException("Ya existe otro cliente con ese DNI");
            }
        }

        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setDni(dni);
        cliente.setEmail(email);

        em.merge(cliente);
    }

    @Override
    public void agregarTarjeta(Long idCliente, String nro, String marca) {
        Cliente cliente = em.find(Cliente.class, idCliente);
        if (cliente == null) {
            throw new IllegalArgumentException("No se encontró un cliente con el ID proporcionado");
        }

        if (!em.createQuery("SELECT t FROM TarjetaCredito t WHERE t.numero = :nro")
                .setParameter("nro", nro)
                .getResultList()
                .isEmpty()) {
            throw new IllegalArgumentException("Ya existe una tarjeta con ese número");
        }

        TarjetaCredito tarjeta = new TarjetaCredito(nro, marca, LocalDate.now(), cliente);
        cliente.agregarTarjeta(tarjeta);
        em.merge(cliente);
    }

    @Override
    public List<TarjetaCredito> listarTarjetas(Long idCliente) {
        Cliente cliente = em.find(Cliente.class, idCliente);
        if (cliente == null) {
            throw new IllegalArgumentException("No se encontró un cliente con el ID proporcionado");
        }

        return em.createQuery("SELECT t FROM TarjetaCredito t WHERE t.cliente.id = :idCliente", TarjetaCredito.class)
                .setParameter("idCliente", idCliente)
                .getResultList();
    }

    @Override
    public Cliente buscarClientePorEmail(String email) { //luego implementar lo de emial y contraseña
        try {
            return em.createQuery("SELECT c FROM Cliente c WHERE c.email = :email", Cliente.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}



