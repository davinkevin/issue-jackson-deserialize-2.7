package com.github.davinkevin.issueJackson.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by kevin on 19/08/2016
 */
public class PodcastTest {

    ObjectMapper objectMapper = new ObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Test
    public void should_serialized() throws IOException {
        /* Given */
        String podcastAsText = "{\"hasToBeDeleted\":true,\"cover\":{\"id\":null,\"url\":\"https://yt3.ggpht.com/-2eu-eG4PLa8/AAAAAAAAAAI/AAAAAAAAAAA/FZUzswUhAmI/s100-c-k-no-rj-c0xffffff/photo.jpg\",\"width\":200,\"height\":200},\"url\":\"https://www.youtube.com/channel/UC_yP2DpIgs5Y1uWC0T03Chw\",\"type\":\"Youtube\",\"title\":\"Joueur Du Grenier\",\"description\":\"Test de jeux à la con !\"}";

        /* When */
        Podcast podcast1 = objectMapper.readValue(podcastAsText, Podcast.class);

        /* Then */
        assertThat(podcast1).isNotNull();
    }
}