# Heads Up DIY

Why did I build this?

1. I'm cheap
2. ChatGPT is free and better at Kotlin than me
3. I wanted more customization options
4. This game shouldn't require an internet connection

![QR code to apk](./qrcode.jpg "Install HeadsUpDiy")

You'll need to [allow installation of unsigned APKs](https://www.androidcentral.com/unknown-sources)
until I have the time and inclination to become a "real" Android developer.

## Roadmap

Some features I plan on adding:

- Separate category data from app code (S3?)
- Customizable timer value (30s/60s/2m)
- "Timeless" game mode (round ends after N answers)
- Difficulty setting (per category?)
- Dynamic categories (make one up in-app)
    - Content filter (prevent inappropriate user-generated categories)
    - Store dynamic results (category names on-device + remote storage/API endpoint for answers)
- Telemetry data capture (to improve sensitivity)


## Architecture

- Build the following AWS Lambda Layers:
  - openai
  - g4f
  - google-gemini
- Build AWS Lambda
  - createHeadsUpCategory(category, genAiEngine)
- Build public AWS API Gateway
  - Need some security mechanism for API Gateway not to abuse OpenAI calls
