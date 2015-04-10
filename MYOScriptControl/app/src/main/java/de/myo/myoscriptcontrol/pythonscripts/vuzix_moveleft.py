import android
UUID = "457807c0-4897-11df-9879-0800200c9a66"
droid = android.Android()
droid.bluetoothConnect(UUID, "98:DA:92:00:06:62")
droid.bluetoothWrite("MOVELEFT")
