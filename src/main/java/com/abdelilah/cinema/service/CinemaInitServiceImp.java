package com.abdelilah.cinema.service;

import com.abdelilah.cinema.dao.*;
import com.abdelilah.cinema.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Service
@Transactional
public class CinemaInitServiceImp implements IcinemaInitService{
    @Autowired
    private VilleRepository villeRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private SalleRepository salleRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private SeanceRepository seanceRepository;
    @Autowired
    private CategorieRepository categorieRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private ProjectionRepository projectionRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public void initVilles() {
        Stream.of("Casablanca","Rabat","Tanger","Dakhla","Oujda").forEach(villeName->{
            Ville ville=new Ville();
            ville.setName(villeName);
            villeRepository.save(ville);
        });
    }

    @Override
    public void initCinemas() {
        villeRepository.findAll().forEach(ville -> {
            Stream.of("MegaRama","IMAX","CHAHRAZAD","RIF","NAJAH").forEach(nameCinema->{
                Cinema cinema=new Cinema();
                cinema.setName(nameCinema);
                cinema.setNombreSalles((int) (5+Math.random()*10));
                cinema.setVille(ville);
                cinemaRepository.save(cinema);
            });
        });

    }

    @Override
    public void initSalles() {
        cinemaRepository.findAll().forEach(cinema -> {
            for(int i=0;i<cinema.getNombreSalles();i++){
                Salle salle=new Salle();
                salle.setCinema(cinema);
                salle.setName("S"+(i+1));
                salle.setNombrePlace((int) (30+Math.random()*20));
                salleRepository.save(salle);
            }
        });

    }

    @Override
    public void initPlaces() {
        salleRepository.findAll().forEach(salle-> {
            for(int i=0;i< salle.getNombrePlace();i++){
                Place place=new Place();
                place.setSalle(salle);
                place.setNumero(i+1);
                placeRepository.save(place);
            }
        });


    }

    @Override
    public void initSeances() {
        DateFormat dateFormat=new SimpleDateFormat("HH:mm");
        Stream.of("08:00","10:00","12:00","14:30","16:30","18:00").forEach(s -> {
            Seance seance=new Seance();
            try {
                seance.setHeureDebut(dateFormat.parse(s));
                seanceRepository.save(seance);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        });

    }

    @Override
    public void initCategories() {
        Stream.of("Action","Drama","Fiction","Comedie").forEach(ctgr->{
            Categorie categorie=new Categorie();
            categorie.setName(ctgr);
            categorieRepository.save(categorie);
        });

    }

    @Override
    public void initFilms() {
        double[] durees=new double[] {1,1.50,2,2.50};
        List<Categorie> categories=categorieRepository.findAll();
        Stream.of("Ace Ventura","The joker", "The God Father","Project Almanac").forEach(filmTitle->{
            Film film=new Film();
            film.setTitre(filmTitle);
            film.setDuree(durees[new Random().nextInt(durees.length)]);
            film.setPhoto(filmTitle.replace(" ","")+".jpg");
            film.setCategorie(categories.get(new Random().nextInt(categories.size())));
            filmRepository.save(film);
        });

    }

    @Override
    public void initProjections() {
        double[] prices=new double[] {50,60,65,70,75};
        villeRepository.findAll().forEach(ville -> {
            ville.getCinemas().forEach(cinema -> {
                cinema.getSalles().forEach(salle -> {
                    filmRepository.findAll().forEach(film -> {
                        seanceRepository.findAll().forEach(seance -> {
                            Projection projection=new Projection();
                            projection.setDateProjection(new Date());
                            projection.setFilm(film);
                            projection.setPrix(prices[new Random().nextInt(prices.length)]);
                            projection.setSalle(salle);
                            projection.setSeance(seance);
                            projectionRepository.save(projection);
                        });
                    });
                });
            });
        });

    }

    @Override
    public void initTickets() {
        projectionRepository.findAll().forEach(projection -> {
            projection.getSalle().getPlaces().forEach(place -> {
                Ticket ticket=new Ticket();
                ticket.setProjection(projection);
                ticket.setPrix(projection.getPrix());
                ticket.setReservee(false);
                ticket.setPlace(place);
                ticketRepository.save(ticket);
            });
        });

    }
}
