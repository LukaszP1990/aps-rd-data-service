package com.advanced.protection.systems.multisensor.rfdata.dao;

import com.advanced.protection.systems.multisensor.rfdata.domain.RfDataDocument;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Repository
public interface RfDataRepository extends ReactiveMongoRepository<RfDataDocument, String> {

    @Query("{'sensor.name': ?0}")
    Mono<RfDataDocument> findBySensorName(String name);

    @Query("{$and : [" +
            "{ 'rssi' : ?0 } , " +
            "{ 'timeAdded' : { $gte: ?1, $lte: ?2 } } , " +
            "{ 'sensor.name' : ?3 } , " +
            "]} ")
    Flux<RfDataDocument> findByDataParam(double rssi,
                                         Date timeAddedFrom,
                                         Date timeAddedTo,
                                         String name);
}
