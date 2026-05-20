package com.librotech.chattech.repository;

import com.librotech.chattech.model.Mensaje;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MensajeRepository extends MongoRepository<Mensaje, String> {

    List<Mensaje> findTop10ByOrderByFechaEnvioDesc();
}