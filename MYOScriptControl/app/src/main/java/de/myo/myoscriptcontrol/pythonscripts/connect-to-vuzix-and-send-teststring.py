import android

droid = android.Android()
UUID = "457807c0-4897-11df-9879-0800200c9a66"

if droid.bluetoothConnect(UUID):
    droid.makeToast("successfully connected to device")
else:
    droid.makeToast("error while establishing bluetooth connection")

droid.bluetoothWrite("wer das liest ist doof")

