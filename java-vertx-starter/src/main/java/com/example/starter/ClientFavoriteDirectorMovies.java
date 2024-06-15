package com.example.starter;

import java.util.List;

public record ClientFavoriteDirectorMovies(Client client, List<Movie> movies) {
    
}
