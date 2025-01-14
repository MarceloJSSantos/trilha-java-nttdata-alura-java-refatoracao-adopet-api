package br.com.alura.adopet.api.businessValidation.Abrigo;

import br.com.alura.adopet.api.dto.Abrigo.CadastroAbrigoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidaSeEmailJaCadastradoOutroAbrigo implements ValidacaoCadastroAbrigo {

    @Autowired
    private AbrigoRepository repository;

    public void valida(CadastroAbrigoDto dto) {
        boolean emailJaCadastrado = repository.existsByEmail(dto.email());

        if (emailJaCadastrado) {
            throw new ValidacaoException("Email já cadastrado para outro abrigo!");
        }
    }
}
