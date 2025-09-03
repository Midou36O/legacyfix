# Legacy Auth Modernizer

Uses modern Authentication URLs that Mojang uses on modern versions of Minecraft, as an added bonus, supports 3rd party authentication through the same API modern minecraft offers to replace them.

## What's the problem ?

BTA Already fixes the authentication of beta 1.7.3 by replacing www.minecraft.net with session.minecraft.net, but who can guarantee those endpoints will stay up in the foreseeable future? This is where the mod
steps in and proceeds to fix the issue.

-   Replaces the complete login of session.minecraft.net legacy authentication with the JSON equivalent of [/session/minecraft/join](https://minecraft.wiki/w/Java_Edition_protocol/Encryption#Client) (Client side)
-   Replaces the old check of session.minecraft.net legacy authentication with the JSON equivalent of [/session/minecraft/hasJoined](https://minecraft.wiki/w/Java_Edition_protocol/Encryption#Server) (Server side)

As a bonus, the -Dminecraft.api.\* endpoints are supported, which should allow for third party authentication servers.

## Building

Just get intelliJ and build the artifact.
