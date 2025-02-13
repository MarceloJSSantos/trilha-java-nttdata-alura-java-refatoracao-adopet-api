package br.com.alura.adopet.api.businessValidation.Adocao;

import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidaSePetAguardaAvaliacaoAdocao implements ValidacaoSolicitacaoAdocao {

    @Autowired
    private AdocaoRepository adocaoRepository;

    public void valida(Adocao adocao){
        boolean petTemAdocaoEmAdamento = adocaoRepository.existsByPetIdAndStatus(
                adocao.getPet().getId(), StatusAdocao.AGUARDANDO_AVALIACAO);
        if (petTemAdocaoEmAdamento) {
            throw new ValidacaoException("Pet já está aguardando avaliação para ser adotado!");
        }
    }
}
