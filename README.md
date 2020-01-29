
### Release Process

As of `0.0.2` there is no automated release script yet. A release can be done the following way.

 1. Ensure all TODOs for the version are either completed or closed
 2. Ensure clean git status
 3. Increment version in `project.config` and `build.sbt`
 4. Build with `./build.sh`
 5. Deploy with `.deploy.sh`
 6. SSH into the remote host with `./ssh.sh`
 7. Restart `Http` and `Server` with `./start-http.sh` and `./start-server.sh`

### Todo

#### 0.0.2

- [ ] Add server logger
- [ ] Add start scripts
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
