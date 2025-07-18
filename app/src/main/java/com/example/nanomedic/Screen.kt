package com.example.nanomedic

// 'sealed' keyword
// Example (React):
// A Route can ONLY have a 'type' of 'CAMERA', 'LOADING', or 'GUIDE'.
//type Route =
//  | { type: 'CAMERA'; routeName: string }
//  | { type: 'LOADING'; routeName: string }
//  | { type: 'GUIDE'; routeName: string };

// 'data' keyword tells the compiler that this class is used to store data. In return, the compiler automatically generates useful functions for you that you would otherwise have to write by hand. These include: equals(), hashCode(), toString(), and copy().

// 'object' keyword creates a Singleton. A singleton is a class that is guaranteed to have only one instance throughout the entire application. You don't need to create an instance of it with a constructor; you just use its name directly.

sealed class Screen(val route: String) {
    data object Camera : Screen("camera_screen")
    data object Loading : Screen("loading_screen")
    data object Guide : Screen("guide_screen")
}