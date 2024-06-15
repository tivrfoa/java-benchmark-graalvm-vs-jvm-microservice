package org.acme.reactive.crud;

import java.util.List;

public record ClientFavoriteDirectorMovies(Client client, List<Movie> movies) {
    
}

