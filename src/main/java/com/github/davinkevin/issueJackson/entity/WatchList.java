package com.github.davinkevin.issueJackson.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Sets;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;
import java.util.UUID;

/**
 * Created by kevin on 17/01/2016 for PodcastServer
 */
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class WatchList {

    private UUID id;
    private String name;
    @JsonView(WatchListDetailsListView.class) private Set<Item> items = Sets.newHashSet();

    public WatchList add(Item item) {
        item.getWatchLists().add(this);
        items.add(item);
        return this;
    }

    public WatchList remove(Item item) {
        item.getWatchLists().remove(this);
        items.remove(item);
        return this;
    }

    public interface WatchListDetailsListView {};
}
