package br.com.sanches.process.infrastructure.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class ExceptionResponseDTO {

    private String message;

    private Integer statusCode;
}
