package demo

/**
 * Groovy extension-method style: the first parameter is the receiver.
 * Add commands here; no command-name-to-method table is needed.
 */
class Commands {
    static void say(CommandContext self, String text) {
        self.emit(text)
    }

    static void pause(CommandContext self, int seconds) {
        self.emit("Pausing for ${seconds} second(s)")
    }

    static void finish(CommandContext self) {
        self.emit('Finished')
    }
}
