package com.prateek.addausers.addaUsers.addaElasticSearch.elasticData;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UsersElasticRepository extends ElasticsearchRepository<UsersElasticModel,String> {
}
