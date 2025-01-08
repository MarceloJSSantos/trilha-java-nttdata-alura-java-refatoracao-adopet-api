package br.com.alura.adopet.api.businessValidation;

import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidaSeTutorAguardaAvaliacaoAdocao implements ValidaSolicitacaoAdocao{

    @Autowired
    private AdocaoRepository adocaoRepository;

    public void valida(Adocao adocao){
        boolean tutorAguardaAvaliacaoAdocao = adocaoRepository.existsByTutorIdAndStatus(
                adocao.getTutor().getId(), StatusAdocao.AGUARDANDO_AVALIACAO);
        if (tutorAguardaAvaliacaoAdocao) {
            throw new ValidacaoException("Tutor já possui outra adoção aguardando avaliação!");
        }
    }
}
