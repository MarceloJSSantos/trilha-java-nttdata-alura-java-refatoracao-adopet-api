package br.com.alura.adopet.api.businessValidation.Tutor;

import br.com.alura.adopet.api.dto.Tutor.AtualizaTutorDto;
import br.com.alura.adopet.api.dto.Tutor.CadastroTutorDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidaSeTelefoneJaCadastradoOutroTutor implements ValidaCadastroTutor, ValidaAtualizaTutor {

    @Autowired
    private TutorRepository repository;

    public void valida(CadastroTutorDto dto) {
        boolean telefoneJaCadastrado = repository.existsByTelefone(dto.telefone());

        if (telefoneJaCadastrado) {
            throw new ValidacaoException("Telefone já cadastrado para outro tutor!");
        }
    }

    public void valida(AtualizaTutorDto dto) {
        boolean telefoneJaCadastrado = repository.existsByTelefone(dto.telefone());

        if (telefoneJaCadastrado) {
            throw new ValidacaoException("Telefone já cadastrado para outro tutor!");
        }
    }
}
