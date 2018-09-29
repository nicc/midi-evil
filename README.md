# midi-evil
A toy project that draws visual elements based on midi notes.

## General
This is still in a fairly rough state.
What we have so far...
- midi signal path 
- quil integration
- the ability to draw basic circles on note events
- basic note->colour mapping that _should_ result in some consistency between colour and music harmony


TODO:
- structural rewrite to incorporate category theory principles (see `simple-applicative` test and corresponding comment in `main-test`)
- consider a different key for drawstate map. using note is very limited. should separate realtime note value tracking from resultant drawstate (hence the intended use of applicatives).
- come up with some fun mutating functions for morphing visual elements.

Caveats:
This project was started (very hungover) as a toy to try Quil. It was some of the earliest Clojure I wrote. I've worked on it very intermittently since then during various points in my own progression. It's a mixed bag and not very serious.

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
Volume: 0 - 127
