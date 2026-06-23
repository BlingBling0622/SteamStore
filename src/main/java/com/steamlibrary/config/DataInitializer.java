package com.steamlibrary.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.steamlibrary.model.Product;
import com.steamlibrary.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    // Fetch a product from the real Steam Store API by App ID
    private Product fetchFromSteam(long appId, boolean featured) {
        try {
            String url = "https://store.steampowered.com/api/appdetails?appids=" + appId + "&cc=us";
            String json = restTemplate.getForObject(url, String.class);
            JsonObject root = JsonParser.parseString(json)
                    .getAsJsonObject().getAsJsonObject(String.valueOf(appId));

            if (!root.get("success").getAsBoolean()) return null;
            JsonObject data = root.getAsJsonObject("data");

            Product p = new Product();
            p.setFeatured(featured);
            p.setName(data.get("name").getAsString());
            p.setDescription(data.has("short_description")
                    ? data.get("short_description").getAsString() : "");
            p.setHeaderImageUrl(data.get("header_image").getAsString());
            p.setDeveloper(data.has("developers")
                    ? data.getAsJsonArray("developers").get(0).getAsString() : "");
            p.setPublisher(data.has("publishers")
                    ? data.getAsJsonArray("publishers").get(0).getAsString() : "");

            // Tags from genres
            if (data.has("genres")) {
                String tags = data.getAsJsonArray("genres").asList().stream()
                        .map(g -> g.getAsJsonObject().get("description").getAsString())
                        .reduce((a, b) -> a + ", " + b).orElse("");
                p.setTags(tags);
            }

            // Price
            if (data.has("price_overview")) {
                JsonObject price = data.getAsJsonObject("price_overview");
                p.setPrice(price.get("initial").getAsDouble() / 100.0);
                p.setDiscountPercent(price.get("discount_percent").getAsInt());
            } else {
                p.setPrice(0.0); // Free to Play
                p.setDiscountPercent(0);
            }
            return p;
        } catch (Exception e) {
            log.warn("Could not fetch Steam app {}: {}", appId, e.getMessage());
            return null;
        }
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) return;

        productRepository.saveAll(List.of(
            product("Elden Ring", "An epic action RPG set in the Lands Between, crafted by FromSoftware and George R. R. Martin.",
                59.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1245620/header.jpg",
                "Action, RPG, Open World", "Overwhelmingly Positive", "FromSoftware", "Bandai Namco", true),
            product("Cyberpunk 2077", "An open-world action adventure set in Night City, a megalopolis obsessed with power.",
                39.99, 50, "https://cdn.akamai.steamstatic.com/steam/apps/1091500/header.jpg",
                "Action, RPG, Cyberpunk", "Very Positive", "CD Projekt RED", "CD Projekt RED", true),
            product("Red Dead Redemption 2", "An epic tale of life in America at the dawn of the modern era.",
                59.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1174180/header.jpg",
                "Action, Adventure, Open World", "Overwhelmingly Positive", "Rockstar Games", "Rockstar Games", true),
            product("Half-Life: Alyx", "Valve's return to the Half-Life universe. A groundbreaking VR game.",
                59.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/546560/header.jpg",
                "VR, Action, Shooter", "Overwhelmingly Positive", "Valve", "Valve", false),
            product("The Witcher 3", "An open world RPG set in a visually stunning fantasy universe.",
                39.99, 70, "https://cdn.akamai.steamstatic.com/steam/apps/292030/header.jpg",
                "RPG, Open World, Fantasy", "Overwhelmingly Positive", "CD Projekt RED", "CD Projekt RED", false),
            product("Counter-Strike 2", "The world's most popular tactical first-person shooter.",
                0.00, 0, "https://cdn.akamai.steamstatic.com/steam/apps/730/header.jpg",
                "Shooter, Multiplayer, Competitive", "Mostly Positive", "Valve", "Valve", false),
            product("Baldur's Gate 3", "Gather your party and return to the Forgotten Realms.",
                59.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1086940/header.jpg",
                "RPG, Turn-Based, Fantasy", "Overwhelmingly Positive", "Larian Studios", "Larian Studios", true),
            product("Hollow Knight", "A challenging action adventure through a vast ruined kingdom of insects.",
                14.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/367520/header.jpg",
                "Action, Platformer, Metroidvania", "Overwhelmingly Positive", "Team Cherry", "Team Cherry", false),
            product("Stardew Valley", "You've inherited your grandfather's old farm plot. Build the farm of your dreams.",
                14.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/413150/header.jpg",
                "Farming, RPG, Relaxing", "Overwhelmingly Positive", "ConcernedApe", "ConcernedApe", false),
            product("Portal 2", "Sequel to the acclaimed puzzle platformer. More science. More cake.",
                9.99, 75, "https://cdn.akamai.steamstatic.com/steam/apps/620/header.jpg",
                "Puzzle, Co-op, Sci-fi", "Overwhelmingly Positive", "Valve", "Valve", false)
        ));
    }

    private Product product(String name, String desc, double price, int discount,
                            String img, String tags, String review, String dev, String pub, boolean featured) {
        Product p = new Product();
        p.setName(name);
        p.setDescription(desc);
        p.setPrice(price);
        p.setDiscountPercent(discount);
        p.setHeaderImageUrl(img);
        p.setTags(tags);
        p.setReviewSummary(review);
        p.setDeveloper(dev);
        p.setPublisher(pub);
        p.setFeatured(featured);
        return p;
    }
}
