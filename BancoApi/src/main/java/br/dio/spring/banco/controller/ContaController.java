package br.dio.spring.banco.controller;

import br.dio.spring.banco.model.Cliente;
import br.dio.spring.banco.model.Conta;
import br.dio.spring.banco.service.ClienteService;
import br.dio.spring.banco.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("conta")
public class ContaController {

    @Autowired
    ContaService contaService;
    @Autowired
    ClienteService clienteService;

    @GetMapping("buscar")
    public ResponseEntity<List<Conta>> buscarContas(){
        return ResponseEntity.ok(contaService.contaFindAll());
    }

    @GetMapping("buscar/{numero}")
    public ResponseEntity<Object> buscaPorId(@PathVariable Long numero){
        Optional<Conta> buscaConta = contaService.contaFindById(numero);
        if(buscaConta.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(buscaConta);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta nao encontrada");
        }
    }

    @PostMapping("novo/{idCliente}")
    public ResponseEntity<Object> cadastrarConta(@PathVariable Long idCliente, @RequestBody Conta conta){
        Optional<Cliente> buscaCliente = clienteService.clienteFindById(idCliente);

        if(buscaCliente.isPresent()) {
            conta.setCliente(buscaCliente.get());
            contaService.contaSave(conta);
            return ResponseEntity.status(HttpStatus.OK).body(conta);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente nao encontrado");
        }
    }

    @PutMapping("editar/{numero}")
    public ResponseEntity<Object> atualizarConta(@PathVariable Long numero, @RequestBody Conta conta){
        Optional<Conta> buscaConta = contaService.contaFindById(numero);
        if(buscaConta.isPresent()){
            conta.setNumero(numero);
            conta.setCliente(buscaConta.get().getCliente());
            contaService.contaSave(conta);
            return ResponseEntity.status(HttpStatus.OK).body(conta);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta nao encontrada");
        }
    }

    @PostMapping("transcao/saque/{contaId}/{valor}")
    public ResponseEntity<Object> sacarValor(@PathVariable Long contaId, double valor){
        Optional<Conta> buscaConta = contaService.contaFindById(contaId);

        if(buscaConta.get().getSaldo() < 0 || valor > buscaConta.get().getSaldo()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Saldo insuficiente para retirada");
        }
        else{
            buscaConta.get().setSaldo(buscaConta.get().getSaldo() - valor);
            contaService.contaSave(buscaConta.get());
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }

    @PostMapping("transcao/deposito/{contaId}/{valor}")
    public ResponseEntity<Object> depositarValor(@PathVariable Long contaId, double valor){
        Optional<Conta> buscaConta = contaService.contaFindById(contaId);

        buscaConta.get().setSaldo(buscaConta.get().getSaldo() + valor);
        contaService.contaSave(buscaConta.get());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("transacao/transferencia/{contaOrigemId}/{contaDestinoId}/{valor}")
    public ResponseEntity<Object> transferirValor(@PathVariable Long contaOrigemId, Long contaDestinoId, double valor){
        ResponseEntity<Object> saque = sacarValor(contaOrigemId, valor);

        if(saque.getStatusCode().is2xxSuccessful())
            return depositarValor(contaDestinoId, valor);
        return saque;
    }
}
