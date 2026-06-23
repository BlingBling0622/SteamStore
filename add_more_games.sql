-- Add 100+ more games to the store catalog

INSERT INTO products (name, description, price, discount_percent, header_image_url, tags, review_summary, developer, publisher, featured) VALUES
-- AAA Action/Adventure
('God of War', 'His vengeance against the Gods of Olympus years behind him, Kratos now lives as a man in the realm of Norse Gods and monsters.', 49.99, 20, 'https://cdn.akamai.steamstatic.com/steam/apps/1593500/header.jpg', 'Action, Adventure, Mythology', 'Overwhelmingly Positive', 'Santa Monica Studio', 'PlayStation Publishing', 1),
('Spider-Man Remastered', 'Be Greater. Be Yourself. Experience the critically acclaimed Marvel''s Spider-Man Remastered.', 59.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/1817070/header.jpg', 'Action, Open World, Superhero', 'Overwhelmingly Positive', 'Insomniac Games', 'PlayStation Publishing', 1),
('Hogwarts Legacy', 'Experience a new story set at Hogwarts in the 1800s.', 59.99, 10, 'https://cdn.akamai.steamstatic.com/steam/apps/990080/header.jpg', 'RPG, Open World, Magic', 'Very Positive', 'Avalanche Software', 'Warner Bros. Games', 1),
('Assassin''s Creed Valhalla', 'Become Eivor, a Viking raider raised to be a fearless warrior.', 59.99, 50, 'https://cdn.akamai.steamstatic.com/steam/apps/2208920/header.jpg', 'Action, Open World, Vikings', 'Mixed', 'Ubisoft Montreal', 'Ubisoft', 0),
('Far Cry 6', 'Welcome to Yara, a tropical paradise frozen in time.', 49.99, 40, 'https://cdn.akamai.steamstatic.com/steam/apps/2369390/header.jpg', 'Action, Shooter, Open World', 'Mostly Positive', 'Ubisoft Toronto', 'Ubisoft', 0),
('Watch Dogs Legion', 'Build a resistance from virtually anyone you see as you hack your way through London.', 39.99, 70, 'https://cdn.akamai.steamstatic.com/steam/apps/2239550/header.jpg', 'Action, Open World, Hacking', 'Mixed', 'Ubisoft Toronto', 'Ubisoft', 0),
('Control', 'A supernatural third-person action-adventure game.', 29.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/870780/header.jpg', 'Action, Supernatural, Third-Person', 'Very Positive', 'Remedy Entertainment', '505 Games', 0),
('Alan Wake 2', 'Saga Anderson and Alan Wake in a horror experience from Remedy Entertainment.', 59.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/1281590/header.jpg', 'Horror, Survival, Psychological', 'Very Positive', 'Remedy Entertainment', 'Epic Games', 1),
('Lies of P', 'A thrilling soulslike set in a dark Belle Époque world.', 59.99, 15, 'https://cdn.akamai.steamstatic.com/steam/apps/1627720/header.jpg', 'Soulslike, Action, Dark Fantasy', 'Very Positive', 'NEOWIZ', 'NEOWIZ', 0),

-- Indie Gems
('Vampire Survivors', 'Mow down thousands of night creatures.', 4.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/1794680/header.jpg', 'Action, Rogue-lite, Bullet Hell', 'Overwhelmingly Positive', 'poncle', 'poncle', 0),
('Cult of the Lamb', 'Build your own cult in a land of false prophets.', 24.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/1313140/header.jpg', 'Action, Rogue-lite, Cult Sim', 'Overwhelmingly Positive', 'Massive Monster', 'Devolver Digital', 0),
('Inscryption', 'A dark and disturbing card-based odyssey.', 19.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/1092790/header.jpg', 'Card Game, Horror, Puzzle', 'Overwhelmingly Positive', 'Daniel Mullins Games', 'Devolver Digital', 0),
('Undertale', 'The RPG game where you don''t have to destroy anyone.', 9.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/391540/header.jpg', 'RPG, Indie, Comedy', 'Overwhelmingly Positive', 'tobyfox', 'tobyfox', 0),
('Disco Elysium', 'A groundbreaking open world role playing game.', 39.99, 60, 'https://cdn.akamai.steamstatic.com/steam/apps/632470/header.jpg', 'RPG, Detective, Story Rich', 'Overwhelmingly Positive', 'ZA/UM', 'ZA/UM', 0),
('Outer Wilds', 'Explore a solar system stuck in a time loop.', 24.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/753640/header.jpg', 'Adventure, Exploration, Space', 'Overwhelmingly Positive', 'Mobius Digital', 'Annapurna Interactive', 0),
('Return of the Obra Dinn', 'A first-person mystery adventure based on deduction.', 19.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/653530/header.jpg', 'Puzzle, Mystery, Detective', 'Overwhelmingly Positive', 'Lucas Pope', '3909', 0),
('Slay the Spire', 'Craft a unique deck, encounter bizarre creatures, and survive the Spire.', 24.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/646570/header.jpg', 'Card Game, Rogue-like, Strategy', 'Overwhelmingly Positive', 'MegaCrit', 'Humble Games', 0),
('Risk of Rain 2', 'Escape a chaotic alien planet by fighting through hordes of enemies.', 24.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/632360/header.jpg', 'Rogue-like, Shooter, Co-op', 'Overwhelmingly Positive', 'Hopoo Games', 'Gearbox Publishing', 0),
('Enter the Gungeon', 'A bullet hell dungeon crawler following a band of misfits.', 14.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/311690/header.jpg', 'Bullet Hell, Rogue-like, Shooter', 'Very Positive', 'Dodge Roll', 'Devolver Digital', 0),

-- RPGs
('Divinity: Original Sin 2', 'Master deep, tactical combat. Join up to 3 other players in multiplayer.', 44.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/435150/header.jpg', 'RPG, Turn-Based, Fantasy', 'Overwhelmingly Positive', 'Larian Studios', 'Larian Studios', 0),
('Persona 5 Royal', 'Don the mask of Joker and join the Phantom Thieves of Hearts.', 59.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/1687950/header.jpg', 'JRPG, Turn-Based, Anime', 'Overwhelmingly Positive', 'ATLUS', 'SEGA', 1),
('Final Fantasy XIV', 'An epic tale unfolds in Eorzea.', 0.00, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/39210/header.jpg', 'MMORPG, Fantasy, Story Rich', 'Very Positive', 'Square Enix', 'Square Enix', 0),
('Dragon Age: Inquisition', 'Lead the Inquisition in an epic RPG adventure.', 19.99, 75, 'https://cdn.akamai.steamstatic.com/steam/apps/1222690/header.jpg', 'RPG, Fantasy, Open World', 'Mostly Positive', 'BioWare', 'Electronic Arts', 0),
('Mass Effect Legendary Edition', 'Experience the legend of Commander Shepard.', 59.99, 50, 'https://cdn.akamai.steamstatic.com/steam/apps/1328670/header.jpg', 'RPG, Sci-fi, Action', 'Very Positive', 'BioWare', 'Electronic Arts', 0),
('NieR:Automata', 'Experience the story of androids in a post-apocalyptic world.', 39.99, 50, 'https://cdn.akamai.steamstatic.com/steam/apps/524220/header.jpg', 'Action, RPG, Post-Apocalyptic', 'Very Positive', 'PlatinumGames', 'Square Enix', 0),
('Persona 4 Golden', 'A coming of age story set in a Japanese countryside.', 19.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/1113000/header.jpg', 'JRPG, Mystery, Social Sim', 'Overwhelmingly Positive', 'ATLUS', 'SEGA', 0),

-- Multiplayer/Competitive
('Overwatch 2', 'Push the limits in the ultimate team-based shooter sequel.', 0.00, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/2357570/header.jpg', 'Hero Shooter, Multiplayer, FPS', 'Mixed', 'Blizzard Entertainment', 'Blizzard Entertainment', 0),
('Valorant', 'A tactical shooter with unique agent abilities.', 0.00, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/2281990/header.jpg', 'FPS, Tactical, Multiplayer', 'Very Positive', 'Riot Games', 'Riot Games', 0),
('Rainbow Six Siege', 'Master the art of destruction and gadgetry.', 19.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/359550/header.jpg', 'Tactical, Shooter, Multiplayer', 'Very Positive', 'Ubisoft Montreal', 'Ubisoft', 0),
('Rocket League', 'High-powered rocket car soccer.', 0.00, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/252950/header.jpg', 'Sports, Racing, Multiplayer', 'Very Positive', 'Psyonix', 'Epic Games', 0),
('Dead by Daylight', 'Death is not an escape in this asymmetric multiplayer horror.', 19.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/381210/header.jpg', 'Horror, Asymmetric, Multiplayer', 'Very Positive', 'Behaviour Interactive', 'Behaviour Interactive', 0),
('Sea of Thieves', 'A shared world pirate adventure.', 39.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/1172620/header.jpg', 'Pirate, Multiplayer, Co-op', 'Very Positive', 'Rare Ltd.', 'Xbox Game Studios', 0),
('Among Us', 'Play online with 4-15 players as you attempt to prep your spaceship.', 4.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/945360/header.jpg', 'Social Deduction, Multiplayer', 'Very Positive', 'Innersloth', 'Innersloth', 0),
('Fall Guys', 'Competitive party mayhem with up to 60 players.', 0.00, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/1097150/header.jpg', 'Battle Royale, Party, Multiplayer', 'Very Positive', 'Mediatonic', 'Epic Games', 0),
('PUBG: Battlegrounds', 'The original battle royale experience.', 0.00, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/578080/header.jpg', 'Battle Royale, Shooter, Survival', 'Mixed', 'KRAFTON', 'KRAFTON', 0),
('Fortnite', 'Epic battle royale with building mechanics.', 0.00, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/1172470/header.jpg', 'Battle Royale, Shooter, Building', 'Mostly Positive', 'Epic Games', 'Epic Games', 0),

-- Horror
('Phasmophobia', 'A co-op psychological horror game.', 13.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/739630/header.jpg', 'Horror, Co-op, Multiplayer', 'Overwhelmingly Positive', 'Kinetic Games', 'Kinetic Games', 0),
('Amnesia: The Dark Descent', 'A first person survival horror experience.', 19.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/57300/header.jpg', 'Horror, Survival, Atmospheric', 'Very Positive', 'Frictional Games', 'Frictional Games', 0),
('The Quarry', 'When the sun goes down, the terror begins.', 59.99, 30, 'https://cdn.akamai.steamstatic.com/steam/apps/1577120/header.jpg', 'Horror, Choice-Based, Cinematic', 'Very Positive', 'Supermassive Games', '2K', 0),
('Little Nightmares II', 'A suspense-adventure game set in a world that seems impossible to escape.', 29.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/860510/header.jpg', 'Horror, Puzzle, Platformer', 'Very Positive', 'Tarsier Studios', 'Bandai Namco', 0),
('SOMA', 'A sci-fi horror game from Frictional Games.', 29.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/282140/header.jpg', 'Horror, Sci-fi, Story Rich', 'Overwhelmingly Positive', 'Frictional Games', 'Frictional Games', 0),

-- Strategy & Management
('XCOM 2', 'Earth has changed and is now under alien rule.', 59.99, 75, 'https://cdn.akamai.steamstatic.com/steam/apps/268500/header.jpg', 'Strategy, Turn-Based, Tactical', 'Very Positive', 'Firaxis Games', '2K', 0),
('Crusader Kings III', 'Love, fight, scheme, and claim greatness.', 49.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/1158310/header.jpg', 'Grand Strategy, Medieval, RPG', 'Very Positive', 'Paradox Development Studio', 'Paradox Interactive', 0),
('Europa Universalis IV', 'Rule your nation through the centuries.', 39.99, 75, 'https://cdn.akamai.steamstatic.com/steam/apps/236850/header.jpg', 'Grand Strategy, Historical, Simulation', 'Very Positive', 'Paradox Development Studio', 'Paradox Interactive', 0),
('Factorio', 'Build and maintain factories in an infinite 2D world.', 30.00, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/427520/header.jpg', 'Automation, Building, Factory', 'Overwhelmingly Positive', 'Wube Software', 'Wube Software', 0),
('RimWorld', 'A sci-fi colony sim driven by an intelligent AI storyteller.', 34.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/294100/header.jpg', 'Colony Sim, Strategy, Survival', 'Overwhelmingly Positive', 'Ludeon Studios', 'Ludeon Studios', 0),
('Prison Architect', 'Build and manage a maximum security prison.', 29.99, 75, 'https://cdn.akamai.steamstatic.com/steam/apps/233450/header.jpg', 'Management, Simulation, Strategy', 'Very Positive', 'Introversion Software', 'Paradox Interactive', 0),
('Two Point Hospital', 'Design and build stunning hospitals and cure curious diseases.', 34.99, 60, 'https://cdn.akamai.steamstatic.com/steam/apps/535930/header.jpg', 'Management, Simulation, Comedy', 'Very Positive', 'Two Point Studios', 'SEGA', 0),
('Oxygen Not Included', 'Manage a space colony to ensure colonists survive.', 24.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/457140/header.jpg', 'Survival, Colony Sim, Space', 'Overwhelmingly Positive', 'Klei Entertainment', 'Klei Entertainment', 0),

-- Platformers & Action
('Cuphead', 'A classic run and gun action game heavily focused on boss battles.', 19.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/268910/header.jpg', 'Run and Gun, Platformer, Difficult', 'Overwhelmingly Positive', 'Studio MDHR', 'Studio MDHR', 0),
('Ori and the Will of the Wisps', 'Embark on a new journey in a vast world.', 29.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/1057090/header.jpg', 'Metroidvania, Platformer, Beautiful', 'Overwhelmingly Positive', 'Moon Studios', 'Xbox Game Studios', 0),
('Shovel Knight', 'An action-adventure with an 8-bit retro aesthetic.', 14.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/250760/header.jpg', 'Platformer, Retro, Action', 'Overwhelmingly Positive', 'Yacht Club Games', 'Yacht Club Games', 0),
('Blasphemous', 'A punishing action-platformer that combines brutal combat.', 24.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/774361/header.jpg', 'Metroidvania, Dark, Action', 'Very Positive', 'The Game Kitchen', 'Team17', 0),
('Katana ZERO', 'A stylish neo-noir action-platformer.', 14.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/460950/header.jpg', 'Action, Platformer, Time Manipulation', 'Overwhelmingly Positive', 'Askiisoft', 'Devolver Digital', 0),

-- Puzzle & Relaxing
('The Talos Principle', 'A philosophical first-person puzzle game.', 29.99, 80, 'https://cdn.akamai.steamstatic.com/steam/apps/257510/header.jpg', 'Puzzle, Philosophical, First-Person', 'Overwhelmingly Positive', 'Croteam', 'Devolver Digital', 0),
('A Short Hike', 'Hike, climb, and soar through a peaceful mountainside.', 7.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/1055540/header.jpg', 'Adventure, Relaxing, Exploration', 'Overwhelmingly Positive', 'adamgryu', 'Whippoorwill', 0),
('Unpacking', 'Zen puzzle game about the familiar experience of unpacking.', 19.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/1135690/header.jpg', 'Puzzle, Relaxing, Story', 'Overwhelmingly Positive', 'Witch Beam', 'Humble Games', 0),
('PowerWash Simulator', 'Clean up dirt and grime with your power washer.', 24.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/1290000/header.jpg', 'Simulation, Relaxing, Cleaning', 'Overwhelmingly Positive', 'FuturLab', 'Square Enix', 0),
('Donut County', 'A story-based physics puzzle about a hole in the ground.', 12.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/702670/header.jpg', 'Puzzle, Indie, Comedy', 'Very Positive', 'Ben Esposito', 'Annapurna Interactive', 0),

-- Racing & Sports
('F1 23', 'Be the last to brake in the official game of the 2023 FIA Formula One season.', 59.99, 30, 'https://cdn.akamai.steamstatic.com/steam/apps/2108330/header.jpg', 'Racing, Simulation, Sports', 'Mostly Positive', 'Codemasters', 'Electronic Arts', 0),
('Assetto Corsa', 'Driving simulation with advanced physics.', 19.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/244210/header.jpg', 'Racing, Simulation, Realistic', 'Very Positive', 'Kunos Simulazioni', '505 Games', 0),
('DiRT Rally 2.0', 'Rally driving at its best.', 39.99, 75, 'https://cdn.akamai.steamstatic.com/steam/apps/690790/header.jpg', 'Racing, Rally, Simulation', 'Very Positive', 'Codemasters', 'Codemasters', 0),
('Riders Republic', 'Jump into a massive multiplayer playground.', 49.99, 60, 'https://cdn.akamai.steamstatic.com/steam/apps/2290180/header.jpg', 'Sports, Extreme Sports, Multiplayer', 'Mostly Positive', 'Ubisoft Annecy', 'Ubisoft', 0),

-- Simulation & Building
('The Sims 4', 'Play with life in The Sims 4.', 0.00, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/1222670/header.jpg', 'Life Sim, Building, Casual', 'Very Positive', 'Maxis', 'Electronic Arts', 0),
('Planet Zoo', 'Build a world for wildlife.', 44.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/703080/header.jpg', 'Management, Animals, Building', 'Very Positive', 'Frontier Developments', 'Frontier Developments', 0),
('Farming Simulator 22', 'Take on the role of a modern farmer.', 39.99, 50, 'https://cdn.akamai.steamstatic.com/steam/apps/1248130/header.jpg', 'Farming, Simulation, Vehicles', 'Very Positive', 'GIANTS Software', 'GIANTS Software', 0),
('House Flipper', 'Buy, repair and upgrade devastated houses.', 19.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/613100/header.jpg', 'Simulation, Building, Renovation', 'Very Positive', 'Empyrean', 'Frozen District', 0),
('Satisfactory', 'Build automated factories on an alien planet.', 29.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/526870/header.jpg', 'Automation, Building, First-Person', 'Overwhelmingly Positive', 'Coffee Stain Studios', 'Coffee Stain Publishing', 0),
('Kerbal Space Program', 'Build spacecraft and explore the cosmos.', 39.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/220200/header.jpg', 'Space, Simulation, Physics', 'Overwhelmingly Positive', 'Squad', 'Private Division', 0),

-- Action & Survival
('Subnautica', 'Descend into the depths of an alien underwater world.', 29.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/264710/header.jpg', 'Survival, Underwater, Exploration', 'Overwhelmingly Positive', 'Unknown Worlds', 'Unknown Worlds', 0),
('The Long Dark', 'Explore the frozen wilderness in this survival simulation.', 34.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/305620/header.jpg', 'Survival, Open World, Winter', 'Very Positive', 'Hinterland Studio', 'Hinterland Studio', 0),
('Don''t Starve Together', 'Fight, Farm, Build and Explore Together.', 14.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/322330/header.jpg', 'Survival, Co-op, Crafting', 'Very Positive', 'Klei Entertainment', 'Klei Entertainment', 0),
('Green Hell', 'Survive the Amazon rainforest.', 24.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/815370/header.jpg', 'Survival, Jungle, Crafting', 'Very Positive', 'Creepy Jar', 'Creepy Jar', 0),
('7 Days to Die', 'Zombie survival with crafting and tower defense.', 24.99, 50, 'https://cdn.akamai.steamstatic.com/steam/apps/251570/header.jpg', 'Survival, Zombies, Crafting', 'Mostly Positive', 'The Fun Pimps', 'The Fun Pimps', 0),
('Project Zomboid', 'Hardcore zombie survival RPG.', 19.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/108600/header.jpg', 'Survival, Zombies, Isometric', 'Overwhelmingly Positive', 'The Indie Stone', 'The Indie Stone', 0),

-- Visual Novels & Story
('Danganronpa', 'Investigate murders, search for clues, and talk to classmates.', 19.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/413410/header.jpg', 'Visual Novel, Mystery, Anime', 'Overwhelmingly Positive', 'Spike Chunsoft', 'Spike Chunsoft', 0),
('Phoenix Wright: Ace Attorney Trilogy', 'Three classic courtroom adventure games.', 29.99, 50, 'https://cdn.akamai.steamstatic.com/steam/apps/787480/header.jpg', 'Visual Novel, Detective, Comedy', 'Overwhelmingly Positive', 'CAPCOM', 'CAPCOM', 0),
('AI: The Somnium Files', 'A detective adventure investigating serial murder cases.', 39.99, 60, 'https://cdn.akamai.steamstatic.com/steam/apps/948740/header.jpg', 'Visual Novel, Mystery, Sci-fi', 'Very Positive', 'Spike Chunsoft', 'Spike Chunsoft', 0),

-- Roguelike/Roguelite
('The Binding of Isaac: Rebirth', 'A randomly generated action RPG shooter.', 14.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/250900/header.jpg', 'Rogue-like, Action, Dark', 'Overwhelmingly Positive', 'Nicalis', 'Nicalis', 0),
('FTL: Faster Than Light', 'Space roguelike about managing a spaceship.', 9.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/212680/header.jpg', 'Rogue-like, Strategy, Space', 'Overwhelmingly Positive', 'Subset Games', 'Subset Games', 0),
('Darkest Dungeon', 'A challenging gothic roguelike turn-based RPG.', 24.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/262060/header.jpg', 'Rogue-like, Turn-Based, Gothic', 'Very Positive', 'Red Hook Studios', 'Red Hook Studios', 0),
('Spelunky 2', 'Cave exploration gone wrong.', 19.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/418530/header.jpg', 'Rogue-like, Platformer, Difficult', 'Very Positive', 'Mossmouth', 'Mossmouth', 0),
('Nuclear Throne', 'Fight your way through the wastelands.', 11.99, 0, 'https://cdn.akamai.steamstatic.com/steam/apps/242680/header.jpg', 'Rogue-like, Shooter, Fast-Paced', 'Overwhelmingly Positive', 'Vlambeer', 'Vlambeer', 0);
