# midi-evil
A toy project that draws visual elements based on midi notes.

## General
This is still in a fairly rough state.
What we have so far...
- midi signal path 
- quil integration
- basic note->colour mapping that _should_ result in some consistency between colour and music harmony
- realtime device state for inferring things like chord identification and velocity spread
- a decent draw state model
- mutator functions in the state model that can alter draw params (to add stuff like fades and movement to the visual elements)
- draw functions that can be dynamically bound to the state model on note entry (to do stuff like bind velocity or octave to different draw styles)


TODO:
- deal with non-note midi commands like aftertouch and sustain. currently these error out
- come up with some fun draw and mutator functions for rendering dynamic visual elements

Caveats:
This project was started (very hungover) as a toy to try Quil. It was some of the earliest Clojure I wrote. I've worked on it very intermittently since then during various points in my own progression. It's a mixed bag and not very serious. The latest iteration incorporates some category theory principles. Future intentions include running this on a raspberry pi and adding visualisations that act as learning aids for the piano.

## Quickstart
1) clone the project
2) plug in a midi controller
3) `boot repl`
4) `(require 'main)`
5) select midi device from the list in a launched applet window
6) play notes
7) see circles

### Notes
Notes: 0 - 127 (middle C is at 60)
