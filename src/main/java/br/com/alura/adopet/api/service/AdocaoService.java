package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.businessValidation.Adocao.ValidacaoSolicitacaoAdocao;
import br.com.alura.adopet.api.dto.Adocao.AprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.Adocao.ReprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.Adocao.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private List<ValidacaoSolicitacaoAdocao> validacoes;

    public void solicitar(SolicitacaoAdocaoDto dto){
        var pet = petRepository.getReferenceById(dto.idPet());
        var tutor = tutorRepository.getReferenceById(dto.idTutor());

        var adocao = new Adocao(tutor, pet, dto.motivo());

        validacoes.forEach(v -> v.valida(adocao));

        repository.save(adocao);

        var toEmail = adocao.getPet().getAbrigo().getEmail();
        emailService.encaminhaEmail(toEmail, "Solicitação de adoção", getTextMessageRegistroAdocao(adocao));
    }

    public void aprovar(AprovacaoAdocaoDto dto){
        var adocao = repository.getReferenceById(dto.idAdocao());
        adocao.registraStatusAprovado();

        var toEmail = adocao.getPet().getAbrigo().getEmail();
        emailService.encaminhaEmail(toEmail, "Adoção aprovada", getMessageAprovaAdocao(adocao));
    }

    public void reprovar(ReprovacaoAdocaoDto dto){
        var adocao = repository.getReferenceById(dto.idAdocao());
        adocao.registraStatusReprovado(dto.justificativaStatus());

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
