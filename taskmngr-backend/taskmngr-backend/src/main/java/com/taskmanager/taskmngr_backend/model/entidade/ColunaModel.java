package com.taskmanager.taskmngr_backend.model.entidade;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import lombok.Data;

@Document(collection = "colunas")
@CompoundIndexes({
    @CompoundIndex(name = "projid_coltitulo_unique", def = "{'projId': 1, 'colTitulo': 1}", unique = true)
})
@Data
public class ColunaModel {
    @Id
    private String colId;
    private String colTitulo;
    private Integer colOrdem;
    private String projId;
}