package shaverma;

import shaverma.frontend.Application;
import shaverma.util.Util.RunMode;

class Entry {

    public static void main(String[] args) {
        Application app = new Application();
        app.run(RunMode.DEBUG);
    }
}
