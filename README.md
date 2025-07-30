# Custom Android Launcher

A feature-rich Android launcher built with Kotlin and Jetpack Compose, offering extensive customization and modern design.

## Features

### 🏠 Home Screen
- **Multiple Pages**: Swipe between multiple home screen pages
- **Customizable Grid**: Adjustable grid layout (3-6 columns, 4-8 rows)
- **Page Indicators**: Visual dots showing current page position
- **Drag & Drop**: Rearrange apps and widgets by dragging
- **Quick Actions**: Bottom dock with app drawer access

### 📱 Widget Support
- **Native Android Widgets**: Support for all standard Android widgets
- **Drag & Drop**: Add and position widgets anywhere on home screen
- **Resizable**: Widgets can be resized to fit your layout
- **Widget Host**: Proper widget lifecycle management

### 🔍 App Menu
- **Grid Layout**: Customizable app grid (3-6 columns, 4-8 rows)
- **Search Functionality**: Quickly find apps by name or package
- **Alphabetical Sorting**: Apps sorted alphabetically for easy browsing
- **Smooth Animations**: Slide-in app drawer with smooth transitions

### 🎨 Customization
- **Grid Size**: Customize grid dimensions for both home and app menu
- **Icon Size**: Adjust app icon size (0.5x to 2.0x)
- **Page Indicators**: Toggle page indicator visibility
- **Wallpaper Support**: Custom wallpaper with parallax scrolling

### 🎯 Drag & Drop
- **Home Screen**: Drag apps from menu to home screen to create shortcuts
- **Rearrangement**: Long-press and drag to rearrange items
- **Multi-page**: Move items between different home screen pages
- **Visual Feedback**: Items scale and lift during drag operations

## Technical Implementation

### Architecture
- **MVVM Pattern**: Clean separation with ViewModel for state management
- **Jetpack Compose**: Modern declarative UI framework
- **Material 3**: Latest Material Design components and theming
- **Kotlin Coroutines**: Asynchronous operations and state management

### Key Components
- **LauncherViewModel**: Central state management for all launcher operations
- **AppManager**: Handles app discovery and launching
- **Widget System**: Custom widget host implementation
- **Grid System**: Flexible grid layout for apps and widgets
- **Settings**: Persistent configuration storage

### Permissions
- `QUERY_ALL_PACKAGES`: Access to all installed applications
- `SET_WALLPAPER`: Wallpaper customization
- `BIND_APPWIDGET`: Widget support

## Installation

1. Clone the repository
2. Open in Android Studio
3. Build and install on your Android device
4. Set as default launcher when prompted

## Usage

### Setting as Default Launcher
1. Install the app
2. Press home button
3. Select "Custom Launcher" and choose "Always"

### Adding Apps to Home Screen
1. Open app drawer (tap the floating action button)
2. Find the app you want to add
3. Long-press and drag to home screen
4. Position where desired

### Customizing Layout
1. Long-press on home screen
2. Access settings from the menu
3. Adjust grid size, icon size, and other preferences

### Managing Widgets
1. Long-press on empty space on home screen
2. Select "Widgets" from context menu
3. Choose widget and position on home screen

## Development

### Building
```bash
./gradlew assembleDebug
```

### Running Tests
```bash
./gradlew test
```

### Code Structure
```
app/src/main/java/com/shalmon/myapplication/
├── data/           # Data models and structures
├── ui/
│   ├── components/ # Reusable UI components
│   ├── screens/    # Main screens (Home, Menu, Settings)
│   └── theme/      # App theming and colors
├── utils/          # Utility classes
├── viewmodel/      # ViewModel classes
└── widget/         # Widget management
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Future Enhancements

- [ ] Folder support for grouping apps
- [ ] Gesture support (swipe gestures for quick actions)
- [ ] Icon pack support
- [ ] Backup and restore settings
- [ ] Theme customization
- [ ] Advanced widget configuration
- [ ] Smart folder auto-categorization
- [ ] Search suggestions and history