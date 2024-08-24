package org.acme;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record ClientFavoriteDirectorMovies(Client client, List<Movie> movies) {
    
}
