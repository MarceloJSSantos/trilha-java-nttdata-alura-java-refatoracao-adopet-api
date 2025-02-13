package br.com.alura.adopet.api.businessValidation.Adocao;

import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import org.springframework.stereotype.Component;

@Component
public class ValidaSePetFoiAdotado implements ValidacaoSolicitacaoAdocao {

    public void valida(Adocao adocao){
        if (adocao.getPet().getAdotado()) {
            throw new ValidacaoException("Pet já foi adotado!");
        }
    }
}
