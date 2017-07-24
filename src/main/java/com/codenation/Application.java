package com.codenation;

import com.codenation.repository.CategoriesRepository;
import com.codenation.repository.ProductsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.codenation.models.*;

@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(ProductsRepository prodRepo, CategoriesRepository catRepo) {
        return (args) -> {

            catRepo.save(new Categories("d1"));
            catRepo.save(new Categories("d2"));
            catRepo.save(new Categories("d3"));
            prodRepo.save(new Products("c1", "n1", "d1",1,4.56,6.1,new Categories("d1")));
            prodRepo.save(new Products("c2", "n2", "d2",2,4.56,6.1,new Categories("d1")));
            prodRepo.save(new Products("c3", "n3", "d3",3,4.56,6.1,new Categories("d2")));

            log.info("Done");
        };
    }
}
