package br.dio.spring.banco.service;

import br.dio.spring.banco.model.Cliente;
import br.dio.spring.banco.repository.ClienteRepository;
import feign.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    ClienteRepository clienteRepository;

    public List<Cliente> clienteFindAll(){
        return clienteRepository.findAll();
    }

    public Optional<Cliente> clienteFindById(Long id){
        return clienteRepository.findById(id);
    }

    public void clienteSave(Cliente cliente){
        clienteRepository.save(cliente);
    }

    public void clienteDeactivate(Long id){
        clienteRepository.deleteById(id);
    }


}
