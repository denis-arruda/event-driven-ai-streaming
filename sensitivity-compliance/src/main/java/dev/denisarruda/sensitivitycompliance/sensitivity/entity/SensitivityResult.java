package dev.denisarruda.sensitivitycompliance.sensitivity.entity;

import dev.langchain4j.model.output.structured.Description;

import java.util.List;

public record SensitivityResult(
        @Description("Suggested age rating, e.g. 16+") String ageRatingSuggested,
        @Description("ISO 3166-1 alpha-2 region codes with restrictions") List<String> sensitiveRegions,
        @Description("List of sensitive content flags") List<String> riskFlags) {
}
