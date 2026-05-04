# Slide Android

Slide Android is the phone/tablet side of Slide: an old Android app that turns a mobile device into a wireless or USB-connected pen tablet, touchpad, keyboard helper, and gesture controller for a desktop computer.

It was built around the idea that a phone screen, touch input, and Samsung S Pen support could act like a lightweight graphics tablet. The companion desktop app receives compact input messages from the Android app and converts them into real mouse, keyboard, scroll, zoom, copy/cut/paste, and right-click events on the computer.

## What it does

- Uses the Android device as a relative mouse/touchpad for a computer.
- Supports S Pen/touch input, including pressure-related behavior where available.
- Sends tap, click-and-drag, long-hold/right-click, scroll, zoom, and keyboard events to the desktop app.
- Can connect over Wi-Fi/LAN or over USB with ADB port forwarding.
- Broadcasts itself on the local network so the desktop app can discover it automatically.
- Provides a full-screen drawing/control canvas with optional path drawing and auto-clear behavior.

## How the pieces fit together

Slide is split across three repositories/projects in this folder:

- `slide-android`: this Android app. It captures touch, S Pen, gestures, and keyboard input.
- `slide-desktop`: the Scala/Java desktop receiver. It discovers the device, connects to it, and uses `java.awt.Robot` to perform input on the computer.
- `SlideLauncher`: a small Windows launcher/updater for the desktop JAR.

The Android app is the server for the live input stream. The desktop app discovers the phone, connects to it, then reads serialized `short[]` command packets.

## Connection flow

### Wi-Fi mode

1. The Android app starts a UDP broadcast thread when Wi-Fi is available.
2. Every couple of seconds it broadcasts:

   `phone_ip,manufacturer,model`

   to UDP port `5000` on the local broadcast address.
3. The desktop app listens for those broadcasts and shows the discovered device.
4. When the user connects from the desktop app, it opens a TCP connection to the phone on port `8074`.
5. The Android app sends Java-serialized `short[]` messages over an `ObjectOutputStream`.

### USB mode

1. The Android app detects a USB connection and checks whether Android USB debugging is enabled.
2. The desktop app uses ADB to forward a local desktop port to the Android app's USB server port.
3. The desktop app connects to `localhost` and reads the same command stream over TCP.
4. USB mode uses port `8072` on the Android side.

## Input protocol

Most commands are small `short[]` arrays. The first value is a command id, usually based on `10000`, and following values contain command parameters.

Examples:

- `10003`: movement mode / moving
- `10004`: tap
- `10005`: mouse down
- `10006`: mouse up
- `10007`: long hold / right click
- `10009`: keyboard key code
- `10011`: relative positioning configuration
- `10012` - `10017`: zoom and scroll directions
- `10018` - `10020`: cut, copy, paste
- `10002`: close connection

The desktop app maps these ids to actions in its `DeviceMessageType` enum and executes them with `java.awt.Robot`.

## Main code locations

- `app/src/main/java/com/j03/mobileinput/SettingsActivity.java`
  - Main launcher/settings activity.
  - Starts Wi-Fi broadcasting and USB detection.
  - Switches between Wi-Fi and USB connection modes.

- `app/src/main/java/com/j03/mobileinput/Canvas/CanvasActivity.java`
  - Full-screen control surface.
  - Sends keyboard, cut/copy/paste, close, and canvas-related commands.

- `app/src/main/java/com/j03/mobileinput/Canvas/CanvasView.java`
  - Drawing/control view used by the canvas activity.

- `app/src/main/java/Gesture/Controller.java`
  - Core gesture interpreter for touch, hover, S Pen, multitouch, scrolling, zooming, right-click, and pressure-click behavior.

- `app/src/main/java/Gesture/Binding/Actions/`
  - Defines command packets for triggers, toggles, and range/continuous controls.

- `app/src/main/java/Connection/Network/`
  - Wi-Fi discovery and TCP server logic.

- `app/src/main/java/Connection/USB/`
  - USB TCP server used together with ADB forwarding from the desktop app.

- `app/src/main/res/xml/preferences.xml`
  - User-facing settings for sensitivity, positioning, action bar, draw path, pressure-clicking, right-click, zoom, and scrolling.

## Building

This is an old Android project and uses the Android Gradle plugin from the Android 5.x era:

- Android Gradle plugin: `com.android.tools.build:gradle:1.2.3`
- Compile SDK: `22`
- Build tools: `22.0.1`
- Min SDK: `17`
- Target SDK: `22`
- Bundled dependencies include Samsung S Pen libraries in `app/libs/`

A historical build would have been run with:

```bash
./gradlew assembleDebug
```

On a modern machine, expect to need an older JDK/Gradle/Android SDK setup or to migrate the project before it builds cleanly.

## Running

1. Build/install the Android app on the device.
2. Start the companion `slide-desktop` application on the computer.
3. Choose a connection mode:
   - Wi-Fi: put both devices on the same LAN and allow the desktop app through the firewall.
   - USB: enable Developer Options and USB debugging on Android, then connect the phone by USB.
4. Tap the discovered device in the desktop app to connect.
5. The Android app opens the canvas. Use the screen/S Pen as the desktop pointer.

## Notable limitations

- This is archival/old code and targets Android SDK 22-era APIs.
- Absolute positioning exists in the protocol and settings, but the UI currently exposes relative positioning only.
- The protocol uses Java object serialization, so the Android and desktop implementations are tightly coupled.
- The app is optimized for the devices and Samsung S Pen SDKs available at the time.
- Network discovery depends on UDP broadcast, which can be blocked by some routers, VPNs, and firewalls.

## License

See `LICENSE`.
