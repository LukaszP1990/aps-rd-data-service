package com.advanced.protection.systems.multisensor.rfdata.dao;

import com.advanced.protection.systems.multisensor.rfdata.domain.ErrorDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Date;

@Repository
public interface ErrorRepository extends ReactiveMongoRepository<ErrorDocument, String> {

    Flux<ErrorDocument> findByTextAndTimeAddedBetween(String text,
													  Date timeAddedFrom,
													  Date timeAddedTo);
}

