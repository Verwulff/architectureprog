package shaverma.frontend;

import shaverma.service.GUIService;
import shaverma.service.RESTService;
import shaverma.util.Util.RunMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Stack;

public class Application {

    private Stack<JFrame> frameStack;
    private GUIService service;
    private RESTService restService;

    public Application() {
        frameStack = new Stack<>();
    }

    public GUIService getService() {
        return service;
    }

    public void run(RunMode runMode) {
        setUp(runMode);
        manageForm(new LoginForm(this), JFrame.EXIT_ON_CLOSE, false);
    }

    public void switchForm(BaseApplicationForm nextForm) {
        manageForm(nextForm, JFrame.EXIT_ON_CLOSE, true);
    }

    public void openForm(BaseApplicationForm nextForm) {
        if (frameStack.size() < 2)
            manageForm(nextForm, JFrame.DISPOSE_ON_CLOSE, false);
    }

    public JFrame getActiveFrame() {
        return frameStack.peek();
    }

    private void manageForm(BaseApplicationForm form, int onClose, boolean replacePrevious) {
        JFrame frame = new JFrame(form.getTitle());
        JPanel panel = form.createFormPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(onClose);
        frame.setLocationRelativeTo(null);
        Dimension size = form.getSize();
        if (size != null) {
            frame.setSize(form.getSize());
        } else {
            frame.pack();
        }
        frame.setResizable(false);
        if (replacePrevious) {
            getActiveFrame().dispose();
            frameStack.pop();
        }
        frameStack.push(frame);
        frame.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent windowEvent) {
                super.windowGainedFocus(windowEvent);
                form.updateForm();
            }
        });
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                super.windowClosing(windowEvent);
                frameStack.pop();
                if (frameStack.empty()) {
                    stop();
                }
            }
        });
        frame.setVisible(true);
    }

    private void setUp(RunMode runMode) {
        service = new GUIService();
        service.setUp(GUIService.DataLayer.DB, runMode);
        restService = new RESTService(service.getRegistry());
        try {
            restService.run(8080);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void stop() {
        restService.stop();
    }

    public String getUserName() {
        return service.activeUserName();
    }
}
