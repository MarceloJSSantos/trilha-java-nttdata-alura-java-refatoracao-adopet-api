package br.com.alura.adopet.api.businessValidation.Abrigo;

import br.com.alura.adopet.api.dto.Abrigo.CadastroAbrigoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidaSeTelefoneJaCadastradoOutroAbrigo implements ValidacaoCadastroAbrigo {

    @Autowired
    private AbrigoRepository repository;

    public void valida(CadastroAbrigoDto dto) {
        boolean telefoneJaCadastrado = repository.existsByTelefone(dto.telefone());

        if (telefoneJaCadastrado) {
            throw new ValidacaoException("Telefone já cadastrado para outro abrigo!");
        }
    }
}
