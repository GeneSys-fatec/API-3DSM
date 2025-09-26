package com.taskmanager.taskmngr_backend.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import com.taskmanager.taskmngr_backend.model.ColunaModel;

public interface ColunaRepository extends MongoRepository<ColunaModel, String> {
    @Query(value="{ 'proj_id' : ?0 }", sort="{ 'colOrdem' : 1 }")
    List<ColunaModel> findByProjIdOrderByColOrdemAsc(String proj_id);

    @Query(value="{ 'proj_id' : ?0 }", count = true)
    long countByProj_id(String proj_id);
}