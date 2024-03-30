to run the project, add these lines to your keybindings.json file in vscode
```json
    {
        "key": "f5",
        "command": "workbench.action.debug.start",
        "when": "debuggersAvailable && !config.customKeybinds.gradle && debugState == 'inactive'"
    },
    {
        "key": "f5",
        "command": "-workbench.action.debug.start",
        "when": "debuggersAvailable && debugState == 'inactive'"
    },
    {
        "key": "f5",
        "command": "workbench.action.tasks.runTask",
        "when": "config.customKeybinds.gradle",
        "args": "project run"
    },
    {
        "key": "f5",
        "command": "workbench.action.tasks.restartTask",
        "when": "config.customKeybinds.gradle && inDebugMode"
    },
    {
        "key": "shift+f5",
        "command": "workbench.action.tasks.terminate",
        "when": "config.customKeybinds.gradle",
        "args": "project run"
    }
```