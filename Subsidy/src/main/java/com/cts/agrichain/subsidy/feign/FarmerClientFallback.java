package com.cts.agrichain.subsidy.feign;

import com.cts.agrichain.subsidy.dto.FarmerDTO;
import org.springframework.stereotype.Component;

/**
 * FALLBACK CLASS — runs when FARMER-SERVICE is down or circuit is OPEN.
 *
 * Instead of throwing an ugly error to the user, this returns a safe
 * dummy response so your subsidy service doesn't crash completely.
 *
 * Flow:
 *   Normal   → FarmerClient calls FARMER-SERVICE via Feign
 *   Failure  → After threshold, circuit OPENS → FarmerClientFallback runs
 *   Recovery → After wait time, circuit goes HALF-OPEN → retries real service
 */
@Component
public class FarmerClientFallback implements FarmerClient {

    @Override
    public FarmerDTO getFarmerById(Long id) {
        // ✅ Return a dummy FarmerDTO so the calling service knows farmer lookup failed
        // DisbursementService will check for this fallback flag and throw proper error
        FarmerDTO fallback = new FarmerDTO();
        fallback.setFarmerId(-1L); // sentinel value — means "farmer service unavailable"
        fallback.setFullName("SERVICE UNAVAILABLE");
        fallback.setEmail("unavailable@fallback.com");
        fallback.setMobileNumber("0000000000");
        return fallback;
    }
}
