package org.acme;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record Movie(String title, short year, double cost, String director) {};