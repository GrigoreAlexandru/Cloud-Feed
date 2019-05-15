# Cloud-Feed

![](https://github.com/GrigoreAlexandru/Cloud-Feed/blob/master/cloud.png?raw=true)

Rss client with real time push notifications (or poll for unsupported feeds), offline db and user sync.

Using Firebase, [cloud-feed-backend](https://github.com/GrigoreAlexandru/Cloud-Feed-Backend) and [rss parser](https://github.com/GrigoreAlexandru/Modern-feed-parser). Uses Android Architecture components: lifecycle-aware components,Room,Live Data, MVVM and Dagger 2.

# Usage
Add standard conforming xml urls (RSS2/RSS1/ATOM)
```
 http://feeds.bbci.co.uk/news/rss.xml
 http://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml
 https://www.theverge.com/rss/index.xml
 ```
Login to save feeds and restore on any device. Choose which feed should be updated by going into the notifications settings.

# Get Started

- Modify keystore.properties with your keystore
- Create new Firebase project
- Add google-services.json to app directory from Firebase console
- Connect app to Firebase via Firebase assistant in Android studio

[APK HERE](https://github.com/GrigoreAlexandru/Cloud-Feed/releases/download/v1.0/Cloud_Feed_debug.apk)
