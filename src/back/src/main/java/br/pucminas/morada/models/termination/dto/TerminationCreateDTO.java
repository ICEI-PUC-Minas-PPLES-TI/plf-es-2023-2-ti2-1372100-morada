package br.pucminas.morada.models.termination.dto;

import java.time.LocalDateTime;

import br.pucminas.morada.models.DTO;
import br.pucminas.morada.models.termination.Termination;
import jakarta.validation.constraints.NotNull;

public record TerminationCreateDTO (
    @NotNull Long rentalId,
    @NotNull Boolean initiated_by_owner,
    @NotNull String message,
    @NotNull LocalDateTime create


) implements DTO<Termination> {}