package dev.denisarruda.marketingnarrative.marketing.entity;

import dev.langchain4j.model.output.structured.Description;

public record MarketingResult(
        @Description("Global marketing headline") String headlineGlobal,
        @Description("Region-adapted headline for Germany") String headlineDE,
        @Description("Short punchy tagline") String tagline,
        @Description("Short promotional description, max two sentences") String shortDescription) {
}
