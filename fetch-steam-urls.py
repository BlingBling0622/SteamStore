import requests
import json

# Steam App IDs for your games
games = {
    "Cyberpunk 2077": 1091500,
    "Elden Ring": 1245620,
    "Dark Souls III": 374320,
    "Grand Theft Auto V": 271590,
    "The Witcher 3: Wild Hunt": 292030
}

print("-- Real Steam Screenshot URLs")
print("USE steam_store;\n")

for game_name, app_id in games.items():
    print(f"-- {game_name} (App ID: {app_id})")

    try:
        # Fetch from Steam API
        url = f"https://store.steampowered.com/api/appdetails?appids={app_id}"
        response = requests.get(url, timeout=10)
        data = response.json()

        if str(app_id) in data and data[str(app_id)]['success']:
            game_data = data[str(app_id)]['data']

            # Get screenshots
            screenshots = game_data.get('screenshots', [])[:5]

            # Get video
            movies = game_data.get('movies', [])
            video_url = movies[0]['webm']['480'] if movies else None

            # Generate SQL
            print(f"UPDATE products SET")

            for i, screenshot in enumerate(screenshots, 1):
                print(f"  screenshot{i}_url = '{screenshot['path_full']}',")

            if video_url:
                print(f"  trailer_video_url = '{video_url}',")

            print(f"  about_the_game = 'Updated from Steam API'")
            print(f"WHERE name = '{game_name}';\n")

    except Exception as e:
        print(f"-- Error fetching {game_name}: {e}\n")

print("-- Run this SQL in MySQL Workbench!")
print("-- Then refresh http://localhost:8080/store/game/1")
