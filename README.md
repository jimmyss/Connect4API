# Connect4 Game Project

This repository contains two separate projects: **Connect4API** and **Connect4Client**. The `Connect4API` project provides all the necessary APIs for the Connect4 game, while the `Connect4Client` project implements a test-based version of the game by utilizing the API.

## Folder Structure

- **Connect4API/**  
  This directory contains the Connect4 game API, which exposes all the necessary functionalities to create and interact with a Connect4 game.  
  The project structure is as follows:
  - `src/`  
    Contains the source code for the API.
  - **Connect4API.jar**  
    A precompiled JAR file of the Connect4 API is generated and located here. This JAR is used by the Connect4Client project to interact with the API.

- **Connect4Client/**  
  This directory contains a test-based Connect4 game client, which uses the Connect4API to implement the game logic. The client can play the Connect4 game using the API from the `Connect4API.jar`.  
  The project structure is as follows:
  - `src/`  
    Contains the source code for the Connect4 game client.

- **docs/**
    Contains the documentation of the API. You can find detailed explanations of each API function, their parameters, and expected behavior.

## How to Run

### Prerequisites
1. Ensure that Java is installed on your system. You can check this by running `java -version` in your terminal or command prompt.
2. Ensure that the `Connect4API.jar` is properly imported into the **Referenced Libraries** section of the Connect4Client project or make it available in the `lib/` folder of the `Connect4Client` project.

### Running the Game Client
1. Navigate to the `Connect4Client` folder.
2. Compile the source code using the following command:

   ```bash
   javac -cp lib/Connect4API.jar src/*.java
   ```
3. Run the client with the following command:

    ```bash
    java -cp lib/Connect4API.jar:. src.App
    ```
    This will launch the text-based Connect4 game in your terminal

## Documentation
For detailed information about the API and the methods available, please refer to the documentation available in the `docs/` folder.