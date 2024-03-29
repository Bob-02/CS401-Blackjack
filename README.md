# **BlackJack: _Software Requirements Specification_**
### Group 5:
###  _Robert Aguirre, Rebecca Taylor, Pravat Timsina, Arinze Ohaemesi_

## Revision History

| **Date**   | **Revision** | **Description**                  | **Author** |
|------------|--------------|----------------------------------| --- |
| 02/21/2024 | 0.1          | Initial Version                  | Pravat Kiran Timsina |
| 02/29/2024 | 1.0          | Ready for Phase 1 Presentation   | Robert Aguirre |
| 03/28/2024 | 1.1          | Updates for Phase 2 finalization | Robert Aguirre |


## Table of Contents

1. **PURPOSE**  
1.1. SCOPE  
1.2. DEFINITIONS, ACRONYMS, ABBREVIATIONS  
1.3. REFERENCES  
1.4. OVERVIEW  
   

2. **OVERALL DESCRIPTION**  
2.1. PRODUCT PERSPECTIVE  
2.2. PRODUCT ARCHITECTURE  
2.3. PRODUCT FUNCTIONALITY/FEATURES  
2.4. CONSTRAINTS  
2.5. ASSUMPTIONS AND DEPENDENCIES
  

3. **SPECIFIC REQUIREMENTS**  
3.1. FUNCTIONAL REQUIREMENTS  
3.2. EXTERNAL INTERFACE REQUIREMENTS  
3.3. INTERNAL INTERFACE REQUIREMENTS  
  

4. **NON-FUNCTIONAL REQUIREMENTS**  
4.1. SECURITY AND PRIVACY REQUIREMENTS  
4.2. ENVIRONMENTAL REQUIREMENTS  
4.3. PERFORMANCE REQUIREMENTS  
4.4. MAINTAINABILITY  

# Purpose
This document outlines the requirements for the BlackJack Program.

## 1.1.  Scope

This document will catalog the user, system, and hardware requirements for the BlackJack program system. It will not, however, document how these requirements will be implemented.

## 1.2.  Definitions, Acronyms, Abbreviations  

### Definitions:

**Player:** Individual participating in the game.  
**Dealer:** Entity managing and dealing cards.  
**Game/Table:** Virtual space for players to play a game of blackjack.  
**Round:** Single iteration of the blackjack game.  
**Bet:** Wagered amount of money on a round.  
**Deck:** 52-Card standard deck.  
**Hand:** Set of cards held by a player or dealer that has an integer value.  
**Hit:** Requesting an additional card.  
**Stand:** Ending a turn without additional cards.  
**Bust:** Exceeding 21, resulting in a loss.  
**Blackjack:** A hand that has 21 with an Ace and a 10-value card, resulting in an automatic win.  

### Acronyms and Abbreviations:

**GUI:** Graphical User Interface.  
**TCP/IP:** Transmission Control Protocol/Internet Protocol.  
**HTML:** Hypertext Markup Language.  

## 1.3.  References

<https://github.com/Bob-02/CS401-Blackjack>

UML Use Case Diagrams Document – Pg. 9

Class Diagrams – Pg. 10 - 11

Sequence Diagrams – Pg. 12

## 1.4.  Overview

The Blackjack program is designed to simulate an online blackjack casino. Players from the internet will login from a Java client application and connect to the casino where the player can join an open blackjack table and play a game of blackjack.

# Overall Description

## 2.1.  Product Perspective

## 2.2.  Product Architecture

The system will be organized into three major modules: the Server module, the Player/Dealer Client module, and the Game module.

## 2.3.  Product Functionality/Features

The high-level features of the system are as follows (see section 3 of this document for more detailed requirements that address these features):

## 2.4.  Constraints

Everything must be coded in Java and use no outside libraries other than the default libraries available in Java may be used to make and run the program.

There will be no browser support or will the program contain any HTML component.

A GUI must be provided for a mouse interactive experience in the casino.

## 2.5.  Assumptions and Dependencies

System needs to be able to scale up with any number of users and blackjack dealers on the server.

Players and Dealers must use the Blackjack Casinos Java client application to connect to the game’s server.

# Specific Requirements
## 3.1.  Functional Requirements

### 3.1.1.  **Common Requirements:**  
  
3.1.1.1 The Blackjack application should be available to play with a Java coded client.  
3.1.1.2 The Blackjack application must work through the internet.  
3.1.1.3 The application should simulate a casino’s Game of blackjack with Players and a Dealer
using a Deck of Playing Cards.

  
### 3.1.2.  **Server Module Requirements:** 
  
