package com.taskmanager.taskmngr_backend.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.taskmanager.taskmngr_backend.model.entidade.ColunaModel;

public interface ColunaRepository extends MongoRepository<ColunaModel, String> {
    @Query(value="{ 'projId' : ?0 }", sort="{ 'colOrdem' : 1 }")
    List<ColunaModel> findByProjIdOrderByColOrdemAsc(String projId);

    @Query(value="{ 'projId' : ?0 }", count = true)
    long countByprojId(String projId);

    long countByProjId(String projId);

    long countByProjIdAndColTitulo(String projId, String colTitulo);

    long countByProjIdAndColTituloAndColIdNot(String projId, String colTitulo, String colId);

    boolean existsByProjIdAndColTitulo(String projId, String colTitulo);
}