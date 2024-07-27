package br.fiap.hackathonpostech.application.validatons;

import lombok.Builder;

@Builder
public record ValidationError(String field, String errorMessage) {
}
