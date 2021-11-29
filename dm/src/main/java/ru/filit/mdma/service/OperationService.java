package ru.filit.mdma.service;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.filit.mdma.model.entity.Operation;
import ru.filit.mdma.repository.OperationRepository;
import ru.filit.mdma.web.dto.OperationDto;
import ru.filit.mdma.web.dto.OperationSearchDto;
import ru.filit.mdma.web.exception.InvalidDataException;
import ru.filit.mdma.web.mapping.DtoMapper;

/**
 * @author A.Khalitova 29-Nov-2021
 */
@Service
public class OperationService {
  private final OperationRepository operationRepository;

  public OperationService(OperationRepository operationRepository) {
    this.operationRepository = operationRepository;
  }

  public List<OperationDto> getAccountOperations(
      OperationSearchDto operationSearchDto) {
    String accountNumber = operationSearchDto.getAccountNumber();
    if (accountNumber == null || accountNumber.isBlank()) {
      throw new InvalidDataException("Отсутствует номер счета");
    }
    final List<Operation> operations = operationRepository.getOperations(accountNumber,
        Integer.parseInt(operationSearchDto.getQuantity()));
    return operations.stream()
        .map(DtoMapper.INSTANCE::operationToOperationDto)
        .toList();
  }
}
