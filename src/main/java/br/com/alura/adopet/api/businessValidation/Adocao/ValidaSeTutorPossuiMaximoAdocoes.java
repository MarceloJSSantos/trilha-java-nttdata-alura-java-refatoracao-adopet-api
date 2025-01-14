package br.com.alura.adopet.api.businessValidation.Adocao;

import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidaSeTutorPossuiMaximoAdocoes implements ValidacaoSolicitacaoAdocao {

    @Autowired
    private AdocaoRepository adocaoRepository;

    public void valida(Adocao adocao){
//        List<Adocao> adocoes = adocaoRepository.findAll();
//        for (Adocao a : adocoes) {
//            int contador = 0;
//            if (a.getTutor() == adocao.getTutor() && a.getStatus() == StatusAdocao.APROVADO) {
//                contador = contador + 1;
//            }
        int quantidadeAdocoesDoTutor = adocaoRepository.countByTutorIdAndStatus(
                adocao.getTutor().getId(), StatusAdocao.APROVADO);
        if (quantidadeAdocoesDoTutor == 5) {
            throw new ValidacaoException("Tutor chegou ao limite máximo de 5 adoções!");
        }
//        }
    }
}
