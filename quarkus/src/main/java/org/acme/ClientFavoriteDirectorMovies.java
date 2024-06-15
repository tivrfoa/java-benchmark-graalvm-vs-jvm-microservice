package org.acme;

import java.util.List;

public record ClientFavoriteDirectorMovies(Client client, List<Movie> movies) {
    
}
