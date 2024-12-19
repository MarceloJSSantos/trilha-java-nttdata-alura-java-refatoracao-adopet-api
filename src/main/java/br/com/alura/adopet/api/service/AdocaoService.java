package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.ReprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AdocaoService {

    @Autowired
    private AdocaoRepository repository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private EmailService emailService;

    public void solicitar(SolicitacaoAdocaoDto dto){
        var pet = petRepository.getReferenceById(dto.idPet());
        var tutor = tutorRepository.getReferenceById(dto.idTutor());

        var adocao = new Adocao();
        adocao.setPet(pet);
        adocao.setTutor(tutor);
        adocao.setData(LocalDateTime.now());
        adocao.setStatus(StatusAdocao.AGUARDANDO_AVALIACAO);
        adocao.setMotivo(dto.motivo());

        if (adocao.getPet().getAdotado()) {
            throw new ValidacaoException("Pet já foi adotado!");
        } else {
            List<Adocao> adocoes = repository.findAll();
            for (Adocao a : adocoes) {
                if (a.getTutor() == adocao.getTutor() && a.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO) {
                    throw new ValidacaoException("Tutor já possui outra adoção aguardando avaliação!");
                }
            }
            for (Adocao a : adocoes) {
                if (a.getPet() == adocao.getPet() && a.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO) {
                    throw new ValidacaoException("Pet já está aguardando avaliação para ser adotado!");
                }
            }
            for (Adocao a : adocoes) {
                int contador = 0;
                if (a.getTutor() == adocao.getTutor() && a.getStatus() == StatusAdocao.APROVADO) {
                    contador = contador + 1;
                }
                if (contador == 5) {
                    throw new ValidacaoException("Tutor chegou ao limite máximo de 5 adoções!");
                }
            }
        }

        repository.save(adocao);

        var toEmail = adocao.getPet().getAbrigo().getEmail();
        emailService.encaminhaEmail(toEmail, "Solicitação de adoção", getTextMessageRegistroAdocao(adocao));
    }

    public void aprovar(AprovacaoAdocaoDto dto){
        var adocao = repository.getReferenceById(dto.idAdocao());
        adocao.setStatus(StatusAdocao.APROVADO);

        var toEmail = adocao.getPet().getAbrigo().getEmail();
        emailService.encaminhaEmail(toEmail, "Adoção aprovada", getMessageAprovaAdocao(adocao));
    }

    public void reprovar(ReprovacaoAdocaoDto dto){
        var adocao = repository.getReferenceById(dto.idAdocao());
        adocao.setJustificativaStatus(dto.justificativaStatus());
        adocao.setStatus(StatusAdocao.REPROVADO);

        var toEmail = adocao.getPet().getAbrigo().getEmail();
        emailService.encaminhaEmail(toEmail, "Adoção reprovada", getMessageAdocaoReprovada(adocao));
    }

    private static String getTextMessageRegistroAdocao(Adocao adocao) {
        return """
                Olá %s!
                
                Uma solicitação de adoção foi registrada hoje para o pet: %s.
                Favor avaliar para aprovação ou reprovação.
                """.formatted(
                adocao.getPet().getAbrigo().getNome(),
                adocao.getPet().getNome()
        );
    }

    private static String getMessageAprovaAdocao(Adocao adocao) {
        return """
                Parabéns %s!
                
                Sua adoção do pet %s, solicitada em %s, foi aprovada.
                favor entrar em contato com o abrigo %s para agendar a busca do seu pet.
                """.formatted(
                adocao.getTutor().getNome(),
                adocao.getPet().getNome(),
                adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                adocao.getPet().getAbrigo().getNome()
        );
    }

    private static String getMessageAdocaoReprovada(Adocao adocao) {
        return """
                Olá %s!
                
                Infelizmente sua adoção do pet %s, solicitada em %s, foi reprovada pelo abrigo %s com a seguinte justificativa: %s.
                """.formatted(
                adocao.getTutor().getNome(),
                adocao.getPet().getNome(),
                adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                adocao.getPet().getAbrigo().getNome(),
                adocao.getJustificativaStatus()
        );
    }
}
