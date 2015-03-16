jQuery("#simulation")
  .on("click", ".s-840cc003-037d-4eca-a1c5-46e3437310f0 .click", function(event, data) {
    var jEvent, jFirer, cases;
    if(data === undefined) { data = event; }
    jEvent = jimEvent(event);
    jFirer = jEvent.getEventFirer();
    if(jFirer.is("#s-Bluetooth")) {
      cases = [
        {
          "blocks": [
            {
              "condition": {
                "action": "jimEquals",
                "parameter": [ {
                  "datatype": "variable",
                  "element": "Bluetooth"
                },"not connected" ]
              },
              "actions": [
                {
                  "action": "jimSetValue",
                  "parameter": {
                    "variable": "Bluetooth",
                    "value": "connected"
                  }
                },
                {
                  "action": "jimSetValue",
                  "parameter": {
                    "target": "#s-Bluetooth",
                    "value": ""
                  }
                }
              ]
            },
            {
              "actions": [
                {
                  "action": "jimSetValue",
                  "parameter": {
                    "variable": "Bluetooth",
                    "value": "not connected"
                  }
                },
                {
                  "action": "jimSetValue",
                  "parameter": {
                    "target": "#s-Bluetooth",
                    "value": ""
                  }
                },
                {
                  "action": "jimSetValue",
                  "parameter": {
                    "variable": "MYO-Status",
                    "value": "unsynced"
                  }
                }
              ]
            }
          ]
        },
        {
          "blocks": [
            {
              "condition": {
                "action": "jimAnd",
                "parameter": [ {
                  "action": "jimEquals",
                  "parameter": [ {
                    "datatype": "variable",
                    "element": "Bluetooth"
                  },"connected" ]
                },{
                  "action": "jimEquals",
                  "parameter": [ {
                    "datatype": "variable",
                    "element": "MYO-Status"
                  },"unsynced" ]
                } ]
              },
              "actions": [
              ]
            }
          ]
        },
        {
          "blocks": [
            {
              "condition": {
                "action": "jimEquals",
                "parameter": [ {
                  "datatype": "variable",
                  "element": "Bluetooth"
                },"not connected" ]
              },
              "actions": [
              ]
            }
          ]
        }
      ];
      event.data = data;
      jEvent.launchCases(cases);
    }
  });