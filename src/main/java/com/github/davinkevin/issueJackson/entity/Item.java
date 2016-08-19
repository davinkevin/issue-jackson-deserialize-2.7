package com.github.davinkevin.issueJackson.entity;

import com.fasterxml.jackson.annotation.*;
import com.google.common.collect.Sets;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;


@Builder
@Getter @Setter
@Accessors(chain = true)
@NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true, value = { "numberOfTry", "localUri", "addATry", "deleteDownloadedFile", "localPath", "proxyURLWithoutExtention", "extention", "hasValidURL", "reset", "coverPath" })
public class Item {

    public  static Path rootFolder;
    public  static final Item DEFAULT_ITEM = new Item();
    private static final String PROXY_URL = "/api/podcast/%s/items/%s/download%s";
    private static final String COVER_PROXY_URL = "/api/podcast/%s/items/%s/cover.%s";

    private UUID id;
    private Cover cover;
    @JsonBackReference("podcast-item") private Podcast podcast;
    @JsonView(ItemSearchListView.class) private String title;
    @JsonView(ItemSearchListView.class) private String url;
    @JsonView(ItemPodcastListView.class) private ZonedDateTime pubDate;
    @JsonView(ItemPodcastListView.class) private String description;
    @JsonView(ItemSearchListView.class) private String mimeType;
    @JsonView(ItemDetailsView.class) private Long length;
    @JsonView(ItemDetailsView.class) private String fileName;
    @JsonView(ItemSearchListView.class) private Status status = Status.NOT_DOWNLOADED;
    @JsonView(ItemDetailsView.class) private Integer progression = 0;
    private Integer numberOfTry = 0;
    @JsonView(ItemDetailsView.class) private ZonedDateTime downloadDate;
    private ZonedDateTime creationDate;
    @JsonIgnore private Set<WatchList> watchLists = Sets.newHashSet();

    public String getLocalUri() {
        return (fileName == null) ? null : getLocalPath().toString();
    }

    public Item setLocalUri(String localUri) {
        fileName = FilenameUtils.getName(localUri);
        return this;
    }

    public Item addATry() {
        this.numberOfTry++;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        if (this == DEFAULT_ITEM && o != DEFAULT_ITEM || this != DEFAULT_ITEM && o == DEFAULT_ITEM) return false;

        Item item = (Item) o;

        if (nonNull(id) && nonNull(item.id))
            return id.equals(item.id);

        if (nonNull(url) && nonNull(item.url)) {
            return url.equals(item.url) || FilenameUtils.getName(item.url).equals(FilenameUtils.getName(url));
        }

        return StringUtils.equals(getProxyURL(), item.getProxyURL());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(url)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", pubDate=" + pubDate +
                ", description='" + description + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", length=" + length +
                ", status='" + status + '\'' +
                ", progression=" + progression +
                ", downloaddate=" + downloadDate +
                ", podcast=" + podcast +
                ", numberOfTry=" + numberOfTry +
                '}';
    }

    @JsonProperty("proxyURL") @JsonView(ItemSearchListView.class)
    public String getProxyURL() {
        return String.format(PROXY_URL, podcast.getId(), id, getExtention());
    }

    @JsonProperty("isDownloaded") @JsonView(ItemSearchListView.class)
    public Boolean isDownloaded() {
        return StringUtils.isNotEmpty(fileName);
    }

    @JsonIgnore
    public Item deleteDownloadedFile() {
        status = Status.DELETED;
        fileName = null;
        return this;
    }

    public Path getLocalPath() {
        return getPodcastPath().resolve(fileName);
    }

    public Path getCoverPath() {
        String url = isNull(cover) ? "" : cover.getUrl();
        return getPodcastPath().resolve(id + "." + FilenameUtils.getExtension(url));
    }

    private Path getPodcastPath() {
        return rootFolder.resolve(podcast.getTitle());
    }

    public String getProxyURLWithoutExtention() {
        return String.format(PROXY_URL, podcast.getId(), id, "");
    }

    private String getExtention() {
        String ext = FilenameUtils.getExtension(fileName);
        return (ext == null) ? "" : "."+ext;
    }

    @JsonProperty("cover") @JsonView(ItemSearchListView.class)
    public Cover getCoverOfItemOrPodcast() {
        return isNull(this.cover)
                ? podcast.getCover()
                : this.cover.toBuilder().url(String.format(COVER_PROXY_URL, podcast.getId(), id, FilenameUtils.getExtension(this.cover.getUrl()))).build();
    }

    @JsonProperty("podcastId") @JsonView(ItemSearchListView.class)
    public UUID getPodcastId() { return isNull(podcast) ? null : podcast.getId();}
        

    public Item reset() {
        setStatus(Status.NOT_DOWNLOADED);
        downloadDate = null;
        fileName = null;
        return this;
    }

    public interface ItemSearchListView {}
    public interface ItemPodcastListView extends ItemSearchListView {}
    public interface ItemDetailsView extends ItemPodcastListView {}
}
