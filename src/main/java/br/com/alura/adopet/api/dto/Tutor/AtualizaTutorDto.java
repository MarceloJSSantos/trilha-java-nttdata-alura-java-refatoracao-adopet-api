package br.com.alura.adopet.api.dto.Tutor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AtualizaTutorDto(
        @NotNull
        Long id,

        String nome,

        @Pattern(regexp = "\\(?\\d{2}\\)?\\d?\\d{4}-?\\d{4}")
        String telefone,

        @Email
        String email) {
}
