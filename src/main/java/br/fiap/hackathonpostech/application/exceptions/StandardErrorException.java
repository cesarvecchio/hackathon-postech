package br.fiap.hackathonpostech.application.exceptions;

import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class StandardErrorException {
    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}
