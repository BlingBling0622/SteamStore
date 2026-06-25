package com.steamlibrary.config;

import com.steamlibrary.model.Product;
import com.steamlibrary.repository.ProductRepository;
import com.steamlibrary.service.SteamAssetService;
import com.steamlibrary.service.SteamDlcService;
import com.steamlibrary.service.SteamMediaCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final SteamAssetService steamAssetService;
    private final SteamMediaCache mediaCache;
    private final SteamDlcService steamDlcService;

    @Override
    public void run(String... args) {
        boolean isFresh = productRepository.count() == 0;

        // Sync cache to database on every startup — always, regardless of fresh or not

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

        // ── More AAA & Action ──
        games.add(p("God of War", "Kratos journeys from the shadows of Olympus to the wilds of Midgard.", 49.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1593500/header.jpg", "Action, Adventure, Story Rich", "Overwhelmingly Positive", "Santa Monica Studio", "PlayStation PC", true));
        games.add(p("Marvel's Spider-Man Remastered", "Swing through Marvel's New York as an experienced Peter Parker.", 59.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1817070/header.jpg", "Action, Open World, Superhero", "Overwhelmingly Positive", "Insomniac Games", "PlayStation PC", true));
        games.add(p("Horizon Zero Dawn", "Explore a vibrant world inhabited by awe-inspiring machines.", 49.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1151640/header.jpg", "Action, RPG, Open World", "Very Positive", "Guerrilla Games", "PlayStation PC", false));
        games.add(p("Sekiro: Shadows Die Twice", "Carve your own clever path to vengeance in this bloody tale.", 59.99, 50, "https://cdn.akamai.steamstatic.com/steam/apps/814380/header.jpg", "Action, Souls-like, Ninja", "Overwhelmingly Positive", "FromSoftware", "Activision", false));
        games.add(p("Devil May Cry 5", "The ultimate Devil Hunter is back with insane over-the-top action.", 29.99, 60, "https://cdn.akamai.steamstatic.com/steam/apps/601150/header.jpg", "Action, Hack and Slash, Stylish", "Overwhelmingly Positive", "Capcom", "Capcom", false));
        games.add(p("Nier: Automata", "A tale of androids and their battle to reclaim a machine-driven dystopia.", 39.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/524220/header.jpg", "Action, RPG, Story Rich", "Very Positive", "PlatinumGames", "Square Enix", false));
        games.add(p("Metal Gear Solid V: The Phantom Pain", "The ultimate story of tactical espionage and revenge.", 19.99, 50, "https://cdn.akamai.steamstatic.com/steam/apps/287700/header.jpg", "Stealth, Action, Open World", "Very Positive", "Kojima Productions", "Konami", false));
        games.add(p("Death Stranding Director's Cut", "Deliver hope to a fractured world in this genre-defying experience.", 39.99, 50, "https://cdn.akamai.steamstatic.com/steam/apps/1850570/header.jpg", "Adventure, Sci-fi, Atmospheric", "Very Positive", "Kojima Productions", "505 Games", false));
        games.add(p("Ghost of Tsushima Director's Cut", "Explore feudal Japan as a samurai fighting to protect Tsushima Island.", 59.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/2215430/header.jpg", "Action, Open World, Samurai", "Overwhelmingly Positive", "Sucker Punch", "PlayStation PC", true));

        // ── More RPGs ──
        games.add(p("Persona 5 Royal", "Unmask the truth and steal hearts in this critically-acclaimed JRPG.", 59.99, 30, "https://cdn.akamai.steamstatic.com/steam/apps/1687950/header.jpg", "JRPG, Story Rich, Turn-Based", "Overwhelmingly Positive", "ATLUS", "SEGA", false));
        games.add(p("Final Fantasy VII Remake Intergrade", "The legend returns — reimagined for a new generation.", 69.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1462040/header.jpg", "JRPG, Action, Story Rich", "Very Positive", "Square Enix", "Square Enix", false));
        games.add(p("Divinity: Original Sin 2", "The eagerly anticipated sequel to the award-winning RPG.", 44.99, 60, "https://cdn.akamai.steamstatic.com/steam/apps/435150/header.jpg", "RPG, Turn-Based, Co-op", "Overwhelmingly Positive", "Larian Studios", "Larian Studios", false));
        games.add(p("Mass Effect Legendary Edition", "Relive the legend of Commander Shepard in the acclaimed trilogy.", 59.99, 75, "https://cdn.akamai.steamstatic.com/steam/apps/1328670/header.jpg", "RPG, Sci-fi, Story Rich", "Overwhelmingly Positive", "BioWare", "EA", false));
        games.add(p("Kingdom Come: Deliverance", "A realistic story-driven open-world RPG set in medieval Bohemia.", 29.99, 70, "https://cdn.akamai.steamstatic.com/steam/apps/379430/header.jpg", "RPG, Historical, Open World", "Very Positive", "Warhorse Studios", "Deep Silver", false));
        games.add(p("Dragon's Dogma 2", "The deep fantasy world of Dragon's Dogma returns with new adventures.", 69.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/2054970/header.jpg", "RPG, Action, Fantasy", "Mostly Positive", "Capcom", "Capcom", false));
        games.add(p("Yakuza: Like a Dragon", "Become Ichiban Kasuga, a low-ranking yakuza grunt left for dead.", 59.99, 65, "https://cdn.akamai.steamstatic.com/steam/apps/1235140/header.jpg", "RPG, Turn-Based, Comedy", "Overwhelmingly Positive", "Ryu Ga Gotoku Studio", "SEGA", false));
        games.add(p("Path of Exile 2", "Next generation free-to-play action RPG from Grinding Gear Games.", 0.00, 0, "https://cdn.akamai.steamstatic.com/steam/apps/2694490/header.jpg", "RPG, Action, Free to Play", "Very Positive", "Grinding Gear Games", "Grinding Gear Games", false));

        // ── Indie Hits ──
        games.add(p("Vampire Survivors", "Mow down thousands of night creatures and survive until dawn.", 4.99, 25, "https://cdn.akamai.steamstatic.com/steam/apps/1794680/header.jpg", "Action, Rogue-like, Bullet Hell", "Overwhelmingly Positive", "poncle", "poncle", false));
        games.add(p("Slay the Spire", "Fuse cards and relics to craft a unique deck and climb the Spire.", 24.99, 50, "https://cdn.akamai.steamstatic.com/steam/apps/646570/header.jpg", "Card Game, Rogue-like, Strategy", "Overwhelmingly Positive", "Mega Crit Games", "Mega Crit Games", false));
        games.add(p("Factorio", "Build and manage automated factories in an infinite 2D world.", 35.00, 0, "https://cdn.akamai.steamstatic.com/steam/apps/427520/header.jpg", "Automation, Base Building, Strategy", "Overwhelmingly Positive", "Wube Software", "Wube Software", false));
        games.add(p("RimWorld", "A sci-fi colony sim driven by an intelligent AI storyteller.", 34.99, 20, "https://cdn.akamai.steamstatic.com/steam/apps/294100/header.jpg", "Colony Sim, Survival, Strategy", "Overwhelmingly Positive", "Ludeon Studios", "Ludeon Studios", false));
        games.add(p("Disco Elysium", "Become a hero or an absolute disaster of a human being.", 39.99, 75, "https://cdn.akamai.steamstatic.com/steam/apps/632470/header.jpg", "RPG, Detective, Story Rich", "Overwhelmingly Positive", "ZA/UM", "ZA/UM", false));
        games.add(p("Cuphead", "A classic run-and-gun action game heavily focused on boss battles.", 19.99, 30, "https://cdn.akamai.steamstatic.com/steam/apps/268910/header.jpg", "Action, Platformer, Hand-drawn", "Overwhelmingly Positive", "Studio MDHR", "Studio MDHR", false));
        games.add(p("Subnautica", "Descend into the depths of an alien underwater world.", 29.99, 50, "https://cdn.akamai.steamstatic.com/steam/apps/264710/header.jpg", "Survival, Open World, Underwater", "Overwhelmingly Positive", "Unknown Worlds", "Unknown Worlds", false));
        games.add(p("Undertale", "The RPG game where you don't have to destroy anyone.", 9.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/391540/header.jpg", "RPG, Story Rich, Pixel Graphics", "Overwhelmingly Positive", "tobyfox", "tobyfox", false));
        games.add(p("Balatro", "A hypnotically satisfying poker-inspired roguelike deckbuilder.", 14.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/2379780/header.jpg", "Card Game, Rogue-like, Strategy", "Overwhelmingly Positive", "LocalThunk", "Playstack", false));
        // ── More Shooters ──
        games.add(p("Tom Clancy's Rainbow Six Siege", "A tactical shooter where the attackers and defenders face off in sieges.", 19.99, 60, "https://cdn.akamai.steamstatic.com/steam/apps/359550/header.jpg", "Shooter, Tactical, Multiplayer", "Very Positive", "Ubisoft Montreal", "Ubisoft", false));
        games.add(p("Destiny 2", "Dive into the world of Destiny 2 to explore new worlds and mysteries.", 0.00, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1085660/header.jpg", "FPS, MMO, Sci-fi", "Very Positive", "Bungie", "Bungie", false));
        games.add(p("Battlefield 2042", "A groundbreaking first-person shooter set in a war of the near future.", 59.99, 70, "https://cdn.akamai.steamstatic.com/steam/apps/1517290/header.jpg", "FPS, Multiplayer, Warfare", "Mixed", "DICE", "EA", false));
        games.add(p("Overwatch 2", "Team-based action game featuring 30+ unique heroes.", 0.00, 0, "https://cdn.akamai.steamstatic.com/steam/apps/2357570/header.jpg", "FPS, Hero Shooter, Multiplayer", "Mostly Positive", "Blizzard", "Blizzard", false));
        games.add(p("Helldivers 2", "Spread democracy with friends in this co-op sci-fi shooter.", 39.99, 20, "https://cdn.akamai.steamstatic.com/steam/apps/553850/header.jpg", "Shooter, Co-op, Sci-fi", "Very Positive", "Arrowhead Game Studios", "PlayStation PC", false));
        games.add(p("Payday 3", "The legendary co-op heist shooter is back with bigger scores.", 39.99, 40, "https://cdn.akamai.steamstatic.com/steam/apps/1272080/header.jpg", "FPS, Co-op, Heist", "Mixed", "Starbreeze Studios", "Deep Silver", false));
        games.add(p("Borderlands 3", "Mayhem-fueled looter shooter with bazillions of guns.", 59.99, 75, "https://cdn.akamai.steamstatic.com/steam/apps/397540/header.jpg", "FPS, Looter, Co-op", "Very Positive", "Gearbox Software", "2K", false));

        // ── Strategy & Simulation ──
        games.add(p("XCOM 2", "Lead the Avenger and strike back against the alien occupation.", 59.99, 80, "https://cdn.akamai.steamstatic.com/steam/apps/268500/header.jpg", "Strategy, Turn-Based, Sci-fi", "Very Positive", "Firaxis Games", "2K", false));
        games.add(p("Frostpunk", "A city-survival game where heat is life and every decision has a cost.", 29.99, 65, "https://cdn.akamai.steamstatic.com/steam/apps/323190/header.jpg", "Survival, City Builder, Strategy", "Very Positive", "11 bit studios", "11 bit studios", false));
        games.add(p("Crusader Kings III", "Guide a dynasty through the turbulent Middle Ages.", 49.99, 50, "https://cdn.akamai.steamstatic.com/steam/apps/1158310/header.jpg", "Strategy, Medieval, Grand Strategy", "Very Positive", "Paradox Development", "Paradox Interactive", false));
        games.add(p("Satisfactory", "A first-person open-world factory building game with exploration.", 29.99, 30, "https://cdn.akamai.steamstatic.com/steam/apps/526870/header.jpg", "Automation, Open World, Building", "Overwhelmingly Positive", "Coffee Stain Studios", "Coffee Stain", false));
        games.add(p("Anno 1800", "Lead the Industrial Revolution in this city-building strategy epic.", 59.99, 50, "https://cdn.akamai.steamstatic.com/steam/apps/916440/header.jpg", "City Builder, Strategy, Historical", "Very Positive", "Blue Byte", "Ubisoft", false));
        games.add(p("Planet Coaster", "Build the ultimate theme park and share your creations.", 37.99, 65, "https://cdn.akamai.steamstatic.com/steam/apps/493340/header.jpg", "Simulation, Building, Management", "Very Positive", "Frontier Developments", "Frontier", false));
        games.add(p("The Sims 4", "Create and control people in a virtual world where there are no rules.", 0.00, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1222670/header.jpg", "Simulation, Life Sim, Casual", "Mostly Positive", "Maxis", "EA", false));
        games.add(p("Workers & Resources: Soviet Republic", "Build a socialist republic from the ground up.", 34.99, 30, "https://cdn.akamai.steamstatic.com/steam/apps/784150/header.jpg", "City Builder, Simulation, Management", "Overwhelmingly Positive", "3Division", "Hooded Horse", false));
        games.add(p("Dyson Sphere Program", "Build the most efficient intergalactic factory in this automation sim.", 19.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1366540/header.jpg", "Automation, Space, Strategy", "Overwhelmingly Positive", "Youthcat Studio", "Gamera Games", false));

        // ── Horror ──
        games.add(p("Phasmophobia", "Investigate paranormal activity with your team of ghost hunters.", 13.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/739630/header.jpg", "Horror, Co-op, Investigation", "Overwhelmingly Positive", "Kinetic Games", "Kinetic Games", false));
        games.add(p("Amnesia: The Bunker", "Survive the horrors lurking within a desolate WW1 bunker.", 24.99, 25, "https://cdn.akamai.steamstatic.com/steam/apps/1944430/header.jpg", "Horror, Survival, Atmospheric", "Very Positive", "Frictional Games", "Frictional Games", false));
        games.add(p("The Evil Within 2", "Detective Sebastian Castellanos descends into a world of nightmares.", 39.99, 75, "https://cdn.akamai.steamstatic.com/steam/apps/601430/header.jpg", "Horror, Survival, Psychological", "Very Positive", "Tango Gameworks", "Bethesda", false));
        games.add(p("Lethal Company", "Scavenge abandoned moons for scrap while avoiding deadly creatures.", 9.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1966720/header.jpg", "Horror, Co-op, Survival", "Overwhelmingly Positive", "Zeekerss", "Zeekerss", false));

        // ── Survival & Crafting ──
        games.add(p("Sons of the Forest", "Sent to find a missing billionaire on a remote island infested with cannibals.", 29.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1326470/header.jpg", "Survival, Horror, Open World", "Very Positive", "Endnight Games", "Newnight", false));
        games.add(p("Raft", "Trapped on a small raft in the middle of an endless ocean with only a hook.", 19.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/648800/header.jpg", "Survival, Co-op, Adventure", "Very Positive", "Redbeet Interactive", "Axolot Games", false));
        games.add(p("Palworld", "Fight, farm, build, and work alongside mysterious Pals in this survival game.", 29.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1623730/header.jpg", "Survival, Open World, Monster Taming", "Very Positive", "Pocketpair", "Pocketpair", false));
        games.add(p("Grounded", "Shrunk to the size of an ant, survive the dangers of your own backyard.", 39.99, 33, "https://cdn.akamai.steamstatic.com/steam/apps/962130/header.jpg", "Survival, Co-op, Adventure", "Very Positive", "Obsidian", "Xbox Game Studios", false));
        games.add(p("7 Days to Die", "The survival horde crafting game set in a brutally unforgiving post-apocalyptic world.", 24.99, 60, "https://cdn.akamai.steamstatic.com/steam/apps/251570/header.jpg", "Survival, Zombies, Open World", "Very Positive", "The Fun Pimps", "The Fun Pimps", false));

        // ── Racing & Sports ──
        games.add(p("Assetto Corsa Competizione", "The official GT World Challenge racing simulation game.", 39.99, 50, "https://cdn.akamai.steamstatic.com/steam/apps/805550/header.jpg", "Racing, Simulation, Realistic", "Very Positive", "Kunos Simulazioni", "505 Games", false));
        games.add(p("F1 23", "Be the last to brake in the official video game of the FIA Formula One World Championship.", 69.99, 70, "https://cdn.akamai.steamstatic.com/steam/apps/2108330/header.jpg", "Racing, Sports, Simulation", "Very Positive", "Codemasters", "EA Sports", false));
        games.add(p("Rocket League", "High-powered hybrid of arcade-style soccer and vehicular mayhem.", 0.00, 0, "https://cdn.akamai.steamstatic.com/steam/apps/252950/header.jpg", "Sports, Racing, Multiplayer", "Very Positive", "Psyonix", "Epic Games", false));
        games.add(p("EA SPORTS FC 24", "The most true-to-football experience ever with HyperMotionV technology.", 69.99, 70, "https://cdn.akamai.steamstatic.com/steam/apps/2195250/header.jpg", "Sports, Soccer, Simulation", "Mixed", "EA Sports", "EA", false));
        games.add(p("Tony Hawk's Pro Skater 1 + 2", "Drop back in with the most iconic skateboarding games ever made.", 39.99, 50, "https://cdn.akamai.steamstatic.com/steam/apps/1081970/header.jpg", "Sports, Skateboarding, Arcade", "Overwhelmingly Positive", "Vicarious Visions", "Activision", false));

        // ── Fighting Games ──
        games.add(p("TEKKEN 8", "The legendary fighting franchise returns with a new aggressive fighting system.", 69.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1778820/header.jpg", "Fighting, Competitive, 3D Fighter", "Very Positive", "Bandai Namco Studios", "Bandai Namco", false));
        games.add(p("GUILTY GEAR -STRIVE-", "The cutting-edge 2D/3D hybrid fighting game with jaw-dropping visuals.", 39.99, 40, "https://cdn.akamai.steamstatic.com/steam/apps/1384160/header.jpg", "Fighting, Anime, 2D Fighter", "Very Positive", "Arc System Works", "Arc System Works", false));
        games.add(p("DRAGON BALL FighterZ", "The explosive 3v3 tag team fighting game based on the DRAGON BALL franchise.", 59.99, 75, "https://cdn.akamai.steamstatic.com/steam/apps/678950/header.jpg", "Fighting, Anime, 2D Fighter", "Very Positive", "Arc System Works", "Bandai Namco", false));

        // ── Free to Play Gems ──
        games.add(p("Genshin Impact", "An open-world action RPG set in the fantasy world of Teyvat.", 0.00, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1903620/header.jpg", "RPG, Open World, Gacha", "Very Positive", "HoYoverse", "HoYoverse", false));
        games.add(p("Lost Ark", "A stunning massively multiplayer online action role-playing game.", 0.00, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1599340/header.jpg", "MMO, Action RPG, Free to Play", "Mostly Positive", "Smilegate RPG", "Amazon Games", false));
        games.add(p("Albion Online", "A truly open-world sandbox MMORPG where you write your own story.", 0.00, 0, "https://cdn.akamai.steamstatic.com/steam/apps/761890/header.jpg", "MMO, Sandbox, PvP", "Mostly Positive", "Sandbox Interactive", "Sandbox Interactive", false));

        // ── Puzzle & Relaxing ──
        games.add(p("It Takes Two", "Embark on the craziest co-op journey of your life in this award-winning game.", 39.99, 60, "https://cdn.akamai.steamstatic.com/steam/apps/1426210/header.jpg", "Co-op, Adventure, Puzzle", "Overwhelmingly Positive", "Hazelight Studios", "EA", false));
        games.add(p("Unpacking", "A zen puzzle game about unpacking belongings and fitting them into a new home.", 19.99, 40, "https://cdn.akamai.steamstatic.com/steam/apps/1135690/header.jpg", "Puzzle, Relaxing, Pixel Graphics", "Overwhelmingly Positive", "Witch Beam", "Humble Games", false));
        games.add(p("PowerWash Simulator", "Release the pressure and wash away your worries with high-pressure water.", 24.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/1290000/header.jpg", "Simulation, Relaxing, Casual", "Overwhelmingly Positive", "FuturLab", "Square Enix Collective", false));
        games.add(p("Dave the Diver", "A casual adventure RPG featuring deep-sea exploration and sushi.", 19.99, 25, "https://cdn.akamai.steamstatic.com/steam/apps/1868140/header.jpg", "Adventure, RPG, Casual", "Overwhelmingly Positive", "MINTROCKET", "MINTROCKET", false));
        games.add(p("Outer Wilds", "Explore a solar system trapped in an endless time loop.", 24.99, 40, "https://cdn.akamai.steamstatic.com/steam/apps/753640/header.jpg", "Exploration, Puzzle, Space", "Overwhelmingly Positive", "Mobius Digital", "Annapurna Interactive", false));

        // ── More Action Adventures ──
        games.add(p("Hi-Fi RUSH", "Feel the beat as wannabe rockstar Chai and his team fight a megacorp.", 29.99, 35, "https://cdn.akamai.steamstatic.com/steam/apps/1817230/header.jpg", "Action, Rhythm, Stylized", "Overwhelmingly Positive", "Tango Gameworks", "Bethesda", false));
        games.add(p("Lies of P", "A souls-like action game inspired by the story of Pinocchio.", 59.99, 25, "https://cdn.akamai.steamstatic.com/steam/apps/1627720/header.jpg", "Souls-like, Action, Dark Fantasy", "Very Positive", "NEOWIZ", "NEOWIZ", false));
        games.add(p("Remnant II", "Survive the toughest realities in this third-person action-survival shooter.", 49.99, 30, "https://cdn.akamai.steamstatic.com/steam/apps/1282100/header.jpg", "Action, Shooter, Souls-like", "Very Positive", "Gunfire Games", "Gearbox Publishing", false));
        games.add(p("Armored Core VI: Fires of Rubicon", "Pilot a giant mech in fast-paced, omni-directional battles.", 59.99, 20, "https://cdn.akamai.steamstatic.com/steam/apps/1888160/header.jpg", "Mech, Action, Sci-fi", "Very Positive", "FromSoftware", "Bandai Namco", false));

        // ── Party & Multiplayer ──
        games.add(p("Among Us", "Work together to find the impostor before they eliminate everyone.", 4.99, 0, "https://cdn.akamai.steamstatic.com/steam/apps/945360/header.jpg", "Party, Social Deduction, Multiplayer", "Very Positive", "InnerSloth", "InnerSloth", false));
        games.add(p("Deep Rock Galactic", "100% destructible environments, procedurally-generated caves, and endless hordes of alien monsters.", 29.99, 50, "https://cdn.akamai.steamstatic.com/steam/apps/548430/header.jpg", "FPS, Co-op, Mining", "Overwhelmingly Positive", "Ghost Ship Games", "Coffee Stain", false));
        games.add(p("Risk of Rain 2", "Fight through hordes of monsters to escape a chaotic alien planet.", 24.99, 50, "https://cdn.akamai.steamstatic.com/steam/apps/632360/header.jpg", "Rogue-like, Shooter, Co-op", "Overwhelmingly Positive", "Hopoo Games", "Gearbox Publishing", false));

        // ── MMO & Online ──
        games.add(p("FINAL FANTASY XIV Online", "Take part in an epic and ever-changing adventure in the realm of Eorzea.", 19.99, 50, "https://cdn.akamai.steamstatic.com/steam/apps/39210/header.jpg", "MMORPG, Story Rich, Fantasy", "Very Positive", "Square Enix", "Square Enix", false));
        games.add(p("Black Desert Online", "A revolutionary MMORPG with intense, fast-paced combat and jaw-dropping graphics.", 9.99, 80, "https://cdn.akamai.steamstatic.com/steam/apps/582660/header.jpg", "MMORPG, Open World, Action", "Mostly Positive", "Pearl Abyss", "Pearl Abyss", false));
        games.add(p("EVE Online", "The largest single-shard space MMO of all time.", 0.00, 0, "https://cdn.akamai.steamstatic.com/steam/apps/8500/header.jpg", "MMO, Space, Sandbox", "Mostly Positive", "CCP Games", "CCP Games", false));

        // ── Military Simulation ──
        games.add(p("Arma 3", "Experience true combat gameplay in a massive military sandbox. Deploying a wide variety of single- and multiplayer content, over 20 vehicles and 40 weapons, and limitless opportunities for content creation, this is the PC's premier military game.", 29.99, 75, "https://cdn.akamai.steamstatic.com/steam/apps/107410/header.jpg", "Military, Simulation, Tactical, Open World, Multiplayer", "Very Positive", "Bohemia Interactive", "Bohemia Interactive", false));
        games.add(p("War Thunder", "Join now and take part in major battles on land, in the air, and at sea, fighting with millions of players from all over the world in an ever-evolving environment. Over 2,500 highly detailed aircraft, tanks, warships, helicopters and other combat vehicles.", 0.00, 0, "https://cdn.akamai.steamstatic.com/steam/apps/236390/header.jpg", "Free to Play, Military, Simulation, Multiplayer, Tanks, Aviation", "Very Positive", "Gaijin Entertainment", "Gaijin Entertainment", false));

        if (isFresh) {
            games = productRepository.saveAll(games); // capture managed entities with IDs
            System.out.println("✅ Initialized " + games.size() + " games in the store");
        }

        // ── Sync cache to DB on every startup ──
        int synced = 0;
        for (Product p : productRepository.findAll()) {
            String appId = extractAppId(p.getHeaderImageUrl());
            if (appId != null && mediaCache.has(appId)) {
                applyCachedMedia(p, mediaCache.get(appId));
                productRepository.save(p);
                synced++;
            }
        }
        if (synced > 0) log.info("📦 Synced {} games from cache", synced);

        // ── Fetch DLCs from Steam API ──
        if (isFresh) {
            List<Product> dlcs = new ArrayList<>();
            try { dlcs = steamDlcService.fetchDlcs(); } catch (Exception e) {}

            // Find parent DB IDs by matching Steam app IDs in header URLs
            for (Product game : games) {
                String appId = extractAppId(game.getHeaderImageUrl());
                if (appId == null) continue;
                long steamAppId = Long.parseLong(appId);

                if (steamAppId == 1245620L) {
                    dlcs.addAll(createDlc("Shadow of the Erdtree", "The Elden Ring expansion", 39.99, 2778580, game.getId()));
                    dlcs.addAll(createDlc("Elden Ring - Deluxe Edition Upgrade", "Upgrade to Deluxe", 19.99, 1892850, game.getId()));
                } else if (steamAppId == 1091500L) {
                    dlcs.addAll(createDlc("Cyberpunk 2077: Phantom Liberty", "New spy-thriller expansion", 29.99, 2138330, game.getId()));
                } else if (steamAppId == 292030L) {
                    dlcs.addAll(createDlc("The Witcher 3: Blood and Wine", "Travel to Toussaint", 19.99, 378648, game.getId()));
                    dlcs.addAll(createDlc("The Witcher 3: Hearts of Stone", "New adventure awaits", 9.99, 378649, game.getId()));
                } else if (steamAppId == 489830L) {
                    dlcs.addAll(createDlc("Skyrim: Dawnguard", "Vampire Lord expansion", 19.99, 211720, game.getId()));
                    dlcs.addAll(createDlc("Skyrim: Dragonborn", "Journey to Solstheim", 19.99, 226880, game.getId()));
                    dlcs.addAll(createDlc("Skyrim: Hearthfire", "Build your own home", 4.99, 220760, game.getId()));
                } else if (steamAppId == 1086940L) {
                    dlcs.addAll(createDlc("Baldur's Gate 3 - Digital Deluxe Upgrade", "Deluxe content pack", 9.99, 2375050, game.getId()));
                } else if (steamAppId == 1174180L) {
                    dlcs.addAll(createDlc("Red Dead Redemption 2: Special Edition Content", "Bonus content", 0.00, 1200240, game.getId()));
                } else if (steamAppId == 730L) {
                    dlcs.addAll(createDlc("CS2 - Prime Status Upgrade", "Premium matchmaking", 14.99, 1245150, game.getId()));
                }
            }

            if (!dlcs.isEmpty()) {
                productRepository.saveAll(dlcs);
                log.info("🎮 Added {} DLCs", dlcs.size());
            }
        }

        // ── Load media from cache, fetch missing from API, auto-save cache ──
        final List<Product> gameList = isFresh ? new ArrayList<>(games) : productRepository.findAll();
        Thread enricher = new Thread(() -> {
            java.util.concurrent.atomic.AtomicInteger fromCache = new java.util.concurrent.atomic.AtomicInteger(0);
            java.util.concurrent.atomic.AtomicInteger fromApi = new java.util.concurrent.atomic.AtomicInteger(0);
            java.util.concurrent.ExecutorService pool = java.util.concurrent.Executors.newFixedThreadPool(8);
            java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(gameList.size());

            for (Product game : gameList) {
                pool.submit(() -> {
                    try {
                        Product fresh = productRepository.findById(game.getId()).orElse(null);
                        if (fresh == null) { latch.countDown(); return; }
                        String appId = extractAppId(fresh.getHeaderImageUrl());
                        if (appId == null) { latch.countDown(); return; }

                        // 1) Try cache first
                        if (mediaCache.has(appId)) {
                            applyCachedMedia(fresh, mediaCache.get(appId));
                            productRepository.saveAndFlush(fresh);
                            fromCache.incrementAndGet();
                            latch.countDown();
                            return;
                        }

                        // 2) Fetch from API
                        SteamAssetService.SteamMedia media = steamAssetService.fetchMedia(appId);
                        if (media != null && !media.screenshotUrls().isEmpty()) {
                            applyLiveMedia(fresh, media);
                            productRepository.saveAndFlush(fresh);
                            // Save to cache for next time
                            mediaCache.put(appId, SteamMediaCache.CachedMedia.from(appId, media));
                            int n = fromApi.incrementAndGet();
                            if (n % 5 == 0) log.info("📸 {}/{} from API (last: {})", n, gameList.size(), fresh.getName());
                        }

                    } catch (Exception e) {
                        log.debug("Enrich failed for {}: {}", game.getName(), e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                });
            }

            try {
                latch.await();
                pool.shutdown();
                mediaCache.save();
                int totalCached = (int) java.util.Arrays.stream(mediaCache.getClass().getDeclaredFields())
                        .filter(f -> f.getName().equals("cache")).count(); // can't access private, use file
                System.out.println("✅ Media: " + fromCache.get() + " from cache, " + fromApi.get() + " from API");

                // Schedule retries for uncached games (every 60s, up to 30 attempts)
                if (fromApi.get() + fromCache.get() < gameList.size()) {
                    log.info("Scheduling retry for {} uncached games every 60s", gameList.size() - fromCache.get() - fromApi.get());
                    java.util.Timer retryTimer = new java.util.Timer("steam-retry", true);
                    retryTimer.scheduleAtFixedRate(new java.util.TimerTask() {
                        int attempts = 0;
                        public void run() {
                            attempts++;
                            int remaining = 0;
                            for (Product game : gameList) {
                                String appId = extractAppId(game.getHeaderImageUrl());
                                if (appId != null && !mediaCache.has(appId)) remaining++;
                            }
                            if (remaining == 0 || attempts > 30) {
                                if (remaining == 0) log.info("All games cached!");
                                retryTimer.cancel();
                                return;
                            }
                            log.info("Retry #{}/30: {} games still uncached", attempts, remaining);
                            for (Product game : gameList) {
                                String appId = extractAppId(game.getHeaderImageUrl());
                                if (appId == null || mediaCache.has(appId)) continue;
                                try {
                                    SteamAssetService.SteamMedia media = steamAssetService.fetchMedia(appId);
                                    if (media != null && !media.screenshotUrls().isEmpty()) {
                                        Product fresh = productRepository.findById(game.getId()).orElse(null);
                                        if (fresh != null) {
                                            applyLiveMedia(fresh, media);
                                            productRepository.saveAndFlush(fresh);
                                            mediaCache.put(appId, SteamMediaCache.CachedMedia.from(appId, media));
                                            mediaCache.save();
                                            log.info("📸 Retry cached: {} ({} screenshots)", fresh.getName(), media.screenshotUrls().size());
                                        }
                                    }
                                } catch (Exception ignored) {}
                            }
                        }
                    }, 60000, 60000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "steam-enricher");
        enricher.setDaemon(true);
        enricher.start();
    }

    // Steam CDN base URL pattern
    private static final String STEAM_CDN = "https://cdn.akamai.steamstatic.com/steam/apps";

    /** Extract Steam app ID from a header image URL like .../steam/apps/123456/header.jpg */
    private String extractAppId(String imgUrl) {
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("/apps/(\\d+)/").matcher(imgUrl);
        return m.find() ? m.group(1) : null;
    }

    /** Build a Steam CDN URL for a given app ID and filename */
    private String cdn(String appId, String file) {
        return STEAM_CDN + "/" + appId + "/" + file;
    }

    /** Create a DLC product entry with real Steam app ID */
    private List<Product> createDlc(String name, String desc, double price, long dlcAppId, Long parentAppId) {
        Product p = new Product();
        p.setName(name);
        p.setDescription(desc);
        p.setPrice(price);
        p.setDiscountPercent(0);
        p.setHeaderImageUrl(cdn(String.valueOf(dlcAppId), "header.jpg"));
        p.setCapsuleImageUrl(cdn(String.valueOf(dlcAppId), "capsule_616x353.jpg"));
        p.setScreenshot1Url(cdn(String.valueOf(dlcAppId), "library_hero.jpg"));
        p.setTags("DLC");
        p.setReviewSummary("");
        p.setDeveloper("");
        p.setPublisher("");
        p.setFeatured(false);
        p.setIsDlc(true);
        p.setParentGameId(parentAppId);
        p.setAboutTheGame(desc);
        return List.of(p);
    }

    /** Apply cached media data to a product */
    private void applyCachedMedia(Product p, SteamMediaCache.CachedMedia cm) {
        // Clear all, then fill with real screenshots
        p.setScreenshot1Url(cm.screenshots.size() >= 1 ? cm.screenshots.get(0) : null);
        p.setScreenshot2Url(cm.screenshots.size() >= 2 ? cm.screenshots.get(1) : null);
        p.setScreenshot3Url(cm.screenshots.size() >= 3 ? cm.screenshots.get(2) : null);
        p.setScreenshot4Url(cm.screenshots.size() >= 4 ? cm.screenshots.get(3) : null);
        p.setScreenshot5Url(cm.screenshots.size() >= 5 ? cm.screenshots.get(4) : null);
        p.setTrailerVideoUrl(cm.trailers.isEmpty() ? "" : String.join("|||", cm.trailers));
        p.setYoutubeVideoId(cm.youtubeVideoId != null && !cm.youtubeVideoId.isBlank() ? cm.youtubeVideoId : "");
        if (cm.aboutTheGame != null && !cm.aboutTheGame.isBlank()) {
            String about = cm.aboutTheGame;
            if (about.length() > 9900) about = about.substring(0, 9900);
            p.setAboutTheGame(about);
        }
    }

    /** Apply freshly fetched media to a product */
    private void applyLiveMedia(Product p, SteamAssetService.SteamMedia media) {
        java.util.List<String> ss = media.screenshotUrls();
        p.setScreenshot1Url(ss.size() >= 1 ? ss.get(0) : null);
        p.setScreenshot2Url(ss.size() >= 2 ? ss.get(1) : null);
        p.setScreenshot3Url(ss.size() >= 3 ? ss.get(2) : null);
        p.setScreenshot4Url(ss.size() >= 4 ? ss.get(3) : null);
        p.setScreenshot5Url(ss.size() >= 5 ? ss.get(4) : null);
        java.util.List<String> tr = media.trailerWebmUrls();
        if (!tr.isEmpty()) p.setTrailerVideoUrl(String.join("|||", tr));
        if (media.aboutTheGame() != null && !media.aboutTheGame().isBlank()) {
            String about = media.aboutTheGame();
            if (about.length() > 9900) {
                about = about.substring(0, 9900);
                int lastTag = about.lastIndexOf('<');
                if (lastTag > about.lastIndexOf('>')) about = about.substring(0, lastTag);
            }
            p.setAboutTheGame(about);
        }
    }

    private Product p(String name, String desc, double price, int discount, String img,
                      String tags, String review, String dev, String pub, boolean featured) {
        Product p = new Product();
        p.setName(name);
        p.setDescription(desc);
        p.setPrice(price);
        p.setDiscountPercent(discount);
        p.setHeaderImageUrl(img);

        // Auto-generate varied media from Steam CDN using the app ID
        String appId = extractAppId(img);
        if (appId != null) {
            // Capsule / thumbnail — different from header
            p.setCapsuleImageUrl(cdn(appId, "capsule_616x353.jpg"));

            // Screenshots — left null, filled by cache enrichment with real ss_*.jpg


            // Trailer video — Steam standard WebM (may 404 if game has no trailer)
            p.setTrailerVideoUrl(cdn(appId, "movie480_vp9.webm"));
        } else {
            // Fallback for non-standard URLs
            p.setCapsuleImageUrl(img);
            // Screenshots will be filled by cache enrichment
        }

        p.setTags(tags);
        p.setReviewSummary(review);
        p.setDeveloper(dev);
        p.setPublisher(pub);
        p.setFeatured(featured);

        // Richer "About the Game" text
        p.setAboutTheGame("About " + name + "\n\n" + desc + "\n\n"
                + "Developer: " + dev + "\n"
                + "Publisher: " + pub + "\n"
                + "Review Summary: " + review);
        return p;
    }
}
