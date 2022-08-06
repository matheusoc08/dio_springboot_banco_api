package br.dio.spring.banco.controller;

import br.dio.spring.banco.model.Cliente;
import br.dio.spring.banco.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("cliente")
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    @GetMapping("buscar")
    public ResponseEntity<List<Cliente>> buscarClientes(){
        return ResponseEntity.ok(clienteService.clienteFindAll());
    }

    @GetMapping("buscar/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable Long id){
        Optional<Cliente> buscaCliente = clienteService.clienteFindById(id);
        if(buscaCliente.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(buscaCliente);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente nao encontrado");
        }
    }

    @PostMapping("novo")
    public ResponseEntity<Cliente> cadastrarCliente(@RequestBody Cliente cliente){
        clienteService.clienteSave(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @PutMapping("editar/{id}")
    public ResponseEntity<Object> atualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente){

        Optional<Cliente> buscaCliente = clienteService.clienteFindById(id);

        if(buscaCliente.isPresent()) {
            cliente.setId(id);
            clienteService.clienteSave(cliente);
            return ResponseEntity.status(HttpStatus.OK).body(cliente);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente nao encontrado");
        }
    }

}
