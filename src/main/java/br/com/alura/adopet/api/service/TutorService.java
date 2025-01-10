package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.businessValidation.Tutor.ValidaAtualizaTutor;
import br.com.alura.adopet.api.businessValidation.Tutor.ValidaCadastroTutor;
import br.com.alura.adopet.api.dto.Tutor.AtualizaTutorDto;
import br.com.alura.adopet.api.dto.Tutor.CadastroTutorDto;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TutorService {

    @Autowired
    private TutorRepository repository;

    @Autowired
    private List<ValidaCadastroTutor> validacoesCadastro;

    @Autowired
    private List<ValidaAtualizaTutor> validacoesAtualizacao;

    public void cadastrar(CadastroTutorDto dto){

        validacoesCadastro.forEach(v -> v.valida(dto));

        Tutor tutor = new Tutor(dto.nome(), dto.telefone(), dto.email());
        repository.save(tutor);
    }

    public void atualizar(AtualizaTutorDto dto){
        boolean nomeNaoNuloENaoVazio = dto.nome() != null && !dto.nome().isEmpty();
        boolean telefoneNaoNulo = dto.telefone() != null;
        boolean emailNaoNuloENaoVazio = dto.email() != null && !dto.email().isEmpty();

        Tutor tutor = repository.getReferenceById(dto.id());

        validacoesAtualizacao.forEach(v -> v.valida(dto));

        if (nomeNaoNuloENaoVazio){tutor.setNome(dto.nome());}
        if(telefoneNaoNulo){tutor.setTelefone(dto.telefone());}
        if (emailNaoNuloENaoVazio){tutor.setEmail(dto.email());}
    }
}
