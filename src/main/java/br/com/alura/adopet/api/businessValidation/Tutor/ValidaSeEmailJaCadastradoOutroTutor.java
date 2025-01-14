package br.com.alura.adopet.api.businessValidation.Tutor;

import br.com.alura.adopet.api.dto.Tutor.AtualizaTutorDto;
import br.com.alura.adopet.api.dto.Tutor.CadastroTutorDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidaSeEmailJaCadastradoOutroTutor implements ValidacaoCadastroTutor, ValidacaoAtualizaTutor {

    @Autowired
    private TutorRepository repository;

    public void valida(CadastroTutorDto dto) {
        boolean emailJaCadastrado = repository.existsByEmail(dto.email());

        if (emailJaCadastrado) {
            throw new ValidacaoException("Email já cadastrado para outro tutor!");
        }
    }

    public void valida(AtualizaTutorDto dto) {
        boolean emailJaCadastrado = repository.existsByEmail(dto.email());

        if (emailJaCadastrado) {
            throw new ValidacaoException("Email já cadastrado para outro tutor!");
        }
    }
}
