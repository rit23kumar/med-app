package com.medapp.dto;

import lombok.Data;
import java.util.List;

@Data
public class BatchMedicineResponse {
    private List<MedicineDto> successfulMedicines;
    private List<FailedMedicine> failedMedicines;
    private int totalSuccess;
    private int totalFailed;

    @Data
    public static class FailedMedicine {
        private String name;
        private String reason;

        public FailedMedicine(String name, String reason) {
            this.name = name;
            this.reason = reason;
        }
    }
} 