package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.Tutor.AtualizaTutorDto;
import br.com.alura.adopet.api.dto.Tutor.CadastroTutorDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TutorService {
    @Autowired
    private TutorRepository repository;

    public void cadastrar(CadastroTutorDto dto){
        boolean telefoneJaCadastrado = repository.existsByTelefone(dto.telefone());
        boolean emailJaCadastrado = repository.existsByEmail(dto.email());

        if (telefoneJaCadastrado || emailJaCadastrado) {
            throw new ValidacaoException("Dados já cadastrados para outro tutor!");
        } else {
            Tutor tutor = new Tutor();
            tutor.setNome(dto.nome());
            tutor.setTelefone(dto.telefone());
            tutor.setEmail(dto.email());
            repository.save(tutor);
        }
    }

    public void atualizar(AtualizaTutorDto dto){
        Tutor tutor = repository.getReferenceById(dto.id());
        if (dto.nome() != null && !dto.nome().isEmpty()){
            tutor.setNome(dto.nome());
        }
        if(dto.telefone() != null){
            tutor.setTelefone(dto.telefone());
        }
        if (dto.email() != null && !dto.email().isEmpty()){
            tutor.setEmail(dto.email());
        }
    }
}
