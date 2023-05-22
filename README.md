# ReaCollider

Use Reaper from SuperCollider. 

Currently this package includes ways to generate projects for Reaper and an interface for the reaper-commandline tools, allowing among other things batch conversion of sound files from within SuperCollider using Reaper's fxchains.

## Usage examples

### Control Reaper using a pattern

A custom event type allows control a reaper track using a pattern:
```supercollider
(
 r = ReaperControl.start(9292);
 Pdef(\r,
     Pbind(
         \type, \reaperTrack,
         \reaperControl, r,
         \reaperTrack, 1,
         \volume, Pwhite(0.0,1.0),
         \pan, Pwhite(-1.0,1.0)
         )
     ).play;
 )
```

## Installation

From within SuperCollider:

```supercollider
Quarks.install("https://github.com/madskjeldgaard/ReaCollider")
```
