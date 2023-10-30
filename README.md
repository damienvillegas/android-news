# Anroid News

GW 2023 project for CSCI4237 - Software Development for Handheld Devices by Damien Villegas

## Description

A news app that uses [NewsAPI](https://newsapi.org/) and [Google Maps API](https://developers.google.com/maps) to display current news articles in a variety of ways. You can view top headlines for common categories like 'health', 'sports', 'general', etc. Also, you can click a spot on Google Maps and get articles for that country. Lastly, you can search for specfic terms and choose news from a specfic source.

To run this app you will need (free) API keys from the links above. I've provided a tutorial to show how to setup Android News locally and where to put the API keys.

## Features

- Real news articles, pulled from the a varitey of sources
- Clicking articles takes you to a browser where you can read the articles
- Search for terms and pick sources (or choose no source)
- Explore news from around the world by clicking countries and scrolling through related articles
- Checkout top headlines from various categories
- Network error handling, so the app won't crash if API calls fail
- Articles missing information (no image or description for example) will still display
- Data persistance using local storage so when you close the app and open it again your search term, map location, and top headlines are all saved

## Showcase

![image](https://github.com/damienvillegas/android-news/assets/29382810/0eb0277f-bd43-4c96-b31d-0ebf964b284a)

![image](https://github.com/damienvillegas/android-news/assets/29382810/ae6166be-c803-48df-b999-a5086e9b6b1d)

![image](https://github.com/damienvillegas/android-news/assets/29382810/13bcb548-567d-4141-94b2-3080037c2ad4)

![image](https://github.com/damienvillegas/android-news/assets/29382810/68b6409a-acf2-4990-bdbb-cacf7c4fe1b2)

![image](https://github.com/damienvillegas/android-news/assets/29382810/28a7f788-ce58-454a-a82a-22e449a62a12)


## Tutorial 

### Step 1

In Android Studio, on the top right, make a new project by clicking the 'Get from VCS button'.

![image](https://github.com/damienvillegas/android-news/assets/29382810/dd5574ee-09cb-415b-be81-f5965fe91e77)

Note: The folder you save it in has to named 'project1'

### Step 2

Create a new file in the values folder of the app. (App -> src -> values). name it "apikeys.xml"

![image](https://github.com/damienvillegas/android-news/assets/29382810/89befe23-7053-4192-a432-1dd76837aeae)

![image](https://github.com/damienvillegas/android-news/assets/29382810/636f31bc-a651-4095-ab2d-2cd72f54495f)

In addition, put the API keys in your local.properties file (Gradle scripts -> local properties)


![image](https://github.com/damienvillegas/android-news/assets/29382810/1588cf28-ddb1-4673-a454-a691c5ab3c11)


### Step 3

Run the app! I designed the app using the Pixel 7 API 34, however any emulator or physical android should scale to fit.


![image](https://github.com/damienvillegas/android-news/assets/29382810/0cf33a47-38a2-4d60-b23b-f1b04453c965)


