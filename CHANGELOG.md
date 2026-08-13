# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [3.0.0] - 2026-08-13

### Removed
- **BREAKING:** Removed the `forgetMe` command. Contentsquare removed `forgetMe` from the Android SDK (it is no longer present in `com.contentsquare.android:library` 4.52.1). There is no direct replacement; refer to the [Contentsquare privacy documentation](https://docs.contentsquare.com/en/android/privacy/) for guidance (opt out to delete data, then opt in again to resume tracking).

### Changed
- **BREAKING:** Raised the minimum supported Android API level (`minSdkVersion`) to 21, to match the Contentsquare SDK requirement.
- Raised the Contentsquare SDK dependency floor to `4.52.1` (`:[4.52.1, 5.0)`).

### Deprecated
- The `application: Application` parameter in `ContentsquareRemoteCommand` constructors is deprecated and will be removed in a future version. Migrate to `ContentsquareRemoteCommand()` (or pass only `commandId`/`description` if needed).

## [2.3.0] - 2025-06-12

### Added
- Added the `sendUserIdentifier` command.
- Added custom variables support on screen views with an index/name/value structure.

### Changed
- Dependency updates and cleanup.

## [2.2.1] - 2022-10-04

### Changed
- Version and dependency updates.

## [2.2.0] - 2022-05-17

### Changed
- Version bump and Contentsquare SDK update.

## [2.1.0] - 2022-03-16

### Changed
- Updated the Contentsquare SDK to the latest version.

## [2.0.0] - 2020-10-20

### Changed
- Updated to Contentsquare SDK 3.2.0.

## [1.0.0] - 2020-04-07

### Added
- Initial release: a [Contentsquare](https://docs.contentsquare.com/en/android/) integration with the [Tealium Android SDK](https://github.com/tealium/tealium-android) that enables Contentsquare API calls to be made through Tealium's track API.

[3.0.0]: https://github.com/Tealium/tealium-android-contentsquare-remote-command/compare/2.3.0...3.0.0
[2.3.0]: https://github.com/Tealium/tealium-android-contentsquare-remote-command/compare/2.2.1...2.3.0
[2.2.1]: https://github.com/Tealium/tealium-android-contentsquare-remote-command/compare/2.2.0...2.2.1
[2.2.0]: https://github.com/Tealium/tealium-android-contentsquare-remote-command/compare/2.1.0...2.2.0
[2.1.0]: https://github.com/Tealium/tealium-android-contentsquare-remote-command/compare/2.0.0...2.1.0
[2.0.0]: https://github.com/Tealium/tealium-android-contentsquare-remote-command/compare/1.0.0...2.0.0
[1.0.0]: https://github.com/Tealium/tealium-android-contentsquare-remote-command/releases/tag/1.0.0
