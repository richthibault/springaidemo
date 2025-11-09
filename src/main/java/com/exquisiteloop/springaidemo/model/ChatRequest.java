package com.exquisiteloop.springaidemo.model;

public record ChatRequest(AiPlatform platform, String message, String ragCompanyId) {

}
