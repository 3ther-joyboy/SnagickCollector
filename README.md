# Snagick Collector (doc)
## About
This is a backend for a server that collects cards from a copy of [MTG](https://en.wikipedia.org/wiki/Magic:_The_Gathering) that we play in scout called Snagicky.
For a long time we had just bunch of Excel documents that hold the whole thing barely to gather, consistent naming convention out of the winow and non-functioning search (Just the names). 
And that's not mentioning that this is "old" (started at 2007) board game, meaning many these excel "databases" have been lost to time.
## Database
Database has main 6 table that are connected by another, additional 3 to connect them (55 colums in the whole) and 2 procedures. <br> In the database there are stored mainly [Cards](#card) and then [Users](#user) that can save the cards that they like or the cards they own (this game have a hevely involved trading element) 
### Card
### User
<img src="https://3ther.org/apps/files_sharing/publicpreview/jifBMBBasNccXRq?file=/&fileId=3191&x=1920&y=1080&a=true&etag=98cb858f665582a2f255b2b38b949764" width="500">

# Setup
Spring boot can handle most of the things by it self, how ever it needs some aditional nudge on the start
