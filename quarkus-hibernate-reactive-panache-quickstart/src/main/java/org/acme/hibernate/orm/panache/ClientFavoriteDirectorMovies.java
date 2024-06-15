package org.acme.hibernate.orm.panache;

import java.util.List;

public record ClientFavoriteDirectorMovies(Client client, List<Movie> movies) {
    
}

