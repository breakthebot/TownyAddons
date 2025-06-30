# TownyAddons
A mix of addons for Towny, created for EarthMC  
Ideas sourced from the EMC Roadmap

## Resident
**Commands**
- `/res onlinefriends` [n13vdsnf](https://github.com/orgs/EarthMC/projects/11/views/2?pane=issue&itemId=104938383)  
  List online friends
- `/res outlawednations` [73gti356](https://github.com/orgs/EarthMC/projects/11/views/2?pane=issue&itemId=71519296)  
  List nations where you're outlawed
- `/res set wiki` [rco8lvuz](https://github.com/orgs/EarthMC/projects/11/views/2?pane=issue&itemId=56892865)  
  Set a link for your page on the EMC Wiki!  
  Visible on your `/res` page
- `/res trustlist` [yfm42d2u](https://github.com/orgs/EarthMC/projects/11/views/2?pane=issue&itemId=57781718)
  & [qi4rma6o](https://github.com/orgs/EarthMC/projects/11/views/2?pane=issue&itemId=70040216)  
  List towns you're trusted in

**Features**
- Cancel Invites [io6xhtf3](https://github.com/orgs/EarthMC/projects/11/views/2?pane=issue&itemId=105133034)  
  Upon joining a town, all of a resident's pending invites are cancelled  
  This is beneficial for both the resident and all the towns that have used up an invite

**Status Screen**
- Wiki  
  View a resident's linked wiki page

## Town
**Commands**
- `/t set plotlimit`  [zw696t2p](https://github.com/orgs/EarthMC/projects/11/views/2?pane=issue&itemId=112003360)  
  Limit how many plots a resident can claim  
  NOTE: Code commented out due to a [Towny bug](https://github.com/TownyAdvanced/Towny/issues/7868)
- `/t set discord` [mwfxfw5n](https://github.com/orgs/EarthMC/projects/11/views/2?pane=issue&itemId=57799203)  
  Set a discord for your town  
  Visible on `/t` lookup
- `/t resetperms` [cln1u95x](https://github.com/orgs/EarthMC/projects/11/views/2?pane=issue&itemId=78808672)  
  Completely reset a town's permission overrides.  
  Remove all members of `/t trust list`, remove all players in each `/plot trust` as well as `/plot perm gui` permission overrides.
- `/t log`  [6yy4qe9h](https://github.com/orgs/EarthMC/projects/11/views/2?pane=issue&itemId=60482575)
  & [mj5kyw77](https://github.com/orgs/EarthMC/projects/11/views/2?pane=issue&itemId=84759143)  
  List recent resident join & leave activity
- `/t toggle friendlyfire (on|off)`  [zwipd7cj](https://github.com/orgs/EarthMC/projects/11/views/2?pane=issue&itemId=78804596)  
  Toggle a town's friendlyfire allowed status.  
  This stops `/plot toggle pvp` in all chunks in the town. Only players with the bypass permission node will bypass.

**Features**
- Overclaim Notify  [nvpov4w5](https://github.com/orgs/EarthMC/projects/11/views/2?pane=issue&itemId=82337873)  
  Notify a town's mayor when their town gets overclaimed. Messages are queued until mayor logs on.

**Status Screen**
- Outsider spawn [s16l4zd1](https://github.com/orgs/EarthMC/projects/11/views/2?pane=issue&itemId=70849842)  
  Display if the town is paying for outsiders to teleport
- Discord  
  View a town's linked discord
- Overclaims remaining [yhb7jsqi](yhb7jsqi)  
  Display how many plots the town is allowed to overclaim

## Nation
**Commands**
- `/n set discord`  
  Set a discord for your nation  
  Visible on `/n` lookup
- `/n shout` [5afc1yfa](https://github.com/orgs/EarthMC/projects/11/views/2?pane=issue&itemId=57781740)  
  Broadcast a global message on behalf of your nation at a set cost

**Features**
- Broadcast rename [6hiljxyc](https://github.com/orgs/EarthMC/projects/11/views/2?pane=issue&itemId=71485879)  
  Send a global message when a nation is renamed

**Status Screen**
- Discord  
  View a nation's linked discord
- Resident count [7usd97yj](https://github.com/orgs/EarthMC/projects/11/views/2?pane=issue&itemId=112003458)  
  Display how many residents the nation has
- Nation bonus [8a3b3174](https://github.com/orgs/EarthMC/projects/11/views/2?pane=issue&itemId=84758983)  
  Display a nation's nation bonus

## Plot
**Vault plot type** [oxv9whay](https://github.com/orgs/EarthMC/projects/11/views/2?pane=issue&itemId=70038439)  
Upon a plot being set to Vault using `/plot set vault`, all members trusted in the town will get all 4 deny-permission overrides in the plot  
Upon someone new being trusted in the town, all plots set to vault will also add deny-permission for the newly trusted player

## DiscordSRV
Towny integration into DiscordSRV

**Features**
Broadcast the following events on discord
- Nation created
- Nation disbanded
- Town created
- Town ruined
- Town deleted


## Config
### Settings for `/n shout`
maxShoutChars: 128  
ShoutCooldownHours: 6  
ShoutPrice: 128
### Settings for `/res set wiki`
wikiURL: "https://wiki.earthmc.net/"
### Settings for DiscordSRV Towny updates
discordSRVChannelID: 969978366394511420
## Permissions
### Perms for `/n shout`
`towny.command.nation.shout`  
Allows using /n shout
### Perms for `/res set wiki`
`towny.command.resident.set.wiki`  
Allow residents to set & remove their /res wiki  
default: true
### Perms for `/t set discord`
`towny.command.town.set.discord`  
Allow players to set & remove their /town discord
### Perms for `/n set discord`
`towny.command.nation.set.discord`  
Allow players to set & remove their /nation discord
### Perms for `/t set plotlimit`
`towny.command.town.set.plotlimit`  
Allow players to set & remove their town's limit for how many plots a resident can claim
### Perms for `/t toggle friendlyfire`
`towny.command.town.toggle.friendlyfire`  
Allow players to toggle their town's friendlyfire-allowed status  
`towny.town.friendlyfirelimitation.bypass`  
Allow players to bypass friendlyfire-allowed restriction, and to be able to toggle a plot's PvP status
### Perms for `/t log`
`towny.town.log.view`  
Allow players to view recent joins & leaves in their own town