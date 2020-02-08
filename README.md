### What is ReSux?
Project Resux is the latest iteration, as of Feb 2020, in a series of projects seeking to create an open source, modular, extendable and robust, online multiplayer Real Time Strategy (RTS) game. Since the project started as a simple experiment, the goal of the project has changed and developed over time.

As it currently stands, the project has a few key points of ideology which it strives to follow, known as the 'Pillars of Development'.

 - Separated Client and Server
 - Scalable
 - Modular, Robust and Extendable

#### Separated Client and Server

The first Pillar of Development is that of maintaining a separation between the client and server. The idea is that the client is, and will always be, regardless of gameplay, simply a 'viewer of deterministic state'. The server and only the server is responsible for changing the game state, which is then simply 'viewed' by the client. If the client wishes to change the state of the game, it can request the change from the server, which then may or may not make the change, depending on it's gameplay logic.

As a very exaggerated example, a minimal client could be a client which simply renders the JSON of the game state to the user. The user can then request changes to this state by sending requests to the server, for example using `curl`. In reality, this is unrealistic, and a more reasonable client would render the state in an easily understandable and aesthetically pleasing manner, and allowing the user to send 'state change requests' to the server in a user friendly way.

To formalize this principle, let us agree the state can only be changed using a `WorldAction` and the server can only be requested to issue `WorldActions` using a `ClientAction`. Furthermore, `ClientActions` can only be sent **from client to server**, while `WorldActions` can only be sent **from server to client**. In order for a client to legally implement this specification, the client must only modify it's local representation of the game state by applying the `WorldActions` received from the server. Furthermore, it must apply each received `WorldAction` only once, as not all `WorldActions` are guaranteed to cause idempotent changes to the state.

#### Scalable

The second Pillar of Development is that of scalability. As a network game, particular attention needs to be given to how much data is being sent over the network. The vast majority of network data comes from synchronizing the game state between the server and the clients. A brute force approach would be to simply send a serialized representation of the entire game state to all clients whenever the state changes. In a RTS game, the state changes on every tick, which usually occurs 10 times a second. Attempting to serialize and send the entire state to all clients this often would use a considerable about of bandwidth, and vastly limit the maximum size of the game state, since a bigger game state means more network data being sent on each tick. In order to be scalable, the amount of data sent over the network on each tick should not increase linearly with the size of the game state itself.

The initial solution to this issue was to simply send the `state delta` over the network. This does solve the scalability problem because no matter how big the game state grows to be, only the change is transmitted over the network. However, in practice, representing a state delta is difficult, since the delta needs to be calculated, serialized, and then finally applied on the client. A more robust solution is to simply describe the state change in a predefined manner, such as an `Action`, which, as explained earlier, is formally called a `WorldAction`.

This has the added benefit that changing the state on the server is done in exactly the same manner as the client, by simply 'applying' the `WorldAction` to the game state. This means, the server can use the game rules to determine which `WorldActions` need to be applied to the game state, and then simply dispatch the `WorldActions` to both the server's own game state and all the clients' game states by sending the `WorldActions` over the network.

#### Modular, Robust and Extendable

The third Pillar of Development is that of modularity, robustness and extendability. Software changes, no matter how well thought through or how well planned, it is inevitable. It is thus of high importance to write functionality in as modular a way as possible. Any added functionality should be thought of as a module, following the well known SOLID principles.

### Code Style

Project Resux is written in Scala, which provides great support for functional programming, and thus it is encouraged to conform to the functional programming paradigm as much as is **reasonable**. The key word in this statement is 'reasonable'. The ultimate goal of any software source code should be that of easy understanding and readability. Functional programming can help in this regard, but this ultimate goal of better readability should not be forgotten, and if a slightly more imperative approach suits a specific task better, one should not hesitate to forgo the strict constraints of pure functional programming.

#### Utilities

A key component of software development is that of abstraction and reusability. It is therefore logical to abstract commonly used patterns and functions into a utility library. The utility library is only useful if it is used, thus it is critically important to familiarize yourself with the existing utility library, so it is used as much as possible, reducing code duplication.

### Release Process

As of `0.0.2` there is no automated release script yet. A release can be done the following way.

 1. Ensure all TODOs for the version are either completed or closed
 2. Ensure clean git status
 3. Increment version in `project.config` and `build.sbt`
 4. Commit version change
 5. Build with `./build.sh`
 6. Deploy with `./deploy.sh`
 7. SSH into the remote host with `./ssh.sh`
 8. Restart `Http` and `Server` with `./start-http.sh` and `./start-server.sh`

### Todo

#### 0.0.3

- [ ] Client mouse input
- [ ] Click to select
- [ ] Right click to move
- [ ] Initial path system (Move concatenation, not path-finding)

###### Optimizations

- [ ] Minimize Draw calls in GridRenderLayer with Paths

#### 0.0.2

- [ ] Add server logger
- [x] Add sprite rendering
- [x] Entity Render Layer
- [x] Performance Debug Timer
- [x] Performance Debug Statistics
- [x] Layout server orchestration
- [x] Add DeterministicVector2F
- [ ] Add creation and manipulation utilities for DeterministicVector2F
- [ ] Add movement methods to orchestration (Teleport, Move from A to B, Move from Current to B)
- [ ] Initial spec system
- [x] Add attributes to entities
- [ ] ~~Add sprite LODs~~ (Measurements reveal LODs are not needed)
- [x] Add start scripts
- [ ] ~~Create initial Vector Render System~~ (Sprites will suffice for now)
- [x] Add Path2D JS interface
- [x] Extend rendering context with Path2D
- [ ] ~~Create release script to increment version and deploy~~

#### 0.0.1

Initial Canvas Debug Renderer, Input Handler, Camera, Action Serialization and Deployment

- [x] Inject version info into client production build
- [x] Inject version info into client development build
- [x] Change version info to use true running computed hash
- [ ] ~~Read build.sbt version from project.config~~
- [ ] ~~Pipeline tool~~
- [x] Clean comments
- [x] Fix multiple connections bug
- [x] Create initial configuration solution
- [x] Create todo list
- [x] Deployment
- [x] Action Serialization
- [x] Camera
- [x] Initial Input Handling
- [x] Canvas Renderer
- [x] Debug Render Layers

### Proposals

#### Vector Render System

TODO