3.1.2.1 The server will be multithreading to allow for many Dealer and Player clients to
connect and interact with the server simultaneously.  
3.1.2.2 Server will store and maintain the data that stores a record of each Players and Dealers usernames and passwords that will be used to allow authorized users to join the server.  
3.1.2.3 The server will store and maintain the data that holds the records of each player's account available funds and an account balance history.  
3.1.2.4 The server should have access to the Casinos Funds so that Dealers in Games can add more house money to the table.  
3.1.2.5 The server should keep track of the number of Games with open Tables, Games with closed Tables, and all players to join a game with an open Table.  
3.1.2.6 The server will keep track and provide Dealers with the number of online players and dealers, games/tables online, full, empty, offline, or games/tables open with or without dealers.  
3.1.2.7 The server will do all calculations for Games on the server and send results to the Dealer/Player clients.  
3.1.2.8 The server will automatically close any empty game and table (no Players or Dealer) after a specified time.  

  
### 3.1.3.  **Client Module Requirements:**  
  
3.1.3.1 Clients will be Java based that will have the ability to connect to the casino server over the internet.  
3.1.3.2 There should be two client-side versions of the application, one for Dealers and one for Players with both being Java based GUI clients.  
3.1.3.3 Player and Dealer clients will not perform any game calculations but only serve as a way to interact with the server.  
3.1.3.4 Both types of clients will allow the user to login or register as a new Player or Dealer.  
3.1.3.5 Players and Dealers clients should display a login screen to be able to connect to the server with a username and password.  
3.1.3.6 Dealer clients logged into the server as an employee should be able to start a new Game and Table with their client and can manually close any table that is empty.  
3.1.3.7 Dealer clients should be able to display how many Players and Dealers are online into the server and how many of the Players and Dealers are in Games.  
3.1.3.8 The Dealer's client should allow the dealer to join and leave open games to allow Players to join the Table.  
3.1.3.9 Player clients logged into the system as a customer should be able to join games with open tables.  
3.1.3.10 Players' clients should be able to display their available funds on their accounts and history of fund history transactions.  
3.1.3.11 The Players’ client should display and allow the players to add money to their accounts for more available funds or cash out a specific amount of available funds.  
3.1.3.12 Player clients should display all games and may filter the games from open and full and allow the players to join open games.  
3.1.3.13 Players clients will display all Players and Dealer usernames connected to the Table.  
  
  
### 3.1.4.  **Game Module Requirements:**  
  
3.1.4.1 A Game contains a Table that can have up to a max 7 Players and must have one Dealer to start and continue a Game.  
3.1.4.2 Games/Tables can either be open, full, empty or without a dealer.  
3.1.4.3 Games will control the Tables game logic, payout, add and remove players.  
3.1.4.4 Dealers can specify a max number of players, up to 7, house returns (3:2, 6:5, etc.), amount of Deck of Cards for the Playing Deck and minimum bets for the Table in for any newly started game.  
3.1.4.5 Dealers should be able to specify a set amount of house money to use per Table or have unlimited access to the casino funds.  
3.1.4.6 The game will use text-based cards with each Card containing the properties of a Suit and a Rank.  
3.1.4.7 Every card can be 1 of 4 Suits (Spade, Heart, Diamond, Club) and 1 of 13 Ranks (Ace, 2 - 10, Jack, Queen, King) making a 52 standard Deck of Cards.  
3.1.4.8 Each Rank corresponds to numeric value. The Ace can be either 1 or 21 depending on if the card will bust the Player or Dealer's hand. Cards with Rank 2 - 10 have a value corresponding to their rank number. The face cards Jack, Queen and King have a value of 10.  
3.1.4.9 Every game of blackjack at each Table should use at minimum three standard 52 cardDecks for each game.  
3.1.4.10 Players should have a time limit to finish their turn.  
3.1.4.11 During each player's turn they may choose to hit (take another card) and bet more money or stand and wait for the dealer and their cards.  
3.1.4.12 Games will have the the following game rules for dealers and players:  
● The dealer's first ace counts as 11 unless it busts the hand.  
● The dealer must draw on 16 or less and stand on 17 or more.  
● The dealer can only flip their face-down card after all players have finished their first turns.  
● If the dealer has a blackjack, the hand ends immediately, and players who don't have blackjack lose their bets without having to complete the rest of the hand.  
● If a player's hand totals closer to 21 than the Dealer's, they win, and are paid the tables pay house money.  
● The player's hand is considered a bust if it's over 21.  
● If the player's hand is lower than the Dealer's, they lose their bet.  


