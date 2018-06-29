package com.ga.cloudfeed.data;

import com.ga.cloudfeed.model.FeedModel;
import com.ga.cloudfeed.model.ItemModel;
import com.ga.feedparser.Feed;
import com.ga.feedparser.Item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.inject.Singleton;

@Singleton
public class FeedToItemConverter {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z");

    public FeedToItemConverter() {
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public void convert(Feed feed, ConverterCallback callback, Map<String, Object> map) {
        String feedLink = feed.getLink();
        String feedTitle = feed.getTitle();
        String feedDescription = feed.getDescription();
        String format = dateFormat.format(feed.getPubDate());
        String feedImage = feed.getLogo();
        String url = null;
        Date feedDate = null;
        boolean subscribed = false;
        boolean registered = false;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            switch (entry.getKey()) {
                case BaseRepository.URL:
                    url = (String) value;
                    break;
                case BaseRepository.SUBSCRIBED:
                    subscribed = ((boolean) value);
                    break;
                case BaseRepository.REGISTERED:
                    registered = ((boolean) value);
                    break;
            }
        }


        try {
            feedDate = dateFormat.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Map<String, String> cloud = feed.getCloud();
        String webSub = feed.getWebSub();
        List<Item> list = feed.getItemList();
        ItemModel[] items = new ItemModel[list.size()];

        for (int i = 0; i < list.size(); i++) {
            Item item = list.get(i);
            Date date = item.getPubDate();
            String link = item.getLink();
            String title = item.getTitle();
            ArrayList<String> categories = new ArrayList<>(item.getCategories());
            String image = item.getThumbnail();
            String description = item.getDescription();
            String encoded = item.getEncoded();
            String author = item.getAuthor();

            items[i] = (new ItemModel(link, title, description, image, date,
                    feedLink, feedTitle, encoded,
                    author, categories));
        }

        callback.callback(new FeedModel(feedLink, feedTitle, feedDescription, feedImage, feedDate,
                webSub, cloud, url, subscribed, registered), items);

    }

    public interface ConverterCallback {
        void callback(FeedModel feed, ItemModel[] items);
    }
}
