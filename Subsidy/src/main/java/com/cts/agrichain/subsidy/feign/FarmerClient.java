package com.cts.agrichain.subsidy.feign;

import com.cts.agrichain.subsidy.config.FeignConfig;
import com.cts.agrichain.subsidy.dto.FarmerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "IAM-FARMERREGISTRATION-SERVICE",
        fallback = FarmerClientFallback.class,
        configuration = FeignConfig.class  // ✅ ADD THIS LINE
)
public interface FarmerClient {

    @GetMapping("/farmers/{id}")
    FarmerDTO getFarmerById(@PathVariable("id") Long id);
}