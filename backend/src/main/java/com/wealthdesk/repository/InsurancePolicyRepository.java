package com.wealthdesk.repository;

import com.wealthdesk.document.InsurancePolicy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsurancePolicyRepository extends MongoRepository<InsurancePolicy, String> {

    List<InsurancePolicy> findByUserId(Long userId);

    List<InsurancePolicy> findByUserIdAndStatus(Long userId, InsurancePolicy.PolicyStatus status);

    List<InsurancePolicy> findByUserIdAndType(Long userId, InsurancePolicy.PolicyType type);
}