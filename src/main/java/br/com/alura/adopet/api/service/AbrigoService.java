package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.businessValidation.Abrigo.ValidacaoCadastroAbrigo;
import br.com.alura.adopet.api.dto.Abrigo.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.Pet.CadastroPetDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbrigoService {

    @Autowired
    private AbrigoRepository repository;

    @Autowired
    private List<ValidacaoCadastroAbrigo> validacoesCadastro;

    public List<Abrigo> listar() {
        return repository.findAll();
    }

    public void cadastrar(CadastroAbrigoDto dto){

        validacoesCadastro.forEach(v -> v.valida(dto));

        var abrigo = new Abrigo(dto.nome(), dto.telefone(), dto.email());
        repository.save(abrigo);
    }

    public void cadastrarPet(String idOuNome, CadastroPetDto dto){
        try {
            Long id = Long.parseLong(idOuNome);
            Abrigo abrigo = repository.getReferenceById(id);
            var pet = new Pet(dto, abrigo, false);
            abrigo.getPets().add(pet);
            repository.save(abrigo);
        } catch (NumberFormatException nfe) {
                Abrigo abrigo = repository.findByNome(idOuNome);
                if (abrigo != null){
                    var pet = new Pet(dto, abrigo, false);
                    abrigo.getPets().add(pet);
                    repository.save(abrigo);
                } else {
                    throw new EntityNotFoundException();
                }
        }
    }

    public List<Pet> listarPets(String idOuNome){
        List<Pet> pets;
        try {
            Long id = Long.parseLong(idOuNome);
            pets = repository.getReferenceById(id).getPets();
        } catch (NumberFormatException e) {
            pets = repository.findByNome(idOuNome).getPets();
        }
        return pets;
    }
}
