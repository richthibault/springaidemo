package com.exquisiteloop.springaidemo.model;

public enum Company {

	CRUMBOLOGY("crumbology","Crumbology Bakery Co."),
	ORBITAL("orbital","Orbital Synergy Systems");

	private final String id;
	private final String name;

	Company(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
