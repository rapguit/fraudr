package com.globo.globosat.repository;

import com.globo.globosat.model.Collision;
import org.junit.Before;
import org.junit.Test;

import java.io.UncheckedIOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

/**
 * Created by raphael on 20/12/16.
 */
public class FileRepositoryTest {

    private FileRepository repo;

    @Before
    public void setUp() throws Exception {
        repo = new FileRepository();
        repo.setCollisionsFile("src/test/resources/collisions.txt");
    }

    @Test
    public void should_load_collisions_from_file() throws Exception {
        List<Collision> collisions = repo.loadCollisionsFromFile();

        assertThat(collisions, hasSize(5));
    }

    @Test(expected = UncheckedIOException.class)
    public void fail_on_load_collisions_from_no_existent_file() throws Exception {
        repo.setCollisionsFile("no-existent-file");
        repo.loadCollisionsFromFile();
    }

}