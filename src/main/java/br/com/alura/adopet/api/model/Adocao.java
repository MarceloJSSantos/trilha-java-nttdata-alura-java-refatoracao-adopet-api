package br.com.alura.adopet.api.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "adocoes")
public class Adocao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(name = "id")
      retirado pois a convensão da JPA já define o nome da coluna como o atributo.
      As demais linhas serão apagadas
     */
    private Long id;

    private LocalDateTime data;

    @ManyToOne(fetch = FetchType.LAZY) // fetch = FetchType.LAZY: Otimiza a consulta para só carregar quando for necessário
    /*@JsonBackReference("tutor_adocoes")
      Só é necessário quando estamos devolvendo entidade, desnecessário caso usemos o padrão DTO
     */
    /*@JoinColumn(name = "tutor_id")
      retirado pois por convensão nos relacionamentos o nome da coluna será
      <nome-da-coluna-tabela-atual>_<nome-coluna-tabela-relacionada>
    */
    private Tutor tutor;

    @OneToOne(fetch = FetchType.LAZY)
    /*@JsonManagedReference("adocao_pets")
      Só é necessário quando estamos recebendo entidade, desnecessário caso usemos o padrão DTO
     */
    private Pet pet;

    private String motivo;

    @Enumerated(EnumType.STRING)
    private StatusAdocao status;

    /*@Column(name = "justificativa_status")
    Já é convesão separar palavras compostas por '_'
     */
    private String justificativaStatus;

    public Adocao() {}

    public Adocao(Tutor tutor, Pet pet, String motivo) {
        this.tutor = tutor;
        this.pet = pet;
        this.motivo = motivo;
        this.data = LocalDateTime.now();
        this.status = StatusAdocao.AGUARDANDO_AVALIACAO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adocao adocao = (Adocao) o;
        return Objects.equals(id, adocao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public Pet getPet() {
        return pet;
    }

    public String getMotivo() {
        return motivo;
    }

    public StatusAdocao getStatus() {
        return status;
    }

    public String getJustificativaStatus() {
        return justificativaStatus;
    }

    public void registraStatusAprovado() {
        this.status = StatusAdocao.APROVADO;
    }

    public void registraStatusReprovado(String justificativaStatus) {
        this.status = StatusAdocao.REPROVADO;
        this.justificativaStatus = justificativaStatus;
    }
}
