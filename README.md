# EBook Library — Android Application

This is the Android client for the EBook Library platform, allowing users to browse, read, and manage PDF books. Admin users can upload new books and delete existing books through the app.

## 📱 Features

- View available books (title, author, cover image, description)
- Mark books as read / unread
- User authentication (signup, login, JWT-based sessions)
- Admin book upload (title, author, description, whole book PDF, cover image)
- Admin book delete from Book Details activity.
- PDF reading via Android PDF Viewer
- Secure token storage using SharedPreferences
- Firebase Cloud Messaging (for push notifications)

## 🛠 Technologies Used

- Java & Android SDK, Recommended (tested on): API 35+
- Retrofit2 + Gson
- AndroidPdfViewer (v2, https://github.com/Infomaniak/android-pdfview)
- Firebase Messaging (FCM)
- Glide (for image loading)
- MVVM architecture
- LiveData, RecyclerView

## 🧪 Requirements

- Android Studio, Recommended (tested on Meerkat)
- Android emulator, Recommended (tested on Pixel 9 , Android 15.0 "VanillaIceCream")

## 🚀 How to Run

1. Clone the repository:

2. Open the project in Android Studio.

3. Create an emulator or connect a device.

4. Run the app.

## 🔐 Backend Setup

This app connects to the backend hosted at: http://10.0.2.2:8080/

> **Note**: `10.0.2.2` is used to access localhost from an Android emulator.

Make sure the backend is running with Docker before launching the app. For backend setup, see the [EBook Library Backend Repository](https://github.com/HorizonHalifa/EBook-Library_Backend).

## 📄 License

This project is licensed under the Apache License 2.0. See [LICENSE](LICENSE) for details.

## 📝 Notice

See [NOTICE](NOTICE) for third-party library acknowledgements.