## 3.2.  External Interface Requirements  
3.2.1.1 Both Players and Dealers should be able to log into the system from their perspective Java GUI clients. Players and Dealers should be able to login with a username and password. New registering Players should be displayed a pop-up to verify that they are 21 or older. Once logged in Dealers may administer a game of blackjack. Players may display their funds within their account and transaction history. They will have the ability to use the GUI to add funds and cashout from their fund account. Or a Player may join an open game with a table that has an open seat.  

## 3.3. Internal Interface Requirements  
3.3.1.1 Employees usernames and passwords should be kept on a backend data files in text bases documents to list and used to verify employee login credentials.  
3.3.1.2 Account details of Player’s funds and transaction history will be stored in text files on the server.  

# Non-Functional Requirements

## 4.1. Security and Privacy Requirements  
4.1.1 The Blackjack Game must implement user login.  
4.1.2 The System should ensure confidentiality in user authentication, preventing unauthorized access to player accounts and sensitive information.  
4.1.3 Dealer’s Client will not display the username for the Players in each game. They will only see what player number they are on the table.  

## 4.2. Environmental Requirements  
4.2.1 This is a Java application without a GUI that operates over TCP/IP.  
4.2.2 No databases, libraries, frameworks, or other technologies may be used.  
4.2.3 There is no web or HTML component.  
4.2.4 This system requires a server application and client application.  
4.2.5 User Interface will ensure both the Player and Dealer client versions of the application.  
are intuitive and user-friendly requiring minimal training for new players.  
4.2.6 The system should provide clear and concise error messages to assist users in understanding and resolving issues.  

## 4.3. Performance Requirements
4.3.1 The game logic and interactions must be responsive, ensuring that actions such as card dealing and player decisions occur promptly to maintain a seamless and engaging gameplay experience.  
4.3.2 The system should be designed to handle a scalable number of concurrent users, preventing performance degradation as the player base increases.  
4.3.3 The system should Implement secure authentication for users and employees. Users should only access their accounts, and employees can only access the functionality necessary for their role.  
4.3.4 Target 99.9% uptime for the server, excluding scheduled maintenance periods, to ensure users can access the game reliably.  
4.3.5 The system must possess the capability to securely store user data in an offline environment. Upon server initialization, it should efficiently retrieve this stored information for immediate use.  
4.3.6 The server architecture should be scalable, allowing for the addition of resources or services without downtime to accommodate a growing number of users.  
4.3.7 Ensure client applications perform well as the number of concurrent users increases.  

## 4.4. Maintainability  
4.5.1 The codebase should be well-documented, allowing for easy maintenance and updates by developers.  
4.5.2 Changes to the game rules or system configuration should be easy to implement without requiring extensive code modifications.  

## UML Case Diagrams:
![Phase2_UML_UseCase_Diagrams drawio](https://github.com/Bob-02/CS401-Blackjack/assets/111538673/d93a8d63-73e8-4b26-99ee-15351b82bf6f)


## Class Diagram:
![Phase2_UML_Class_Diagrams drawio](https://github.com/Bob-02/CS401-Blackjack/assets/111538673/72df15ee-ee0b-4401-b05e-ec82388a0b28)


## Sequence Diagram
![Phase2_Pravat_Login_Sequence_Diagram](https://github.com/Bob-02/CS401-Blackjack/assets/111538673/9f51bb51-f697-4fd7-97a8-05da565b2042)
![Phase2_Pravat_Sequence_diagram_Client-Server-GamePlay](https://github.com/Bob-02/CS401-Blackjack/assets/111538673/6b4cbd4b-45e0-4278-9525-c8be1b539b9a)
![Phase2_Pravat_Login_Sequence_Diagram](https://github.com/Bob-02/CS401-Blackjack/assets/111538673/412ec538-2409-43a8-9add-6e05c71148d0)
![Capture1](https://github.com/Bob-02/CS401-Blackjack/assets/111538673/2563ae18-0587-4756-bb55-74ec85f51360)
![Capture2](https://github.com/Bob-02/CS401-Blackjack/assets/111538673/ab5df8cb-c0d8-4507-a5cd-9a927bf3094e)
![Capture3](https://github.com/Bob-02/CS401-Blackjack/assets/111538673/2f09fb42-dd12-4824-80dc-ad930060f76b)



