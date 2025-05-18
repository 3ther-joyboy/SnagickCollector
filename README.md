# Snagick Collector (doc)
## Navigation
  - [About](#about)
  - [Technologies](#technologies)
  - [Database](#database)
    - [Database Structure](#database-structure)
    - [Card](#card)
    - [Edition](#edition)
    - [Type](#type)
    - [SubType](#subtype)
    - [User](#user)
    - [Token](#token)
      - [Perrmission list](#perrmission-list)
      - [Other](#other)
  - [End Points](#end-points)
    - [Card Advanced Get](#card-advanced-get)
  - [Setup](#setup)
## About
This is a backend for a server that collects cards from a copy of [MTG](https://en.wikipedia.org/wiki/Magic:_The_Gathering) that we play in scout called Snagicky.
For a long time we had just bunch of Excel documents that hold the whole thing barely to gather, consistent naming convention out of the winow and non-functioning search (Just the names). 
And that's not mentioning that this is "old" (started at 2007) board game, meaning many these excel "databases" have been lost to time.
## Technologies
Whole project is mainly created in Java21 with SpringBoot, but other technologies had to be user to make it work.
  - Java 21
    - Spring Boot - For connecting to database and creation of rest api
  - MariaDB - Open source database program
  - PostMan - RestApi tester
  - Fedora - ServerOS by RedHead
  - CloudFlare - DNS server for [3ther.org:8080](http://3ther.org:8080/api/test/) (Mine server is runing on port ``:8080``)
  - Google - For sanding emails
## Database
Database has main 6 table that are connected by another and additional 3 to connect them (56 colums in the whole database). <br> In the database there are stored mainly [Cards](#card) and then [Users](#user) that can save the cards that they like or the cards they own (this game have a hevely involved trading element) 
### Database structure
[Card](#card) has a (Many-To-One) [User](#user) who created it, [Users](#user) that saved the card or is owned by them (Many-To-Many), a [Type](#type) (One-To-Many) and [Edition](#edition) theyr are apart of (Many-To-Many). <br>
[Type](#type) has additionaly a [SubType](#subtype) attached to them (Many-To-One). There are a lot of types of creatures, but all creatures behive the same meaning all creatures types will have same creature sub type. <br>
[Users](#user) have [Tokens](#token) connected to them to allow them log it to the server (One-To-Many). 
<img src="https://3ther.org/apps/files_sharing/publicpreview/jifBMBBasNccXRq?file=/&fileId=3191&x=1920&y=1080&a=true&etag=98cb858f665582a2f255b2b38b949764" width="500">

### Card
Every card have colums for it costs and everything is storet as a ``byte`` (Snagickys have just 4 basic elements unlike MGT where there are 6 + multicolor)
  - ``red``
  - ``blue``
  - ``green``
  - ``white``
  - ``multi``
  - ``special_cost`` - If some cards cost some X mana it has to be writen as a string for displaying (``xxb2``, for X X and 2 blue manas)

Colums that describes the card
  - ``name``
  - ``description`` - What abilityes does the card have
  - ``story`` - Small bit of throw away gag about the card
  - ``note`` - If creators need something to say about the card, for an example if the card is banned in turnament play.
  - ``rarity`` - How rare is the card, basic ratings are ``c u r`` but sometimes card with ``S``pesial rarities are created.
  - ``type`` - Links to waht [Type](#type) the card is
  - ``id`` - Unike identifier for the card

Most of the cards are creatures therefore they have ``attack`` and ``defense`` by default
  - ``attack``
  - ``defense``

Bonus info that doesnt isnt needed most of the times
  - ``created_by`` - By who the card was created, eather as a someone who put it in the database, or who have come up with the consept
  - ``created`` - when the card was created in the database
  - ``updated`` - when was the last update of the card
### Edition
Editions have just a few information because they are not needed for anything (Mabe just how rare the card is compare to somehting else)
  - ``id``
  - ``name`` - Name of the edition
  - ``description`` - How old is the Edition out, if there are any reprints or any possible usefull information
### Type
  - ``id``
  - ``name``
  - ``sub_type`` - There are usually types of "types" (red cristal, blue cristal.. its still a cristal and it acts like it)
### SubType
  - ``id``
  - ``name``
  - ``description`` - How this type acts, what are its special trades (creatures can attack and block, cristals give mana every turn)
### User
  - ``id``
  - ``bio`` - To tell something about the user (In our scout group it would be something like a discord tag)
  - ``name`` - Has to be unike
  - ``password`` - hased, pappered, salted password
  - ``re_email`` - email for any authenticiation
  - ``perrmission`` - Perrmission level (0 - Unverified, 1 - User, 2 - Admin, 3 - Root)
  - ``ctime`` - Creation time
  - ``utime`` - Last Updated
### Token
Tokens that are more accuratly called sessios, they grand users acces to theyr account and allows users to do sertant things. (create consept cards, create real ones or delete cards) <br>
Perrmissions are stored in the [Token](#token) table (in hindsight, it wasnt a good idea). The idea was to create a system where administrator can give timed privilegia to other user to help him out (When we are on a meating, I could allow some scouts to manage some stuff and when the meeting ends, the tokens would terminate). How ever, you cannot say what token is user currently using, it takes aditional space in database, the privilegias are generated on user perrmissions level anyway and when updating someones perrmissions they have to re-log to create new token where are theyr new perrmissions.
#### Perrmission list
  - ``verify_self`` - Users cannot veryfi them selfs on theyr own, a special tokenw with this perrmissions is generated and send on theyr email, or a user with ``verify_other`` can verify them insted
  - ``verify_other`` - With this token, users can verify others (from perrmission 0 to 1)
  - ``edit_self`` - User can edit theyr name, bio, or what ever they have on theyr profile
  - ``delete_self`` - This token is eather send on users email, or it has to be requested on the site. By default this perrmissions is false due to cross site scripting.
  - ``edit_lower`` - User can edit profiles of other users that have lower perrmission levl then him.
  - ``create_cards`` - Allows user to create cards with editions/types/subtipes and asign them
  - ``create_test_cards`` - Allows normal users to create cards without edition attached to them (these cards doesnt show up in the card search unless specificly searched for them)
  - ``edit_cards`` - Deletion of existing cards, transfer of ownership (who created them) and editing them.
  - ``change_perrmission`` - Allows user to change perrmissions of lower rated users (how ever not allowed to put them on the same perrmission level as them: Root[3\] user can give just Admin[2\] privilegia)
  - ``change_password`` - Allows user to change theyr current password, it has to be requested or sended to email.
#### Other
  - ``termination_date`` - Token delete it self after passing this date
  - ``created`` - when the token was created
  - ``user`` - what user does own this token
  - ``code`` - Acces code to this token

## End Points
### Card Advanced Get
There are multiple ways to do this, send a Card model in the body (same as creating a new card). Problem with this is when user wants to send someone cards they found, but they are unable to just copy the url link because thatwould have the header/body missing. There fore everything can be searched in query parameters (part after ? in urls ``/card/?equals_id=1``). <br>
For more options before saying what paramteter to search for you add what type of search you use. <br>
  - General search
    - ``search_###`` - Searches in text
    - ``equals_###`` - Has to equal
    - ``higher_###`` - Parameter in database has to be higher then given number
    - ``lower_####`` - Parameter in database has to be lower then given number
    - ``not_######`` - Is not equal
    - ``link_#####`` - Works like qual, but on many-to-many/one-to-many conenections (for anexample, search for cards owned by this user ``/?link_OwnedBy=1``)
  - Aditional options
    - ``sort`` - What column should sort searched cards
    - ``rsort`` - Reverse sort
    - ``page`` - What page of of the search you are on
    - ``size`` - How big is the page (default is 20 cards)
    - ``type`` - Same as using link, just nicer
    - ``edition`` - Same as link, just nicer. If you arnt doing anything, cards without edition (cards created by someone other then admins) arnt showing up, you can filter just for these cards by including this paramteter and not filling it. 
I have saved all end points in to PostMan database to try out
# Setup
Spring boot can handle most of the things by it self, how ever it needs some aditional nudge on the start. <br> 
First of all, there is a config file at ``/src/main/resources/application.properties`` that has to be filled with with basic information.
 - Aplication details
   - Application name
   - Server port (make sure to open that port on your computer, router and check if your dns server isnt forwarding that port for some reason)
 - Database
   - Create database, user and give the user perrmissions for the database (no table creation needed)
   - Database url, locally or anywhere alse
   - Database user and password
 - Pepper
   - In [User](#user) class there is a String that should be replaced for "security" to change the hash of the finall password
 - Email (tested with gmail amd pm gmail servers)
   - name is your (that you have password and everything) email adress: ``noreplay@gmail.com``
   - your password is a passkey that you have to generate on [google](https://www.google.com/account/about/passkeys/) 
 - Root User
   - You cannot have a Root user unless you edit perrmissions of already created user throught database
