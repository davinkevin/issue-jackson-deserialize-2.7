package com.github.davinkevin.issueJackson.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by kevin on 19/08/2016
 */
@Builder
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true, value = {"signature", "items", "contains", "add", "lastUpdateToNow" })
public class Podcast {

    private UUID id;

    @JsonView(PodcastListingView.class)
    private String title;

    @JsonView(PodcastDetailsView.class)
    private String url;
    private String signature;

    @JsonView(PodcastListingView.class)
    private String type;

    @JsonView(PodcastListingView.class)
    private ZonedDateTime lastUpdate;

    @JsonManagedReference("podcast-item")
    private Set<Item> items = new HashSet<>();

    @JsonView(PodcastListingView.class)
    private Cover cover;

    @JsonView(PodcastDetailsView.class)
    private String description;

    @JsonView(PodcastDetailsView.class)
    private Boolean hasToBeDeleted;

    @JsonView(PodcastDetailsView.class)
    private Set<Tag> tags = new HashSet<>();

    @Override
    public String toString() {
        return "Podcast{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", signature='" + signature + '\'' +
                ", type='" + type + '\'' +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Podcast)) return false;

        Podcast podcast = (Podcast) o;

        return new EqualsBuilder()
                .append(id, podcast.id)
                .append(title, podcast.title)
                .append(url, podcast.url)
                .append(signature, podcast.signature)
                .append(lastUpdate, podcast.lastUpdate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(title)
                .append(url)
                .append(signature)
                .append(lastUpdate)
                .toHashCode();
    }

    public Boolean contains(Item item) {
        return items.contains(item);
    }

    public Podcast add(Item item) {
        items.add(item.setPodcast(this));
        return this;
    }

    public Podcast lastUpdateToNow() {
        this.lastUpdate = ZonedDateTime.now();
        return this;
    }

    public interface PodcastListingView {}
    public interface PodcastDetailsView extends PodcastListingView{}

}
