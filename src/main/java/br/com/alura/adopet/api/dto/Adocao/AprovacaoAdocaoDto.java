package br.com.alura.adopet.api.dto.Adocao;

import jakarta.validation.constraints.NotNull;

public record AprovacaoAdocaoDto(@NotNull Long idAdocao) {
}
