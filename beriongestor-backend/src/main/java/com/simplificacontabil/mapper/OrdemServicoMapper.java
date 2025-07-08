package com.simplificacontabil.mapper;

import com.simplificacontabil.dto.OrdemServicoDTO;
import com.simplificacontabil.model.OrdemServico;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OrdemServicoMapper {

    @Mappings({
            @Mapping(source = "cliente.id",    target = "clienteId"),
    })
    OrdemServicoDTO toDTO(OrdemServico os);


    @Mapping(target = "cliente", ignore = true)
    OrdemServico toEntity(OrdemServicoDTO dto);

    // helper padrão: mapeia e já seta o cliente
    default OrdemServico toEntity(OrdemServicoDTO dto, com.simplificacontabil.model.Cliente cliente) {
        OrdemServico os = toEntity(dto);
        os.setCliente(cliente);
        return os;
    }
}
