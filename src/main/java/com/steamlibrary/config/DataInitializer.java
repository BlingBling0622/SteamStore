package com.steamlibrary.config;

import com.steamlibrary.model.Product;
import com.steamlibrary.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) return;

        List<Product> games = new ArrayList<>();

        // AAA Games
        games.add(p("Elden Ring", "An epic action RPG set in the Lands Between.", 59.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1245620/header.jpg", "Action, RPG, Open World", "Overwhelmingly Positive", "FromSoftware", "Bandai Namco", true));
        games.add(p("Cyberpunk 2077", "Open-world action adventure in Night City.", 39.99, 50, "https://cdn.akamai.steamstatic.com/steam/apps/1091500/header.jpg", "Action, RPG, Cyberpunk", "Very Positive", "CD Projekt RED", "CD Projekt RED", true));
        games.add(p("Red Dead Redemption 2", "Epic tale of life in America at the dawn of the modern era.", 59.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1174180/header.jpg", "Action, Adventure, Open World", "Overwhelmingly Positive", "Rockstar Games", "Rockstar Games", true));
        games.add(p("Baldur's Gate 3", "Gather your party and return to the Forgotten Realms.", 59.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1086940/header.jpg", "RPG, Turn-Based, Fantasy", "Overwhelmingly Positive", "Larian Studios", "Larian Studios", true));
        games.add(p("The Witcher 3: Wild Hunt", "Open world RPG set in a visually stunning fantasy universe.", 39.99, 70, "https://cdn.akamai.steamstatic.com/steam/apps/292030/header.jpg", "RPG, Open World, Fantasy", "Overwhelmingly Positive", "CD Projekt RED", "CD Projekt RED", true));
        games.add(p("Grand Theft Auto V", "When a young street hustler, a retired bank robber, and a terrifying psychopath meet up.", 29.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/271590/header.jpg", "Action, Open World, Multiplayer", "Very Positive", "Rockstar Games", "Rockstar Games", true));

        // Shooters
        games.add(p("Counter-Strike 2", "The world's most popular tactical shooter.", 0.00, 0, "https://cdn.akamai.steamstatic.com/steam/apps/730/header.jpg", "Shooter, Multiplayer, Competitive", "Mostly Positive", "Valve", "Valve", false));
        games.add(p("Call of Duty: Modern Warfare II", "The ultimate weapon is team. Modern Warfare II is back.", 69.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1938090/header.jpg", "Action, Shooter, Multiplayer", "Mixed", "Infinity Ward", "Activision", false));
        games.add(p("DOOM Eternal", "Rip and tear, until it is done.", 39.99, 60, "https://cdn.akamai.steamstatic.com/steam/apps/782330/header.jpg", "Action, Shooter, Gore", "Very Positive", "id Software", "Bethesda", false));
        games.add(p("Half-Life: Alyx", "Return to the Half-Life universe in VR.", 59.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/546560/header.jpg", "VR, Action, Shooter", "Overwhelmingly Positive", "Valve", "Valve", false));
        games.add(p("Halo Infinite", "The legendary Halo series returns.", 0.00, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1240440/header.jpg", "Shooter, Sci-fi, Multiplayer", "Mostly Positive", "343 Industries", "Xbox Game Studios", false));

        // RPGs
        games.add(p("Skyrim Special Edition", "Epic fantasy open world RPG.", 39.99, 50, "https://cdn.akamai.steamstatic.com/steam/apps/489830/header.jpg", "RPG, Open World, Fantasy", "Very Positive", "Bethesda", "Bethesda", false));
        games.add(p("Fallout 4", "Post-apocalyptic open world RPG.", 19.99, 75, "https://cdn.akamai.steamstatic.com/steam/apps/377160/header.jpg", "RPG, Open World, Post-apocalyptic", "Very Positive", "Bethesda", "Bethesda", false));
        games.add(p("Dark Souls III", "Embrace the darkness.", 59.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/374320/header.jpg", "Action, RPG, Dark Fantasy", "Very Positive", "FromSoftware", "Bandai Namco", false));
        games.add(p("Monster Hunter: World", "Welcome to a new world of hunting.", 29.99, 50, "https://cdn.akamai.steamstatic.com/steam/apps/582010/header.jpg", "Action, RPG, Co-op", "Very Positive", "Capcom", "Capcom", false));

        // Indie Games
        games.add(p("Hollow Knight", "A challenging adventure through a ruined kingdom.", 14.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/367520/header.jpg", "Action, Platformer, Metroidvania", "Overwhelmingly Positive", "Team Cherry", "Team Cherry", false));
        games.add(p("Stardew Valley", "Build the farm of your dreams.", 14.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/413150/header.jpg", "Farming, RPG, Relaxing", "Overwhelmingly Positive", "ConcernedApe", "ConcernedApe", false));
        games.add(p("Hades", "Defy the god of the dead in this rogue-like dungeon crawler.", 24.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1145360/header.jpg", "Action, Rogue-like, Indie", "Overwhelmingly Positive", "Supergiant Games", "Supergiant Games", false));
        games.add(p("Celeste", "Help Madeline survive her inner demons on her journey to the top.", 19.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/504230/header.jpg", "Platformer, Indie, Difficult", "Overwhelmingly Positive", "Maddy Makes Games", "Maddy Makes Games", false));
        games.add(p("Terraria", "Dig, fight, explore, build in this 2D adventure.", 9.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/105600/header.jpg", "Sandbox, Adventure, 2D", "Overwhelmingly Positive", "Re-Logic", "Re-Logic", false));
        games.add(p("Dead Cells", "A rogue-lite, metroidvania action-platformer.", 24.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/588650/header.jpg", "Action, Rogue-like, Platformer", "Overwhelmingly Positive", "Motion Twin", "Motion Twin", false));

        // Strategy
        games.add(p("Civilization VI", "Build an empire to stand the test of time.", 59.99, 75, "https://cdn.akamai.steamstatic.com/steam/apps/289070/header.jpg", "Strategy, Turn-Based, Historical", "Very Positive", "Firaxis Games", "2K", false));
        games.add(p("Total War: WARHAMMER III", "Epic fantasy strategy game.", 59.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1142710/header.jpg", "Strategy, Fantasy, War", "Very Positive", "Creative Assembly", "SEGA", false));
        games.add(p("Age of Empires IV", "Return to history in this real-time strategy classic.", 59.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1466860/header.jpg", "Strategy, RTS, Historical", "Very Positive", "Relic Entertainment", "Xbox Game Studios", false));
        games.add(p("Stellaris", "Explore a vast galaxy full of wonder.", 39.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/281990/header.jpg", "Strategy, Space, 4X", "Very Positive", "Paradox Development", "Paradox Interactive", false));

        // Horror
        games.add(p("Resident Evil Village", "Experience survival horror like never before.", 39.99, 50, "https://cdn.akamai.steamstatic.com/steam/apps/1196590/header.jpg", "Horror, Action, Survival", "Very Positive", "Capcom", "Capcom", false));
        games.add(p("Dead Space", "The sci-fi survival horror classic returns.", 59.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1693980/header.jpg", "Horror, Survival, Sci-fi", "Very Positive", "Motive Studios", "EA", false));
        games.add(p("Outlast", "Survive the night in this first-person survival horror.", 19.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/238320/header.jpg", "Horror, Survival, Stealth", "Very Positive", "Red Barrels", "Red Barrels", false));

        // Free to Play
        games.add(p("Dota 2", "The most-played game on Steam. Every day, millions fight.", 0.00, 0, "https://cdn.akamai.steamstatic.com/steam/apps/570/header.jpg", "MOBA, Multiplayer, Strategy", "Mostly Positive", "Valve", "Valve", false));
        games.add(p("Team Fortress 2", "Team-based multiplayer FPS.", 0.00, 0, "https://cdn.akamai.steamstatic.com/steam/apps/440/header.jpg", "Shooter, Multiplayer, FPS", "Very Positive", "Valve", "Valve", false));
        games.add(p("Apex Legends", "Battle royale shooter set in the Titanfall universe.", 0.00, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1172470/header.jpg", "Battle Royale, Shooter, FPS", "Mostly Positive", "Respawn", "EA", false));
        games.add(p("Warframe", "Ninjas play free.", 0.00, 0, "https://cdn.akamai.steamstatic.com/steam/apps/230410/header.jpg", "Action, Shooter, Co-op", "Very Positive", "Digital Extremes", "Digital Extremes", false));

        // Puzzle
        games.add(p("Portal 2", "More science. More cake.", 9.99, 75, "https://cdn.akamai.steamstatic.com/steam/apps/620/header.jpg", "Puzzle, Co-op, Sci-fi", "Overwhelmingly Positive", "Valve", "Valve", false));
        games.add(p("The Witness", "Explore a mysterious island and solve puzzles.", 39.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/210970/header.jpg", "Puzzle, Exploration, First-Person", "Very Positive", "Thekla Inc.", "Thekla Inc.", false));

        // Racing
        games.add(p("Forza Horizon 5", "Your Ultimate Horizon Adventure awaits in Mexico.", 59.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1551360/header.jpg", "Racing, Open World, Driving", "Very Positive", "Playground Games", "Xbox Game Studios", false));
        games.add(p("Need for Speed Heat", "Hustle by day and risk it all at night.", 39.99, 60, "https://cdn.akamai.steamstatic.com/steam/apps/1222680/header.jpg", "Racing, Action, Open World", "Mostly Positive", "Ghost Games", "EA", false));

        // Simulation
        games.add(p("Microsoft Flight Simulator", "Take to the skies in the next generation of flight simulation.", 59.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1250410/header.jpg", "Simulation, Flight, Realistic", "Very Positive", "Asobo Studio", "Xbox Game Studios", false));
        games.add(p("Cities: Skylines", "Build the city of your dreams.", 29.99, 75, "https://cdn.akamai.steamstatic.com/steam/apps/255710/header.jpg", "Simulation, City Builder, Strategy", "Very Positive", "Colossal Order", "Paradox Interactive", false));
        games.add(p("Euro Truck Simulator 2", "Travel across Europe as king of the road.", 19.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/227300/header.jpg", "Simulation, Driving, Relaxing", "Overwhelmingly Positive", "SCS Software", "SCS Software", false));

        // Survival
        games.add(p("ARK: Survival Evolved", "Survive on a mysterious island inhabited by dinosaurs.", 29.99, 50, "https://cdn.akamai.steamstatic.com/steam/apps/346110/header.jpg", "Survival, Open World, Multiplayer", "Mixed", "Studio Wildcard", "Studio Wildcard", false));
        games.add(p("Rust", "The only aim is to survive.", 39.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/252490/header.jpg", "Survival, Multiplayer, Crafting", "Mostly Positive", "Facepunch Studios", "Facepunch Studios", false));
        games.add(p("Valheim", "A brutal exploration and survival game for 1-10 players.", 19.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/892970/header.jpg", "Survival, Open World, Co-op", "Overwhelmingly Positive", "Iron Gate AB", "Coffee Stain", false));
        games.add(p("The Forest", "Survive in a mysterious forest after a plane crash.", 19.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/242760/header.jpg", "Survival, Horror, Open World", "Very Positive", "Endnight Games", "Endnight Games", false));

        // Sports
        games.add(p("FIFA 23", "Play the World's Game with 19,000+ players.", 59.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1811260/header.jpg", "Sports, Soccer, Multiplayer", "Mixed", "EA Sports", "EA", false));
        games.add(p("NBA 2K23", "Rise to the occasion in the latest basketball game.", 59.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1919590/header.jpg", "Sports, Basketball, Simulation", "Mostly Positive", "Visual Concepts", "2K", false));

        // Fighting
        games.add(p("Street Fighter 6", "The evolution of fighting games starts here.", 59.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1364780/header.jpg", "Fighting, Competitive, Multiplayer", "Very Positive", "Capcom", "Capcom", false));
        games.add(p("Mortal Kombat 11", "The critically acclaimed fighting game.", 49.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/976310/header.jpg", "Fighting, Gore, Multiplayer", "Very Positive", "NetherRealm Studios", "Warner Bros.", false));

        productRepository.saveAll(games);
        System.out.println("✅ Initialized " + games.size() + " games in the store");
    }

    private Product p(String name, String desc, double price, int discount, String img,
                      String tags, String review, String dev, String pub, boolean featured) {
        Product p = new Product();
        p.setName(name);
        p.setDescription(desc);
        p.setPrice(price);
        p.setDiscountPercent(discount);
        p.setHeaderImageUrl(img);
        p.setCapsuleImageUrl(img); // Use same image for capsule

        // Add 5 screenshots (using header image as placeholder for now)
        p.setScreenshot1Url(img);
        p.setScreenshot2Url(img);
        p.setScreenshot3Url(img);
        p.setScreenshot4Url(img);
        p.setScreenshot5Url(img);

        p.setTags(tags);
        p.setReviewSummary(review);
        p.setDeveloper(dev);
        p.setPublisher(pub);
        p.setFeatured(featured);
        p.setAboutTheGame(desc); // Use description as about text
        return p;
    }
}
