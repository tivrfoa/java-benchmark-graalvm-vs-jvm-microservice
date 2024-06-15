package org.acme.hibernate.orm.panache;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Movie extends PanacheEntityBase {
    @Id
    private String title;
    private short year;
    private double cost;
    private String director;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public short getYear() {
        return year;
    }
    public void setYear(short year) {
        this.year = year;
    }
    public double getCost() {
        return cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }
    public String getDirector() {
        return director;
    }
    public void setDirector(String director) {
        this.director = director;
    }
    
}
