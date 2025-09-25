package com.taskmanager.taskmngr_backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.taskmanager.taskmngr_backend.model.ColunaModel;

public interface ColunaRepository extends MongoRepository<ColunaModel, String>{
    List<ColunaModel>findAllByOrderByColOrdemAsc();
}