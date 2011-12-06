package basestation;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

public class BaseStationApp extends SingleFrameApplication {

    BaseStationEntity baseStation;

    @Override protected void startup() {
        BaseStationMainForm theForm = new BaseStationMainForm();
        baseStation = new BaseStationEntity(theForm);
        show(theForm);
    }

    @Override protected void configureWindow(java.awt.Window root) {
    }

    public static BaseStationApp getApplication() {
        return Application.getInstance(BaseStationApp.class);
    }

    public static void main(String[] args) {
        launch(BaseStationApp.class, args);
    }
}

