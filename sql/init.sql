drop table if exists movie;

CREATE TABLE movie (
    title text not null,
    year smallint,
    cost numeric(9, 2),
    director text
);

insert into movie values
('Star Wars: The Force Awakens',  2015, 447, 'J J Abrams'),
('Jurassic World: Fallen Kingdom',  2018, 432, 'J A Bayona'),
('Star Wars: The Rise of Skywalker',  2019, 416, 'J J Abrams'),
('Fast X',  2023, 379, 'Louis Leterrier'),
('Pirates of the Caribbean: On Stranger Tides',  2011, 379, 'Rob Marshall'),
('Avengers: Age of Ultron',  2015, 365, 'Joss Whedon'),
('Avengers: Endgame',  2019, 356, 'Anthony Russo & Joe Russo'),
('Doctor Strange in the Multiverse of Madness',  2022, 351, 'Sam Raimi'),
('Avatar: The Way of Water',  2022, 350, 'James Cameron'),
('Indiana Jones and the Dial of Destiny',  2023, 326, 'James Mangold'),
('Avengers: Infinity War',  2018, 325, 'Anthony Russo & Joe Russo'),
('Pirates of the Caribbean: At World''s End',  2007, 300, 'Gore Verbinski'),
('Justice League',  2017, 300, 'Zack Snyder & Joss Whedon'),
('Star Wars: The Last Jedi',  2017, 300, 'Rian Johnson'),
('Mission: Impossible – Dead Reckoning Part One',  2023, 291, 'Christopher McQuarrie');

select director, count(*)
from movie
group by director;
