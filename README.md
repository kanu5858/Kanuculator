# Kanuculator - Professional Android Calculator

Kanuculator is a sleek, modern, and professional calculator application built with Jetpack Compose. It combines a minimalist "Black" aesthetic with a powerful mathematical engine capable of handling both standard and scientific calculations.

## ✨ Features

-   **Professional UI**: Deep black theme inspired by premium mobile calculators.
-   **Dual Mode**:
    -   **Standard Mode**: Clean interface for everyday arithmetic.
    -   **Scientific Mode**: Accessible via a quick-toggle "√" button, providing functions like `sin`, `cos`, `tan`, `log`, `ln`, powers, and constants (`π`, `e`).
-   **Dynamic Typography**: Intelligent font scaling that shrinks text as expressions grow longer to prevent clipping.
-   **Smart Engine**: Uses `exp4j` for robust expression evaluation, handling operator precedence and complex scientific formulas.
-   **Expression History**: Tracks your last calculation in a subtle secondary display line.
-   **Input Safety**: Auto-closing parentheses and intelligent error handling.

## 🛠 Tech Stack

-   **Language**: [Kotlin](https://kotlinlang.org/)
-   **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
-   **Material Design**: [Material 3](https://m3.material.io/)
-   **Math Engine**: [exp4j](https://www.objecthunter.net/exp4j/)
-   **Architecture**: Modern Android Development (MAD) practices.

## 🚀 Getting Started

1.  **Clone the repository**:
    ```bash
    git clone https://github.com/yourusername/Kanuculator.git
    ```
2.  **Open in Android Studio**:
    Open the project using Android Studio.
3.  **Sync Gradle**:
    Let the project sync and download dependencies (specifically `exp4j`).
4.  **Run**:
    Deploy to an emulator or physical device running Android 7.0 (API 24) or higher.

## 📸 Screenshots

| Standard Mode | Scientific Panel |
| :---: | :---: |
| ![Standard](https://via.placeholder.com/200x400?text=Standard+UI) | ![Scientific](https://via.placeholder.com/200x400?text=Scientific+UI) |

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
