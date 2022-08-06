package br.dio.spring.banco.service;

import br.dio.spring.banco.model.Conta;
import br.dio.spring.banco.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContaService {

    @Autowired
    ContaRepository contaRepository;

    public List<Conta> contaFindAll(){
        return contaRepository.findAll();
    }
    public Optional<Conta> contaFindById(Long numero){
        return contaRepository.findById(numero);
    }
    public void contaSave(Conta conta){
        contaRepository.save(conta);
    }

}
