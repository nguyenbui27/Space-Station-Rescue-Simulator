# Space Station Rescue Simulator

A basic pixel game: move your shuttle, aim ahead of a drifting space station,
and deliver fuel across three increasingly fast rescue rounds.

**[Play in your browser](https://nguyenbui27.github.io/Space-Station-Rescue-Simulator/)**

## Controls

Arrow Up/Down or K/J move; Space fires. P pauses, R restarts.
Mouse/touch buttons provide the same controls. Select Fullscreen to fill the
screen (browser support required). Losing window focus pauses the game.

## Verified project numbers

- 3 rounds and 3 difficulty settings.
- 5-cell capacity; +2 cells per successful delivery, capped at 5.
- 840-point maximum for three first-shot deliveries: 180 + 280 + 380.
- 800 × 600 logical playfield with responsive pixel rendering.
- 13 passing automated engine tests; no third-party runtime dependencies.

These are implementation and test results, not player counts or performance
benchmarks. Collision checks use a swept relative segment to avoid tunneling.

## Tech stack

Original desktop version: Java, Swing, AWT, object-oriented classes.
Playable browser edition: JavaScript ES modules, HTML5 Canvas, HTML/CSS,
Pointer Events, Fullscreen API. Tests: Node.js built-in test runner.
Hosting: GitHub Pages.

## Run locally

`npm run dev`, then open http://localhost:8031. `npm test` runs the engine tests.
For the Java version, run `cd java` followed by `./run.command` (JDK required).

## Source preservation

`java/original/` contains all 9 original Java sources unchanged.
`java/` contains their extended desktop version and the added pixel renderer.
The browser edition is an additional port, not a replacement for the Java code.
The original educational project credits remain in the original source headers.

## Checks

Engine tests cover initial state, pre-start launch blocking, fuel consumption,
pause/resume, movement limits, difficulty speeds, round progression, reward cap,
three-round win/scoring, swept collision, fuel exhaustion, station loss, restart.
Desktop compilation is verified; desktop fullscreen still needs local QA.
