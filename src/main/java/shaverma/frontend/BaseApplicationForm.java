package shaverma.frontend;

import shaverma.service.GUIService;

import javax.swing.*;
import java.awt.*;

public abstract class BaseApplicationForm {

    private Application app;

    public BaseApplicationForm(Application app) {
        this.app = app;
    }

    protected Application getApp() {
        return app;
    }

    protected GUIService getService() {
        return getApp().getService();
    }

    protected void switchToForm(BaseApplicationForm nextForm) {
        app.switchForm(nextForm);
    }

    protected void openForm(BaseApplicationForm newForm) {
        app.openForm(newForm);
    }

    abstract JPanel createFormPanel();

    abstract String getTitle();

    abstract Dimension getSize();

    public void updateForm() {}
}
