import android

droid = android.Android()

#Mac-Adresse der Vuzix
UUID = "457807c0-4897-11df-9879-0800200c9a66"
command = "testcommand"

if droid.checkBluetoothState().result:
    droid.makeToast("Bluetooth bereits eingeschaltet...")

else:
	#dialog if the user wants to switch on bluetooth
    droid.dialogCreateAlert("Bluetooth ausgeschaltet","Wollen Sie Bluetooth einschalten?")
	droid.dialogSetPositiveButtonText("Ja")
	droid.dialogSetNegativeButtonText("Nein")

	droid.dialogShow()
	response = droid.dialogGetResponse().result
	droid.dialogDismiss()

	if response.has_key("which"):
	  	result = response["which"]
	  	if result == "positive":
	    	#turn on bluetooth
			droid.toggleBluetoothState(True)
	else:
		#aboard
	  	droid.makeToast("Abbruch, da keine Vrbindung ohne Bluetooth möglich!")
	  	#exit script
	  	droid.exit()

if droid.bluetoothConnect(UUID):
	droid.bluetoothWrite(command)
	droid.ttsSpeak("Befehl erfolgreich gesendet")
	droid.makeToast("Befehl erfolgreich gesendet")

####Snippets###
'''
name = droid.getInput("Befehl", "Welcher Befehl mein Meister?")
droid.bluetoothWrite(name.result)
'''