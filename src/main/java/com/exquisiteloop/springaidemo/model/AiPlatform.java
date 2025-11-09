package com.exquisiteloop.springaidemo.model;

public enum AiPlatform {

	AZURE_OPENAI("Azure OpenAI"),
	PERPLEXITY("Perplexity");

	private String displayName;

	AiPlatform(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

}
