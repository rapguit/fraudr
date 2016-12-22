package com.globo.globosat.repository;

import com.globo.globosat.model.Collision;
import com.globo.globosat.model.Node;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by raphael on 17/12/16.
 */
@Repository
public class FileRepository {

    @Setter
    @Value("${file.collisions.path}")
    private String collisionsFile;

    public List<Collision> loadCollisionsFromFile(){

        try (Stream<String> stream = Files.lines(Paths.get(collisionsFile))) {

            return stream.map(collision -> {
                String left = collision.split(" ")[0];
                String right = collision.split(" ")[1];
                return new Collision(new Node(left), new Node(right));
            }).collect(Collectors.toList());

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
