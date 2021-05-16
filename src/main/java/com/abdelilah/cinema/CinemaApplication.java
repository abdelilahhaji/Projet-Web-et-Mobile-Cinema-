package com.abdelilah.cinema;

import com.abdelilah.cinema.service.IcinemaInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CinemaApplication implements CommandLineRunner {
    @Autowired
    private IcinemaInitService icinemaInitService;

    public static void main(String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        icinemaInitService.initVilles();
        icinemaInitService.initCinemas();
        icinemaInitService.initSalles();
        icinemaInitService.initPlaces();
        icinemaInitService.initSeances();
        icinemaInitService.initCategories();
        icinemaInitService.initFilms();
        icinemaInitService.initProjections();
        icinemaInitService.initTickets();
    }
}
